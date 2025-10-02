package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.EventResponseDTO;
import com.nagi.school_management_system_spring.model.EventModel;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponseDTO toResponseDTO(EventModel event) {
        if (event == null) {
            return null;
        }

        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setType(event.getType());
        dto.setAudience(event.getAudience());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());

        return dto;
    }
}
