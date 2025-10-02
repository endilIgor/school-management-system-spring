package com.nagi.school_management_system_spring.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.dto.GradeRequestDTO;
import com.nagi.school_management_system_spring.dto.GradeResponseDTO;
import com.nagi.school_management_system_spring.exception.InvalidGradeException;
import com.nagi.school_management_system_spring.exception.ResourceNotFoundException;
import com.nagi.school_management_system_spring.mapper.GradeMapper;
import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.TeacherModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.model.enums.ReportCardStatusEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GradeRepository;
import com.nagi.school_management_system_spring.repository.ReportCardRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;
import com.nagi.school_management_system_spring.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ReportCardRepository reportCardRepository;

    @Autowired
    private GradeMapper gradeMapper;

    @Transactional
    public GradeResponseDTO recordGrade(GradeRequestDTO requestDTO) {
        StudentModel student = studentRepository.findById(requestDTO.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + requestDTO.getStudentId()));

        SubjectModel subject = subjectRepository.findById(requestDTO.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + requestDTO.getSubjectId()));

        ClassroomModel classroom = classroomRepository.findById(requestDTO.getClassroomId())
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + requestDTO.getClassroomId()));

        TeacherModel teacher = teacherRepository.findById(requestDTO.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + requestDTO.getTeacherId()));

        if (requestDTO.getValue().compareTo(BigDecimal.ZERO) < 0 || requestDTO.getValue().compareTo(BigDecimal.TEN) > 0) {
            throw new InvalidGradeException("Grade value must be between 0 and 10. Received: " + requestDTO.getValue());
        }

        GradeModel grade = new GradeModel();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setClassroom(classroom);
        grade.setQuarter(requestDTO.getQuarter());
        grade.setEvaluationType(requestDTO.getEvaluationType());
        grade.setValue(requestDTO.getValue());
        grade.setRecordDate(requestDTO.getRecordDate() != null ? requestDTO.getRecordDate() : LocalDate.now());
        grade.setTeacher(teacher);

        GradeModel savedGrade = gradeRepository.save(grade);
        return gradeMapper.toResponseDTO(savedGrade);
    }

    @Transactional
    public GradeResponseDTO updateGrade(Long id, GradeRequestDTO requestDTO) {
        GradeModel grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));

        if (requestDTO.getValue() != null) {
            if (requestDTO.getValue().compareTo(BigDecimal.ZERO) < 0 || requestDTO.getValue().compareTo(BigDecimal.TEN) > 0) {
                throw new InvalidGradeException("Grade value must be between 0 and 10. Received: " + requestDTO.getValue());
            }
            grade.setValue(requestDTO.getValue());
        }
        if (requestDTO.getQuarter() != null) {
            grade.setQuarter(requestDTO.getQuarter());
        }
        if (requestDTO.getEvaluationType() != null) {
            grade.setEvaluationType(requestDTO.getEvaluationType());
        }
        if (requestDTO.getRecordDate() != null) {
            grade.setRecordDate(requestDTO.getRecordDate());
        }

        GradeModel updatedGrade = gradeRepository.save(grade);
        return gradeMapper.toResponseDTO(updatedGrade);
    }

    public BigDecimal calculateAverage(Long studentId, Long subjectId, QuarterEnum quarter) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        List<GradeModel> grades = gradeRepository.findByStudentAndQuarter(student, quarter).stream()
                .filter(g -> g.getSubject().getId().equals(subjectId))
                .collect(Collectors.toList());

        if (grades.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = grades.stream()
                .map(GradeModel::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(grades.size()), 2, RoundingMode.HALF_UP);
    }

    public List<GradeResponseDTO> findGradesByStudent(Long studentId) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        return gradeRepository.findByStudent(student).stream()
            .map(gradeMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<GradeResponseDTO> findGradesByClassroom(Long classroomId, Long subjectId) {
        classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        SubjectModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        return gradeRepository.findBySubject(subject).stream()
                .filter(g -> g.getClassroom().getId().equals(classroomId))
                .map(gradeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> generateReportCard(Long studentId, QuarterEnum quarter, String schoolYear) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<GradeModel> grades = gradeRepository.findByStudentAndQuarter(student, quarter);

        Map<Long, List<GradeModel>> gradesBySubject = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getId()));

        Map<String, BigDecimal> subjectAverages = new HashMap<>();
        BigDecimal totalSum = BigDecimal.ZERO;
        int subjectCount = 0;

        for (Map.Entry<Long, List<GradeModel>> entry : gradesBySubject.entrySet()) {
            List<GradeModel> subjectGrades = entry.getValue();

            BigDecimal subjectSum = subjectGrades.stream()
                    .map(GradeModel::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal average = subjectSum.divide(
                    BigDecimal.valueOf(subjectGrades.size()), 2, RoundingMode.HALF_UP);

            String subjectName = subjectGrades.get(0).getSubject().getSubjectName();
            subjectAverages.put(subjectName, average);

            totalSum = totalSum.add(average);
            subjectCount++;
        }

        BigDecimal overallAverage = subjectCount > 0
                ? totalSum.divide(BigDecimal.valueOf(subjectCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ReportCardStatusEnum status = determineStatus(overallAverage);

        ReportCardModel reportCard = new ReportCardModel();
        reportCard.setStudent(student);
        reportCard.setClassroom(student.getClassroom());
        reportCard.setQuarter(quarter);
        reportCard.setSchoolYear(schoolYear);
        reportCard.setOverallAverage(overallAverage);
        reportCard.setStatus(status);

        reportCardRepository.save(reportCard);

        Map<String, Object> reportCardData = new HashMap<>();
        reportCardData.put("studentId", studentId);
        reportCardData.put("studentName", student.getUser() != null ? student.getUser().getName() : "N/A");
        reportCardData.put("quarter", quarter);
        reportCardData.put("schoolYear", schoolYear);
        reportCardData.put("subjectAverages", subjectAverages);
        reportCardData.put("overallAverage", overallAverage);
        reportCardData.put("status", status);

        return reportCardData;
    }

    private ReportCardStatusEnum determineStatus(BigDecimal average) {
        if (average.compareTo(BigDecimal.valueOf(7.0)) >= 0) {
            return ReportCardStatusEnum.APPROVED;
        } else if (average.compareTo(BigDecimal.valueOf(5.0)) >= 0) {
            return ReportCardStatusEnum.RECOVERY;
        } else {
            return ReportCardStatusEnum.FAILED;
        }
    }
}
