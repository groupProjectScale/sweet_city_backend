package com.example.services;

import com.example.configurations.S3Configuration;
import com.example.dto.ImageTaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    private final String BUCKET;
    private final String ENDPOINT;
    private S3Client s3Client;
    private SqsProducerService sqsProducerService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static final Logger logger = LogManager.getLogger(S3Service.class);

    public S3Service(
            S3Client s3Client,
            S3Configuration s3Configuration,
            SqsProducerService sqsProducerService) {
        this.s3Client = s3Client;
        this.BUCKET = s3Configuration.Bucket();
        this.ENDPOINT = s3Configuration.getEndpointUrl();
        this.sqsProducerService = sqsProducerService;
    }

    @Transactional
    public Future<List<String>> uploadFileAsync(UUID activityId, List<String> paths) {
        return executorService.submit(
                () -> {
                    List<String> res = new ArrayList<>();
                    try {
                        for (String path : paths) {
                            String key = UUID.randomUUID().toString();
                            PutObjectRequest request =
                                    PutObjectRequest.builder().bucket(BUCKET).key(key).build();
                            s3Client.putObject(request, RequestBody.fromBytes(getObjectFile(path)));

                            String url = String.format("%s/%s/%s", ENDPOINT, BUCKET, key);
                            ImageTaskDto imageTaskDto =
                                    new ImageTaskDto(activityId, UUID.fromString(key), url);
                            String json = objectMapper.writeValueAsString(imageTaskDto);
                            sqsProducerService.sendMessage(json);
                            res.add(key);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    return res;
                });
    }

    private static byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return bytesArray;
    }
}
