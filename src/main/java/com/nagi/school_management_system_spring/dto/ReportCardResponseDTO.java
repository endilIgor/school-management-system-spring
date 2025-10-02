package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.model.enums.ReportCardStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCardResponseDTO {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEnrollmentNumber;
    private Long classroomId;
    private String classroomName;
    private QuarterEnum quarter;
    private String schoolYear;
    private BigDecimal overallAverage;
    private ReportCardStatusEnum status;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
