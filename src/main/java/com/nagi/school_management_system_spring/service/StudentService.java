package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.StudentRequestDTO;
import com.nagi.school_management_system_spring.dto.StudentResponseDTO;
import com.nagi.school_management_system_spring.exception.ClassroomFullException;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.exception.StudentAlreadyEnrolledException;
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
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return studentMapper.toResponseDTO(student);
    }

    public StudentResponseDTO findByEnrollmentNumber(String enrollmentNumber) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with enrollment number: " + enrollmentNumber));
        return studentMapper.toResponseDTO(student);
    }

    public List<StudentResponseDTO> listByClassroom(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        return studentRepository.findByClassroom(classroom).stream()
            .map(studentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> listByGuardian(Long guardianId) {
        GuardianModel guardian = guardianRepository.findById(guardianId)
            .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with id: " + guardianId));
        return studentRepository.findByGuardian(guardian).stream()
            .map(studentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Map<String, Object> findAcademicHistoryByEnrollmentNumber(String enrollmentNumber) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with enrollment number: " + enrollmentNumber));

        Map<String, Object> history = new HashMap<>();
        history.put("student", student);
        history.put("grades", student.getGrades());
        history.put("attendances", student.getAttendances());
        history.put("reportCards", student.getReportCards());

        return history;
    }

    public StudentResponseDTO enrollStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.findByEnrollmentNumber(requestDTO.getEnrollmentNumber()).isPresent()) {
            throw new StudentAlreadyEnrolledException("Student already enrolled with enrollment number: " + requestDTO.getEnrollmentNumber());
        }

        UserModel user = userRepository.findById(requestDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.getUserId()));

        StudentModel student = new StudentModel();
        student.setUser(user);
        student.setEnrollmentNumber(requestDTO.getEnrollmentNumber());
        student.setStatus(requestDTO.getStatus());
        student.setObservations(requestDTO.getObservations());

        if (requestDTO.getGuardianId() != null) {
            GuardianModel guardian = guardianRepository.findById(requestDTO.getGuardianId())
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with id: " + requestDTO.getGuardianId()));
            student.setGuardian(guardian);
        }

        if (requestDTO.getClassroomId() != null) {
            ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + requestDTO.getClassroomId()));

            long currentStudentCount = studentRepository.findByClassroom(classroom).size();
            if (currentStudentCount >= classroom.getMaxCapacity()) {
                throw new ClassroomFullException("Classroom is full. Max capacity: " + classroom.getMaxCapacity());
            }

            student.setClassroom(classroom);
        }

        StudentModel savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    public StudentResponseDTO updateDataByEnrollmentNumber(String enrollmentNumber, StudentRequestDTO requestDTO) {
        StudentModel student = studentRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with enrollment number: " + enrollmentNumber));

        if (requestDTO.getGuardianId() != null) {
            GuardianModel guardian = guardianRepository.findById(requestDTO.getGuardianId())
                .orElseThrow(() -> new ResourceNotFoundException("Guardian not found with id: " + requestDTO.getGuardianId()));
            student.setGuardian(guardian);
        }

        if (requestDTO.getClassroomId() != null) {
            ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + requestDTO.getClassroomId()));

            long currentStudentCount = studentRepository.findByClassroom(classroom).size();
            if (currentStudentCount >= classroom.getMaxCapacity() &&
                (student.getClassroom() == null || !student.getClassroom().getId().equals(requestDTO.getClassroomId()))) {
                throw new ClassroomFullException("Classroom is full. Max capacity: " + classroom.getMaxCapacity());
            }

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
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with enrollment number: " + enrollmentNumber));

        ClassroomModel classroom = classroomRepository.findById(classroomId)
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        long currentStudentCount = studentRepository.findByClassroom(classroom).size();
        if (currentStudentCount >= classroom.getMaxCapacity()) {
            throw new ClassroomFullException("Cannot transfer student. Classroom is full. Max capacity: " + classroom.getMaxCapacity());
        }

        student.setClassroom(classroom);
        StudentModel updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

}
