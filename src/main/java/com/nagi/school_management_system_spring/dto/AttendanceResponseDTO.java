package com.nagi.school_management_system_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDTO {

    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEnrollmentNumber;
    private Long classroomId;
    private String classroomName;
    private Long subjectId;
    private String subjectName;
    private LocalDate date;
    private Boolean present;
    private String justification;
    private Long teacherId;
    private String teacherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
