package org.netcracker.learningcenter.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AnalysisUtils {

    private static final String COMMENTS = "comments";
    private static final String TITLE = "title";
    private static final String BODY = "body";
    public static final String KEY_WORDS_PATH = "keyWords";
    public static final String ALANALYSIS_PARAM_PATH = "analysisParam";
    public static final String ACCURACY_PATH = "accuracy";
    public static final String MIN_SENTENSE_NUMBERS_PATH = "minSentenceNumbers";
    public static final String TOP_WORDS_COUNT_PATH = "topWordsCount";
    public static final String REQUEST_ID = "requestId";
    public static final String SOURCE = "source";
    public static final String KEYWORDS_LIST = "keywordsList";
    public static final String SELECTED_SOURCES = "selectedSources";
    public static final String USER_ID = "userId";
    public static final String MODIFICATION_DATE = "modificationDate";

    public static List<AnalysisDataModel> jsonToAnalysisDataModel(List<JsonNode> dataFromElastic) {
        List<AnalysisDataModel> dataModelList = new ArrayList<>();
        for (JsonNode node : dataFromElastic) {
            AnalysisDataModel dataModel = new AnalysisDataModel();
            dataModel.setDataSource(node.path(SOURCE).asText());
            dataModel.setTitle(node.path(TITLE).asText());
            dataModel.setModificationDate(node.path(MODIFICATION_DATE).asText());
            StringBuilder sb = new StringBuilder();
            sb.append(node.path(BODY).asText()).append(System.lineSeparator());
            Iterator<JsonNode> iterator = node.path(COMMENTS).elements();
            while (iterator.hasNext()) {
                sb.append(iterator.next().asText()).append(System.lineSeparator());
            }
            dataModel.setText(sb.toString());
            dataModelList.add(dataModel);
        }
        return dataModelList;
    }

    public static List<String> jsonNodeToStringList(JsonNode node) {
        Iterator<JsonNode> iterator = node.elements();
        List<String> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            resultList.add(iterator.next().asText());
        }
        return resultList;
    }
}
