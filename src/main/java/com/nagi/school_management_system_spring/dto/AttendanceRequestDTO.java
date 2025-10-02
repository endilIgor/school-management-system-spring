package com.nagi.school_management_system_spring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Present status is required")
    private Boolean present;

    private String justification;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
}
