package com.nagi.school_management_system_spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import com.nagi.school_management_system_spring.model.EventModel;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
import com.nagi.school_management_system_spring.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventModel>> getAllEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) EventTypeEnum type) {

        try {
            if (date != null) {
                List<EventModel> events = eventService.findEventsByDate(date);
                return ResponseEntity.ok(events);
            } else if (type != null) {
                List<EventModel> events = eventService.findEventsByType(type);
                return ResponseEntity.ok(events);
            } else {
                List<EventModel> events = eventService.listEvents();
                return ResponseEntity.ok(events);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/calendar/{month}/{year}")
    public ResponseEntity<List<EventModel>> getEventsByMonthYear(
            @PathVariable int month,
            @PathVariable int year) {
        try {
            List<EventModel> events = eventService.findEventsByMonthYear(month, year);
            return ResponseEntity.ok(events);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    public ResponseEntity<EventModel> createEvent(@Valid @RequestBody EventModel event) {
        try {
            EventModel createdEvent = eventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventModel event) {
        try {
            EventModel updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<Map<String, Object>> notifyParticipants(@PathVariable Long id) {
        try {
            Map<String, Object> notification = eventService.notifyParticipants(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
