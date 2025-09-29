package com.nagi.school_management_system_spring.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @GetMapping("/id/{id}")
    public Optional<StudentModel> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/enrollment/{enrollmentNumber}")
    public Optional<StudentModel> findByEnrollmentNumber(@PathVariable String enrollmentNumber) {
        return studentService.findByEnrollmentNumber(enrollmentNumber);
    }

    @GetMapping("/classroom/{classroomId}")
    public List<StudentModel> listByClassroom(@PathVariable Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new RuntimeException("Classroom not found: " + classroomId));
        return studentService.listByClassroom(classroom);
    }

    @GetMapping("/academic-history/{enrollmentNumber}")
    public Map<String, Object> findAcademicHistory(@PathVariable String enrollmentNumber) {
        return studentService.findAcademicHistoryByEnrollmentNumber(enrollmentNumber);
    }

    @PostMapping
    public StudentModel enrollStudent(@RequestBody StudentModel student) {
        return studentService.enrollStudent(student);
    }

    @PutMapping("/{enrollmentNumber}")
    public StudentModel updateData(@PathVariable String enrollmentNumber, @RequestBody StudentModel updateStudent) {
        return studentService.updateDataByEnrollmentNumber(enrollmentNumber, updateStudent);
    }

    @PutMapping("/{enrollmentNumber}/transfer/{classroomId}")
    public StudentModel transferStudent(@PathVariable String enrollmentNumber, @PathVariable Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new RuntimeException("Classroom not found: " + classroomId));
        return studentService.transferStudentByEnrollmentNumber(enrollmentNumber, classroom);
    }
}
