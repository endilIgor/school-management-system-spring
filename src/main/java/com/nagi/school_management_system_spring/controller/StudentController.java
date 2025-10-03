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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
@Tag(name = "Student Management", description = "APIs for managing student enrollment, transfers, and academic records")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Get student by ID", description = "Retrieve student information by their database ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found successfully",
                content = @Content(schema = @Schema(implementation = StudentResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @Parameter(description = "Student database ID") @PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Find student by enrollment number", description = "Retrieve student information using their unique enrollment number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found with given enrollment number")
    })
    @GetMapping("/enrollment/{enrollmentNumber}")
    public ResponseEntity<StudentResponseDTO> findByEnrollmentNumber(
            @Parameter(description = "Student enrollment number (format: YYYY######)") @PathVariable String enrollmentNumber) {
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

    @Operation(summary = "Enroll new student", description = "Register a new student in the system with user, guardian and classroom assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student enrolled successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or student already enrolled"),
        @ApiResponse(responseCode = "409", description = "Classroom is full")
    })
    @PostMapping
    public ResponseEntity<StudentResponseDTO> enrollStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student enrollment details")
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO student = studentService.enrollStudent(requestDTO);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{enrollmentNumber}")
    public ResponseEntity<StudentResponseDTO> updateData(@PathVariable String enrollmentNumber, @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO student = studentService.updateDataByEnrollmentNumber(enrollmentNumber, requestDTO);
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Transfer student to another classroom", description = "Move a student from their current classroom to a new one")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student transferred successfully"),
        @ApiResponse(responseCode = "404", description = "Student or classroom not found"),
        @ApiResponse(responseCode = "409", description = "Target classroom is full")
    })
    @PutMapping("/{enrollmentNumber}/transfer/{classroomId}")
    public ResponseEntity<StudentResponseDTO> transferStudent(
            @Parameter(description = "Student enrollment number") @PathVariable String enrollmentNumber,
            @Parameter(description = "Target classroom ID") @PathVariable Long classroomId) {
        StudentResponseDTO student = studentService.transferStudentByEnrollmentNumber(enrollmentNumber, classroomId);
        return ResponseEntity.ok(student);
    }
}
