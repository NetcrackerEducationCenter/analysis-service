package org.netcracker.learningcenter.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class AnalysisUtils {
    private static final String TYPE = "type";
    private static final String COMMENTS = "comments";
    private static final String TEXT = "text";

    public static String getTextFromJsonNode(JsonNode node) {
        String resultText = "";
        switch (node.path(TYPE).asText()) {
            case "FILE":
                resultText = node.path(TEXT).asText();
                break;
            case "TICKET":
                Iterator<JsonNode> iterator = node.path(COMMENTS).elements();
                StringBuilder sb = new StringBuilder();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().asText());
                }
                resultText = sb.toString();
                break;
        }
        return resultText;
    }
}
