package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.ClassroomRequestDTO;
import com.nagi.school_management_system_spring.dto.ClassroomResponseDTO;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.mapper.ClassroomMapper;
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

    @Autowired
    private ClassroomMapper classroomMapper;

    @Transactional
    public ClassroomResponseDTO createClassroom(ClassroomRequestDTO requestDTO) {
        ClassroomModel classroom = new ClassroomModel();
        classroom.setName(requestDTO.getName());
        classroom.setGrade(requestDTO.getGrade());
        classroom.setShift(requestDTO.getShift());
        classroom.setRoom(requestDTO.getRoom());
        classroom.setMaxCapacity(requestDTO.getMaxCapacity());
        classroom.setIsActive(requestDTO.getIsActive());
        classroom.setSchoolYear(requestDTO.getSchoolYear());

        if (requestDTO.getHomeRoomTeacherId() != null) {
            TeacherModel teacher = teacherRepository.findById(requestDTO.getHomeRoomTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + requestDTO.getHomeRoomTeacherId()));
            classroom.setHomeRoomTeacher(teacher);
        }

        ClassroomModel savedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(savedClassroom);
    }

    @Transactional
    public ClassroomResponseDTO setHomeRoomTeacher(Long classroomId, Long teacherId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        TeacherModel teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        classroom.setHomeRoomTeacher(teacher);
        ClassroomModel updatedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }

    @Transactional
    public void addStudent(Long classroomId, Long studentId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<StudentModel> currentStudents = studentRepository.findByClassroom(classroom);
        if (currentStudents.size() >= classroom.getMaxCapacity()) {
            throw new IllegalArgumentException("Classroom is at full capacity");
        }

        student.setClassroom(classroom);
        studentRepository.save(student);
    }

    public List<ClassroomResponseDTO> listByYear(String schoolYear) {
        return classroomRepository.findBySchoolYear(schoolYear).stream()
            .map(classroomMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ClassroomResponseDTO getClassroomById(Long id) {
        ClassroomModel classroom = classroomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
        return classroomMapper.toResponseDTO(classroom);
    }

    public List<ClassroomResponseDTO> getAllClassrooms() {
        return classroomRepository.findAll().stream()
            .map(classroomMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<ClassroomResponseDTO> findByGradeAndSchoolYear(String grade, String year) {
        return classroomRepository.findByGradeAndSchoolYear(grade, year).stream()
            .map(classroomMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<ClassroomResponseDTO> findByShift(com.nagi.school_management_system_spring.model.enums.ShiftEnum shift) {
        return classroomRepository.findByShift(shift).stream()
            .map(classroomMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Integer findCapacity(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        return classroom.getMaxCapacity();
    }

    public Map<String, Object> checkCapacity(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

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
    public ClassroomResponseDTO updateData(Long id, ClassroomRequestDTO requestDTO) {
        ClassroomModel classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));

        if (requestDTO.getName() != null) {
            classroom.setName(requestDTO.getName());
        }
        if (requestDTO.getGrade() != null) {
            classroom.setGrade(requestDTO.getGrade());
        }
        if (requestDTO.getShift() != null) {
            classroom.setShift(requestDTO.getShift());
        }
        if (requestDTO.getRoom() != null) {
            classroom.setRoom(requestDTO.getRoom());
        }
        if (requestDTO.getMaxCapacity() != null) {
            classroom.setMaxCapacity(requestDTO.getMaxCapacity());
        }
        if (requestDTO.getIsActive() != null) {
            classroom.setIsActive(requestDTO.getIsActive());
        }
        if (requestDTO.getSchoolYear() != null) {
            classroom.setSchoolYear(requestDTO.getSchoolYear());
        }
        if (requestDTO.getHomeRoomTeacherId() != null) {
            TeacherModel teacher = teacherRepository.findById(requestDTO.getHomeRoomTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + requestDTO.getHomeRoomTeacherId()));
            classroom.setHomeRoomTeacher(teacher);
        }

        ClassroomModel updatedClassroom = classroomRepository.save(classroom);
        return classroomMapper.toResponseDTO(updatedClassroom);
    }

    public List<ClassroomSubjectTeacherModel> getClassroomSubjects(Long classroomId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        return classroomSubjectTeacherRepository.findByClassroom(classroom);
    }
}
