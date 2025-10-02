package com.nagi.school_management_system_spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.StudentRequestDTO;
import com.nagi.school_management_system_spring.dto.StudentResponseDTO;
import com.nagi.school_management_system_spring.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/id/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/enrollment/{enrollmentNumber}")
    public ResponseEntity<StudentResponseDTO> findByEnrollmentNumber(@PathVariable String enrollmentNumber) {
        StudentResponseDTO student = studentService.findByEnrollmentNumber(enrollmentNumber);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<StudentResponseDTO>> listByClassroom(@PathVariable Long classroomId) {
        List<StudentResponseDTO> students = studentService.listByClassroom(classroomId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/guardian/{guardianId}")
    public ResponseEntity<List<StudentResponseDTO>> listByGuardian(@PathVariable Long guardianId) {
        List<StudentResponseDTO> students = studentService.listByGuardian(guardianId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/academic-history/{enrollmentNumber}")
    public ResponseEntity<Map<String, Object>> findAcademicHistory(@PathVariable String enrollmentNumber) {
        Map<String, Object> history = studentService.findAcademicHistoryByEnrollmentNumber(enrollmentNumber);
        return ResponseEntity.ok(history);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> enrollStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO student = studentService.enrollStudent(requestDTO);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{enrollmentNumber}")
    public ResponseEntity<StudentResponseDTO> updateData(@PathVariable String enrollmentNumber, @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO student = studentService.updateDataByEnrollmentNumber(enrollmentNumber, requestDTO);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{enrollmentNumber}/transfer/{classroomId}")
    public ResponseEntity<StudentResponseDTO> transferStudent(@PathVariable String enrollmentNumber, @PathVariable Long classroomId) {
        StudentResponseDTO student = studentService.transferStudentByEnrollmentNumber(enrollmentNumber, classroomId);
        return ResponseEntity.ok(student);
    }
}
