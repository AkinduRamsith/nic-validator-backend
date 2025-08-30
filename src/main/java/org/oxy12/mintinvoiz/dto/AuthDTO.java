package org.oxy12.mintinvoiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oxy12.mintinvoiz.entity.UserEntity;

public class AuthDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        private String email;
        
        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest {
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String password;
        
        @NotBlank(message = "First name is required")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        private String lastName;
        
        private String phoneNumber;
        private String companyName;
        private String businessRegistrationNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequest {
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordRequest {
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        private String email;
        
        @NotBlank(message = "OTP is required")
        private String otp;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String newPassword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        @NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserDTO user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserDTO {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String companyName;
        private String businessRegistrationNumber;
        private UserEntity.Role role;
        private Boolean isActive;
        private Boolean emailVerified;
        private String profileImageUrl;
        private SubscriptionDTO subscription;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubscriptionDTO {
        private Long id;
        private String packageType;
        private String billingCycle;
        private String startDate;
        private String endDate;
        private Boolean isActive;
        private Boolean autoRenew;
        private Integer invoiceQuotaUsed;
        private Integer printQuotaUsed;
        private Integer editQuotaUsed;
        private String price;
    }
}