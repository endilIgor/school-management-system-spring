package com.nagi.school_management_system_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.repository.TeacherRepository;
import com.nagi.school_management_system_spring.service.TeacherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping
    public ResponseEntity<List<TeacherModel>> getAllTeachers() {
        List<TeacherModel> teachers = teacherRepository.findAll();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherModel> getTeacherById(@PathVariable Long id) {
        try {
            TeacherModel teacher = teacherRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
            return ResponseEntity.ok(teacher);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<TeacherModel> createTeacher(@Valid @RequestBody TeacherModel teacher) {
        try {
            TeacherModel createdTeacher = teacherService.hireTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherModel> updateTeacher(@PathVariable Long id,
            @Valid @RequestBody TeacherModel teacher) {
        try {
            TeacherModel updatedTeacher = teacherService.updateData(id, teacher);
            return ResponseEntity.ok(updatedTeacher);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/classrooms")
    public ResponseEntity<List<ClassroomModel>> getTeacherClassrooms(@PathVariable Long id) {
        try {
            TeacherModel teacher = teacherRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
            List<ClassroomModel> classrooms = teacher.getHomeRoomClasses();
            return ResponseEntity.ok(classrooms);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<ClassroomSubjectTeacherModel>> getTeacherSchedule(@PathVariable Long id) {
        try {
            TeacherModel teacher = teacherRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
            List<ClassroomSubjectTeacherModel> schedule = teacher.getClassroomSubjects();
            return ResponseEntity.ok(schedule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<TeacherModel>> getTeachersBySpecialization(
            @PathVariable String specialization) {
        List<TeacherModel> teachers = teacherService.listBySpecialization(specialization);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}/workload")
    public ResponseEntity<Integer> getTeacherWorkload(@PathVariable Long id) {
        try {
            Integer workload = teacherService.findWorkload(id);
            return ResponseEntity.ok(workload);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
