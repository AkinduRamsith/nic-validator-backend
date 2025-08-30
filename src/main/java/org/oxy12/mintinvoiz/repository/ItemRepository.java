package org.oxy12.mintinvoiz.repository;

import org.oxy12.mintinvoiz.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    
    @Query("SELECT i FROM ItemEntity i WHERE i.type = 'GLOBAL' AND i.isActive = true ORDER BY i.name ASC")
    List<ItemEntity> findGlobalItems();
    
    @Query("SELECT i FROM ItemEntity i WHERE i.type = 'GLOBAL' AND i.isActive = true ORDER BY i.name ASC")
    Page<ItemEntity> findGlobalItems(Pageable pageable);
    
    @Query("SELECT i FROM ItemEntity i WHERE i.type = 'GLOBAL' AND i.isActive = true AND " +
           "(i.name LIKE %:search% OR i.description LIKE %:search% OR i.itemCode LIKE %:search%)")
    Page<ItemEntity> searchGlobalItems(@Param("search") String search, Pageable pageable);
    
    boolean existsByItemCodeAndType(String itemCode, ItemEntity.ItemType type);
}