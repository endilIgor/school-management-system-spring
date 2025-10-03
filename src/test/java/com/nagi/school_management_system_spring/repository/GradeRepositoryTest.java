package com.nagi.school_management_system_spring.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.util.TestDataBuilder;

@DataJpaTest
class GradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GradeRepository gradeRepository;

    private StudentModel student;
    private SubjectModel subject;
    private ClassroomModel classroom;
    private TeacherModel teacher;

    @BeforeEach
    void setUp() {
        classroom = TestDataBuilder.createValidClassroom("Class A", "Room 101");
        entityManager.persist(classroom);

        UserModel userStudent = TestDataBuilder.createValidUser("John Doe", "john@example.com", "12345678909");
        entityManager.persist(userStudent);

        student = new StudentModel();
        student.setUser(userStudent);
        student.setEnrollmentNumber("ENR001");
        student.setClassroom(classroom);
        entityManager.persist(student);

        subject = new SubjectModel();
        subject.setSubjectName("Mathematics");
        subject.setWorkload(80);
        entityManager.persist(subject);

        UserModel userTeacher = TestDataBuilder.createValidUser("Teacher Smith", "teacher@example.com", "98765432100");
        entityManager.persist(userTeacher);

        teacher = new TeacherModel();
        teacher.setUser(userTeacher);
        teacher.setSpecialization("Mathematics");
        entityManager.persist(teacher);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should find grades by student")
    void testFindByStudent() {
        createGrade(student, subject, new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("9.0"), QuarterEnum.FIRST);
        entityManager.flush();

        List<GradeModel> grades = gradeRepository.findByStudent(student);

        assertEquals(2, grades.size());
    }

    @Test
    @DisplayName("Should find grades by subject")
    void testFindBySubject() {
        createGrade(student, subject, new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("7.5"), QuarterEnum.SECOND);
        entityManager.flush();

        List<GradeModel> grades = gradeRepository.findBySubject(subject);

        assertEquals(2, grades.size());
    }

    @Test
    @DisplayName("Should find grades by student and quarter")
    void testFindByStudentAndQuarter() {
        createGrade(student, subject, new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("9.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("7.0"), QuarterEnum.SECOND);
        entityManager.flush();

        List<GradeModel> firstQuarterGrades = gradeRepository.findByStudentAndQuarter(student, QuarterEnum.FIRST);

        assertEquals(2, firstQuarterGrades.size());
    }

    @Test
    @DisplayName("Should find grades by classroom and quarter")
    void testFindByClassroomAndQuarter() {
        createGrade(student, subject, new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("9.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("7.0"), QuarterEnum.SECOND);
        entityManager.flush();

        List<GradeModel> firstQuarterGrades = gradeRepository.findByClassroomAndQuarter(classroom, QuarterEnum.FIRST);

        assertEquals(2, firstQuarterGrades.size());
    }

    @Test
    @DisplayName("Should find grades by teacher and classroom")
    void testFindByTeacherAndClassroom() {
        createGrade(student, subject, new BigDecimal("8.0"), QuarterEnum.FIRST);
        createGrade(student, subject, new BigDecimal("9.0"), QuarterEnum.SECOND);
        entityManager.flush();

        List<GradeModel> grades = gradeRepository.findByTeacherAndClassroom(teacher, classroom);

        assertEquals(2, grades.size());
    }

    @Test
    @DisplayName("Should save grade with correct values")
    void testSaveGrade() {
        GradeModel grade = createGrade(student, subject, new BigDecimal("8.5"), QuarterEnum.FIRST);
        entityManager.flush();

        GradeModel saved = gradeRepository.save(grade);

        assertEquals(new BigDecimal("8.5"), saved.getValue());
        assertEquals(QuarterEnum.FIRST, saved.getQuarter());
    }

    private GradeModel createGrade(StudentModel student, SubjectModel subject, BigDecimal value, QuarterEnum quarter) {
        GradeModel grade = new GradeModel();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setClassroom(classroom);
        grade.setTeacher(teacher);
        grade.setValue(value);
        grade.setQuarter(quarter);
        grade.setRecordDate(LocalDate.now());
        entityManager.persist(grade);
        return grade;
    }
}
