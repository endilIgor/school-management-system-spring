package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.AudienceEnum;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
import jakarta.validation.constraints.NotBlank;
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
public class EventRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Event type is required")
    private EventTypeEnum type;

    @NotNull(message = "Audience is required")
    private AudienceEnum audience;
}
