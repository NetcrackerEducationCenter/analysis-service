package org.netcracker.learningcenter.utils;

public class AnalysisDataModel {
    private String modificationDate;
    private String dataSource;
    private String text;
    private String title;

    public AnalysisDataModel() {
    }

    public AnalysisDataModel(String dataSource, String text, String modificationDate) {
        this.dataSource = dataSource;
        this.text = text;
        this.modificationDate = modificationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
