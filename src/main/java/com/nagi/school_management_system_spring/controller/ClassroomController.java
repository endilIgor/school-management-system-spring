package com.nagi.school_management_system_spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<ClassroomModel>> getAllClassrooms(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) ShiftEnum shift) {

        List<ClassroomModel> classrooms;

        if (year != null && grade != null) {
            classrooms = classroomRepository.findByGradeAndSchoolYear(grade, year);
        } else if (year != null) {
            classrooms = classroomService.listByYear(year);
        } else if (shift != null) {
            classrooms = classroomRepository.findByShift(shift);
        } else {
            classrooms = classroomRepository.findAll();
        }

        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomModel> getClassroomById(@PathVariable Long id) {
        try {
            ClassroomModel classroom = classroomRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + id));
            return ResponseEntity.ok(classroom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<ClassroomModel> createClassroom(@Valid @RequestBody ClassroomModel classroom) {
        try {
            ClassroomModel createdClassroom = classroomService.createClassroom(classroom);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClassroom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomModel> updateClassroom(@PathVariable Long id,
            @Valid @RequestBody ClassroomModel classroom) {
        try {
            ClassroomModel updatedClassroom = classroomService.updateData(id, classroom);
            return ResponseEntity.ok(updatedClassroom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentModel>> getClassroomStudents(@PathVariable Long id) {
        try {
            ClassroomModel classroom = classroomRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + id));
            List<StudentModel> students = studentRepository.findByClassroom(classroom);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/students/{studentId}")
    public ResponseEntity<Void> addStudentToClassroom(@PathVariable Long id, @PathVariable Long studentId) {
        try {
            classroomService.addStudent(id, studentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<ClassroomSubjectTeacherModel>> getClassroomSubjects(@PathVariable Long id) {
        try {
            List<ClassroomSubjectTeacherModel> subjects = classroomService.getClassroomSubjects(id);
            return ResponseEntity.ok(subjects);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{classroomId}/home-room-teacher/{teacherId}")
    public ResponseEntity<ClassroomModel> setHomeRoomTeacher(@PathVariable Long classroomId,
            @PathVariable Long teacherId) {
        try {
            ClassroomModel classroom = classroomService.setHomeRoomTeacher(classroomId, teacherId);
            return ResponseEntity.ok(classroom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/capacity")
    public ResponseEntity<Integer> getClassroomCapacity(@PathVariable Long id) {
        try {
            Integer capacity = classroomService.findCapacity(id);
            return ResponseEntity.ok(capacity);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/capacity-check")
    public ResponseEntity<Map<String, Object>> checkClassroomCapacity(@PathVariable Long id) {
        try {
            Map<String, Object> capacityInfo = classroomService.checkCapacity(id);
            return ResponseEntity.ok(capacityInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
