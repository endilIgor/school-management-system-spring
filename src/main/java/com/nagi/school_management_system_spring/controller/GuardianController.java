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
import org.springframework.web.bind.annotation.RestController;

import com.nagi.school_management_system_spring.model.GuardianModel;
import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.repository.GuardianRepository;
import com.nagi.school_management_system_spring.service.GuardianService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/guardians")
public class GuardianController {

    @Autowired
    private GuardianService guardianService;

    @Autowired
    private GuardianRepository guardianRepository;

    @GetMapping
    public ResponseEntity<List<GuardianModel>> getAllGuardians() {
        List<GuardianModel> guardians = guardianRepository.findAll();
        return ResponseEntity.ok(guardians);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuardianModel> getGuardianById(@PathVariable Long id) {
        try {
            GuardianModel guardian = guardianRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Guardian not found with id: " + id));
            return ResponseEntity.ok(guardian);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<GuardianModel> createGuardian(@Valid @RequestBody GuardianModel guardian) {
        try {
            GuardianModel createdGuardian = guardianService.registerGuardian(guardian);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGuardian);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuardianModel> updateGuardian(@PathVariable Long id,
            @Valid @RequestBody GuardianModel guardian) {
        try {
            GuardianModel updatedGuardian = guardianService.updateData(id, guardian);
            return ResponseEntity.ok(updatedGuardian);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{guardianId}/link-student/{studentId}")
    public ResponseEntity<Void> linkStudent(@PathVariable Long guardianId, @PathVariable Long studentId) {
        try {
            guardianService.linkStudent(guardianId, studentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<StudentModel>> listChildren(@PathVariable Long id) {
        try {
            List<StudentModel> children = guardianService.listChildren(id);
            return ResponseEntity.ok(children);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/financial-info")
    public ResponseEntity<Map<String, Object>> getFinancialInfo(@PathVariable Long id) {
        try {
            Map<String, Object> financialInfo = guardianService.findFinancialInfo(id);
            return ResponseEntity.ok(financialInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
