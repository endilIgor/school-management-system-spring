package com.nagi.school_management_system_spring.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.AttendanceRequestDTO;
import com.nagi.school_management_system_spring.dto.AttendanceResponseDTO;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.mapper.AttendanceMapper;
import com.nagi.school_management_system_spring.model.AttendanceModel;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.repository.AttendanceRepository;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Transactional
    public AttendanceResponseDTO recordAttendance(AttendanceRequestDTO requestDTO) {
        StudentModel student = studentRepository.findById(requestDTO.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + requestDTO.getStudentId()));

        ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + requestDTO.getClassroomId()));

        SubjectModel subject = subjectRepository.findById(requestDTO.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + requestDTO.getSubjectId()));

        TeacherModel teacher = teacherRepository.findById(requestDTO.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + requestDTO.getTeacherId()));

        AttendanceModel attendance = new AttendanceModel();
        attendance.setStudent(student);
        attendance.setClassroom(classroom);
        attendance.setSubject(subject);
        attendance.setDate(requestDTO.getDate() != null ? requestDTO.getDate() : LocalDate.now());
        attendance.setPresent(requestDTO.getPresent());
        attendance.setJustification(requestDTO.getJustification());
        attendance.setTeacher(teacher);

        AttendanceModel savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toResponseDTO(savedAttendance);
    }

    @Transactional
    public AttendanceResponseDTO justifyAbsence(Long attendanceId, String justification) {
        AttendanceModel attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + attendanceId));

        if (Boolean.TRUE.equals(attendance.getPresent())) {
            throw new IllegalArgumentException("Cannot justify absence for a present attendance");
        }

        if (justification == null || justification.trim().isEmpty()) {
            throw new IllegalArgumentException("Justification cannot be empty");
        }

        attendance.setJustification(justification);
        AttendanceModel updatedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toResponseDTO(updatedAttendance);
    }

    public BigDecimal calculateOverallAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

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

    public List<AttendanceResponseDTO> findAttendanceByStudent(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        return attendanceRepository.findByStudentAndDateBetween(student, startDate, endDate).stream()
            .map(attendanceMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<AttendanceResponseDTO> findAttendanceByClassroomAndDate(Long classroomId, LocalDate date) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        return attendanceRepository.findByClassroomAndDate(classroom, date).stream()
            .map(attendanceMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Map<String, Object> generateAttendanceReport(Long studentId, LocalDate startDate, LocalDate endDate) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

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
