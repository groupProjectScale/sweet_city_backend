package com.example.services;

import com.example.configurations.SqsConfiguration;
import com.example.dto.ImageDto;
import com.example.model.Activity;
import com.example.model.Image;
import com.example.repository.ActivityRepository;
import com.example.repository.ImageRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Service
public class SqsConsumerService {
    private final ScheduledExecutorService sqsExecutors = Executors.newScheduledThreadPool(8);
    private SqsClient sqsClient;
    private String queueUrl;
    private S3Service s3Service;
    private ActivityRepository activityRepository;
    private ImageRepository imageRepository;
    private static final Logger logger = LogManager.getLogger(SqsConsumerService.class);

    public SqsConsumerService(
            SqsClient sqsClient,
            S3Service s3Service,
            ActivityRepository activityRepository,
            ImageRepository imageRepository,
            SqsConfiguration sqsConfiguration) {
        this.sqsClient = sqsClient;
        GetQueueUrlRequest getQueueRequest =
                GetQueueUrlRequest.builder().queueName(sqsConfiguration.queueName()).build();

        this.queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
        this.s3Service = s3Service;
        this.activityRepository = activityRepository;
        this.imageRepository = imageRepository;
        initSqsPoller();
    }

    private void initSqsPoller() {
        sqsExecutors.scheduleAtFixedRate(this::receiveMessages, 0, 5000, TimeUnit.MILLISECONDS);
    }

    public void receiveMessages() {
        System.out.println("receive message");
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

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Transactional
    public void process(Message msg) {
        String[] body = msg.body().split(" ");
        String activityId = body[0];

        // upload image to s3
//        ImageDto imageDto = s3Service.uploadFile(activityId, path, fileName);
        // store image url in postgres
        Activity a = activityRepository.findById(UUID.fromString(activityId)).get();
        Image image = new Image();
        image = imageRepository.save(image);
        a.addImages(image);
        activityRepository.save(a);
    }

}
