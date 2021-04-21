package org.netcracker.learningcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.netcracker.educationcenter.elasticsearch.connection.Connection;
import org.netcracker.educationcenter.elasticsearch.search.DocumentModelSearch;
import org.netcracker.educationcenter.elasticsearch.search.DocumentSearch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ElasticsearchService {
    /**
     * Elasticsearch document index for Jira-issues
     */
    public static final String JIRA_INDEX = "jiraissues";

    /**
     * Elasticsearch document index for FTP-server files
     */
    public static final String FTP_INDEX = "ftpfileobjects";

    /**
     * Elasticsearch document index for Confluence-pages
     */
    public static final String CONFLUENCE_INDEX = "confluencepages";
    private static final String REQUEST_ID = "requestId";
    private final Properties properties;

    public ElasticsearchService(@Value("${eshostname}") String hostname,
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
            DocumentSearch search = new DocumentModelSearch(connection);
            data.addAll(search.searchByFieldValue(id, REQUEST_ID,JIRA_INDEX));
            data.addAll(search.searchByFieldValue(id, REQUEST_ID,FTP_INDEX));
            data.addAll(search.searchByFieldValue(id, REQUEST_ID,CONFLUENCE_INDEX));
        }
        return data;
    }
}
