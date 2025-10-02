package com.nagi.school_management_system_spring.mapper;

import com.nagi.school_management_system_spring.dto.ClassroomResponseDTO;
import com.nagi.school_management_system_spring.dto.ClassroomWithStudentsResponseDTO;
import com.nagi.school_management_system_spring.dto.StudentSummaryResponseDTO;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassroomMapper {

    private final StudentMapper studentMapper;

    public ClassroomMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public ClassroomResponseDTO toResponseDTO(ClassroomModel classroom) {
        if (classroom == null) {
            return null;
        }

        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setGrade(classroom.getGrade());
        dto.setShift(classroom.getShift());
        dto.setRoom(classroom.getRoom());
        dto.setMaxCapacity(classroom.getMaxCapacity());
        dto.setHomeRoomTeacherId(classroom.getHomeRoomTeacher() != null ? classroom.getHomeRoomTeacher().getId() : null);
        dto.setHomeRoomTeacherName(classroom.getHomeRoomTeacher() != null && classroom.getHomeRoomTeacher().getUser() != null
            ? classroom.getHomeRoomTeacher().getUser().getName() : null);
        dto.setIsActive(classroom.getIsActive());
        dto.setSchoolYear(classroom.getSchoolYear());
        dto.setSchoolId(classroom.getSchool() != null ? classroom.getSchool().getId() : null);
        dto.setSchoolName(classroom.getSchool() != null ? classroom.getSchool().getName() : null);
        dto.setCurrentStudentCount(classroom.getStudents() != null ? classroom.getStudents().size() : 0);
        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setUpdatedAt(classroom.getUpdatedAt());

        return dto;
    }

    public ClassroomWithStudentsResponseDTO toWithStudentsDTO(ClassroomModel classroom) {
        if (classroom == null) {
            return null;
        }

        ClassroomWithStudentsResponseDTO dto = new ClassroomWithStudentsResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setGrade(classroom.getGrade());
        dto.setShift(classroom.getShift());
        dto.setRoom(classroom.getRoom());
        dto.setMaxCapacity(classroom.getMaxCapacity());
        dto.setHomeRoomTeacherId(classroom.getHomeRoomTeacher() != null ? classroom.getHomeRoomTeacher().getId() : null);
        dto.setHomeRoomTeacherName(classroom.getHomeRoomTeacher() != null && classroom.getHomeRoomTeacher().getUser() != null
            ? classroom.getHomeRoomTeacher().getUser().getName() : null);
        dto.setIsActive(classroom.getIsActive());
        dto.setSchoolYear(classroom.getSchoolYear());
        dto.setSchoolId(classroom.getSchool() != null ? classroom.getSchool().getId() : null);
        dto.setSchoolName(classroom.getSchool() != null ? classroom.getSchool().getName() : null);

        if (classroom.getStudents() != null) {
            List<StudentSummaryResponseDTO> students = classroom.getStudents().stream()
                .map(studentMapper::toSummaryDTO)
                .collect(Collectors.toList());
            dto.setStudents(students);
        }

        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setUpdatedAt(classroom.getUpdatedAt());

        return dto;
    }
}
