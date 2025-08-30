package org.oxy12.mintinvoiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.oxy12.mintinvoiz.dto.AuthDTO;
import org.oxy12.mintinvoiz.entity.SubscriptionEntity;
import org.oxy12.mintinvoiz.entity.UserEntity;
import org.oxy12.mintinvoiz.repository.UserRepository;
import org.oxy12.mintinvoiz.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private UserEntity testUser;
    private AuthDTO.SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        testUser = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(UserEntity.Role.CUSTOMER)
                .isActive(true)
                .emailVerified(false)
                .build();

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .user(testUser)
                .packageType(SubscriptionEntity.PackageType.FREE)
                .billingCycle(SubscriptionEntity.BillingCycle.MONTHLY)
                .isActive(true)
                .build();

        testUser.setSubscription(subscription);

        signupRequest = AuthDTO.SignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .companyName("Test Company")
                .build();
    }

    @Test
    void signup_Success_ReturnsAuthResponse() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(jwtUtil.generateAccessToken(any(), anyString(), any())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(), any())).thenReturn("refresh-token");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(org.springframework.security.core.userdetails.UserDetails.class));
        
        AuthDTO.UserDTO userDTO = AuthDTO.UserDTO.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(UserEntity.Role.CUSTOMER)
                .build();
        
        when(modelMapper.map(any(UserEntity.class), eq(AuthDTO.UserDTO.class))).thenReturn(userDTO);

        // When
        AuthDTO.AuthResponse response = authService.signup(signupRequest);

        // Then
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getUser());
        assertEquals("test@example.com", response.getUser().getEmail());

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(UserEntity.class));
        verify(emailService).sendWelcomeEmail("test@example.com", "John");
    }

    @Test
    void signup_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.signup(signupRequest);
        });

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void forgotPassword_UserExists_SendsEmail() {
        // Given
        AuthDTO.ForgotPasswordRequest request = AuthDTO.ForgotPasswordRequest.builder()
                .email("test@example.com")
                .build();
        
        when(userRepository.findByEmailAndIsActiveTrue(anyString())).thenReturn(Optional.of(testUser));
        doNothing().when(emailService).sendPasswordResetOTP(anyString(), anyString());

        // When
        assertDoesNotThrow(() -> authService.forgotPassword(request));

        // Then
        verify(emailService).sendPasswordResetOTP(eq("test@example.com"), anyString());
    }

    @Test
    void forgotPassword_UserNotExists_DoesNotThrowException() {
        // Given
        AuthDTO.ForgotPasswordRequest request = AuthDTO.ForgotPasswordRequest.builder()
                .email("nonexistent@example.com")
                .build();
        
        when(userRepository.findByEmailAndIsActiveTrue(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertDoesNotThrow(() -> authService.forgotPassword(request));
        
        // Should not send email for non-existent users (security)
        verify(emailService, never()).sendPasswordResetOTP(anyString(), anyString());
    }
}