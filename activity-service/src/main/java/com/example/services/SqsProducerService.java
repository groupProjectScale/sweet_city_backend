package com.example.services;

import com.example.configurations.SqsConfiguration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsProducerService {
    private SqsClient sqsClient;
    private String queueUrl;

    public SqsProducerService(SqsClient sqsClient, SqsConfiguration sqsConfiguration) {
        this.sqsClient = sqsClient;
        GetQueueUrlRequest getQueueRequest =
                GetQueueUrlRequest.builder().queueName(sqsConfiguration.queueName()).build();

        this.queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
    }

    public void sendMessage(String activityId, String path) {
        System.out.println("send Message");
        try {
            SendMessageRequest sendMsgRequest =
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(activityId + " " + path)
                            .build();
            sqsClient.sendMessage(sendMsgRequest);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
