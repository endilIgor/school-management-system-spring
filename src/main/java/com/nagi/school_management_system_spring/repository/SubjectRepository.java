package com.nagi.school_management_system_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagi.school_management_system_spring.model.SubjectModel;

public interface SubjectRepository extends JpaRepository<SubjectModel, Long> {

    List<SubjectModel> findBySubjectNameContaining(String subjectName);

    List<SubjectModel> findByIsActiveTrue();

    List<SubjectModel> findByWorkload(Integer workload);

    List<SubjectModel> findByWorkloadGreaterThan(Integer minWorkload);

    List<SubjectModel> findByWorkloadBetween(Integer min, Integer max);
}