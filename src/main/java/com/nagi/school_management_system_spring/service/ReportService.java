package com.nagi.school_management_system_spring.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.AttendanceModel;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.enums.ReportCardStatusEnum;
import com.nagi.school_management_system_spring.repository.AttendanceRepository;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GradeRepository;
import com.nagi.school_management_system_spring.repository.ReportCardRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;

@Service
public class ReportService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ReportCardRepository reportCardRepository;

    public Map<String, Object> gradesByClassroomReport(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        List<StudentModel> students = studentRepository.findByClassroom(classroom);
        List<Map<String, Object>> studentGrades = new ArrayList<>();

        for (StudentModel student : students) {
            List<GradeModel> grades = gradeRepository.findByStudent(student);

            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", student.getId());
            studentData.put("studentName", student.getUser() != null ? student.getUser().getName() : "N/A");
            studentData.put("enrollmentNumber", student.getEnrollmentNumber());
            studentData.put("grades", grades);

            if (!grades.isEmpty()) {
                BigDecimal average = grades.stream()
                        .map(GradeModel::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(grades.size()), 2, RoundingMode.HALF_UP);
                studentData.put("average", average);
            } else {
                studentData.put("average", BigDecimal.ZERO);
            }

            studentGrades.add(studentData);
        }

        Map<String, Object> report = new HashMap<>();
        report.put("classroomId", classroomId);
        report.put("classroomName", classroom.getName());
        report.put("totalStudents", students.size());
        report.put("studentGrades", studentGrades);

        return report;
    }

    public Map<String, Object> overallAttendanceReport(Long classroomId, LocalDate startDate, LocalDate endDate) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        List<StudentModel> students = studentRepository.findByClassroom(classroom);
        List<Map<String, Object>> studentAttendances = new ArrayList<>();

        for (StudentModel student : students) {
            List<AttendanceModel> attendances = attendanceRepository.findByStudentAndDateBetween(
                    student, startDate, endDate);

            long totalClasses = attendances.size();
            long presentCount = attendances.stream()
                    .filter(a -> Boolean.TRUE.equals(a.getPresent()))
                    .count();

            BigDecimal attendanceRate = totalClasses > 0
                    ? BigDecimal.valueOf(presentCount)
                            .divide(BigDecimal.valueOf(totalClasses), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", student.getId());
            studentData.put("studentName", student.getUser() != null ? student.getUser().getName() : "N/A");
            studentData.put("enrollmentNumber", student.getEnrollmentNumber());
            studentData.put("totalClasses", totalClasses);
            studentData.put("presentCount", presentCount);
            studentData.put("attendanceRate", attendanceRate);

            studentAttendances.add(studentData);
        }

        Map<String, Object> report = new HashMap<>();
        report.put("classroomId", classroomId);
        report.put("classroomName", classroom.getName());
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalStudents", students.size());
        report.put("studentAttendances", studentAttendances);

        return report;
    }

    public Map<String, Object> approvalReport(String schoolYear) {
        List<ReportCardModel> reportCards = reportCardRepository.findAll().stream()
                .filter(rc -> rc.getSchoolYear().equals(schoolYear))
                .toList();

        long approved = reportCards.stream()
                .filter(rc -> rc.getStatus() == ReportCardStatusEnum.APPROVED)
                .count();

        long failed = reportCards.stream()
                .filter(rc -> rc.getStatus() == ReportCardStatusEnum.FAILED)
                .count();

        long recovery = reportCards.stream()
                .filter(rc -> rc.getStatus() == ReportCardStatusEnum.RECOVERY)
                .count();

        long studying = reportCards.stream()
                .filter(rc -> rc.getStatus() == ReportCardStatusEnum.STUDYING)
                .count();

        BigDecimal approvalRate = reportCards.size() > 0
                ? BigDecimal.valueOf(approved)
                        .divide(BigDecimal.valueOf(reportCards.size()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Map<String, Object> report = new HashMap<>();
        report.put("schoolYear", schoolYear);
        report.put("totalStudents", reportCards.size());
        report.put("approved", approved);
        report.put("failed", failed);
        report.put("recovery", recovery);
        report.put("studying", studying);
        report.put("approvalRate", approvalRate);

        return report;
    }

    public Map<String, Object> dropoutReport() {
        List<StudentModel> allStudents = studentRepository.findAll();

        long inactiveStudents = allStudents.stream()
                .filter(s -> s.getStatus() != null && s.getStatus().name().equals("INACTIVE"))
                .count();

        BigDecimal dropoutRate = allStudents.size() > 0
                ? BigDecimal.valueOf(inactiveStudents)
                        .divide(BigDecimal.valueOf(allStudents.size()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Map<String, Object> report = new HashMap<>();
        report.put("totalStudents", allStudents.size());
        report.put("inactiveStudents", inactiveStudents);
        report.put("dropoutRate", dropoutRate);

        return report;
    }

    public String exportDataToCSV(Long classroomId) {
        Map<String, Object> data = gradesByClassroomReport(classroomId);
        StringBuilder csv = new StringBuilder();

        csv.append("Student ID,Student Name,Enrollment Number,Average\n");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> studentGrades = (List<Map<String, Object>>) data.get("studentGrades");

        for (Map<String, Object> student : studentGrades) {
            csv.append(student.get("studentId")).append(",");
            csv.append(student.get("studentName")).append(",");
            csv.append(student.get("enrollmentNumber")).append(",");
            csv.append(student.get("average")).append("\n");
        }

        return csv.toString();
    }

    public byte[] exportDataToExcel(Long classroomId) {
        Map<String, Object> data = gradesByClassroomReport(classroomId);

        StringBuilder excel = new StringBuilder();
        excel.append("Student ID\tStudent Name\tEnrollment Number\tAverage\n");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> studentGrades = (List<Map<String, Object>>) data.get("studentGrades");

        for (Map<String, Object> student : studentGrades) {
            excel.append(student.get("studentId")).append("\t");
            excel.append(student.get("studentName")).append("\t");
            excel.append(student.get("enrollmentNumber")).append("\t");
            excel.append(student.get("average")).append("\n");
        }

        return excel.toString().getBytes();
    }
}
