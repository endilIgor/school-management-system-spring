package com.nagi.school_management_system_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;

public interface ReportCardRepository extends JpaRepository<ReportCardModel, Long>{

    List<ReportCardModel> findByStudentAndSchoolYear(StudentModel student, String schoolYear);

    List<ReportCardModel> findByClassroomAndQuarter(ClassroomModel classroom, QuarterEnum quarter);
}
