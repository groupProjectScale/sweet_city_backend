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
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamodbConfiguration {
    @Value("${amazon.dynamodb.endpointUrl}") private String endpointUrl;

    @Value("${amazon.dynamodb.accesskey}") private String accessKey;

    @Value("${amazon.dynamodb.secretkey}") private String secretKey;

    @Bean
    public DynamoDbClient dynamodbClient() {

        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        Region region = Region.US_EAST_1;
        return DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(endpointUrl))
                .region(region)
                .build();
    }

    @Bean
    public String participantsStateTable() {
        return "participants_state_table";
    }

    @Bean
    public String liveParticipantsTable() {
        return "live_participants_table";
    }
}
