package org.netcracker.learningcenter.model;

import nonapi.io.github.classgraph.json.Id;
import org.netcracker.learningcenter.utils.AnalysisDataModel;
import org.netcracker.learningcenter.utils.Status;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Report {
    @Id
    private String id;
    private String requestId;
    private String userId;
    private String date;
    private List<AnalysisDataModel> dataModels;
    private Status status;
    private List<String> keywords;

    public Report() {
    }

    public Report(String requestId, String date, List<String> keywords, List<AnalysisDataModel> dataModel,Status status) {
        this.requestId = requestId;
        this.date = date;
        this.keywords = keywords;
        this.dataModels = dataModel;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<AnalysisDataModel> getDataModels() {
        return dataModels;
    }

    public void setDataModels(List<AnalysisDataModel> dataModels) {
        this.dataModels = dataModels;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Report{" +
                "requestId='" + requestId + '\'' +
                ", date='" + date + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
