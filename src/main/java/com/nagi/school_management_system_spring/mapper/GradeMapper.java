package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.GradeResponseDTO;
import com.nagi.school_management_system_spring.model.GradeModel;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public GradeResponseDTO toResponseDTO(GradeModel grade) {
        if (grade == null) {
            return null;
        }

        GradeResponseDTO dto = new GradeResponseDTO();
        dto.setId(grade.getId());
        dto.setStudentId(grade.getStudent() != null ? grade.getStudent().getId() : null);
        dto.setStudentName(grade.getStudent() != null && grade.getStudent().getUser() != null
            ? grade.getStudent().getUser().getName() : null);
        dto.setSubjectId(grade.getSubject() != null ? grade.getSubject().getId() : null);
        dto.setSubjectName(grade.getSubject() != null ? grade.getSubject().getSubjectName() : null);
        dto.setClassroomId(grade.getClassroom() != null ? grade.getClassroom().getId() : null);
        dto.setClassroomName(grade.getClassroom() != null ? grade.getClassroom().getName() : null);
        dto.setQuarter(grade.getQuarter());
        dto.setEvaluationType(grade.getEvaluationType());
        dto.setValue(grade.getValue());
        dto.setRecordDate(grade.getRecordDate());
        dto.setTeacherId(grade.getTeacher() != null ? grade.getTeacher().getId() : null);
        dto.setTeacherName(grade.getTeacher() != null && grade.getTeacher().getUser() != null
            ? grade.getTeacher().getUser().getName() : null);
        dto.setCreatedAt(grade.getCreatedAt());
        dto.setUpdatedAt(grade.getUpdatedAt());

        return dto;
    }
}
