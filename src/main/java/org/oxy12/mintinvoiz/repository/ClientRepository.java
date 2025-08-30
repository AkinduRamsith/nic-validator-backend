package org.oxy12.mintinvoiz.repository;

import org.oxy12.mintinvoiz.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    
    Page<ClientEntity> findByUserIdAndIsActiveTrueOrderByCompanyNameAsc(Long userId, Pageable pageable);
    
    List<ClientEntity> findByUserIdAndIsActiveTrueOrderByCompanyNameAsc(Long userId);
    
    Optional<ClientEntity> findByIdAndUserIdAndIsActiveTrue(Long id, Long userId);
    
    boolean existsByEmailAndUserId(String email, Long userId);
    
    @Query("SELECT c FROM ClientEntity c WHERE c.user.id = :userId AND c.isActive = true AND " +
           "(c.companyName LIKE %:search% OR c.email LIKE %:search% OR c.contactPerson LIKE %:search%)")
    Page<ClientEntity> searchClientsByUser(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM ClientEntity c WHERE c.user.id = :userId AND c.isActive = true")
    long countActiveClientsByUser(@Param("userId") Long userId);
}