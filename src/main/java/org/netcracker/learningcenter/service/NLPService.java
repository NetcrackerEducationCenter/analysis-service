package org.netcracker.learningcenter.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.math3.util.Precision;
import org.netcracker.learningcenter.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.netcracker.learningcenter.utils.AnalysisUtils.SOURCE;


@Service
public class NLPService {

    private final FrequencyTextAnalysis frequencyTextAnalysis;
    private final ReportService reportService;
    private final ElasticsearchService elasticsearchService;
    private final Pipeline pipeline;
    private final ProducerService producerService;

    @Autowired
    public NLPService(FrequencyTextAnalysis frequencyTextAnalysis, ReportService reportService, ElasticsearchService elasticsearchService, Pipeline pipeline, ProducerService producerService) {
        this.frequencyTextAnalysis = frequencyTextAnalysis;
        this.reportService = reportService;
        this.elasticsearchService = elasticsearchService;
        this.pipeline = pipeline;
        this.producerService = producerService;
    }

    public void analyzingDataFromElasticsearch(List<String> keywords, int accuracy, int minSentenceNumbers, int topWordsCount, String requestId) throws Exception {
        List<String> texts = new ArrayList<>();
        Set<String> dataSource = new HashSet<>();
        String startDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        producerService.sendMessage(new AnalyticsServiceResponse(requestId, Status.IN_PROCESS, keywords, startDate));
        reportService.setStatus(requestId, Status.IN_PROCESS);
        List<JsonNode> dataFromElastic = elasticsearchService.getDataByRequestId(requestId);
        for (JsonNode node : dataFromElastic) {
            dataSource.add(node.path(SOURCE).asText());
            texts.add(AnalysisUtils.getTextFromJsonNode(node));
        }
        reportService.createReport(keywords, requestId, dataSource, Status.IN_PROCESS);
        List<String> searchInfo = searchInformation(keywords, texts, accuracy, minSentenceNumbers, topWordsCount);
        String endDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        reportService.updateReport(requestId, searchInfo, Status.COMPLETED, endDate);
        producerService.sendMessage(new AnalyticsServiceResponse(requestId, Status.COMPLETED, keywords, endDate));
        reportService.setStatus(requestId, Status.COMPLETED);
    }

    /**
     * Selects main information from a given list of texts containing words from keywords list
     *
     * @param keywords    a list of words that should be contained in the analyzed texts
     * @param searchTexts a list of texts in which the requested information is searched
     * @return list of selected main information from texts
     */
    public List<String> searchInformation(List<String> keywords, List<String> searchTexts, int accuracy, int minSentenceNumbers, int topWordsCount) {
        List<String> foundInfo = new ArrayList<>();
        for (String text : searchTexts) {
            Annotation annotation = pipeline.getStanfordCoreNLP().process(text);
            List<CoreMap> sentenses = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            List<String> topWords = Counters.topKeys(frequencyTextAnalysis.termFrequency(sentenses), topWordsCount);
            if (containsKeywords(topWords, keywords)) {
                if (sentenses.size() <= minSentenceNumbers) {
                    foundInfo.add(text);
                } else {
                    foundInfo.add(findImportantInfo(sentenses, accuracy));
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
