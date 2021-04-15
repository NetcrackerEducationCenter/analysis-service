package org.netcracker.learningcenter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.netcracker.learningcenter.exceptions.ResourceNotFoundException;
import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.service.NLPService;
import org.netcracker.learningcenter.service.ProducerService;
import org.netcracker.learningcenter.service.ReportService;
import org.netcracker.learningcenter.utils.Status;
import org.netcracker.learningcenter.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.netcracker.learningcenter.utils.AnalysisUtils.*;

@RestController
public class NLPController {
    private final NLPService nlpService;
    private final ReportService reportService;
    private final ProducerService producerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public NLPController(NLPService nlpService, ReportService reportService, ProducerService producerService, ObjectMapper objectMapper) {
        this.nlpService = nlpService;
        this.reportService = reportService;
        this.producerService = producerService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.dc-topic}")
    public void analyzingDataFromElasticsearch(ConsumerRecord<String, String> record) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(record.value());
        JsonNode requestId = jsonNode.path(REQUEST_ID);
        JsonNode keywords = jsonNode.path(KEYWORDS_LIST);
        JsonNode userId = jsonNode.path(USER_ID);
        Validations.checkJsonNode(requestId, keywords,userId);
        Iterator<JsonNode> iterator = keywords.elements();
        List<String> keyWordsList = new ArrayList<>();
        while (iterator.hasNext()) {
            keyWordsList.add(iterator.next().asText());
        }
        nlpService.analyzingDataFromElasticsearch(keyWordsList,requestId.asText());
    }

    @PostMapping(value = "/analysis", produces = "application/json", consumes = "application/json")
    public void analysisInformation(@RequestBody JsonNode jsonNode) throws Exception {
        JsonNode keyWordsNode = jsonNode.path(KEY_WORDS_PATH);
        JsonNode requestId = jsonNode.path(REQUEST_ID);
        Validations.checkJsonNode(keyWordsNode, requestId);
        Iterator<JsonNode> iterator = keyWordsNode.elements();
        List<String> keyWordsList = new ArrayList<>();
        while (iterator.hasNext()) {
            keyWordsList.add(iterator.next().asText());
        }
        nlpService.analyzingDataFromElasticsearch(keyWordsList,requestId.asText());
    }

    @GetMapping("/status/{requestId}")
    public Status getAnalysisStatus(@PathVariable String requestId) {
        return reportService.getStatus(requestId);
    }

    @KafkaListener(topics = "${kafka.getReport-topic}", groupId = "${kafka.consumer.report-group-id}")
    public void getDataByRequestId(ConsumerRecord<String, String> record) throws JsonProcessingException, ResourceNotFoundException {
        JsonNode jsonNode = objectMapper.readTree(record.value());
        JsonNode requestId = jsonNode.path(REQUEST_ID);
        Validations.checkJsonNode(requestId);
        Optional<Report> data = reportService.findByRequestId(requestId.asText());
        if (data.isPresent()) {
            producerService.sendReport(objectMapper.valueToTree(data.get()));
        }
    }
}
