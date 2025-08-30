package org.oxy12.mintinvoiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oxy12.mintinvoiz.dto.ApiResponse;
import org.oxy12.mintinvoiz.entity.UserEntity;
import org.oxy12.mintinvoiz.repository.UserRepository;
import org.oxy12.mintinvoiz.repository.InvoiceRepository;
import org.oxy12.mintinvoiz.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Admin management endpoints")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping("/customers")
    @Operation(summary = "Get all customers", description = "Get paginated list of customers")
    public ResponseEntity<ApiResponse<Page<UserEntity>>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserEntity> customers = userRepository.findByRoleAndIsActiveTrue(UserEntity.Role.CUSTOMER, pageable);
            return ResponseEntity.ok(ApiResponse.success(customers));
        } catch (Exception e) {
            log.error("Failed to get customers: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get system analytics", description = "Get system-wide analytics and statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            
            // User statistics
            long totalCustomers = userRepository.countActiveCustomers();
            analytics.put("totalCustomers", totalCustomers);
            
            // Invoice statistics
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            long invoicesThisMonth = invoiceRepository.countInvoicesCreatedAfter(startOfMonth);
            analytics.put("invoicesThisMonth", invoicesThisMonth);
            
            // Subscription statistics
            Map<String, Long> subscriptionStats = new HashMap<>();
            subscriptionStats.put("FREE", subscriptionRepository.countActiveSubscriptionsByPackage(SubscriptionEntity.PackageType.FREE));
            subscriptionStats.put("ELITE", subscriptionRepository.countActiveSubscriptionsByPackage(SubscriptionEntity.PackageType.ELITE));
            subscriptionStats.put("PRO", subscriptionRepository.countActiveSubscriptionsByPackage(SubscriptionEntity.PackageType.PRO));
            subscriptionStats.put("ULTRA", subscriptionRepository.countActiveSubscriptionsByPackage(SubscriptionEntity.PackageType.ULTRA));
            analytics.put("subscriptionStats", subscriptionStats);
            
            analytics.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponse.success("Analytics retrieved successfully", analytics));
        } catch (Exception e) {
            log.error("Failed to get analytics: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/customers/{customerId}/subscription")
    @Operation(summary = "Update customer subscription", description = "Manually update customer subscription package")
    public ResponseEntity<ApiResponse<String>> updateCustomerSubscription(
            @PathVariable Long customerId,
            @RequestBody Map<String, Object> subscriptionData) {
        try {
            // TODO: Implement subscription update logic
            log.info("Admin updating subscription for customer: {}", customerId);
            return ResponseEntity.ok(ApiResponse.success("Subscription updated successfully", "Updated"));
        } catch (Exception e) {
            log.error("Failed to update customer subscription: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}