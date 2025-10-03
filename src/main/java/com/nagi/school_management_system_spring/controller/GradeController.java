package com.nagi.school_management_system_spring.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.GradeRequestDTO;
import com.nagi.school_management_system_spring.dto.GradeResponseDTO;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.service.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/grades")
@Tag(name = "Grade Management", description = "APIs for recording, updating and calculating student grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponseDTO>> getGradesByStudent(@PathVariable Long studentId) {
        List<GradeResponseDTO> grades = gradeService.findGradesByStudent(studentId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/classroom/{classroomId}/subject/{subjectId}")
    public ResponseEntity<List<GradeResponseDTO>> getGradesByClassroomAndSubject(
            @PathVariable Long classroomId,
            @PathVariable Long subjectId) {
        List<GradeResponseDTO> grades = gradeService.findGradesByClassroom(classroomId, subjectId);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Calculate grade average", description = "Calculate the average grade for a student in a specific subject and quarter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Average calculated successfully"),
        @ApiResponse(responseCode = "404", description = "Student or subject not found")
    })
    @GetMapping("/average")
    public ResponseEntity<BigDecimal> calculateAverage(
            @Parameter(description = "Student ID") @RequestParam Long studentId,
            @Parameter(description = "Subject ID") @RequestParam Long subjectId,
            @Parameter(description = "Quarter (FIRST, SECOND, THIRD, FOURTH)") @RequestParam QuarterEnum quarter) {
        BigDecimal average = gradeService.calculateAverage(studentId, subjectId, quarter);
        return ResponseEntity.ok(average);
    }

    @Operation(summary = "Record new grade", description = "Record a new grade for a student in a specific subject and quarter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grade recorded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid grade value (must be between 0 and 10)"),
        @ApiResponse(responseCode = "404", description = "Student, subject, classroom or teacher not found")
    })
    @PostMapping
    public ResponseEntity<GradeResponseDTO> recordGrade(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Grade details including student, subject, value (0-10), and quarter")
            @Valid @RequestBody GradeRequestDTO requestDTO) {
        GradeResponseDTO grade = gradeService.recordGrade(requestDTO);
        return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequestDTO requestDTO) {
        GradeResponseDTO grade = gradeService.updateGrade(id, requestDTO);
        return ResponseEntity.ok(grade);
    }

    @Operation(summary = "Generate report card", description = "Generate a complete report card with all subject averages and overall status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report card generated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/report-card/{studentId}/{quarter}")
    public ResponseEntity<Map<String, Object>> generateReportCard(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Quarter") @PathVariable QuarterEnum quarter,
            @Parameter(description = "School year") @RequestParam(defaultValue = "2024") String schoolYear) {
        Map<String, Object> reportCard = gradeService.generateReportCard(studentId, quarter, schoolYear);
        return ResponseEntity.ok(reportCard);
    }
}
