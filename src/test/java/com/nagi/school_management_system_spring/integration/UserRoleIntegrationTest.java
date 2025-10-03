package com.nagi.school_management_system_spring.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;
import com.nagi.school_management_system_spring.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRoleIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private UserModel adminUser;
    private UserModel teacherUser;
    private UserModel studentUser;

    @BeforeEach
    void setUp() {
        adminUser = new UserModel();
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@school.com");
        adminUser.setCpf("11111111111");
        adminUser.setPhoneNumber("1111111111");
        adminUser.setPassword("admin123");
        adminUser.setAddress("Admin Street");
        adminUser.setBirthDate(LocalDate.of(1980, 1, 1));
        adminUser.setUsertype(UserTypeEnum.ADMIN);
        adminUser.setIsActive(true);
        adminUser = userRepository.save(adminUser);

        teacherUser = new UserModel();
        teacherUser.setName("Teacher User");
        teacherUser.setEmail("teacher@school.com");
        teacherUser.setCpf("22222222222");
        teacherUser.setPhoneNumber("2222222222");
        teacherUser.setPassword("teacher123");
        teacherUser.setAddress("Teacher Street");
        teacherUser.setBirthDate(LocalDate.of(1985, 1, 1));
        teacherUser.setUsertype(UserTypeEnum.TEACHER);
        teacherUser.setIsActive(true);
        teacherUser = userRepository.save(teacherUser);

        studentUser = new UserModel();
        studentUser.setName("Student User");
        studentUser.setEmail("student@school.com");
        studentUser.setCpf("33333333333");
        studentUser.setPhoneNumber("3333333333");
        studentUser.setPassword("student123");
        studentUser.setAddress("Student Street");
        studentUser.setBirthDate(LocalDate.of(2005, 1, 1));
        studentUser.setUsertype(UserTypeEnum.STUDENT);
        studentUser.setIsActive(true);
        studentUser = userRepository.save(studentUser);
    }

    @Test
    @DisplayName("Should verify admin user role")
    void testVerifyAdminUserRole() {
        assertEquals(UserTypeEnum.ADMIN, adminUser.getUsertype());
        assertEquals(true, adminUser.getIsActive());
    }

    @Test
    @DisplayName("Should verify teacher user role")
    void testVerifyTeacherUserRole() {
        assertEquals(UserTypeEnum.TEACHER, teacherUser.getUsertype());
        assertEquals(true, teacherUser.getIsActive());
    }

    @Test
    @DisplayName("Should verify student user role")
    void testVerifyStudentUserRole() {
        assertEquals(UserTypeEnum.STUDENT, studentUser.getUsertype());
        assertEquals(true, studentUser.getIsActive());
    }

    @Test
    @DisplayName("Should retrieve user by ID from repository and verify role")
    void testGetUserByIdAndVerifyRole() {
        UserModel foundUser = userRepository.findById(adminUser.getId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(UserTypeEnum.ADMIN, foundUser.getUsertype());
        assertEquals("admin@school.com", foundUser.getEmail());
    }

    @Test
    @DisplayName("Should verify different user roles exist in database")
    void testDifferentUserRolesExist() {
        UserModel admin = userRepository.findById(adminUser.getId()).orElse(null);
        UserModel teacher = userRepository.findById(teacherUser.getId()).orElse(null);
        UserModel student = userRepository.findById(studentUser.getId()).orElse(null);

        assertNotNull(admin);
        assertNotNull(teacher);
        assertNotNull(student);

        assertEquals(UserTypeEnum.ADMIN, admin.getUsertype());
        assertEquals(UserTypeEnum.TEACHER, teacher.getUsertype());
        assertEquals(UserTypeEnum.STUDENT, student.getUsertype());
    }
}
