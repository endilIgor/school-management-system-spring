package com.nagi.school_management_system_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;

public interface GradeRepository extends JpaRepository<GradeModel, Long> {

    List<GradeModel> findByStudent(StudentModel student);

    List<GradeModel> findBySubject(SubjectModel subject);

    List<GradeModel> findByClassroomAndQuarter(ClassroomModel classroom, QuarterEnum quarter);

    List<GradeModel> findByStudentAndQuarter(StudentModel student, QuarterEnum quarter);

    List<GradeModel> findByTeacherAndClassroom(TeacherModel teacher, ClassroomModel classroomId);
}
