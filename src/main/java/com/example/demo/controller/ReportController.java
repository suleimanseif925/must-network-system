package com.example.demo.controller;

import com.example.demo.entity.Report;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @PostMapping
    public Report createReport(@RequestBody Report report) {
        report.setStatus("Pending");
        return reportRepository.save(report);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @GetMapping("/user/{email}")
    public List<Report> getReportsByEmail(@PathVariable String email) {
        return reportRepository.findByEmail(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @RequestBody Report updatedReport) {
        Optional<Report> optional = reportRepository.findById(id);
        if (optional.isPresent()) {
            Report report = optional.get();
            if (!"Pending".equals(report.getStatus())) {
                return ResponseEntity.badRequest().build();
            }
            report.setProblemType(updatedReport.getProblemType());
            report.setLocation(updatedReport.getLocation());
            report.setPriority(updatedReport.getPriority());
            report.setDescription(updatedReport.getDescription());
            return ResponseEntity.ok(reportRepository.save(report));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Report> changeStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Report> optional = reportRepository.findById(id);
        if (optional.isPresent()) {
            Report report = optional.get();
            report.setStatus(status.replace("\"", ""));
            return ResponseEntity.ok(reportRepository.save(report));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/remark")
    public ResponseEntity<Report> addRemark(@PathVariable Long id, @RequestBody String remark) {
        Optional<Report> optional = reportRepository.findById(id);
        if (optional.isPresent()) {
            Report report = optional.get();
            report.setSupervisorRemark(remark.replace("\"", ""));
            return ResponseEntity.ok(reportRepository.save(report));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}