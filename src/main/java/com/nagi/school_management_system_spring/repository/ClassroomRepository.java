package com.nagi.school_management_system_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;

public interface ClassroomRepository extends JpaRepository<ClassroomModel, Long>{
    
    List<ClassroomModel> findBySchoolYear(String schoolYear);

    List<ClassroomModel> findByGradeAndSchoolYear(String grade, String schoolYear);

    List<ClassroomModel> findByShift(ShiftEnum shift);

    List<ClassroomModel> findByHomeRoomTeacher(TeacherModel homeRoomTeacher);
}
