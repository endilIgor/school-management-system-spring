package com.nagi.school_management_system_spring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.AttendanceModel;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;

public interface AttendanceRepository extends JpaRepository<AttendanceModel, Long> {

    List<AttendanceModel> findByStudentAndDateBetween(StudentModel student, LocalDate start, LocalDate end);

    List<AttendanceModel> findByClassroomAndDate(ClassroomModel classroom, LocalDate date);

    List<AttendanceModel> findBySubjectAndDate(SubjectModel subject, LocalDate date);

    Long countByStudentAndPresentTrue(StudentModel student);

    Long countByStudentAndPresentTrueAndDateBetween(StudentModel student, LocalDate start, LocalDate end);
}
