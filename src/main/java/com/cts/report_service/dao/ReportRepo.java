package com.cts.report_service.dao;

import com.cts.report_service.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// public interface [Repo Name] extends JpaRepository<[Entity Name], [Entity ID type in wrapper class]>
@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {
    // Finds reports by exact scope string
    List<Report> findByScope(String scope);

    // Finds reports between two dates
    List<Report> findByGeneratedDateBetween(LocalDate start, LocalDate end);
}
