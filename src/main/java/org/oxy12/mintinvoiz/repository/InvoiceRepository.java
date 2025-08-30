package org.oxy12.mintinvoiz.repository;

import org.oxy12.mintinvoiz.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    
    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);
    
    Optional<InvoiceEntity> findByShareToken(String shareToken);
    
    Page<InvoiceEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    Page<InvoiceEntity> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, InvoiceEntity.Status status, Pageable pageable);
    
    Page<InvoiceEntity> findByClientIdOrderByCreatedAtDesc(Long clientId, Pageable pageable);
    
    @Query("SELECT i FROM InvoiceEntity i WHERE i.user.id = :userId AND i.invoiceDate BETWEEN :startDate AND :endDate ORDER BY i.createdAt DESC")
    Page<InvoiceEntity> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate, 
                                                Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM InvoiceEntity i WHERE i.user.id = :userId AND i.createdAt >= :startDate")
    long countByUserIdAndCreatedAtAfter(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(i) FROM InvoiceEntity i WHERE i.createdAt >= :startDate")
    long countInvoicesCreatedAfter(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT i FROM InvoiceEntity i WHERE i.dueDate < :currentDate AND i.status = 'SENT'")
    List<InvoiceEntity> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);
    
    boolean existsByInvoiceNumber(String invoiceNumber);
}