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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/grades")
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

    @GetMapping("/average")
    public ResponseEntity<BigDecimal> calculateAverage(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestParam QuarterEnum quarter) {
        BigDecimal average = gradeService.calculateAverage(studentId, subjectId, quarter);
        return ResponseEntity.ok(average);
    }

    @PostMapping
    public ResponseEntity<GradeResponseDTO> recordGrade(@Valid @RequestBody GradeRequestDTO requestDTO) {
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

    @GetMapping("/report-card/{studentId}/{quarter}")
    public ResponseEntity<Map<String, Object>> generateReportCard(
            @PathVariable Long studentId,
            @PathVariable QuarterEnum quarter,
            @RequestParam(defaultValue = "2024") String schoolYear) {
        Map<String, Object> reportCard = gradeService.generateReportCard(studentId, quarter, schoolYear);
        return ResponseEntity.ok(reportCard);
    }
}
