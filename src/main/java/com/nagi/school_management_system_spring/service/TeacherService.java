package com.nagi.school_management_system_spring.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.TeacherRequestDTO;
import com.nagi.school_management_system_spring.dto.TeacherResponseDTO;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.mapper.TeacherMapper;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.UserModel;
import com.nagi.school_management_system_spring.repository.ClassroomSubjectTeacherRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;
import com.nagi.school_management_system_spring.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassroomSubjectTeacherRepository classroomSubjectTeacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Transactional
    public TeacherResponseDTO hireTeacher(TeacherRequestDTO requestDTO) {
        UserModel user = userRepository.findById(requestDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.getUserId()));

        TeacherModel teacher = new TeacherModel();
        teacher.setUser(user);
        teacher.setRegistration(requestDTO.getRegistration());
        teacher.setSpecialization(requestDTO.getSpecialization());
        teacher.setStatus(requestDTO.getStatus());
        teacher.setSalary(requestDTO.getSalary());
        teacher.setHireDate(requestDTO.getHireDate() != null ? requestDTO.getHireDate() : LocalDateTime.now());

        TeacherModel savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toResponseDTO(savedTeacher);
    }

    @Transactional
    public TeacherResponseDTO updateData(Long id, TeacherRequestDTO requestDTO) {
        TeacherModel teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        if (requestDTO.getSpecialization() != null) {
            teacher.setSpecialization(requestDTO.getSpecialization());
        }
        if (requestDTO.getStatus() != null) {
            teacher.setStatus(requestDTO.getStatus());
        }
        if (requestDTO.getSalary() != null) {
            teacher.setSalary(requestDTO.getSalary());
        }

        TeacherModel updatedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toResponseDTO(updatedTeacher);
    }

    @Transactional
    public void assignSubjects(Long teacherId, List<ClassroomSubjectTeacherModel> assignments) {
        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        for (ClassroomSubjectTeacherModel assignment : assignments) {
            assignment.setTeacher(teacher);
            classroomSubjectTeacherRepository.save(assignment);
        }
    }

    public List<TeacherResponseDTO> listBySpecialization(String specialization) {
        return teacherRepository.findBySpecialization(specialization).stream()
            .map(teacherMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public TeacherResponseDTO getTeacherById(Long id) {
        TeacherModel teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return teacherMapper.toResponseDTO(teacher);
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
            .map(teacherMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Integer findWorkload(Long teacherId) {
        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        List<ClassroomSubjectTeacherModel> assignments = classroomSubjectTeacherRepository.findByTeacher(teacher);

        return assignments.stream()
                .map(ClassroomSubjectTeacherModel::getSubject)
                .map(SubjectModel::getWorkload)
                .reduce(0, Integer::sum);
    }

}
