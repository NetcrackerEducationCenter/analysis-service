package org.netcracker.learningcenter.utils;

public class AnalysisStatus {
    private static AnalysisStatus instanse;
    private Status status;

    private AnalysisStatus(Status status) {
        this.status = status;
    }

    public static AnalysisStatus getInstance() {
        if (instanse == null) {
            instanse = new AnalysisStatus(Status.NOT_STARTED);
        }
        return instanse;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

