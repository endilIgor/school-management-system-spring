package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.AttendanceResponseDTO;
import com.nagi.school_management_system_spring.model.AttendanceModel;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponseDTO toResponseDTO(AttendanceModel attendance) {
        if (attendance == null) {
            return null;
        }

        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudent() != null ? attendance.getStudent().getId() : null);
        dto.setStudentName(attendance.getStudent() != null && attendance.getStudent().getUser() != null
            ? attendance.getStudent().getUser().getName() : null);
        dto.setStudentEnrollmentNumber(attendance.getStudent() != null ? attendance.getStudent().getEnrollmentNumber() : null);
        dto.setClassroomId(attendance.getClassroom() != null ? attendance.getClassroom().getId() : null);
        dto.setClassroomName(attendance.getClassroom() != null ? attendance.getClassroom().getName() : null);
        dto.setSubjectId(attendance.getSubject() != null ? attendance.getSubject().getId() : null);
        dto.setSubjectName(attendance.getSubject() != null ? attendance.getSubject().getSubjectName() : null);
        dto.setDate(attendance.getDate());
        dto.setPresent(attendance.getPresent());
        dto.setJustification(attendance.getJustification());
        dto.setTeacherId(attendance.getTeacher() != null ? attendance.getTeacher().getId() : null);
        dto.setTeacherName(attendance.getTeacher() != null && attendance.getTeacher().getUser() != null
            ? attendance.getTeacher().getUser().getName() : null);
        dto.setCreatedAt(attendance.getCreatedAt());
        dto.setUpdatedAt(attendance.getUpdatedAt());

        return dto;
    }
}
