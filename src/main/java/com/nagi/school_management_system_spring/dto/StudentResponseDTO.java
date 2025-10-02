package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.StudentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String enrollmentNumber;
    private Long guardianId;
    private String guardianName;
    private Long classroomId;
    private String classroomName;
    private StudentStatusEnum status;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
