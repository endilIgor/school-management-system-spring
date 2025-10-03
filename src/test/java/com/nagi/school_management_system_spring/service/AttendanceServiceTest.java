package com.nagi.school_management_system_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private AttendanceMapper attendanceMapper;

    @InjectMocks
    private AttendanceService attendanceService;

    private StudentModel student;
    private ClassroomModel classroom;
    private SubjectModel subject;
    private TeacherModel teacher;
    private AttendanceModel attendance;
    private AttendanceRequestDTO requestDTO;
    private AttendanceResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        student = new StudentModel();
        student.setId(1L);

        classroom = new ClassroomModel();
        classroom.setId(1L);

        subject = new SubjectModel();
        subject.setId(1L);

        teacher = new TeacherModel();
        teacher.setId(1L);

        attendance = new AttendanceModel();
        attendance.setId(1L);
        attendance.setStudent(student);
        attendance.setClassroom(classroom);
        attendance.setSubject(subject);
        attendance.setTeacher(teacher);
        attendance.setDate(LocalDate.now());
        attendance.setPresent(true);

        requestDTO = new AttendanceRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setClassroomId(1L);
        requestDTO.setSubjectId(1L);
        requestDTO.setTeacherId(1L);
        requestDTO.setPresent(true);

        responseDTO = new AttendanceResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setPresent(true);
    }

    @Test
    @DisplayName("Should record attendance successfully")
    void testRecordAttendance_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(attendanceRepository.save(any(AttendanceModel.class))).thenReturn(attendance);
        when(attendanceMapper.toResponseDTO(attendance)).thenReturn(responseDTO);

        AttendanceResponseDTO result = attendanceService.recordAttendance(requestDTO);

        assertNotNull(result);
        assertEquals(true, result.getPresent());
        verify(attendanceRepository, times(1)).save(any(AttendanceModel.class));
    }

    @Test
    @DisplayName("Should calculate overall attendance correctly with 100% presence")
    void testCalculateOverallAttendance_FullPresence() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        AttendanceModel att1 = createAttendance(true);
        AttendanceModel att2 = createAttendance(true);
        AttendanceModel att3 = createAttendance(true);
        AttendanceModel att4 = createAttendance(true);

        List<AttendanceModel> attendances = Arrays.asList(att1, att2, att3, att4);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentAndDateBetween(eq(student), eq(startDate), eq(endDate)))
            .thenReturn(attendances);

        BigDecimal result = attendanceService.calculateOverallAttendance(1L, startDate, endDate);

        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    @DisplayName("Should calculate overall attendance correctly with 75% presence")
    void testCalculateOverallAttendance_PartialPresence() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        AttendanceModel att1 = createAttendance(true);
        AttendanceModel att2 = createAttendance(true);
        AttendanceModel att3 = createAttendance(true);
        AttendanceModel att4 = createAttendance(false);

        List<AttendanceModel> attendances = Arrays.asList(att1, att2, att3, att4);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentAndDateBetween(eq(student), eq(startDate), eq(endDate)))
            .thenReturn(attendances);

        BigDecimal result = attendanceService.calculateOverallAttendance(1L, startDate, endDate);

        assertEquals(new BigDecimal("75.00"), result);
    }

    @Test
    @DisplayName("Should calculate overall attendance correctly with 50% presence")
    void testCalculateOverallAttendance_HalfPresence() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        AttendanceModel att1 = createAttendance(true);
        AttendanceModel att2 = createAttendance(false);
        AttendanceModel att3 = createAttendance(true);
        AttendanceModel att4 = createAttendance(false);

        List<AttendanceModel> attendances = Arrays.asList(att1, att2, att3, att4);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentAndDateBetween(eq(student), eq(startDate), eq(endDate)))
            .thenReturn(attendances);

        BigDecimal result = attendanceService.calculateOverallAttendance(1L, startDate, endDate);

        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    @DisplayName("Should return zero when no attendance records exist")
    void testCalculateOverallAttendance_NoRecords() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentAndDateBetween(eq(student), eq(startDate), eq(endDate)))
            .thenReturn(List.of());

        BigDecimal result = attendanceService.calculateOverallAttendance(1L, startDate, endDate);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should justify absence successfully")
    void testJustifyAbsence_Success() {
        attendance.setPresent(false);
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(attendance)).thenReturn(attendance);
        when(attendanceMapper.toResponseDTO(attendance)).thenReturn(responseDTO);

        AttendanceResponseDTO result = attendanceService.justifyAbsence(1L, "Medical appointment");

        assertNotNull(result);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    @DisplayName("Should throw exception when justifying a present attendance")
    void testJustifyAbsence_PresentAttendance() {
        attendance.setPresent(true);
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        assertThrows(IllegalArgumentException.class,
            () -> attendanceService.justifyAbsence(1L, "Some justification"));
    }

    @Test
    @DisplayName("Should throw exception when justification is empty")
    void testJustifyAbsence_EmptyJustification() {
        attendance.setPresent(false);
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        assertThrows(IllegalArgumentException.class,
            () -> attendanceService.justifyAbsence(1L, ""));
    }

    @Test
    @DisplayName("Should generate attendance report correctly")
    void testGenerateAttendanceReport_Success() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        AttendanceModel att1 = createAttendance(true);
        AttendanceModel att2 = createAttendance(false);
        att2.setJustification("Medical");
        AttendanceModel att3 = createAttendance(true);
        AttendanceModel att4 = createAttendance(false);

        List<AttendanceModel> attendances = Arrays.asList(att1, att2, att3, att4);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentAndDateBetween(eq(student), eq(startDate), eq(endDate)))
            .thenReturn(attendances);

        Map<String, Object> report = attendanceService.generateAttendanceReport(1L, startDate, endDate);

        assertNotNull(report);
        assertEquals(4L, report.get("totalClasses"));
        assertEquals(2L, report.get("presentCount"));
        assertEquals(2L, report.get("absentCount"));
        assertEquals(1L, report.get("justifiedAbsences"));
        assertEquals(1L, report.get("unjustifiedAbsences"));
        assertEquals(new BigDecimal("50.00"), report.get("attendanceRate"));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student not found")
    void testCalculateOverallAttendance_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> attendanceService.calculateOverallAttendance(1L, LocalDate.now(), LocalDate.now()));
    }

    private AttendanceModel createAttendance(boolean present) {
        AttendanceModel att = new AttendanceModel();
        att.setStudent(student);
        att.setPresent(present);
        att.setDate(LocalDate.now());
        return att;
    }
}
