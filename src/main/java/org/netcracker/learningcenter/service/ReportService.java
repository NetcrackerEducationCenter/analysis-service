package org.netcracker.learningcenter.service;

import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.repository.ReportRepository;
import org.netcracker.learningcenter.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private Map<String, Status> statuses;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
        this.statuses = new HashMap<>();
    }

    public Optional<Report> findByRequestId(String id) {
        return reportRepository.findByRequestId(id);
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public void updateReport(String id, String text) {
        Optional<Report> report = reportRepository.findByRequestId(id);
        if (report.isPresent()) {
            report.get().addToText(text);
            reportRepository.save(report.get());
        }

    }

    public Status getStatus(String key) {
        if (!statuses.containsValue(key)) {
            return Status.NOT_STARTED;
        }
        return statuses.get(key);
    }

    public void setStatus(String key, Status status) {
        this.statuses.put(key, status);
    }

}
