package org.oxy12.mintinvoiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oxy12.mintinvoiz.entity.SubscriptionEntity;
import org.oxy12.mintinvoiz.repository.InvoiceRepository;
import org.oxy12.mintinvoiz.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuotaService {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    @Value("${invoice.quota.free:25}")
    private int freeQuota;

    @Value("${invoice.quota.elite:100}")
    private int eliteQuota;

    @Value("${invoice.quota.pro:500}")
    private int proQuota;

    @Value("${invoice.quota.ultra:-1}")
    private int ultraQuota;

    public boolean canCreateInvoice(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        
        if (!subscription.getIsActive()) {
            return false;
        }

        int quota = getInvoiceQuota(subscription.getPackageType());
        if (quota == -1) { // Unlimited
            return true;
        }

        // Check current month usage
        LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        long currentUsage = invoiceRepository.countByUserIdAndCreatedAtAfter(userId, startOfMonth);
        
        return currentUsage < quota;
    }

    public boolean canShareInvoice(Long userId, String method) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        
        switch (method) {
            case "link":
                return true; // All packages support link sharing
            case "email":
                return subscription.getPackageType() != SubscriptionEntity.PackageType.FREE;
            case "sms":
                return subscription.getPackageType() == SubscriptionEntity.PackageType.PRO ||
                       subscription.getPackageType() == SubscriptionEntity.PackageType.ULTRA;
            default:
                return false;
        }
    }

    public boolean canEditInvoice(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        int editQuota = getEditQuota(subscription.getPackageType());
        
        if (editQuota == -1) { // Unlimited
            return true;
        }
        
        return subscription.getEditQuotaUsed() < editQuota;
    }

    public boolean canPrintInvoice(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        int printQuota = getPrintQuota(subscription.getPackageType());
        
        if (printQuota == -1) { // Unlimited
            return true;
        }
        
        return subscription.getPrintQuotaUsed() < printQuota;
    }

    @Transactional
    public void incrementInvoiceQuota(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        subscription.setInvoiceQuotaUsed(subscription.getInvoiceQuotaUsed() + 1);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void incrementEmailQuota(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        subscription.setEmailQuotaUsed(subscription.getEmailQuotaUsed() + 1);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void incrementSmsQuota(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        subscription.setSmsQuotaUsed(subscription.getSmsQuotaUsed() + 1);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void incrementPrintQuota(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        subscription.setPrintQuotaUsed(subscription.getPrintQuotaUsed() + 1);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void incrementEditQuota(Long userId) {
        SubscriptionEntity subscription = getActiveSubscription(userId);
        subscription.setEditQuotaUsed(subscription.getEditQuotaUsed() + 1);
        subscriptionRepository.save(subscription);
    }

    private SubscriptionEntity getActiveSubscription(Long userId) {
        return subscriptionRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("No active subscription found"));
    }

    private int getInvoiceQuota(SubscriptionEntity.PackageType packageType) {
        switch (packageType) {
            case FREE: return freeQuota;
            case ELITE: return eliteQuota;
            case PRO: return proQuota;
            case ULTRA: return ultraQuota;
            default: return 0;
        }
    }

    private int getEditQuota(SubscriptionEntity.PackageType packageType) {
        switch (packageType) {
            case FREE: return 5;
            case ELITE: return 20;
            case PRO: return 100;
            case ULTRA: return -1; // Unlimited
            default: return 0;
        }
    }

    private int getPrintQuota(SubscriptionEntity.PackageType packageType) {
        switch (packageType) {
            case FREE: return 10;
            case ELITE: return 50;
            case PRO: return -1; // Unlimited
            case ULTRA: return -1; // Unlimited
            default: return 0;
        }
    }
}