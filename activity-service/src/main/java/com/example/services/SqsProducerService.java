package com.example.services;

import com.example.configurations.SqsConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsProducerService {
    private SqsClient sqsClient;
    private String queueUrl;
    private static final Logger logger = LogManager.getLogger(SqsProducerService.class);

    public SqsProducerService(SqsClient sqsClient, SqsConfiguration sqsConfiguration) {
        this.sqsClient = sqsClient;
        GetQueueUrlRequest getQueueRequest =
                GetQueueUrlRequest.builder().queueName(sqsConfiguration.queueName()).build();

        this.queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
    }

    public void sendMessage(String activityId) {
        System.out.println("send Message");
        try {
            SendMessageRequest sendMsgRequest =
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(activityId + " " + path)
                            .build();
            sqsClient.sendMessage(sendMsgRequest);

        } catch (SqsException e) {
            logger.error(e.awsErrorDetails().errorMessage());
        }
    }
}
