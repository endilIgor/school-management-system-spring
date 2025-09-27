package com.nagi.school_management_system_spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.enums.StudentStatusEnum;

public interface StudentRepository extends JpaRepository<StudentModel, Long> {

    List<StudentModel> findByClassroom(ClassroomModel classroom);

    List<StudentModel> findByGuardian(GuardianModel guardian);

    Optional<StudentModel> findByEnrollmentNumber(String enrollmentNumber);

    List<StudentModel> findByStatus(StudentStatusEnum status);

    List<StudentModel> findByClassroomAndStatus(ClassroomModel classroom, StudentStatusEnum status);
}
