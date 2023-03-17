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
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Configuration {

    @Value("${amazon.s3.endpointUrl}") private String endpointUrl;

    @Value("${amazon.s3.accesskey}") private String accessKey;

    @Value("${amazon.s3.region}") private String region;

    @Value("${amazon.s3.secretkey}") private String secretKey;

    @Bean
    public S3Client s3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public String getEndpointUrl() {
        return endpointUrl;
    }

    @Bean
    public String Bucket() {
        return "sweet-city-storage";
    }
}
