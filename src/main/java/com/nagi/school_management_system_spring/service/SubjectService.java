package com.nagi.school_management_system_spring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.ClassroomSubjectTeacherRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomSubjectTeacherRepository classroomSubjectTeacherRepository;

    @Transactional
    public SubjectModel createSubject(SubjectModel subject) {
        return subjectRepository.save(subject);
    }

    @Transactional
    public ClassroomSubjectTeacherModel assignTeacher(Long subjectId, Long teacherId, Long classroomId,
            String schoolYear) {
        SubjectModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        ClassroomSubjectTeacherModel assignment = new ClassroomSubjectTeacherModel();
        assignment.setSubject(subject);
        assignment.setTeacher(teacher);
        assignment.setClassroom(classroom);
        assignment.setSchoolYear(schoolYear);

        return classroomSubjectTeacherRepository.save(assignment);
    }

    @Transactional
    public SubjectModel setWorkload(Long subjectId, Integer workload) {
        SubjectModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        subject.setWorkload(workload);
        return subjectRepository.save(subject);
    }

    public List<SubjectModel> listByClassroom(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        List<ClassroomSubjectTeacherModel> assignments = classroomSubjectTeacherRepository.findByClassroom(classroom);

        return assignments.stream()
                .map(ClassroomSubjectTeacherModel::getSubject)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<SubjectModel> findByTeacher(Long teacherId) {
        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        List<ClassroomSubjectTeacherModel> assignments = classroomSubjectTeacherRepository.findByTeacher(teacher);

        return assignments.stream()
                .map(ClassroomSubjectTeacherModel::getSubject)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public SubjectModel updateData(Long id, SubjectModel updatedSubject) {
        SubjectModel subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        if (updatedSubject.getSubjectName() != null) {
            subject.setSubjectName(updatedSubject.getSubjectName());
        }
        if (updatedSubject.getCode() != null) {
            subject.setCode(updatedSubject.getCode());
        }
        if (updatedSubject.getWorkload() != null) {
            subject.setWorkload(updatedSubject.getWorkload());
        }
        if (updatedSubject.getDescription() != null) {
            subject.setDescription(updatedSubject.getDescription());
        }
        if (updatedSubject.getIsActive() != null) {
            subject.setIsActive(updatedSubject.getIsActive());
        }

        return subjectRepository.save(subject);
    }
}
