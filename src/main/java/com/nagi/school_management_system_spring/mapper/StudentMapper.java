package com.nagi.school_management_system_spring.mapper;

import org.springframework.stereotype.Component;

import com.nagi.school_management_system_spring.dto.StudentResponseDTO;
import com.nagi.school_management_system_spring.dto.StudentSummaryResponseDTO;
import com.nagi.school_management_system_spring.model.StudentModel;

@Component
public class StudentMapper {

    public StudentResponseDTO toResponseDTO(StudentModel student) {
        if (student == null) {
            return null;
        }

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setUserId(student.getUser() != null ? student.getUser().getId() : null);
        dto.setUserName(student.getUser() != null ? student.getUser().getName() : null);
        dto.setUserEmail(student.getUser() != null ? student.getUser().getEmail() : null);
        dto.setEnrollmentNumber(student.getEnrollmentNumber());
        dto.setGuardianId(student.getGuardian() != null ? student.getGuardian().getId() : null);
        dto.setGuardianName(student.getGuardian() != null && student.getGuardian().getUser() != null
            ? student.getGuardian().getUser().getName() : null);
        dto.setClassroomId(student.getClassroom() != null ? student.getClassroom().getId() : null);
        dto.setClassroomName(student.getClassroom() != null ? student.getClassroom().getName() : null);
        dto.setStatus(student.getStatus());
        dto.setObservations(student.getObservations());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());

        return dto;
    }

    public StudentSummaryResponseDTO toSummaryDTO(StudentModel student) {
        if (student == null) {
            return null;
        }

        StudentSummaryResponseDTO dto = new StudentSummaryResponseDTO();
        dto.setId(student.getId());
        dto.setUserName(student.getUser() != null ? student.getUser().getName() : null);
        dto.setEnrollmentNumber(student.getEnrollmentNumber());
        dto.setClassroomName(student.getClassroom() != null ? student.getClassroom().getName() : null);
        dto.setStatus(student.getStatus());

        return dto;
    }
}
