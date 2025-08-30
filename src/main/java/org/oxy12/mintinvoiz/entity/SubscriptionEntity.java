package org.oxy12.mintinvoiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType = PackageType.FREE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle")
    private BillingCycle billingCycle = BillingCycle.MONTHLY;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "auto_renew")
    private Boolean autoRenew = false;
    
    @Column(name = "invoice_quota_used")
    private Integer invoiceQuotaUsed = 0;
    
    @Column(name = "print_quota_used")
    private Integer printQuotaUsed = 0;
    
    @Column(name = "edit_quota_used")
    private Integer editQuotaUsed = 0;
    
    @Column(name = "email_quota_used")
    private Integer emailQuotaUsed = 0;
    
    @Column(name = "sms_quota_used")
    private Integer smsQuotaUsed = 0;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;
    
    @Column(name = "paypal_subscription_id")
    private String paypalSubscriptionId;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum PackageType {
        FREE, ELITE, PRO, ULTRA
    }
    
    public enum BillingCycle {
        MONTHLY, ANNUAL
    }
}