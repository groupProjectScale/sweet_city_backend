package com.example.configurations;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfiguration {

    @Value("${amazon.s3.endpointUrl}") private String endpointUrl;

    @Value("${amazon.s3.accesskey}") private String accessKey;

    @Value("${amazon.s3.region}") private String region;

    @Value("${amazon.s3.secretkey}") private String secretKey;

    private String queueName = "activity-sqs";

    @Bean
    public SqsClient sqsClient() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        return SqsClient.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public String queueName() {
        return queueName;
    }
}
