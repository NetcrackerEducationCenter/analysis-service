package org.netcracker.learningcenter.service;

import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.repository.ReportRepository;
import org.netcracker.learningcenter.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void createReport(List<String> keywords, String requestId, Set<String> dataSource, Status status) {
        Report report = new Report();
        report.setKeywords(keywords);
        report.setStatus(status);
        report.setRequestId(requestId);
        report.setDataSource(dataSource);
        reportRepository.save(report);
    }

    public void updateReport(String id, List<String> text, Status status, String date) {
        Optional<Report> report = reportRepository.findByRequestId(id);
        if (report.isPresent()) {
            Report r = report.get();
            r.setText(text);
            r.setStatus(status);
            r.setDate(date);
            reportRepository.save(r);
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
