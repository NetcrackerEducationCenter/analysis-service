package org.netcracker.learningcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.service.NLPService;
import org.netcracker.learningcenter.service.ReportService;
import org.netcracker.learningcenter.utils.Status;
import org.netcracker.learningcenter.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.netcracker.learningcenter.utils.AnalysisUtils.*;

@RestController
public class NLPController {
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
    private final NLPService nlpService;
    private final ReportService reportService;

    @Autowired
    public NLPController(NLPService nlpService, ReportService reportService) {
        this.nlpService = nlpService;
        this.reportService = reportService;
    }

    @PostMapping(value = "/analysis", produces = "application/json", consumes = "application/json")
    public void analysisInformation(@RequestBody JsonNode jsonNode) throws Exception {
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
        nlpService.analyzingDataFromElasticsearch(keyWordsList, accuracy, sentenceNumbers, topWordCount, requestId.asText());
    }

    @GetMapping("/status/{requestId}")
    public Status getAnalysisStatus(@PathVariable String requestId) {
        return reportService.getStatus(requestId);
    }

    @GetMapping("/dataByRequestId/{requestId}")
    public ResponseEntity<?> getDataByRequestId(@PathVariable String requestId) {
        Optional<Report> data = reportService.findByRequestId(requestId);
        if (data.isPresent()) {
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No report found with this request id", HttpStatus.NOT_FOUND);
    }
}
