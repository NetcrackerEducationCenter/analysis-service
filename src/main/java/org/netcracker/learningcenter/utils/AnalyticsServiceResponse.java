package org.netcracker.learningcenter.utils;

import java.util.List;

public class AnalyticsServiceResponse {
    private String requestId;
    private Status status;
    private List<String> keywords;
    private String date;
    private String userId;

    public AnalyticsServiceResponse() {
    }

    public AnalyticsServiceResponse(String requestId, Status status) {
        this.requestId = requestId;
        this.status = status;
    }

    public AnalyticsServiceResponse(String requestId, Status status, List<String> keywords, String date, String userId) {
        this.requestId = requestId;
        this.status = status;
        this.keywords = keywords;
        this.date = date;
        this.userId = userId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
