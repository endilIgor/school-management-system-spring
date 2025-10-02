package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.AudienceEnum;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
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
public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventTypeEnum type;
    private AudienceEnum audience;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
