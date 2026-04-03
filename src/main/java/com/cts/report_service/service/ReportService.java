package com.cts.report_service.service;

import com.cts.report_service.dao.ReportRepo;
import com.cts.report_service.dto.TransactionReportDTO;
import com.cts.report_service.entity.Report;
import com.cts.report_service.entity.Transaction;
import com.cts.report_service.exception.NullDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private RestTemplate restTemplate;

    public Report create(Report report) {
        return reportRepo.save(report);
    }

    public Report findReport(Long id) {
        return reportRepo.findById(id).orElse(null);
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public Report update(Long id, Report updatedReport) {
        return reportRepo.findById(id).map(report -> {
            report.setScope(updatedReport.getScope());
            report.setMetrics(updatedReport.getMetrics());

            return reportRepo.save(report);
        }).orElse(null);
    }

    public void delete(Long id) {
        reportRepo.deleteById(id);
    }


    public List<Report> findByGeneratedDateBetween(LocalDate start, LocalDate end) {
        return reportRepo.findByGeneratedDateBetween(start, end);
    }

    public List<Report> getReportsByScope(String scope) {
        return reportRepo.findByScope(scope);
    }

    public Report generateTransactionReport(String status) {
        TransactionReportDTO[] transactions = restTemplate.getForObject("http://localhost:8090/api/transactions/status/" + status, TransactionReportDTO[].class);

        double totalAmount = 0;
        if (transactions.length == 0) {
            throw new NullDataException("Transactions not found");
        }
        for (TransactionReportDTO tx : transactions) {
            totalAmount += tx.getTransactionAmount();
        }

        Report report = new Report();
        report.setScope("Total Transactions");
        report.setMetrics("Total Completed Sales: " + totalAmount + " | Count: " + transactions.length);
        report.setGeneratedDate(LocalDate.now());

        return reportRepo.save(report);
    }
}
