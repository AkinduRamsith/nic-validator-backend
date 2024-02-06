package org.mobiOs.nicValidator.repository;

import org.mobiOs.nicValidator.dao.NICValidatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface NICValidatorRepository extends JpaRepository<NICValidatorEntity,Long> {
}
