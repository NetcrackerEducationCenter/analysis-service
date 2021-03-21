package org.netcracker.learningcenter.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;


public class AnalysisUtils {
    private static final String TYPE = "type";
    private static final String COMMENTS = "comments";
    private static final String TEXT = "text";
    private static final String ISSUE_TITLE = "issueTitle";
    private static final String ISSUE_BODY = "issueBody";
    public static final String KEY_WORDS_PATH = "keyWords";
    public static final String ALANALYSIS_PARAM_PATH = "analysisParam";
    public static final String ACCURACY_PATH = "accuracy";
    public static final String MIN_SENTENSE_NUMBERS_PATH = "minSentenceNumbers";
    public static final String TOP_WORDS_COUNT_PATH = "topWordsCount";
    public static final String REQUEST_ID = "requestId";
    public static final String SOURCE = "source";

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
