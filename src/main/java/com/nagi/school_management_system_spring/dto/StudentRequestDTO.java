package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.StudentStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Enrollment number is required")
    private String enrollmentNumber;

    private Long guardianId;

    private Long classroomId;

    @NotNull(message = "Status is required")
    private StudentStatusEnum status;

    private String observations;
}
