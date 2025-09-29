package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Optional<StudentModel> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<StudentModel> findByEnrollmentNumber(String enrollmentNumber) {
        return studentRepository.findByEnrollmentNumber(enrollmentNumber);
    }

    public List<StudentModel> listByClassroom(ClassroomModel classroom) {
        return studentRepository.findByClassroom(classroom);
    }

    public List<StudentModel> listByGuardian(GuardianModel guardian) {
        return studentRepository.findByGuardian(guardian);
    }

    public Map<String, Object> findAcademicHistoryByEnrollmentNumber(String enrollmentNumber) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Student not found: " + enrollmentNumber));

        Map<String, Object> history = new HashMap<>();
        history.put("student", student);
        history.put("grades", student.getGrades());
        history.put("attendances", student.getAttendances());
        history.put("reportCards", student.getReportCards());

        return history;
    }

    public StudentModel enrollStudent(StudentModel student) {
        if (studentRepository.findByEnrollmentNumber(student.getEnrollmentNumber()).isPresent()) {
            throw new RuntimeException("Enrollment number already exists: " + student.getEnrollmentNumber());
        }
        return studentRepository.save(student);
    }

    public StudentModel updateDataByEnrollmentNumber(String enrollmentNumber, StudentModel updateStudent) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Enrollment Number not found: " + enrollmentNumber));

        if (updateStudent.getGuardian() != null) {
            student.setGuardian(updateStudent.getGuardian());
        }
        if (updateStudent.getClassroom() != null) {
            student.setClassroom(updateStudent.getClassroom());
        }
        if (updateStudent.getStatus() != null) {
            student.setStatus(updateStudent.getStatus());
        }
        if (updateStudent.getObservations() != null) {
            student.setObservations(updateStudent.getObservations());
        }

        return studentRepository.save(student);
    }

    public StudentModel transferStudentByEnrollmentNumber(String enrollmentNumber, ClassroomModel newClassroom) {
        if (newClassroom == null) {
            throw new RuntimeException("New classroom cannot be null");
        }

        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Student not found: " + enrollmentNumber));

        student.setClassroom(newClassroom);
        return studentRepository.save(student);
    }

}
