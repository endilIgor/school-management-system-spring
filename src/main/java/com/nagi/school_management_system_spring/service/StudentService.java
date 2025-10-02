package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.StudentRequestDTO;
import com.nagi.school_management_system_spring.dto.StudentResponseDTO;
import com.nagi.school_management_system_spring.mapper.StudentMapper;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GuardianRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.UserRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentMapper studentMapper;

    public StudentResponseDTO getStudentById(Long id) {
        StudentModel student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found: " + id));
        return studentMapper.toResponseDTO(student);
    }

    public StudentResponseDTO findByEnrollmentNumber(String enrollmentNumber) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
            .orElseThrow(() -> new RuntimeException("Student not found: " + enrollmentNumber));
        return studentMapper.toResponseDTO(student);
    }

    public List<StudentResponseDTO> listByClassroom(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new RuntimeException("Classroom not found: " + classroomId));
        return studentRepository.findByClassroom(classroom).stream()
            .map(studentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> listByGuardian(Long guardianId) {
        GuardianModel guardian = guardianRepository.findById(guardianId)
            .orElseThrow(() -> new RuntimeException("Guardian not found: " + guardianId));
        return studentRepository.findByGuardian(guardian).stream()
            .map(studentMapper::toResponseDTO)
            .collect(Collectors.toList());
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

    public StudentResponseDTO enrollStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.findByEnrollmentNumber(requestDTO.getEnrollmentNumber()).isPresent()) {
            throw new RuntimeException("Enrollment number already exists: " + requestDTO.getEnrollmentNumber());
        }

        UserModel user = userRepository.findById(requestDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found: " + requestDTO.getUserId()));

        StudentModel student = new StudentModel();
        student.setUser(user);
        student.setEnrollmentNumber(requestDTO.getEnrollmentNumber());
        student.setStatus(requestDTO.getStatus());
        student.setObservations(requestDTO.getObservations());

        if (requestDTO.getGuardianId() != null) {
            GuardianModel guardian = guardianRepository.findById(requestDTO.getGuardianId())
                .orElseThrow(() -> new RuntimeException("Guardian not found: " + requestDTO.getGuardianId()));
            student.setGuardian(guardian);
        }

        if (requestDTO.getClassroomId() != null) {
            ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found: " + requestDTO.getClassroomId()));
            student.setClassroom(classroom);
        }

        StudentModel savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    public StudentResponseDTO updateDataByEnrollmentNumber(String enrollmentNumber, StudentRequestDTO requestDTO) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Enrollment Number not found: " + enrollmentNumber));

        if (requestDTO.getGuardianId() != null) {
            GuardianModel guardian = guardianRepository.findById(requestDTO.getGuardianId())
                .orElseThrow(() -> new RuntimeException("Guardian not found: " + requestDTO.getGuardianId()));
            student.setGuardian(guardian);
        }

        if (requestDTO.getClassroomId() != null) {
            ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found: " + requestDTO.getClassroomId()));
            student.setClassroom(classroom);
        }

        if (requestDTO.getStatus() != null) {
            student.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getObservations() != null) {
            student.setObservations(requestDTO.getObservations());
        }

        StudentModel updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

    public StudentResponseDTO transferStudentByEnrollmentNumber(String enrollmentNumber, Long classroomId) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Student not found: " + enrollmentNumber));

        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new RuntimeException("Classroom not found: " + classroomId));

        student.setClassroom(classroom);
        StudentModel updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

}
