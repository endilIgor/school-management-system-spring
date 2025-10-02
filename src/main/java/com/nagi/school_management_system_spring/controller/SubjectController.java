package com.nagi.school_management_system_spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.service.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<List<SubjectModel>> getAllSubjects(
            @RequestParam(required = false) Boolean active) {
        List<SubjectModel> subjects;

        if (active != null && active) {
            subjects = subjectRepository.findByIsActiveTrue();
        } else {
            subjects = subjectRepository.findAll();
        }

        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectModel> getSubjectById(@PathVariable Long id) {
        try {
            SubjectModel subject = subjectRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<SubjectModel> createSubject(@Valid @RequestBody SubjectModel subject) {
        try {
            SubjectModel createdSubject = subjectService.createSubject(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectModel> updateSubject(@PathVariable Long id,
            @Valid @RequestBody SubjectModel subject) {
        try {
            SubjectModel updatedSubject = subjectService.updateData(id, subject);
            return ResponseEntity.ok(updatedSubject);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{subjectId}/assign-teacher")
    public ResponseEntity<ClassroomSubjectTeacherModel> assignTeacher(
            @PathVariable Long subjectId,
            @RequestBody Map<String, Object> assignmentData) {
        try {
            Long teacherId = Long.valueOf(assignmentData.get("teacherId").toString());
            Long classroomId = Long.valueOf(assignmentData.get("classroomId").toString());
            String schoolYear = assignmentData.get("schoolYear").toString();

            ClassroomSubjectTeacherModel assignment = subjectService.assignTeacher(
                    subjectId, teacherId, classroomId, schoolYear);
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/workload")
    public ResponseEntity<SubjectModel> setWorkload(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> workloadData) {
        try {
            Integer workload = workloadData.get("workload");
            SubjectModel subject = subjectService.setWorkload(id, workload);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<SubjectModel>> getSubjectsByClassroom(@PathVariable Long classroomId) {
        try {
            List<SubjectModel> subjects = subjectService.listByClassroom(classroomId);
            return ResponseEntity.ok(subjects);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<SubjectModel>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        try {
            List<SubjectModel> subjects = subjectService.findByTeacher(teacherId);
            return ResponseEntity.ok(subjects);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
