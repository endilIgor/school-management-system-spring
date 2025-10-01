package com.nagi.school_management_system_spring.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.AttendanceModel;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.repository.AttendanceRepository;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Transactional
    public AttendanceModel recordAttendance(AttendanceModel attendance) {
        if (attendance.getDate() == null) {
            attendance.setDate(LocalDate.now());
        }
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public AttendanceModel justifyAbsence(Long attendanceId, String justification) {
        AttendanceModel attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + attendanceId));

        if (Boolean.TRUE.equals(attendance.getPresent())) {
            throw new RuntimeException("Cannot justify absence for a present attendance");
        }

        if (justification == null || justification.trim().isEmpty()) {
            throw new RuntimeException("Justification cannot be empty");
        }

        attendance.setJustification(justification);
        return attendanceRepository.save(attendance);
    }

    public BigDecimal calculateOverallAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        List<AttendanceModel> allAttendances = attendanceRepository.findByStudentAndDateBetween(
                student, startDate, endDate);

        if (allAttendances.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long presentCount = allAttendances.stream()
                .filter(a -> Boolean.TRUE.equals(a.getPresent()))
                .count();

        BigDecimal attendanceRate = BigDecimal.valueOf(presentCount)
                .divide(BigDecimal.valueOf(allAttendances.size()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return attendanceRate.setScale(2, RoundingMode.HALF_UP);
    }

    public List<AttendanceModel> findAttendanceByStudent(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return attendanceRepository.findByStudentAndDateBetween(student, startDate, endDate);
    }

    public List<AttendanceModel> findAttendanceByClassroomAndDate(Long classroomId, LocalDate date) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        return attendanceRepository.findByClassroomAndDate(classroom, date);
    }

    public Map<String, Object> generateAttendanceReport(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        List<AttendanceModel> attendances = attendanceRepository.findByStudentAndDateBetween(
                student, startDate, endDate);

        long totalClasses = attendances.size();
        long presentCount = attendances.stream()
                .filter(a -> Boolean.TRUE.equals(a.getPresent()))
                .count();
        long absentCount = totalClasses - presentCount;
        long justifiedAbsences = attendances.stream()
                .filter(a -> Boolean.FALSE.equals(a.getPresent()) && a.getJustification() != null)
                .count();
        long unjustifiedAbsences = absentCount - justifiedAbsences;

        BigDecimal attendanceRate = totalClasses > 0
                ? BigDecimal.valueOf(presentCount)
                        .divide(BigDecimal.valueOf(totalClasses), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Map<String, Object> report = new HashMap<>();
        report.put("studentId", studentId);
        report.put("studentName", student.getUser() != null ? student.getUser().getName() : "N/A");
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalClasses", totalClasses);
        report.put("presentCount", presentCount);
        report.put("absentCount", absentCount);
        report.put("justifiedAbsences", justifiedAbsences);
        report.put("unjustifiedAbsences", unjustifiedAbsences);
        report.put("attendanceRate", attendanceRate);
        report.put("attendances", attendances);

        return report;
    }
}
