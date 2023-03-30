package com.example.services;

import com.example.configurations.SqsConfiguration;
import com.example.dto.ImageTaskDto;
import com.example.model.Activity;
import com.example.model.Image;
import com.example.repository.ActivityRepository;
import com.example.repository.ImageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsConsumerService {
    private final ScheduledExecutorService sqsExecutors = Executors.newScheduledThreadPool(8);
    private SqsClient sqsClient;
    private String queueUrl;
    private ObjectMapper objectMapper;
    private ActivityRepository activityRepository;
    private ImageRepository imageRepository;
    private static final Logger logger = LogManager.getLogger(SqsConsumerService.class);

    public SqsConsumerService(
            SqsClient sqsClient,
            ActivityRepository activityRepository,
            ImageRepository imageRepository,
            ObjectMapper objectMapper,
            SqsConfiguration sqsConfiguration) {
        this.sqsClient = sqsClient;
        GetQueueUrlRequest getQueueRequest =
                GetQueueUrlRequest.builder().queueName(sqsConfiguration.queueName()).build();
        this.queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
        this.activityRepository = activityRepository;
        this.imageRepository = imageRepository;
        this.objectMapper = objectMapper;
        initSqsPoller();
    }

    private void initSqsPoller() {
        sqsExecutors.scheduleAtFixedRate(this::receiveMessages, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void receiveMessages() {
        try {
            ReceiveMessageRequest receiveMessageRequest =
                    ReceiveMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .maxNumberOfMessages(5)
                            .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

            for (Message msg : messages) {
                process(msg);
            }

        } catch (SqsException e) {
            logger.info(e.getMessage());
        }
    }

    public void process(Message msg) {
        try {
            ImageTaskDto imageTaskDto = objectMapper.readValue(msg.body(), ImageTaskDto.class);
            // get activityId and imageId
            UUID activityId = imageTaskDto.getActivityId();
            Activity a = activityRepository.findById(activityId).get();
            UUID imageId = imageTaskDto.getImageId();
            String url = imageTaskDto.getUrl();
            // store image
            Image image = new Image(imageId, url);
            image = imageRepository.save(image);
            // update images in activity
            a.addImages(image);
            activityRepository.save(a);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }
}
