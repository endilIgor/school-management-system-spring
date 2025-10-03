package com.nagi.school_management_system_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagi.school_management_system_spring.dto.GradeRequestDTO;
import com.nagi.school_management_system_spring.dto.GradeResponseDTO;
import com.nagi.school_management_system_spring.exception.InvalidGradeException;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.mapper.GradeMapper;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GradeRepository;
import com.nagi.school_management_system_spring.repository.ReportCardRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ReportCardRepository reportCardRepository;

    @Mock
    private GradeMapper gradeMapper;

    @InjectMocks
    private GradeService gradeService;

    private StudentModel student;
    private SubjectModel subject;
    private ClassroomModel classroom;
    private TeacherModel teacher;
    private GradeModel grade;
    private GradeRequestDTO requestDTO;
    private GradeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        student = new StudentModel();
        student.setId(1L);

        subject = new SubjectModel();
        subject.setId(1L);
        subject.setSubjectName("Mathematics");

        classroom = new ClassroomModel();
        classroom.setId(1L);

        teacher = new TeacherModel();
        teacher.setId(1L);

        grade = new GradeModel();
        grade.setId(1L);
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setClassroom(classroom);
        grade.setTeacher(teacher);
        grade.setValue(new BigDecimal("8.5"));
        grade.setQuarter(QuarterEnum.FIRST);
        grade.setRecordDate(LocalDate.now());

        requestDTO = new GradeRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setSubjectId(1L);
        requestDTO.setClassroomId(1L);
        requestDTO.setTeacherId(1L);
        requestDTO.setValue(new BigDecimal("8.5"));
        requestDTO.setQuarter(QuarterEnum.FIRST);

        responseDTO = new GradeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setValue(new BigDecimal("8.5"));
    }

    @Test
    @DisplayName("Should record grade successfully")
    void testRecordGrade_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(gradeRepository.save(any(GradeModel.class))).thenReturn(grade);
        when(gradeMapper.toResponseDTO(grade)).thenReturn(responseDTO);

        GradeResponseDTO result = gradeService.recordGrade(requestDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("8.5"), result.getValue());
        verify(gradeRepository, times(1)).save(any(GradeModel.class));
    }

    @Test
    @DisplayName("Should throw InvalidGradeException when grade is negative")
    void testRecordGrade_NegativeValue() {
        requestDTO.setValue(new BigDecimal("-1.0"));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        assertThrows(InvalidGradeException.class, () -> gradeService.recordGrade(requestDTO));
    }

    @Test
    @DisplayName("Should throw InvalidGradeException when grade exceeds 10")
    void testRecordGrade_ExceedsMaximum() {
        requestDTO.setValue(new BigDecimal("11.0"));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        assertThrows(InvalidGradeException.class, () -> gradeService.recordGrade(requestDTO));
    }

    @Test
    @DisplayName("Should calculate average correctly")
    void testCalculateAverage_Success() {
        GradeModel grade1 = new GradeModel();
        grade1.setStudent(student);
        grade1.setSubject(subject);
        grade1.setValue(new BigDecimal("8.0"));

        GradeModel grade2 = new GradeModel();
        grade2.setStudent(student);
        grade2.setSubject(subject);
        grade2.setValue(new BigDecimal("9.0"));

        GradeModel grade3 = new GradeModel();
        grade3.setStudent(student);
        grade3.setSubject(subject);
        grade3.setValue(new BigDecimal("7.0"));

        List<GradeModel> grades = Arrays.asList(grade1, grade2, grade3);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(gradeRepository.findByStudentAndQuarter(student, QuarterEnum.FIRST)).thenReturn(grades);

        BigDecimal average = gradeService.calculateAverage(1L, 1L, QuarterEnum.FIRST);

        assertEquals(new BigDecimal("8.00"), average);
    }

    @Test
    @DisplayName("Should return zero when no grades exist")
    void testCalculateAverage_NoGrades() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(gradeRepository.findByStudentAndQuarter(student, QuarterEnum.FIRST)).thenReturn(List.of());

        BigDecimal average = gradeService.calculateAverage(1L, 1L, QuarterEnum.FIRST);

        assertEquals(BigDecimal.ZERO, average);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student not found for average calculation")
    void testCalculateAverage_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> gradeService.calculateAverage(1L, 1L, QuarterEnum.FIRST));
    }

    @Test
    @DisplayName("Should update grade successfully")
    void testUpdateGrade_Success() {
        GradeRequestDTO updateDTO = new GradeRequestDTO();
        updateDTO.setValue(new BigDecimal("9.0"));

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        when(gradeRepository.save(grade)).thenReturn(grade);
        when(gradeMapper.toResponseDTO(grade)).thenReturn(responseDTO);

        GradeResponseDTO result = gradeService.updateGrade(1L, updateDTO);

        assertNotNull(result);
        verify(gradeRepository, times(1)).save(grade);
    }

    @Test
    @DisplayName("Should throw InvalidGradeException when updating with invalid value")
    void testUpdateGrade_InvalidValue() {
        GradeRequestDTO updateDTO = new GradeRequestDTO();
        updateDTO.setValue(new BigDecimal("15.0"));

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));

        assertThrows(InvalidGradeException.class, () -> gradeService.updateGrade(1L, updateDTO));
    }

    @Test
    @DisplayName("Should calculate average with decimal precision")
    void testCalculateAverage_DecimalPrecision() {
        GradeModel grade1 = new GradeModel();
        grade1.setStudent(student);
        grade1.setSubject(subject);
        grade1.setValue(new BigDecimal("7.5"));

        GradeModel grade2 = new GradeModel();
        grade2.setStudent(student);
        grade2.setSubject(subject);
        grade2.setValue(new BigDecimal("8.3"));

        List<GradeModel> grades = Arrays.asList(grade1, grade2);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(gradeRepository.findByStudentAndQuarter(student, QuarterEnum.FIRST)).thenReturn(grades);

        BigDecimal average = gradeService.calculateAverage(1L, 1L, QuarterEnum.FIRST);

        assertEquals(new BigDecimal("7.90"), average);
    }
}
