package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.EvaluationTypeEnum;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeResponseDTO {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private Long classroomId;
    private String classroomName;
    private QuarterEnum quarter;
    private EvaluationTypeEnum evaluationType;
    private BigDecimal value;
    private LocalDate recordDate;
    private Long teacherId;
    private String teacherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
