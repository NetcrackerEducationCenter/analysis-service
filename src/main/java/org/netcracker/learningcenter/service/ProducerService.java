package org.netcracker.learningcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
public class ProducerService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;
    @Value("${kafka.analysis-topic}")
    private String topic;
    @Value("${kafka.reports-topic}")
    private String reportTopic;


    @Autowired
    public ProducerService(KafkaTemplate<String, JsonNode> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(JsonNode message) {
        ListenableFuture<SendResult<String, JsonNode>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, JsonNode>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to send message=[{}] due to : {}", message.toString(), throwable.getMessage());

            }

            @Override
            public void onSuccess(SendResult<String, JsonNode> stringSendResult) {
                LOGGER.info("Sent message=[{}] with offset=[{}]", message.toString(), stringSendResult.getRecordMetadata().offset());
            }
        });

    }
}
