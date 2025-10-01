package com.nagi.school_management_system_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.repository.GuardianRepository;
import com.nagi.school_management_system_spring.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class GuardianService {

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public GuardianModel registerGuardian(GuardianModel guardian) {
        return guardianRepository.save(guardian);
    }

    @Transactional
    public void linkStudent(Long guardianId, Long studentId) {
        GuardianModel guardian = guardianRepository.findById(guardianId)
                .orElseThrow(() -> new RuntimeException("Guardian not found with id: " + guardianId));

        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        student.setGuardian(guardian);
        studentRepository.save(student);
    }

    @Transactional
    public GuardianModel updateData(Long id, GuardianModel updatedGuardian) {
        GuardianModel guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guardian not found with id: " + id));

        if (updatedGuardian.getProfession() != null) {
            guardian.setProfession(updatedGuardian.getProfession());
        }
        if (updatedGuardian.getWorkplace() != null) {
            guardian.setWorkplace(updatedGuardian.getWorkplace());
        }
        if (updatedGuardian.getFamilyIncome() != null) {
            guardian.setFamilyIncome(updatedGuardian.getFamilyIncome());
        }

        return guardianRepository.save(guardian);
    }

    public List<StudentModel> listChildren(Long guardianId) {
        GuardianModel guardian = guardianRepository.findById(guardianId)
                .orElseThrow(() -> new RuntimeException("Guardian not found with id: " + guardianId));

        return studentRepository.findByGuardian(guardian);
    }

    public Map<String, Object> findFinancialInfo(Long guardianId) {
        GuardianModel guardian = guardianRepository.findById(guardianId)
                .orElseThrow(() -> new RuntimeException("Guardian not found with id: " + guardianId));

        Map<String, Object> financialInfo = new HashMap<>();
        financialInfo.put("guardianId", guardian.getId());
        financialInfo.put("profession", guardian.getProfession());
        financialInfo.put("workplace", guardian.getWorkplace());
        financialInfo.put("familyIncome", guardian.getFamilyIncome());

        List<StudentModel> children = studentRepository.findByGuardian(guardian);
        financialInfo.put("numberOfChildren", children.size());

        return financialInfo;
    }
}
