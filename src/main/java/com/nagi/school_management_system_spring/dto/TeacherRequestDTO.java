package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.StatusTeacherEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class TeacherRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Registration is required")
    private String registration;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Status is required")
    private StatusTeacherEnum status;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    private LocalDateTime hireDate;

    private Long schoolId;
}
