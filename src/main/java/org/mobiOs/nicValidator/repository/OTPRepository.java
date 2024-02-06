package org.mobiOs.nicValidator.repository;

import org.mobiOs.nicValidator.dao.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity,Long> {
    List<OTPEntity> findByEmail(String email);
//    OTPEntity findByEmailAndIsActive(String email,Boolean isActive);
}
