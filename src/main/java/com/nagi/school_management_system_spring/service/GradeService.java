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

import com.nagi.school_management_system_spring.model.ClassroomModel;
import com.nagi.school_management_system_spring.model.GradeModel;
import com.nagi.school_management_system_spring.model.ReportCardModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.model.SubjectModel;
import com.nagi.school_management_system_spring.model.enums.QuarterEnum;
import com.nagi.school_management_system_spring.model.enums.ReportCardStatusEnum;
import com.nagi.school_management_system_spring.repository.ClassroomRepository;
import com.nagi.school_management_system_spring.repository.GradeRepository;
import com.nagi.school_management_system_spring.repository.ReportCardRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;
import com.nagi.school_management_system_spring.repository.SubjectRepository;

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
    private ReportCardRepository reportCardRepository;

    @Transactional
    public GradeModel recordGrade(GradeModel grade) {
        if (grade.getRecordDate() == null) {
            grade.setRecordDate(LocalDate.now());
        }
        return gradeRepository.save(grade);
    }

    @Transactional
    public GradeModel updateGrade(Long id, GradeModel updatedGrade) {
        GradeModel grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));

        if (updatedGrade.getValue() != null) {
            grade.setValue(updatedGrade.getValue());
        }
        if (updatedGrade.getQuarter() != null) {
            grade.setQuarter(updatedGrade.getQuarter());
        }
        if (updatedGrade.getEvaluationType() != null) {
            grade.setEvaluationType(updatedGrade.getEvaluationType());
        }
        if (updatedGrade.getRecordDate() != null) {
            grade.setRecordDate(updatedGrade.getRecordDate());
        }

        return gradeRepository.save(grade);
    }

    public BigDecimal calculateAverage(Long studentId, Long subjectId, QuarterEnum quarter) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        SubjectModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

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

    public List<GradeModel> findGradesByStudent(Long studentId) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return gradeRepository.findByStudent(student);
    }

    public List<GradeModel> findGradesByClassroom(Long classroomId, Long subjectId) {
        ClassroomModel classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        SubjectModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        return gradeRepository.findBySubject(subject).stream()
                .filter(g -> g.getClassroom().getId().equals(classroomId))
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> generateReportCard(Long studentId, QuarterEnum quarter, String schoolYear) {
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        List<GradeModel> grades = gradeRepository.findByStudentAndQuarter(student, quarter);

        Map<Long, List<GradeModel>> gradesBySubject = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getId()));

        Map<String, BigDecimal> subjectAverages = new HashMap<>();
        BigDecimal totalSum = BigDecimal.ZERO;
        int subjectCount = 0;

        for (Map.Entry<Long, List<GradeModel>> entry : gradesBySubject.entrySet()) {
            Long subjectId = entry.getKey();
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
