package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.StatusTeacherEnum;
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
public class TeacherResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String registration;
    private String specialization;
    private StatusTeacherEnum status;
    private BigDecimal salary;
    private LocalDateTime hireDate;
    private Long schoolId;
    private String schoolName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
