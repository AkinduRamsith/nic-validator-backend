package org.oxy12.mintinvoiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oxy12.mintinvoiz.dto.AuthDTO;
import org.oxy12.mintinvoiz.entity.SubscriptionEntity;
import org.oxy12.mintinvoiz.entity.UserEntity;
import org.oxy12.mintinvoiz.repository.UserRepository;
import org.oxy12.mintinvoiz.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserEntity user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        
        String accessToken = jwtUtil.generateAccessToken(userDetails, user.getRole().name(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails, user.getId());

        AuthDTO.UserDTO userDTO = convertToUserDTO(user);

        return AuthDTO.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L) // 15 minutes
                .user(userDTO)
                .build();
    }

    @Transactional
    public AuthDTO.AuthResponse signup(AuthDTO.SignupRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .role(UserEntity.Role.CUSTOMER)
                .isActive(true)
                .emailVerified(false)
                .build();

        // Create free subscription
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .user(user)
                .packageType(SubscriptionEntity.PackageType.FREE)
                .billingCycle(SubscriptionEntity.BillingCycle.MONTHLY)
                .startDate(LocalDateTime.now())
                .isActive(true)
                .autoRenew(false)
                .invoiceQuotaUsed(0)
                .printQuotaUsed(0)
                .editQuotaUsed(0)
                .emailQuotaUsed(0)
                .smsQuotaUsed(0)
                .build();

        user.setSubscription(subscription);
        user = userRepository.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        } catch (Exception e) {
            log.warn("Failed to send welcome email to {}: {}", user.getEmail(), e.getMessage());
        }

        // Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtil.generateAccessToken(userDetails, user.getRole().name(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails, user.getId());

        AuthDTO.UserDTO userDTO = convertToUserDTO(user);

        return AuthDTO.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .user(userDTO)
                .build();
    }

    public void forgotPassword(AuthDTO.ForgotPasswordRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmailAndIsActiveTrue(request.getEmail());
        
        if (userOpt.isPresent()) {
            // Generate OTP and send email
            String otp = generateOTP();
            // TODO: Store OTP in cache/database with expiration
            
            try {
                emailService.sendPasswordResetOTP(request.getEmail(), otp);
                log.info("Password reset OTP sent to {}", request.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password reset email to {}: {}", request.getEmail(), e.getMessage());
                throw new RuntimeException("Failed to send reset email");
            }
        } else {
            // Don't reveal if email exists or not for security
            log.info("Password reset requested for non-existent email: {}", request.getEmail());
        }
    }

    @Transactional
    public void resetPassword(AuthDTO.ResetPasswordRequest request) {
        // TODO: Validate OTP from cache/database
        
        UserEntity user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password reset successful for user: {}", request.getEmail());
    }

    public AuthDTO.AuthResponse refreshToken(AuthDTO.RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtil.isValidToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String tokenType = jwtUtil.extractTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Invalid token type");
        }

        String email = jwtUtil.extractUsername(refreshToken);
        UserEntity user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails, user.getRole().name(), user.getId());
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails, user.getId());

        AuthDTO.UserDTO userDTO = convertToUserDTO(user);

        return AuthDTO.AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .user(userDTO)
                .build();
    }

    private AuthDTO.UserDTO convertToUserDTO(UserEntity user) {
        AuthDTO.UserDTO userDTO = modelMapper.map(user, AuthDTO.UserDTO.class);
        
        if (user.getSubscription() != null) {
            AuthDTO.SubscriptionDTO subscriptionDTO = modelMapper.map(user.getSubscription(), AuthDTO.SubscriptionDTO.class);
            userDTO.setSubscription(subscriptionDTO);
        }
        
        return userDTO;
    }

    private String generateOTP() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}