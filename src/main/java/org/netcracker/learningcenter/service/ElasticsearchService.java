package org.netcracker.learningcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.netcracker.educationcenter.elasticsearch.connection.Connection;
import org.netcracker.educationcenter.elasticsearch.database.operations.ElasticsearchOperations;
import org.netcracker.educationcenter.elasticsearch.database.operations.FTPFileObjectOperations;
import org.netcracker.educationcenter.elasticsearch.database.operations.JiraIssueOperations;
import org.netcracker.educationcenter.elasticsearch.search.DocumentSearch;
import org.netcracker.educationcenter.elasticsearch.search.FTPFileObjectSearch;
import org.netcracker.educationcenter.elasticsearch.search.JiraIssueSearch;
import org.netcracker.educationcenter.elasticsearch.search.SearchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class ElasticsearchService {
    private static final String REQUEST_ID = "requestId";
    private final Properties properties;

    public ElasticsearchService(@Value("${hostname}") String hostname,
                                @Value("${scheme}") String scheme,
                                @Value("${port1}") String port1,
                                @Value("${port2}") String port2) {
        properties = new Properties();
        properties.setProperty("hostname", hostname);
        properties.setProperty("scheme", scheme);
        properties.setProperty("port1", port1);
        properties.setProperty("port2", port2);
    }

    public List<JsonNode> getDataByRequestId(String id) throws Exception {
        List<JsonNode> data = new ArrayList<>();
        try (Connection connection = new Connection(properties)) {
            connection.makeConnection();
            FTPFileObjectSearch documentSearch = new FTPFileObjectSearch(connection);
            JiraIssueSearch jiraIssueSearch = new JiraIssueSearch(connection);
            data.addAll(documentSearch.searchByFieldValue(id, REQUEST_ID));
            data.addAll(jiraIssueSearch.searchByFieldValue(id, REQUEST_ID));
        }
        return data;
    }
}
