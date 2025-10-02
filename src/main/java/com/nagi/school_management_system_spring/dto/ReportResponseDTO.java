package com.nagi.school_management_system_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {

    private String reportType;
    private LocalDateTime generatedAt;
    private String schoolYear;
    private String period;
    private List<Object> data;
    private Integer totalRecords;
}
