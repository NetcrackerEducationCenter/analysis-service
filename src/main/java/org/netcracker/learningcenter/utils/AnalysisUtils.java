package org.netcracker.learningcenter.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class AnalysisUtils {
    private static final String TYPE = "type";
    private static final String COMMENTS = "comments";
    private static final String TEXT = "text";
    private static final String ISSUE_TITLE = "issueTitle";
    private static final String ISSUE_BODY = "issueBody";

    public static String getTextFromJsonNode(JsonNode node) {
        String resultText = "";
        switch (node.path(TYPE).asText()) {
            case "FILE":
                resultText = node.path(TEXT).asText();
                break;
            case "TICKET":
                StringBuilder sb = new StringBuilder();
                sb.append(node.path(ISSUE_TITLE).asText()).append("\n");
                sb.append(node.path(ISSUE_BODY).asText()).append("\n");
                Iterator<JsonNode> iterator = node.path(COMMENTS).elements();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().asText()).append("\n");
                }
                resultText = sb.toString();
                break;
        }
        return resultText;
    }
}
