package com.nagi.school_management_system_spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;

public interface UserRepository extends JpaRepository<UserModel, Long>{

    Optional<UserModel> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<UserModel> findByUsertype(UserTypeEnum typeUser);

    List<UserModel> findByIsActiveTrue();
}
