package com.nagi.school_management_system_spring.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/classroom/{classroomId}/grades")
    public ResponseEntity<Map<String, Object>> getGradesByClassroom(@PathVariable Long classroomId) {
        try {
            Map<String, Object> report = reportService.gradesByClassroomReport(classroomId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/classroom/{classroomId}/attendance")
    public ResponseEntity<Map<String, Object>> getAttendanceByClassroom(
            @PathVariable Long classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = reportService.overallAttendanceReport(classroomId, startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/approval/{schoolYear}")
    public ResponseEntity<Map<String, Object>> getApprovalReport(@PathVariable String schoolYear) {
        try {
            Map<String, Object> report = reportService.approvalReport(schoolYear);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dropout")
    public ResponseEntity<Map<String, Object>> getDropoutReport() {
        try {
            Map<String, Object> report = reportService.dropoutReport();
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportToCSV(@RequestParam Long classroomId) {
        try {
            String csv = reportService.exportDataToCSV(classroomId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "grades_report.csv");
            return new ResponseEntity<>(csv, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam Long classroomId) {
        try {
            byte[] excelData = reportService.exportDataToExcel(classroomId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/tab-separated-values"));
            headers.setContentDispositionFormData("attachment", "grades_report.tsv");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
