package org.netcracker.learningcenter.service;

import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.repository.ReportRepository;
import org.netcracker.learningcenter.utils.AnalysisDataModel;
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

    public void createReport(List<String> keywords, String requestId,String userId, Status status) {
        Report report = new Report();
        report.setKeywords(keywords);
        report.setStatus(status);
        report.setRequestId(requestId);
        report.setUserId(userId);
        reportRepository.save(report);
    }

    public void updateReport(String id, List<AnalysisDataModel> dataModels, Status status, String date) {
        Optional<Report> report = reportRepository.findByRequestId(id);
        if (report.isPresent()) {
            report.get().setDataModels(dataModels);
            report.get().setStatus(status);
            report.get().setDate(date);
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
