package org.oxy12.mintinvoiz.repository;

import org.oxy12.mintinvoiz.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByEmailAndIsActiveTrue(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role AND u.isActive = true")
    Page<UserEntity> findByRoleAndIsActiveTrue(@Param("role") UserEntity.Role role, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.role = 'CUSTOMER' AND u.isActive = true")
    long countActiveCustomers();
    
    @Query("SELECT u FROM UserEntity u WHERE u.companyName LIKE %:search% OR u.email LIKE %:search% OR CONCAT(u.firstName, ' ', u.lastName) LIKE %:search%")
    Page<UserEntity> searchUsers(@Param("search") String search, Pageable pageable);
}