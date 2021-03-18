package org.netcracker.learningcenter.service;

import org.apache.log4j.Logger;
import org.netcracker.learningcenter.model.Report;
import org.netcracker.learningcenter.utils.AnalyticsServiceResponse;
import org.netcracker.learningcenter.utils.DataCollectorRequest;
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
    private final KafkaTemplate<String, Report> reportKafkaTemplate;
    @Value("${kafka.analysis-topic}")
    private String topic;
    @Value("${kafka.reports-topic}")
    private String reportTopic;


    @Autowired
    public ProducerService(KafkaTemplate<String, AnalyticsServiceResponse> kafkaTemplate, KafkaTemplate<String, Report> reportKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.reportKafkaTemplate = reportKafkaTemplate;
    }

    public void sendMessage(AnalyticsServiceResponse message) {
        ListenableFuture<SendResult<String, AnalyticsServiceResponse>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, AnalyticsServiceResponse>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to send message=[ RequestId: " + message.getRequestId() + " status: " + message.getStatus() + "] due to : " + throwable.getMessage());

            }

            @Override
            public void onSuccess(SendResult<String, AnalyticsServiceResponse> stringStringSendResult) {
                LOGGER.info("Sent message=[ RequestId: " + message.getRequestId() + " status: " + message.getStatus() + "] with offset=[" + stringStringSendResult.getRecordMetadata().offset() + "]");
            }
        });

    }

    public void sendReport(Report report) {
        ListenableFuture<SendResult<String, Report>> future = reportKafkaTemplate.send(reportTopic, report);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Report>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Unable to send message=[Report with requestId : " + report.getRequestId() + "] due to : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Report> stringReportSendResult) {
                LOGGER.info("Sent message=[Report with requestId : " + report.getRequestId() + "] with offset=[" + stringReportSendResult.getRecordMetadata().offset() + "]");
            }
        });

    }
}
