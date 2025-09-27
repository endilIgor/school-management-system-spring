package com.nagi.school_management_system_spring.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.GuardianModel;

public interface GuardianRepository extends JpaRepository<GuardianModel, Long>{

    List<GuardianModel> findByProfession(String profession);

    List<GuardianModel> findByFamilyIncomeBetween(BigDecimal min, BigDecimal max);
}
