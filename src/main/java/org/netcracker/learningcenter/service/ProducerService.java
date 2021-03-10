package org.netcracker.learningcenter.service;

import org.apache.log4j.Logger;
import org.netcracker.learningcenter.utils.AnalyticsServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class ProducerService {
    private static final Logger LOGGER = Logger.getRootLogger();
    private final KafkaTemplate<String, AnalyticsServiceResponse> kafkaTemplate;
    @Value("${kafka.analysis-topic}")
    private String topic;

    @Autowired
    public ProducerService(KafkaTemplate<String, AnalyticsServiceResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AnalyticsServiceResponse message) {
        ListenableFuture<SendResult<String, AnalyticsServiceResponse>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, AnalyticsServiceResponse>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.info("Unable to send message=[" + message.toString() + "] due to : " + throwable.getMessage());

            }

            @Override
            public void onSuccess(SendResult<String, AnalyticsServiceResponse> stringStringSendResult) {
                LOGGER.info("Sent message=[" + message.toString() + "] with offset=[" + stringStringSendResult.getRecordMetadata().offset() + "]");
            }
        });

    }
}
