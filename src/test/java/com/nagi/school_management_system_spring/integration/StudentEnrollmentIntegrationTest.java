package com.nagi.school_management_system_spring.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.nagi.school_management_system_spring.dto.StudentRequestDTO;
import com.nagi.school_management_system_spring.dto.StudentResponseDTO;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GuardianRepository;
import com.nagi.school_management_system_spring.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentEnrollmentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    private UserModel user;
    private GuardianModel guardian;
    private ClassroomModel classroom;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setCpf("12345678901");
        user.setPhoneNumber("1234567890");
        user.setPassword("password123");
        user.setAddress("123 Main St");
        user.setBirthDate(java.time.LocalDate.of(2000, 1, 1));
        user = userRepository.save(user);

        UserModel guardianUser = new UserModel();
        guardianUser.setName("Parent Guardian");
        guardianUser.setEmail("guardian@example.com");
        guardianUser.setCpf("11122233344");
        guardianUser.setPhoneNumber("9876543210");
        guardianUser.setPassword("password123");
        guardianUser.setAddress("456 Second St");
        guardianUser.setBirthDate(java.time.LocalDate.of(1970, 1, 1));
        guardianUser = userRepository.save(guardianUser);

        guardian = new GuardianModel();
        guardian.setUser(guardianUser);
        guardian = guardianRepository.save(guardian);

        classroom = new ClassroomModel();
        classroom.setName("Class A");
        classroom.setShift(ShiftEnum.MORNING);
        classroom.setGrade("9th Grade");
        classroom.setRoom("Room 101");
        classroom.setMaxCapacity(30);
        classroom.setIsActive(true);
        classroom.setSchoolYear("2025");
        classroom = classroomRepository.save(classroom);
    }

    @Test
    @DisplayName("Should enroll student successfully through complete flow")
    void testCompleteEnrollmentFlow() {
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setUserId(user.getId());
        requestDTO.setEnrollmentNumber("ENR2025001");
        requestDTO.setGuardianId(guardian.getId());
        requestDTO.setClassroomId(classroom.getId());

        ResponseEntity<StudentResponseDTO> response = restTemplate.postForEntity(
            "/student",
            requestDTO,
            StudentResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENR2025001", response.getBody().getEnrollmentNumber());
    }

    @Test
    @DisplayName("Should retrieve student by enrollment number")
    void testRetrieveStudentByEnrollmentNumber() {
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setUserId(user.getId());
        requestDTO.setEnrollmentNumber("ENR2025002");
        requestDTO.setClassroomId(classroom.getId());

        restTemplate.postForEntity("/student", requestDTO, StudentResponseDTO.class);

        ResponseEntity<StudentResponseDTO> response = restTemplate.getForEntity(
            "/student/enrollment/ENR2025002",
            StudentResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENR2025002", response.getBody().getEnrollmentNumber());
    }

    @Test
    @DisplayName("Should transfer student to another classroom")
    void testTransferStudent() {
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setUserId(user.getId());
        requestDTO.setEnrollmentNumber("ENR2025003");
        requestDTO.setClassroomId(classroom.getId());

        restTemplate.postForEntity("/student", requestDTO, StudentResponseDTO.class);

        ClassroomModel newClassroom = new ClassroomModel();
        newClassroom.setName("Class B");
        newClassroom.setShift(ShiftEnum.AFTERNOON);
        newClassroom.setGrade("9th Grade");
        newClassroom.setRoom("Room 102");
        newClassroom.setMaxCapacity(30);
        newClassroom.setIsActive(true);
        newClassroom.setSchoolYear("2025");
        newClassroom = classroomRepository.save(newClassroom);

        ResponseEntity<StudentResponseDTO> response = restTemplate.exchange(
            "/student/ENR2025003/transfer/" + newClassroom.getId(),
            HttpMethod.PUT,
            null,
            StudentResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should update student data")
    void testUpdateStudentData() {
        StudentRequestDTO enrollDTO = new StudentRequestDTO();
        enrollDTO.setUserId(user.getId());
        enrollDTO.setEnrollmentNumber("ENR2025004");
        enrollDTO.setClassroomId(classroom.getId());

        restTemplate.postForEntity("/student", enrollDTO, StudentResponseDTO.class);

        StudentRequestDTO updateDTO = new StudentRequestDTO();
        updateDTO.setObservations("Updated observations");

        HttpEntity<StudentRequestDTO> request = new HttpEntity<>(updateDTO);

        ResponseEntity<StudentResponseDTO> response = restTemplate.exchange(
            "/student/ENR2025004",
            HttpMethod.PUT,
            request,
            StudentResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when student not found")
    void testStudentNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/student/enrollment/NONEXISTENT",
            String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should list all students in a classroom")
    void testListStudentsByClassroom() {
        StudentRequestDTO student1 = new StudentRequestDTO();
        student1.setUserId(user.getId());
        student1.setEnrollmentNumber("ENR2025005");
        student1.setClassroomId(classroom.getId());

        UserModel user2 = new UserModel();
        user2.setName("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setCpf("22233344455");
        user2.setPhoneNumber("1234567891");
        user2.setPassword("password123");
        user2.setAddress("789 Third St");
        user2.setBirthDate(java.time.LocalDate.of(2001, 1, 1));
        user2 = userRepository.save(user2);

        StudentRequestDTO student2 = new StudentRequestDTO();
        student2.setUserId(user2.getId());
        student2.setEnrollmentNumber("ENR2025006");
        student2.setClassroomId(classroom.getId());

        restTemplate.postForEntity("/student", student1, StudentResponseDTO.class);
        restTemplate.postForEntity("/student", student2, StudentResponseDTO.class);

        ResponseEntity<StudentResponseDTO[]> response = restTemplate.getForEntity(
            "/student/classroom/" + classroom.getId(),
            StudentResponseDTO[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }
}
