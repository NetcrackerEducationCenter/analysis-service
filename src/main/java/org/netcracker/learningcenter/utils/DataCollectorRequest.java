package org.netcracker.learningcenter.utils;

import java.util.List;

public class DataCollectorRequest {
    private String requestId;
    private List<String> keywords;

    public DataCollectorRequest() {
    }

    public DataCollectorRequest(String requestId, List<String> keywords) {
        this.requestId = requestId;
        this.keywords = keywords;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
