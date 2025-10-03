package com.nagi.school_management_system_spring.util;

import java.time.LocalDate;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;

public class TestDataBuilder {

    public static UserModel createValidUser(String name, String email, String cpf) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setCpf(cpf);
        user.setPhoneNumber("1234567890");
        user.setPassword("password123");
        user.setAddress("Test Address");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setUsertype(UserTypeEnum.STUDENT);
        user.setIsActive(true);
        return user;
    }

    public static ClassroomModel createValidClassroom(String name, String room) {
        ClassroomModel classroom = new ClassroomModel();
        classroom.setName(name);
        classroom.setShift(ShiftEnum.MORNING);
        classroom.setGrade("9th Grade");
        classroom.setRoom(room);
        classroom.setMaxCapacity(30);
        classroom.setIsActive(true);
        classroom.setSchoolYear("2025");
        return classroom;
    }
}
