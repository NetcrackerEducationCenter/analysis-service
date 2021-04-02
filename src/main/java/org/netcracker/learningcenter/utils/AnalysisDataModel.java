package org.netcracker.learningcenter.utils;

public class AnalysisDataModel {
    private String dataSource;
    private String text;

    public AnalysisDataModel() {
    }

    public AnalysisDataModel(String dataSource, String text) {
        this.dataSource = dataSource;
        this.text = text;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
