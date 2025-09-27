package com.nagi.school_management_system_spring.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.enums.StatusTeacherEnum;

public interface TeacherRepository extends JpaRepository<TeacherModel, Long>{

    List<TeacherModel> findBySpecialization(String specialization);

    List<TeacherModel> findByStatus(StatusTeacherEnum status);

    List<TeacherModel> findByHireDate(LocalDateTime hireDate);
}
