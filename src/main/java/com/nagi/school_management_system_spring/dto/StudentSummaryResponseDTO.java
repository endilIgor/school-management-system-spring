package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.StudentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryResponseDTO {

    private Long id;
    private String userName;
    private String enrollmentNumber;
    private String classroomName;
    private StudentStatusEnum status;
}
