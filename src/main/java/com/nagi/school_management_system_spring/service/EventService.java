package com.nagi.school_management_system_spring.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.EventModel;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;
import com.nagi.school_management_system_spring.repository.EventRepository;

import jakarta.transaction.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public EventModel createEvent(EventModel event) {
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        return eventRepository.save(event);
    }

    public List<EventModel> listEvents() {
        return eventRepository.findAll();
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

    public List<EventModel> findEventsByDate(LocalDate date) {
        return eventRepository.findByStartDateBetween(date, date);
    }

    public List<EventModel> findEventsByType(EventTypeEnum type) {
        return eventRepository.findAll().stream()
                .filter(e -> e.getType().equals(type))
                .toList();
    }

    public List<EventModel> findEventsByMonthYear(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return eventRepository.findByStartDateBetween(startDate, endDate);
    }

    @Transactional
    public EventModel updateEvent(Long id, EventModel updatedEvent) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }
        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getStartDate() != null) {
            event.setStartDate(updatedEvent.getStartDate());
        }
        if (updatedEvent.getEndDate() != null) {
            event.setEndDate(updatedEvent.getEndDate());
        }
        if (updatedEvent.getType() != null) {
            event.setType(updatedEvent.getType());
        }
        if (updatedEvent.getAudience() != null) {
            event.setAudience(updatedEvent.getAudience());
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
    }
}
