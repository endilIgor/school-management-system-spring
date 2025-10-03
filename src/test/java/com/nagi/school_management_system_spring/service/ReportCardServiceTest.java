package com.nagi.school_management_system_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.school_management_system_spring.model.AttendanceModel;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.ReportCardStatusEnum;
import com.nagi.school_management_system_spring.repository.AttendanceRepository;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GradeRepository;
import com.nagi.school_management_system_spring.repository.ReportCardRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class ReportCardServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ReportCardRepository reportCardRepository;

    @InjectMocks
    private ReportCardService reportCardService;

    private ClassroomModel classroom;
    private StudentModel student1;
    private StudentModel student2;
    private UserModel user1;
    private UserModel user2;

    @BeforeEach
    void setUp() {
        classroom = new ClassroomModel();
        classroom.setId(1L);
        classroom.setName("Class A");

        user1 = new UserModel();
        user1.setName("John Doe");

        user2 = new UserModel();
        user2.setName("Jane Smith");

        student1 = new StudentModel();
        student1.setId(1L);
        student1.setEnrollmentNumber("ENR001");
        student1.setUser(user1);

        student2 = new StudentModel();
        student2.setId(2L);
        student2.setEnrollmentNumber("ENR002");
        student2.setUser(user2);
    }

    @Test
    @DisplayName("Should generate grades report by classroom")
    void testGradesByClassroomReport_Success() {
        GradeModel grade1 = new GradeModel();
        grade1.setValue(new BigDecimal("8.0"));

        GradeModel grade2 = new GradeModel();
        grade2.setValue(new BigDecimal("9.0"));

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(Arrays.asList(student1, student2));
        when(gradeRepository.findByStudent(student1)).thenReturn(Arrays.asList(grade1, grade2));
        when(gradeRepository.findByStudent(student2)).thenReturn(List.of());

        Map<String, Object> report = reportCardService.gradesByClassroomReport(1L);

        assertNotNull(report);
        assertEquals(1L, report.get("classroomId"));
        assertEquals("Class A", report.get("classroomName"));
        assertEquals(2, report.get("totalStudents"));
    }

    @Test
    @DisplayName("Should calculate average correctly in classroom report")
    void testGradesByClassroomReport_CalculateAverage() {
        GradeModel grade1 = new GradeModel();
        grade1.setValue(new BigDecimal("8.0"));

        GradeModel grade2 = new GradeModel();
        grade2.setValue(new BigDecimal("6.0"));

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of(student1));
        when(gradeRepository.findByStudent(student1)).thenReturn(Arrays.asList(grade1, grade2));

        Map<String, Object> report = reportCardService.gradesByClassroomReport(1L);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> studentGrades = (List<Map<String, Object>>) report.get("studentGrades");

        assertEquals(new BigDecimal("7.00"), studentGrades.get(0).get("average"));
    }

    @Test
    @DisplayName("Should generate overall attendance report")
    void testOverallAttendanceReport_Success() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        AttendanceModel att1 = createAttendance(student1, true);
        AttendanceModel att2 = createAttendance(student1, true);
        AttendanceModel att3 = createAttendance(student1, false);
        AttendanceModel att4 = createAttendance(student1, true);

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of(student1));
        when(attendanceRepository.findByStudentAndDateBetween(student1, startDate, endDate))
            .thenReturn(Arrays.asList(att1, att2, att3, att4));

        Map<String, Object> report = reportCardService.overallAttendanceReport(1L, startDate, endDate);

        assertNotNull(report);
        assertEquals(1L, report.get("classroomId"));
        assertEquals("Class A", report.get("classroomName"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> studentAttendances = (List<Map<String, Object>>) report.get("studentAttendances");

        assertEquals(4L, studentAttendances.get(0).get("totalClasses"));
        assertEquals(3L, studentAttendances.get(0).get("presentCount"));
        assertEquals(new BigDecimal("75.00"), studentAttendances.get(0).get("attendanceRate"));
    }

    @Test
    @DisplayName("Should generate approval report with correct statistics")
    void testApprovalReport_Success() {
        ReportCardModel rc1 = createReportCard(ReportCardStatusEnum.APPROVED);
        ReportCardModel rc2 = createReportCard(ReportCardStatusEnum.APPROVED);
        ReportCardModel rc3 = createReportCard(ReportCardStatusEnum.FAILED);
        ReportCardModel rc4 = createReportCard(ReportCardStatusEnum.RECOVERY);

        when(reportCardRepository.findAll()).thenReturn(Arrays.asList(rc1, rc2, rc3, rc4));

        Map<String, Object> report = reportCardService.approvalReport("2025");

        assertNotNull(report);
        assertEquals("2025", report.get("schoolYear"));
        assertEquals(4, report.get("totalStudents"));
        assertEquals(2L, report.get("approved"));
        assertEquals(1L, report.get("failed"));
        assertEquals(1L, report.get("recovery"));
        assertEquals(new BigDecimal("50.00"), report.get("approvalRate"));
    }

    @Test
    @DisplayName("Should generate dropout report with correct statistics")
    void testDropoutReport_Success() {
        student1.setStatus(null);
        student2.setStatus(null);

        StudentModel inactiveStudent = new StudentModel();
        inactiveStudent.setId(3L);

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2, inactiveStudent));

        Map<String, Object> report = reportCardService.dropoutReport();

        assertNotNull(report);
        assertEquals(3, report.get("totalStudents"));
    }

    @Test
    @DisplayName("Should export data to CSV format")
    void testExportDataToCSV_Success() {
        GradeModel grade1 = new GradeModel();
        grade1.setValue(new BigDecimal("8.5"));

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of(student1));
        when(gradeRepository.findByStudent(student1)).thenReturn(List.of(grade1));

        String csv = reportCardService.exportDataToCSV(1L);

        assertNotNull(csv);
        assertEquals(true, csv.contains("Student ID,Student Name,Enrollment Number,Average"));
        assertEquals(true, csv.contains("John Doe"));
        assertEquals(true, csv.contains("ENR001"));
    }

    @Test
    @DisplayName("Should export data to Excel format")
    void testExportDataToExcel_Success() {
        GradeModel grade1 = new GradeModel();
        grade1.setValue(new BigDecimal("9.0"));

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of(student1));
        when(gradeRepository.findByStudent(student1)).thenReturn(List.of(grade1));

        byte[] excel = reportCardService.exportDataToExcel(1L);

        assertNotNull(excel);
        assertEquals(true, excel.length > 0);
    }

    @Test
    @DisplayName("Should handle empty grades in classroom report")
    void testGradesByClassroomReport_EmptyGrades() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of(student1));
        when(gradeRepository.findByStudent(student1)).thenReturn(List.of());

        Map<String, Object> report = reportCardService.gradesByClassroomReport(1L);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> studentGrades = (List<Map<String, Object>>) report.get("studentGrades");

        assertEquals(BigDecimal.ZERO, studentGrades.get(0).get("average"));
    }

    private AttendanceModel createAttendance(StudentModel student, boolean present) {
        AttendanceModel attendance = new AttendanceModel();
        attendance.setStudent(student);
        attendance.setPresent(present);
        attendance.setDate(LocalDate.now());
        return attendance;
    }

    private ReportCardModel createReportCard(ReportCardStatusEnum status) {
        ReportCardModel rc = new ReportCardModel();
        rc.setStatus(status);
        rc.setSchoolYear("2025");
        return rc;
    }
}
