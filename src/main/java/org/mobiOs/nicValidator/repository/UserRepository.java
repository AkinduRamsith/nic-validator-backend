package org.mobiOs.nicValidator.repository;

import org.apache.catalina.User;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;
import org.mobiOs.nicValidator.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findOneByUserNameAndPassword(String userName,String password);

    UserEntity findByUserName(String userName);

    boolean existsByUserName(String userName);

    UserEntity findByEmail(String email);
}
