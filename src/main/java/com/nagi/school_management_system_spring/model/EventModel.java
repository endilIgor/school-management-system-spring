package com.nagi.school_management_system_spring.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nagi.school_management_system_spring.model.enums.AudienceEnum;
import com.nagi.school_management_system_spring.model.enums.EventTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory!!")
    private String title;

    private String description;

    @NotNull(message = "Start date is mandatory!!")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory!!")
    private LocalDate endDate;

    @NotNull(message = "Event type is mandatory!!")
    @Enumerated(EnumType.STRING)
    private EventTypeEnum type;

    @NotNull(message = "Audience is mandatory!!")
    @Enumerated(EnumType.STRING)
    private AudienceEnum audience;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
