package com.nagi.school_management_system_spring.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nagi.school_management_system_spring.model.enums.ShiftEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classrooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassroomModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory!!")
    private String name;

    @NotBlank(message = "Grade is mandatory!!")
    private String grade;

    @NotNull(message = "Shift is mandatory!!")
    @Enumerated(EnumType.STRING)
    private ShiftEnum shift;

    @NotBlank(message = "Room is mandatory!!")
    private String room;

    @NotNull(message = "Max Capacity is mandatory!!")
    private Integer maxCapacity;

    @ManyToOne
    @JoinColumn(name = "home_room_teacher_id")
    private TeacherModel homeRoomTeacher;

    @NotNull(message = "Active status is mandatory!!")
    private Boolean isActive;

    @NotBlank(message = "School Year is mandatory!!")
    private String schoolYear;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentModel> students;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClassroomSubjectTeacherModel> classroomSubjects;
}
