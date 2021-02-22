package org.netcracker.learningcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.netcracker.learningcenter.exceptions.ResourceNotFoundException;
import org.netcracker.learningcenter.service.NLPService;
import org.netcracker.learningcenter.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class NLPController {
    private final NLPService nlpService;
    /**
     * Number of decimal places to round the average sentence weight
     */
    private static final int ACCURACY = 3;
    /**
     * The minimum number of sentences at which the text will not be analyzed,
     * but the entire text will be returned
     */
    private static final int MIN_SENTENSE_NUMBERS = 10;
    private static final int TOP_WORDS_COUNT = 10;
    private static final String KEY_WORDS_PATH = "keyWords";
    private static final String ALANALYSIS_PARAM_PATH = "analysisParam";
    private static final String ACCURACY_PATH = "accuracy";
    private static final String MIN_SENTENSE_NUMBERS_PATH = "minSentenceNumbers";
    private static final String TOP_WORDS_COUNT_PATH = "topWordsCount";
    private static final String REQUEST_ID = "requestId";

    @Autowired
    public NLPController(NLPService nlpService) {
        this.nlpService = nlpService;
    }

    @PostMapping(value = "/split", produces = "application/json", consumes = "application/json")
    public List<String> searchInformation(@RequestBody JsonNode jsonNode) throws IOException, ResourceNotFoundException, URISyntaxException {
        List<String> texts = new ArrayList<>();
        JsonNode keyWordsNode = jsonNode.path(KEY_WORDS_PATH);
        JsonNode accuracyNode = jsonNode.path(ALANALYSIS_PARAM_PATH).path(ACCURACY_PATH);
        JsonNode sentenceNumbersNode = jsonNode.path(ALANALYSIS_PARAM_PATH).path(MIN_SENTENSE_NUMBERS_PATH);
        JsonNode topWordCountNode = jsonNode.path(ALANALYSIS_PARAM_PATH).path(TOP_WORDS_COUNT_PATH);
        JsonNode requestId = jsonNode.path(REQUEST_ID);
        Validations.checkJsonNode(keyWordsNode, requestId);

        Iterator<JsonNode> iterator = keyWordsNode.elements();
        List<String> keyWordsList = new ArrayList<>();
        while (iterator.hasNext()) {
            keyWordsList.add(iterator.next().asText());
        }
        int accuracy = accuracyNode.isMissingNode() ? ACCURACY : accuracyNode.asInt();
        int sentenceNumbers = sentenceNumbersNode.isMissingNode() ? MIN_SENTENSE_NUMBERS : sentenceNumbersNode.asInt();
        int topWordCount = topWordCountNode.isMissingNode() ? TOP_WORDS_COUNT : topWordCountNode.asInt();
        return nlpService.searchInformation(keyWordsList, texts, accuracy, sentenceNumbers, topWordCount);

    }
}
