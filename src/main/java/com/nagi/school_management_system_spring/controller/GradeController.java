package com.nagi.school_management_system_spring.controller;

import java.math.BigDecimal;
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

import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.service.GradeService;

@RestController
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeModel>> getGradesByStudent(@PathVariable Long studentId) {
        try {
            List<GradeModel> grades = gradeService.findGradesByStudent(studentId);
            return ResponseEntity.ok(grades);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/classroom/{classroomId}/subject/{subjectId}")
    public ResponseEntity<List<GradeModel>> getGradesByClassroomAndSubject(
            @PathVariable Long classroomId,
            @PathVariable Long subjectId) {
        try {
            List<GradeModel> grades = gradeService.findGradesByClassroom(classroomId, subjectId);
            return ResponseEntity.ok(grades);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/average")
    public ResponseEntity<BigDecimal> calculateAverage(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestParam QuarterEnum quarter) {
        try {
            BigDecimal average = gradeService.calculateAverage(studentId, subjectId, quarter);
            return ResponseEntity.ok(average);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<GradeModel> recordGrade(@RequestBody GradeModel grade) {
        try {
            GradeModel savedGrade = gradeService.recordGrade(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeModel> updateGrade(
            @PathVariable Long id,
            @RequestBody GradeModel grade) {
        try {
            GradeModel updatedGrade = gradeService.updateGrade(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/report-card/{studentId}/{quarter}")
    public ResponseEntity<Map<String, Object>> generateReportCard(
            @PathVariable Long studentId,
            @PathVariable QuarterEnum quarter,
            @RequestParam(defaultValue = "2024") String schoolYear) {
        try {
            Map<String, Object> reportCard = gradeService.generateReportCard(studentId, quarter, schoolYear);
            return ResponseEntity.ok(reportCard);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
