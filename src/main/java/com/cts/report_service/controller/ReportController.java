package com.cts.report_service.controller;

import com.cts.report_service.dto.ReportDTO;
import com.cts.report_service.entity.Report;
import com.cts.report_service.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<Report> generateReport(@Valid @RequestBody ReportDTO reportDto) {
        // 1. Convert DTO to Entity (Manual or using a Mapper)
        Report report = new Report();
        report.setScope(reportDto.getScope());
        report.setMetrics(reportDto.getMetrics());
        report.setGeneratedDate(reportDto.getGeneratedDate());

        // 2. Save using Service
        return ResponseEntity.ok(reportService.create(report));
    }

    @GetMapping("/{id}")
    public Report getReport(@PathVariable Long id) {
        return reportService.findReport(id);
    }

    @GetMapping("/all")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/scope/{scope}")
    public List<Report> getByScope(@PathVariable String scope) {
        return reportService.getReportsByScope(scope);
    }

    @GetMapping("/range")
    public List<Report> getByRange(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {
        return reportService.findByGeneratedDateBetween(start, end);
    }
}