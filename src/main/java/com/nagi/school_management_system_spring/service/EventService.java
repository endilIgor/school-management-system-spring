package com.nagi.school_management_system_spring.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.EventRequestDTO;
import com.nagi.school_management_system_spring.dto.EventResponseDTO;
import com.nagi.school_management_system_spring.mapper.EventMapper;
import com.nagi.school_management_system_spring.model.EventModel;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
import com.nagi.school_management_system_spring.repository.EventRepository;

import jakarta.transaction.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO requestDTO) {
        if (requestDTO.getStartDate().isAfter(requestDTO.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        EventModel event = new EventModel();
        event.setTitle(requestDTO.getTitle());
        event.setDescription(requestDTO.getDescription());
        event.setStartDate(requestDTO.getStartDate());
        event.setEndDate(requestDTO.getEndDate());
        event.setType(requestDTO.getType());
        event.setAudience(requestDTO.getAudience());

        EventModel savedEvent = eventRepository.save(event);
        return eventMapper.toResponseDTO(savedEvent);
    }

    public List<EventResponseDTO> listEvents() {
        return eventRepository.findAll().stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Map<String, Object> notifyParticipants(Long eventId) {
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        Map<String, Object> notification = new HashMap<>();
        notification.put("eventId", eventId);
        notification.put("eventTitle", event.getTitle());
        notification.put("audience", event.getAudience());
        notification.put("startDate", event.getStartDate());
        notification.put("message", "Notification sent to " + event.getAudience() + " participants");
        notification.put("status", "SUCCESS");

        return notification;
    }

    public List<EventResponseDTO> findEventsByDate(LocalDate date) {
        return eventRepository.findByStartDateBetween(date, date).stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<EventResponseDTO> findEventsByType(EventTypeEnum type) {
        return eventRepository.findAll().stream()
                .filter(e -> e.getType().equals(type))
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventResponseDTO> findEventsByMonthYear(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return eventRepository.findByStartDateBetween(startDate, endDate).stream()
            .map(eventMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO requestDTO) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        if (requestDTO.getTitle() != null) {
            event.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getDescription() != null) {
            event.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getStartDate() != null) {
            event.setStartDate(requestDTO.getStartDate());
        }
        if (requestDTO.getEndDate() != null) {
            event.setEndDate(requestDTO.getEndDate());
        }
        if (requestDTO.getType() != null) {
            event.setType(requestDTO.getType());
        }
        if (requestDTO.getAudience() != null) {
            event.setAudience(requestDTO.getAudience());
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        EventModel updatedEvent = eventRepository.save(event);
        return eventMapper.toResponseDTO(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
    }
}
