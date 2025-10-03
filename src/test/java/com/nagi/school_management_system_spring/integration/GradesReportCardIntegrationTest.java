package com.nagi.school_management_system_spring.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.nagi.school_management_system_spring.dto.GradeRequestDTO;
import com.nagi.school_management_system_spring.dto.GradeResponseDTO;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.EvaluationTypeEnum;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;
import com.nagi.school_management_system_spring.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GradesReportCardIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    private StudentModel student;
    private SubjectModel subject;
    private TeacherModel teacher;
    private ClassroomModel classroom;

    @BeforeEach
    void setUp() {
        UserModel userStudent = new UserModel();
        userStudent.setName("John Doe");
        userStudent.setEmail("john@example.com");
        userStudent.setCpf("12345678901");
        userStudent.setPhoneNumber("1234567890");
        userStudent.setPassword("password123");
        userStudent.setAddress("123 Main St");
        userStudent.setBirthDate(LocalDate.of(2000, 1, 1));
        userStudent = userRepository.save(userStudent);

        classroom = new ClassroomModel();
        classroom.setName("Class A");
        classroom.setShift(ShiftEnum.MORNING);
        classroom.setGrade("9th Grade");
        classroom.setRoom("Room 101");
        classroom.setMaxCapacity(30);
        classroom.setIsActive(true);
        classroom.setSchoolYear("2025");
        classroom = classroomRepository.save(classroom);

        student = new StudentModel();
        student.setUser(userStudent);
        student.setEnrollmentNumber("ENR001");
        student.setClassroom(classroom);
        student = studentRepository.save(student);

        subject = new SubjectModel();
        subject.setSubjectName("Mathematics");
        subject.setWorkload(80);
        subject = subjectRepository.save(subject);

        UserModel userTeacher = new UserModel();
        userTeacher.setName("Teacher Smith");
        userTeacher.setEmail("teacher@example.com");
        userTeacher.setCpf("98765432101");
        userTeacher.setPhoneNumber("9876543210");
        userTeacher.setPassword("password123");
        userTeacher.setAddress("456 Teacher St");
        userTeacher.setBirthDate(LocalDate.of(1980, 1, 1));
        userTeacher = userRepository.save(userTeacher);

        teacher = new TeacherModel();
        teacher.setUser(userTeacher);
        teacher.setSpecialization("Mathematics");
        teacher = teacherRepository.save(teacher);
    }

    @Test
    @DisplayName("Should record grade successfully")
    void testRecordGrade() {
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setStudentId(student.getId());
        requestDTO.setSubjectId(subject.getId());
        requestDTO.setClassroomId(classroom.getId());
        requestDTO.setTeacherId(teacher.getId());
        requestDTO.setValue(new BigDecimal("8.5"));
        requestDTO.setQuarter(QuarterEnum.FIRST);
        requestDTO.setEvaluationType(EvaluationTypeEnum.EXAM);
        requestDTO.setRecordDate(LocalDate.now());

        ResponseEntity<GradeResponseDTO> response = restTemplate.postForEntity(
            "/grade",
            requestDTO,
            GradeResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal("8.5"), response.getBody().getValue());
    }

    @Test
    @DisplayName("Should calculate average correctly")
    void testCalculateAverage() {
        createGrade(student.getId(), subject.getId(), new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student.getId(), subject.getId(), new BigDecimal("9.0"), QuarterEnum.FIRST);
        createGrade(student.getId(), subject.getId(), new BigDecimal("7.0"), QuarterEnum.FIRST);

        String url = String.format("/grade/average?studentId=%d&subjectId=%d&quarter=FIRST",
            student.getId(), subject.getId());

        ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal("8.00"), response.getBody());
    }

    @Test
    @DisplayName("Should generate report card with correct data")
    void testGenerateReportCard() {
        createGrade(student.getId(), subject.getId(), new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student.getId(), subject.getId(), new BigDecimal("9.0"), QuarterEnum.FIRST);

        String url = String.format("/grade/report-card?studentId=%d&quarter=FIRST&schoolYear=2025",
            student.getId());

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("overallAverage"));
        assertTrue(response.getBody().containsKey("status"));
    }

    @Test
    @DisplayName("Should validate grade value between 0 and 10")
    void testValidateGradeValue() {
        GradeRequestDTO invalidGrade = new GradeRequestDTO();
        invalidGrade.setStudentId(student.getId());
        invalidGrade.setSubjectId(subject.getId());
        invalidGrade.setClassroomId(classroom.getId());
        invalidGrade.setTeacherId(teacher.getId());
        invalidGrade.setValue(new BigDecimal("15.0"));
        invalidGrade.setQuarter(QuarterEnum.FIRST);
        invalidGrade.setEvaluationType(EvaluationTypeEnum.EXAM);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/grade",
            invalidGrade,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should retrieve all grades for a student")
    void testGetStudentGrades() {
        createGrade(student.getId(), subject.getId(), new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student.getId(), subject.getId(), new BigDecimal("9.0"), QuarterEnum.SECOND);

        ResponseEntity<GradeResponseDTO[]> response = restTemplate.getForEntity(
            "/grade/student/" + student.getId(),
            GradeResponseDTO[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    @DisplayName("Should update grade value")
    void testUpdateGrade() {
        GradeResponseDTO createdGrade = createGrade(
            student.getId(),
            subject.getId(),
            new BigDecimal("7.0"),
            QuarterEnum.FIRST
        );

        GradeRequestDTO updateDTO = new GradeRequestDTO();
        updateDTO.setValue(new BigDecimal("9.5"));

        ResponseEntity<GradeResponseDTO> response = restTemplate.exchange(
            "/grade/" + createdGrade.getId(),
            HttpMethod.PUT,
            new org.springframework.http.HttpEntity<>(updateDTO),
            GradeResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private GradeResponseDTO createGrade(Long studentId, Long subjectId, BigDecimal value, QuarterEnum quarter) {
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setStudentId(studentId);
        requestDTO.setSubjectId(subjectId);
        requestDTO.setClassroomId(classroom.getId());
        requestDTO.setTeacherId(teacher.getId());
        requestDTO.setValue(value);
        requestDTO.setQuarter(quarter);
        requestDTO.setEvaluationType(EvaluationTypeEnum.EXAM);
        requestDTO.setRecordDate(LocalDate.now());

        ResponseEntity<GradeResponseDTO> response = restTemplate.postForEntity(
            "/grade",
            requestDTO,
            GradeResponseDTO.class
        );

        return response.getBody();
    }
}
