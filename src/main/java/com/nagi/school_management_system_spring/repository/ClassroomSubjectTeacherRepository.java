package com.nagi.school_management_system_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;

public interface ClassroomSubjectTeacherRepository extends JpaRepository<ClassroomSubjectTeacherModel, Long> {

    List<ClassroomSubjectTeacherModel> findByClassroom(ClassroomModel classroomId);

    List<ClassroomSubjectTeacherModel> findByTeacher(TeacherModel teacher);

    List<ClassroomSubjectTeacherModel> findBySubject(SubjectModel subject);

    List<ClassroomSubjectTeacherModel> findBySchoolYear(String schoolYear);
}
