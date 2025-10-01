package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.ClassroomSubjectTeacherRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomSubjectTeacherRepository classroomSubjectTeacherRepository;

    @Transactional
    public ClassroomModel createClassroom(ClassroomModel classroom) {
        return classroomRepository.save(classroom);
    }

    @Transactional
    public ClassroomModel setHomeRoomTeacher(Long classroomId, Long teacherId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        classroom.setHomeRoomTeacher(teacher);
        return classroomRepository.save(classroom);
    }

    @Transactional
    public void addStudent(Long classroomId, Long studentId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        List<StudentModel> currentStudents = studentRepository.findByClassroom(classroom);
        if (currentStudents.size() >= classroom.getMaxCapacity()) {
            throw new RuntimeException("Classroom is at full capacity");
        }

        student.setClassroom(classroom);
        studentRepository.save(student);
    }

    public List<ClassroomModel> listByYear(String schoolYear) {
        return classroomRepository.findBySchoolYear(schoolYear);
    }

    public Integer findCapacity(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        return classroom.getMaxCapacity();
    }

    public Map<String, Object> checkCapacity(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        List<StudentModel> students = studentRepository.findByClassroom(classroom);
        Integer currentStudents = students.size();
        Integer maxCapacity = classroom.getMaxCapacity();
        Integer availableSpots = maxCapacity - currentStudents;
        Double occupancyRate = (currentStudents.doubleValue() / maxCapacity.doubleValue()) * 100;

        Map<String, Object> capacityInfo = new HashMap<>();
        capacityInfo.put("classroomId", classroom.getId());
        capacityInfo.put("classroomName", classroom.getName());
        capacityInfo.put("maxCapacity", maxCapacity);
        capacityInfo.put("currentStudents", currentStudents);
        capacityInfo.put("availableSpots", availableSpots);
        capacityInfo.put("occupancyRate", String.format("%.2f%%", occupancyRate));
        capacityInfo.put("isFull", currentStudents >= maxCapacity);

        return capacityInfo;
    }

    @Transactional
    public ClassroomModel updateData(Long id, ClassroomModel updatedClassroom) {
        ClassroomModel classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + id));

        if (updatedClassroom.getName() != null) {
            classroom.setName(updatedClassroom.getName());
        }
        if (updatedClassroom.getGrade() != null) {
            classroom.setGrade(updatedClassroom.getGrade());
        }
        if (updatedClassroom.getShift() != null) {
            classroom.setShift(updatedClassroom.getShift());
        }
        if (updatedClassroom.getRoom() != null) {
            classroom.setRoom(updatedClassroom.getRoom());
        }
        if (updatedClassroom.getMaxCapacity() != null) {
            classroom.setMaxCapacity(updatedClassroom.getMaxCapacity());
        }
        if (updatedClassroom.getIsActive() != null) {
            classroom.setIsActive(updatedClassroom.getIsActive());
        }
        if (updatedClassroom.getSchoolYear() != null) {
            classroom.setSchoolYear(updatedClassroom.getSchoolYear());
        }

        return classroomRepository.save(classroom);
    }

    public List<ClassroomSubjectTeacherModel> getClassroomSubjects(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        return classroomSubjectTeacherRepository.findByClassroom(classroom);
    }
}
