package com.nagi.school_management_system_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GuardianRepository guardianRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private StudentModel student;
    private UserModel user;
    private GuardianModel guardian;
    private ClassroomModel classroom;
    private StudentRequestDTO requestDTO;
    private StudentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");

        guardian = new GuardianModel();
        guardian.setId(1L);

        classroom = new ClassroomModel();
        classroom.setId(1L);
        classroom.setName("Class A");
        classroom.setMaxCapacity(30);

        student = new StudentModel();
        student.setId(1L);
        student.setUser(user);
        student.setEnrollmentNumber("ENR001");
        student.setClassroom(classroom);
        student.setGuardian(guardian);

        requestDTO = new StudentRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setEnrollmentNumber("ENR001");
        requestDTO.setClassroomId(1L);
        requestDTO.setGuardianId(1L);

        responseDTO = new StudentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEnrollmentNumber("ENR001");
    }

    @Test
    @DisplayName("Should get student by ID successfully")
    void testGetStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toResponseDTO(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student not found")
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    @DisplayName("Should find student by enrollment number")
    void testFindByEnrollmentNumber_Success() {
        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.of(student));
        when(studentMapper.toResponseDTO(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.findByEnrollmentNumber("ENR001");

        assertNotNull(result);
        assertEquals("ENR001", result.getEnrollmentNumber());
    }

    @Test
    @DisplayName("Should list students by classroom")
    void testListByClassroom_Success() {
        List<StudentModel> students = Arrays.asList(student);
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(students);
        when(studentMapper.toResponseDTO(any(StudentModel.class))).thenReturn(responseDTO);

        List<StudentResponseDTO> result = studentService.listByClassroom(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should enroll student successfully")
    void testEnrollStudent_Success() {
        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(guardianRepository.findById(1L)).thenReturn(Optional.of(guardian));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(studentRepository.findByClassroom(classroom)).thenReturn(List.of());
        when(studentRepository.save(any(StudentModel.class))).thenReturn(student);
        when(studentMapper.toResponseDTO(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.enrollStudent(requestDTO);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(StudentModel.class));
    }

    @Test
    @DisplayName("Should throw StudentAlreadyEnrolledException when enrollment number exists")
    void testEnrollStudent_AlreadyEnrolled() {
        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.of(student));

        assertThrows(StudentAlreadyEnrolledException.class, () -> studentService.enrollStudent(requestDTO));
    }

    @Test
    @DisplayName("Should throw ClassroomFullException when classroom is at max capacity")
    void testEnrollStudent_ClassroomFull() {
        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(guardianRepository.findById(1L)).thenReturn(Optional.of(guardian));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        List<StudentModel> fullClassroom = Arrays.asList(new StudentModel[30]);
        when(studentRepository.findByClassroom(classroom)).thenReturn(fullClassroom);

        assertThrows(ClassroomFullException.class, () -> studentService.enrollStudent(requestDTO));
    }

    @Test
    @DisplayName("Should transfer student to new classroom")
    void testTransferStudent_Success() {
        ClassroomModel newClassroom = new ClassroomModel();
        newClassroom.setId(2L);
        newClassroom.setMaxCapacity(30);

        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.of(student));
        when(classroomRepository.findById(2L)).thenReturn(Optional.of(newClassroom));
        when(studentRepository.findByClassroom(newClassroom)).thenReturn(List.of());
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toResponseDTO(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.transferStudentByEnrollmentNumber("ENR001", 2L);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    @DisplayName("Should throw ClassroomFullException when transferring to full classroom")
    void testTransferStudent_ClassroomFull() {
        ClassroomModel newClassroom = new ClassroomModel();
        newClassroom.setId(2L);
        newClassroom.setMaxCapacity(30);

        when(studentRepository.findByEnrollmentNumber("ENR001")).thenReturn(Optional.of(student));
        when(classroomRepository.findById(2L)).thenReturn(Optional.of(newClassroom));

        List<StudentModel> fullClassroom = Arrays.asList(new StudentModel[30]);
        when(studentRepository.findByClassroom(newClassroom)).thenReturn(fullClassroom);

        assertThrows(ClassroomFullException.class,
            () -> studentService.transferStudentByEnrollmentNumber("ENR001", 2L));
    }
}
