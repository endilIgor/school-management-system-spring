package com.nagi.school_management_system_spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.dto.ClassroomRequestDTO;
import com.nagi.school_management_system_spring.dto.ClassroomResponseDTO;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.ClassroomSubjectTeacherModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.enums.ShiftEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.service.ClassroomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> getAllClassrooms(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) ShiftEnum shift) {

        List<ClassroomResponseDTO> classrooms;

        if (year != null && grade != null) {
            classrooms = classroomService.findByGradeAndSchoolYear(grade, year);
        } else if (year != null) {
            classrooms = classroomService.listByYear(year);
        } else if (shift != null) {
            classrooms = classroomService.findByShift(shift);
        } else {
            classrooms = classroomService.getAllClassrooms();
        }

        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getClassroomById(@PathVariable Long id) {
        ClassroomResponseDTO classroom = classroomService.getClassroomById(id);
        return ResponseEntity.ok(classroom);
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> createClassroom(@Valid @RequestBody ClassroomRequestDTO requestDTO) {
        ClassroomResponseDTO classroom = classroomService.createClassroom(requestDTO);
        return ResponseEntity.ok(classroom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> updateClassroom(@PathVariable Long id,
            @Valid @RequestBody ClassroomRequestDTO requestDTO) {
        ClassroomResponseDTO classroom = classroomService.updateData(id, requestDTO);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentModel>> getClassroomStudents(@PathVariable Long id) {
        ClassroomModel classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + id));
        List<StudentModel> students = studentRepository.findByClassroom(classroom);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{id}/students/{studentId}")
    public ResponseEntity<Void> addStudentToClassroom(@PathVariable Long id, @PathVariable Long studentId) {
        classroomService.addStudent(id, studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<ClassroomSubjectTeacherModel>> getClassroomSubjects(@PathVariable Long id) {
        List<ClassroomSubjectTeacherModel> subjects = classroomService.getClassroomSubjects(id);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/{classroomId}/home-room-teacher/{teacherId}")
    public ResponseEntity<ClassroomResponseDTO> setHomeRoomTeacher(@PathVariable Long classroomId,
            @PathVariable Long teacherId) {
        ClassroomResponseDTO classroom = classroomService.setHomeRoomTeacher(classroomId, teacherId);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping("/{id}/capacity")
    public ResponseEntity<Integer> getClassroomCapacity(@PathVariable Long id) {
        Integer capacity = classroomService.findCapacity(id);
        return ResponseEntity.ok(capacity);
    }

    @GetMapping("/{id}/capacity-check")
    public ResponseEntity<Map<String, Object>> checkClassroomCapacity(@PathVariable Long id) {
        Map<String, Object> capacityInfo = classroomService.checkCapacity(id);
        return ResponseEntity.ok(capacityInfo);
    }
}
