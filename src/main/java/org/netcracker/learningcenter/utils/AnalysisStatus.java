package org.netcracker.learningcenter.utils;

import java.util.HashMap;
import java.util.Map;

public class AnalysisStatus {
    private static AnalysisStatus instanse;
    private Map<String, Status> statuses;

    private AnalysisStatus() {
        this.statuses = new HashMap<>();
    }

    public static AnalysisStatus getInstance() {
        if (instanse == null) {
            instanse = new AnalysisStatus();
        }
        return instanse;
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

