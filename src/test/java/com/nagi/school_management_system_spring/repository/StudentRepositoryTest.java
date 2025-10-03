package com.nagi.school_management_system_spring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.model.enums.StudentStatusEnum;
import com.nagi.school_management_system_spring.util.TestDataBuilder;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private ClassroomModel classroom;
    private GuardianModel guardian;
    private UserModel user;

    @BeforeEach
    void setUp() {
        classroom = TestDataBuilder.createValidClassroom("Class A", "Room 101");
        entityManager.persist(classroom);

        UserModel guardianUser = TestDataBuilder.createValidUser("Parent Guardian", "guardian@example.com", "11144477735");
        entityManager.persist(guardianUser);

        guardian = new GuardianModel();
        guardian.setUser(guardianUser);
        entityManager.persist(guardian);

        user = TestDataBuilder.createValidUser("John Doe", "john@example.com", "12345678909");
        entityManager.persist(user);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should find student by enrollment number")
    void testFindByEnrollmentNumber() {
        StudentModel student = new StudentModel();
        student.setUser(user);
        student.setEnrollmentNumber("ENR001");
        student.setStatus(StudentStatusEnum.ACTIVE);
        entityManager.persist(student);
        entityManager.flush();

        Optional<StudentModel> found = studentRepository.findByEnrollmentNumber("ENR001");

        assertTrue(found.isPresent());
        assertEquals("ENR001", found.get().getEnrollmentNumber());
    }

    @Test
    @DisplayName("Should find students by classroom")
    void testFindByClassroom() {
        StudentModel student1 = createStudent("ENR001", classroom);
        StudentModel student2 = createStudent("ENR002", classroom);
        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        List<StudentModel> students = studentRepository.findByClassroom(classroom);

        assertEquals(2, students.size());
    }

    @Test
    @DisplayName("Should find students by guardian")
    void testFindByGuardian() {
        StudentModel student1 = createStudent("ENR001", classroom);
        student1.setGuardian(guardian);
        StudentModel student2 = createStudent("ENR002", classroom);
        student2.setGuardian(guardian);

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        List<StudentModel> students = studentRepository.findByGuardian(guardian);

        assertEquals(2, students.size());
    }

    @Test
    @DisplayName("Should find students by status")
    void testFindByStatus() {
        StudentModel activeStudent = createStudent("ENR001", classroom);
        activeStudent.setStatus(StudentStatusEnum.ACTIVE);

        StudentModel inactiveStudent = createStudent("ENR002", classroom);
        inactiveStudent.setStatus(StudentStatusEnum.DROPPED_OUT);

        entityManager.persist(activeStudent);
        entityManager.persist(inactiveStudent);
        entityManager.flush();

        List<StudentModel> activeStudents = studentRepository.findByStatus(StudentStatusEnum.ACTIVE);

        assertEquals(1, activeStudents.size());
        assertEquals(StudentStatusEnum.ACTIVE, activeStudents.get(0).getStatus());
    }

    @Test
    @DisplayName("Should find students by classroom and status")
    void testFindByClassroomAndStatus() {
        StudentModel student1 = createStudent("ENR001", classroom);
        student1.setStatus(StudentStatusEnum.ACTIVE);

        StudentModel student2 = createStudent("ENR002", classroom);
        student2.setStatus(StudentStatusEnum.ACTIVE);

        StudentModel student3 = createStudent("ENR003", classroom);
        student3.setStatus(StudentStatusEnum.DROPPED_OUT);

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        List<StudentModel> activeStudents = studentRepository.findByClassroomAndStatus(
            classroom, StudentStatusEnum.ACTIVE);

        assertEquals(2, activeStudents.size());
    }

    @Test
    @DisplayName("Should save and retrieve student")
    void testSaveAndRetrieve() {
        StudentModel student = createStudent("ENR001", classroom);
        student.setObservations("Test observations");

        StudentModel saved = studentRepository.save(student);

        assertNotNull(saved.getId());
        assertEquals("ENR001", saved.getEnrollmentNumber());
        assertEquals("Test observations", saved.getObservations());
    }

    private StudentModel createStudent(String enrollmentNumber, ClassroomModel classroom) {
        String cpf = String.format("%011d", Math.abs(enrollmentNumber.hashCode() % 100000000000L));
        UserModel studentUser = TestDataBuilder.createValidUser(
            "Student " + enrollmentNumber,
            enrollmentNumber + "@example.com",
            "12345678909"
        );
        entityManager.persist(studentUser);

        StudentModel student = new StudentModel();
        student.setUser(studentUser);
        student.setEnrollmentNumber(enrollmentNumber);
        student.setClassroom(classroom);
        student.setStatus(StudentStatusEnum.ACTIVE);
        return student;
    }
}
