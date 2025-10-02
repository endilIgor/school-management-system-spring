package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.EvaluationTypeEnum;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequestDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;

    @NotNull(message = "Quarter is required")
    private QuarterEnum quarter;

    @NotNull(message = "Evaluation type is required")
    private EvaluationTypeEnum evaluationType;

    @NotNull(message = "Grade value is required")
    private BigDecimal value;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
}
