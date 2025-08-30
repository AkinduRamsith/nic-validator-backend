package org.oxy12.mintinvoiz.repository;

import org.oxy12.mintinvoiz.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    
    Optional<SubscriptionEntity> findByUserId(Long userId);
    
    Optional<SubscriptionEntity> findByUserIdAndIsActiveTrue(Long userId);
    
    @Query("SELECT s FROM SubscriptionEntity s WHERE s.endDate < :currentDate AND s.isActive = true")
    List<SubscriptionEntity> findExpiredSubscriptions(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT s FROM SubscriptionEntity s WHERE s.endDate BETWEEN :startDate AND :endDate AND s.isActive = true")
    List<SubscriptionEntity> findSubscriptionsExpiringBetween(@Param("startDate") LocalDateTime startDate, 
                                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(s) FROM SubscriptionEntity s WHERE s.packageType = :packageType AND s.isActive = true")
    long countActiveSubscriptionsByPackage(@Param("packageType") SubscriptionEntity.PackageType packageType);
}