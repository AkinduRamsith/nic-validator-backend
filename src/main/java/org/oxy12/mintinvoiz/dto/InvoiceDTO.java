package org.oxy12.mintinvoiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateInvoiceRequest {
        @NotNull(message = "Client ID is required")
        private Long clientId;
        
        private Long projectId;
        
        @NotNull(message = "Invoice date is required")
        private LocalDate invoiceDate;
        
        private LocalDate dueDate;
        
        @NotEmpty(message = "Invoice items are required")
        @Valid
        private List<InvoiceItemRequest> items;
        
        private String notes;
        private String termsConditions;
        private String paymentInstructions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceItemRequest {
        private Long itemId; // null for ad-hoc items
        
        @NotBlank(message = "Item name is required")
        private String itemName;
        
        private String itemDescription;
        
        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
        private BigDecimal quantity;
        
        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.00", message = "Unit price must be non-negative")
        private BigDecimal unitPrice;
        
        @DecimalMin(value = "0.00", message = "Tax rate must be non-negative")
        @DecimalMax(value = "100.00", message = "Tax rate cannot exceed 100%")
        private BigDecimal taxRate = BigDecimal.ZERO;
        
        private String discountType = "NONE";
        
        @DecimalMin(value = "0.00", message = "Discount value must be non-negative")
        private BigDecimal discountValue = BigDecimal.ZERO;
        
        private Integer sortOrder = 0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InvoiceResponse {
        private Long id;
        private String invoiceNumber;
        private String status;
        private LocalDate invoiceDate;
        private LocalDate dueDate;
        private String subtotal;
        private String totalTax;
        private String totalDiscount;
        private String grandTotal;
        private String notes;
        private String termsConditions;
        private String paymentInstructions;
        private String shareToken;
        private Boolean pdfGenerated;
        private Integer editCount;
        private Integer printCount;
        private ClientSummaryDTO client;
        private ProjectSummaryDTO project;
        private List<InvoiceItemResponse> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InvoiceItemResponse {
        private Long id;
        private String itemName;
        private String itemDescription;
        private String quantity;
        private String unitPrice;
        private String taxRate;
        private String discountType;
        private String discountValue;
        private String lineTotal;
        private String lineTax;
        private String lineDiscount;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientSummaryDTO {
        private Long id;
        private String companyName;
        private String email;
        private String phoneNumber;
        private String contactPerson;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProjectSummaryDTO {
        private Long id;
        private String name;
        private String description;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareInvoiceRequest {
        @NotBlank(message = "Method is required")
        @Pattern(regexp = "email|sms|link", message = "Method must be email, sms, or link")
        private String method;
        
        private String to; // email address or phone number
        private String message; // optional custom message
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ShareResponse {
        private String shareLink;
        private String message;
        private Boolean sent;
    }
}