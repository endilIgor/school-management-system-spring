package com.nagi.school_management_system_spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.EventRequestDTO;
import com.nagi.school_management_system_spring.dto.EventResponseDTO;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
import com.nagi.school_management_system_spring.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) EventTypeEnum type) {

        List<EventResponseDTO> events;
        if (date != null) {
            events = eventService.findEventsByDate(date);
        } else if (type != null) {
            events = eventService.findEventsByType(type);
        } else {
            events = eventService.listEvents();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/calendar/{month}/{year}")
    public ResponseEntity<List<EventResponseDTO>> getEventsByMonthYear(
            @PathVariable int month,
            @PathVariable int year) {
        List<EventResponseDTO> events = eventService.findEventsByMonthYear(month, year);
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO requestDTO) {
        EventResponseDTO event = eventService.createEvent(requestDTO);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO requestDTO) {
        EventResponseDTO event = eventService.updateEvent(id, requestDTO);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Map<String, Object>> notifyParticipants(@PathVariable Long id) {
        Map<String, Object> notification = eventService.notifyParticipants(id);
        return ResponseEntity.ok(notification);
    }
}
