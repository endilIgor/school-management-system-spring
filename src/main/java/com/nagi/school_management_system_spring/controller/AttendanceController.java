package com.nagi.school_management_system_spring.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.AttendanceRequestDTO;
import com.nagi.school_management_system_spring.dto.AttendanceResponseDTO;
import com.nagi.school_management_system_spring.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByStudent(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceResponseDTO> attendances = attendanceService.findAttendanceByStudent(
                studentId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/{studentId}/rate")
    public ResponseEntity<BigDecimal> getAttendanceRate(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal rate = attendanceService.calculateOverallAttendance(studentId, startDate, endDate);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/student/{studentId}/report")
    public ResponseEntity<Map<String, Object>> getAttendanceReport(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> report = attendanceService.generateAttendanceReport(
                studentId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/classroom/{classroomId}/date/{date}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByClassroomAndDate(
            @PathVariable Long classroomId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponseDTO> attendances = attendanceService.findAttendanceByClassroomAndDate(
                classroomId, date);
        return ResponseEntity.ok(attendances);
    }

    @PostMapping("/record")
    public ResponseEntity<AttendanceResponseDTO> recordAttendance(@Valid @RequestBody AttendanceRequestDTO requestDTO) {
        AttendanceResponseDTO attendance = attendanceService.recordAttendance(requestDTO);
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/{id}/justify")
    public ResponseEntity<AttendanceResponseDTO> justifyAbsence(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String justification = request.get("justification");
        if (justification == null || justification.trim().isEmpty()) {
            throw new RuntimeException("Justification cannot be empty");
        }
        AttendanceResponseDTO attendance = attendanceService.justifyAbsence(id, justification);
        return ResponseEntity.ok(attendance);
    }
}
