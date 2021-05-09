package org.netcracker.learningcenter.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.netcracker.learningcenter.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NLPService {
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

    private final FrequencyTextAnalysis frequencyTextAnalysis;
    private final ReportService reportService;
    private final ElasticsearchService elasticsearchService;
    private final Pipeline pipeline;
    private final ProducerService producerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public NLPService(FrequencyTextAnalysis frequencyTextAnalysis, ReportService reportService, ElasticsearchService elasticsearchService, Pipeline pipeline, ProducerService producerService, ObjectMapper objectMapper) {
        this.frequencyTextAnalysis = frequencyTextAnalysis;
        this.reportService = reportService;
        this.elasticsearchService = elasticsearchService;
        this.pipeline = pipeline;
        this.producerService = producerService;
        this.objectMapper = objectMapper;
    }

    public void analyzingDataFromElasticsearch(List<String> keywords, String requestId, String userId) throws Exception {
        String startDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        producerService.sendMessage(objectMapper.valueToTree(new AnalyticsServiceResponse(requestId, Status.IN_PROCESS, keywords, startDate, userId)));
        reportService.createReport(keywords, requestId, userId, Status.IN_PROCESS);
        List<JsonNode> dataFromElastic = elasticsearchService.getDataByRequestId(requestId);
        List<AnalysisDataModel> dataModels = AnalysisUtils.jsonToAnalysisDataModel(dataFromElastic);
        List<AnalysisDataModel> searchInfo = searchInformation(keywords, dataModels, ACCURACY, MIN_SENTENSE_NUMBERS, TOP_WORDS_COUNT);
        String endDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        reportService.updateReport(requestId, searchInfo, Status.COMPLETED, endDate);
        producerService.sendMessage(objectMapper.valueToTree(new AnalyticsServiceResponse(requestId, Status.COMPLETED, keywords, endDate, userId)));
    }

    /**
     * Selects main information from a given list of texts containing words from keywords list
     *
     * @param keywords   a list of words that should be contained in the analyzed texts
     * @param dataModels list of objects containing text for analysis
     * @return list of selected main information from texts
     */

    public List<AnalysisDataModel> searchInformation(List<String> keywords, List<AnalysisDataModel> dataModels, int accuracy, int minSentenceNumbers, int topWordsCount) {
        List<AnalysisDataModel> foundInfo = new ArrayList<>();
        for (AnalysisDataModel model : dataModels) {
            Annotation annotation = pipeline.getStanfordCoreNLP().process(model.getText());
            List<CoreMap> sentenses = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            List<String> topWords = Counters.topKeys(frequencyTextAnalysis.termFrequency(sentenses), topWordsCount);
            if (containsKeywords(topWords, keywords)) {
                if (sentenses.size() <= minSentenceNumbers) {
                    foundInfo.add(model);
                } else {
                    foundInfo.add(new AnalysisDataModel(model.getDataSource(), findImportantInfo(sentenses, accuracy), model.getModificationDate()));
                }
            }
        }
        return foundInfo;
    }

    private boolean containsKeywords(List<String> topWords, List<String> keywords) {
        for (String word : keywords) {
            if (topWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Analyzes the text and based on the weights of the sentences extracts the main information
     *
     * @param sentenses list of sentences from text
     * @return main sentences from the text
     */
    private String findImportantInfo(List<CoreMap> sentenses, int accuracy) {
        StringBuilder sb = new StringBuilder();
        Counter<Integer> weights = frequencyTextAnalysis.sentencesWeights(sentenses);
        double averageWeight = Precision.round(Counters.mean(weights), accuracy, BigDecimal.ROUND_DOWN);
        for (Integer k : weights.keySet()) {
            if (weights.getCount(k) >= averageWeight) {
                sb.append(sentenses.get(k));
            }
        }
        return sb.toString();
    }
}
