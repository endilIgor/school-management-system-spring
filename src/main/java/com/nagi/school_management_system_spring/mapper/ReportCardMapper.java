package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.ReportCardResponseDTO;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import org.springframework.stereotype.Component;

@Component
public class ReportCardMapper {

    public ReportCardResponseDTO toResponseDTO(ReportCardModel reportCard) {
        if (reportCard == null) {
            return null;
        }

        ReportCardResponseDTO dto = new ReportCardResponseDTO();
        dto.setId(reportCard.getId());
        dto.setStudentId(reportCard.getStudent() != null ? reportCard.getStudent().getId() : null);
        dto.setStudentName(reportCard.getStudent() != null && reportCard.getStudent().getUser() != null
            ? reportCard.getStudent().getUser().getName() : null);
        dto.setStudentEnrollmentNumber(reportCard.getStudent() != null ? reportCard.getStudent().getEnrollmentNumber() : null);
        dto.setClassroomId(reportCard.getClassroom() != null ? reportCard.getClassroom().getId() : null);
        dto.setClassroomName(reportCard.getClassroom() != null ? reportCard.getClassroom().getName() : null);
        dto.setQuarter(reportCard.getQuarter());
        dto.setSchoolYear(reportCard.getSchoolYear());
        dto.setOverallAverage(reportCard.getOverallAverage());
        dto.setStatus(reportCard.getStatus());
        dto.setObservations(reportCard.getObservations());
        dto.setCreatedAt(reportCard.getCreatedAt());
        dto.setUpdatedAt(reportCard.getUpdatedAt());

        return dto;
    }
}
