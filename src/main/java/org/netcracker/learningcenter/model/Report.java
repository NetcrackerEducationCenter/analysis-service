package org.netcracker.learningcenter.model;

import nonapi.io.github.classgraph.json.Id;
import org.netcracker.learningcenter.utils.Status;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document
public class Report {
    @Id
    private String id;
    private String requestId;
    private String date;
    private Set<String> dataSource;
    private Status status;
    private List<String> keywords;
    private List<String> text;

    public Report() {
    }

    public Report(String requestId, String date, List<String> keywords, List<String> text, Set<String> dataSource, Status status) {
        this.requestId = requestId;
        this.date = date;
        this.keywords = keywords;
        this.text = text;
        this.dataSource = dataSource;
        this.status = status;
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

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public void addToText(String text) {
        this.text.add(text);
    }

    public Set<String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Set<String> dataSource) {
        this.dataSource = dataSource;
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
                ", text='" + text + '\'' +
                '}';
    }
}
