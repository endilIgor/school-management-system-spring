package com.nagi.school_management_system_spring.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.EventModel;
import com.nagi.school_management_system_spring.model.enums.AudienceEnum;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;

public interface EventRepository extends JpaRepository<EventModel, Long>{

    List<EventModel> findByStartDateAfter(LocalDate startDate);

    List<EventModel> findByTypeAndAudience(EventTypeEnum type, AudienceEnum audience);

    List<EventModel> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
