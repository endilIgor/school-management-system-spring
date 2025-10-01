package com.nagi.school_management_system_spring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.repository.ClassroomSubjectTeacherRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassroomSubjectTeacherRepository classroomSubjectTeacherRepository;

    @Transactional
    public TeacherModel hireTeacher(TeacherModel teacher) {
        teacher.setHireDate(LocalDateTime.now());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public TeacherModel updateData(Long id, TeacherModel updatedTeacher) {
        TeacherModel teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        if (updatedTeacher.getSpecialization() != null) {
            teacher.setSpecialization(updatedTeacher.getSpecialization());
        }
        if (updatedTeacher.getStatus() != null) {
            teacher.setStatus(updatedTeacher.getStatus());
        }
        if (updatedTeacher.getSalary() != null) {
            teacher.setSalary(updatedTeacher.getSalary());
        }

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void assignSubjects(Long teacherId, List<ClassroomSubjectTeacherModel> assignments) {
        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        for (ClassroomSubjectTeacherModel assignment : assignments) {
            assignment.setTeacher(teacher);
            classroomSubjectTeacherRepository.save(assignment);
        }
    }

    public List<TeacherModel> listBySpecialization(String specialization) {
        return teacherRepository.findBySpecialization(specialization);
    }

    public Integer findWorkload(Long teacherId) {
        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        List<ClassroomSubjectTeacherModel> assignments = classroomSubjectTeacherRepository.findByTeacher(teacher);

        return assignments.stream()
                .map(ClassroomSubjectTeacherModel::getSubject)
                .map(SubjectModel::getWorkload)
                .reduce(0, Integer::sum);
    }

}
