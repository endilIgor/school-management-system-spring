package com.nagi.school_management_system_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.TeacherRequestDTO;
import com.nagi.school_management_system_spring.dto.TeacherResponseDTO;
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
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<TeacherResponseDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        TeacherResponseDTO teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO requestDTO) {
        TeacherResponseDTO teacher = teacherService.hireTeacher(requestDTO);
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id,
            @Valid @RequestBody TeacherRequestDTO requestDTO) {
        TeacherResponseDTO teacher = teacherService.updateData(id, requestDTO);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/{id}/classrooms")
    public ResponseEntity<List<ClassroomModel>> getTeacherClassrooms(@PathVariable Long id) {
        TeacherModel teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        List<ClassroomModel> classrooms = teacher.getHomeRoomClasses();
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<ClassroomSubjectTeacherModel>> getTeacherSchedule(@PathVariable Long id) {
        TeacherModel teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        List<ClassroomSubjectTeacherModel> schedule = teacher.getClassroomSubjects();
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<TeacherResponseDTO>> getTeachersBySpecialization(
            @PathVariable String specialization) {
        List<TeacherResponseDTO> teachers = teacherService.listBySpecialization(specialization);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}/workload")
    public ResponseEntity<Integer> getTeacherWorkload(@PathVariable Long id) {
        Integer workload = teacherService.findWorkload(id);
        return ResponseEntity.ok(workload);
    }

}
