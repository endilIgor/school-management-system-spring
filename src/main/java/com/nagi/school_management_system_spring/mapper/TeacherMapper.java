package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.TeacherResponseDTO;
import com.nagi.school_management_system_spring.model.TeacherModel;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherResponseDTO toResponseDTO(TeacherModel teacher) {
        if (teacher == null) {
            return null;
        }

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setUserId(teacher.getUser() != null ? teacher.getUser().getId() : null);
        dto.setUserName(teacher.getUser() != null ? teacher.getUser().getName() : null);
        dto.setUserEmail(teacher.getUser() != null ? teacher.getUser().getEmail() : null);
        dto.setRegistration(teacher.getRegistration());
        dto.setSpecialization(teacher.getSpecialization());
        dto.setStatus(teacher.getStatus());
        dto.setSalary(teacher.getSalary());
        dto.setHireDate(teacher.getHireDate());
        dto.setSchoolId(teacher.getSchool() != null ? teacher.getSchool().getId() : null);
        dto.setSchoolName(teacher.getSchool() != null ? teacher.getSchool().getName() : null);
        dto.setCreatedAt(teacher.getCreatedAt());
        dto.setUpdatedAt(teacher.getUpdatedAt());

        return dto;
    }
}
