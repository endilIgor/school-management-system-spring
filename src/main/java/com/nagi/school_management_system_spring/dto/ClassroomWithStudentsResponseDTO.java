package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomWithStudentsResponseDTO {

    private Long id;
    private String name;
    private String grade;
    private ShiftEnum shift;
    private String room;
    private Integer maxCapacity;
    private Long homeRoomTeacherId;
    private String homeRoomTeacherName;
    private Boolean isActive;
    private String schoolYear;
    private Long schoolId;
    private String schoolName;
    private List<StudentSummaryResponseDTO> students;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
