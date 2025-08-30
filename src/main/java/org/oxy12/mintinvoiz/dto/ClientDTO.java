package org.oxy12.mintinvoiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ClientDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateClientRequest {
        @NotBlank(message = "Company name is required")
        private String companyName;
        
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        private String email;
        
        private String phoneNumber;
        private String businessRegistrationNumber;
        private String contactPerson;
        private String websiteUrl;
        private String logoUrl;
        
        // Address fields
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String stateProvince;
        private String postalCode;
        private String country;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientResponse {
        private Long id;
        private String companyName;
        private String email;
        private String phoneNumber;
        private String businessRegistrationNumber;
        private String contactPerson;
        private String websiteUrl;
        private String logoUrl;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String stateProvince;
        private String postalCode;
        private String country;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer totalInvoices;
        private Integer totalProjects;
    }
}