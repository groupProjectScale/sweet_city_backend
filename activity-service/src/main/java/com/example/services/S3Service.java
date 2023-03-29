package com.example.services;

import com.example.configurations.S3Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import com.example.dto.ActivityImageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


@Service
public class S3Service {
    private final String BUCKET;
    private S3Client s3Client;
    private SqsProducerService sqsProducerService;
    private final String endpointUrl;
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(S3Service.class);

    public S3Service(S3Client s3Client, S3Configuration s3Configuration,
                     SqsProducerService sqsProducerService,
                     ObjectMapper objectMapper) {
        this.s3Client = s3Client;
        this.BUCKET = s3Configuration.Bucket();
        this.endpointUrl = s3Configuration.getEndpointUrl();
        this.sqsProducerService = sqsProducerService;
        this.objectMapper = objectMapper;
    }

    public UUID uploadFile(UUID acticityId, String path) {
        try {
            UUID imageId = UUID.randomUUID();
            String key = imageId.toString();
            PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET).key(key).build();

            s3Client.putObject(request, RequestBody.fromBytes(getObjectFile(path)));

            String jsonRes = objectMapper.writeValueAsString(new ActivityImageDto(acticityId, imageId));
            sqsProducerService.sendMessage(jsonRes);
            return imageId;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
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
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }

        return bytesArray;
    }

}
