package org.netcracker.learningcenter.utils;

public class AnalyticsServiceResponse {
    private String requestId;
    private Status status;

    public AnalyticsServiceResponse() {
    }

    public AnalyticsServiceResponse(String requestId, Status status) {
        this.requestId = requestId;
        this.status = status;
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
