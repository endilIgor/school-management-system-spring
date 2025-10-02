package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Grade is required")
    private String grade;

    @NotNull(message = "Shift is required")
    private ShiftEnum shift;

    @NotBlank(message = "Room is required")
    private String room;

    @NotNull(message = "Max capacity is required")
    @Positive(message = "Max capacity must be positive")
    private Integer maxCapacity;

    private Long homeRoomTeacherId;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotBlank(message = "School year is required")
    private String schoolYear;

    private Long schoolId;
}
