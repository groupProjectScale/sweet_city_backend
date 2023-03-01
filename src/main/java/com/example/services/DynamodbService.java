package com.example.services;

import com.example.configurations.DynamodbConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Service
public class DynamodbService {
    private final DynamoDbClient dynamodbClient;
    private final String participantsStateTable;
    private final String liveParticipantsTable;

    public DynamodbService(
            DynamoDbClient dynamodbClient, DynamodbConfiguration dynamodbConfiguration) {
        this.dynamodbClient = dynamodbClient;
        this.participantsStateTable = dynamodbConfiguration.participantsStateTable();
        this.liveParticipantsTable = dynamodbConfiguration.liveParticipantsTable();
    }

    // update participant's state
    public void updateParticipantState(String activityUuid, String userUuid, String newState) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("activity_uuid", AttributeValue.builder().s(activityUuid).build());
        key.put("user_uuid", AttributeValue.builder().s(userUuid).build());

        // Create the update item request
        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(participantsStateTable)
                        .key(key)
                        .updateExpression("SET participant_state = :val")
                        .expressionAttributeValues(
                                Collections.singletonMap(
                                        ":val", AttributeValue.builder().s(newState).build()))
                        .build();

        dynamodbClient.updateItem(request);
    }

    public void incrementLiveParticipants(String activityId) {
        int currentLiveParticipants = getLiveParticipants(activityId);
        int newLiveParticipants = currentLiveParticipants + 1;
        UpdateItemRequest updateItemRequest =
                UpdateItemRequest.builder()
                        .tableName(liveParticipantsTable)
                        .key(
                                Collections.singletonMap(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityId).build()))
                        .updateExpression("SET number_of_participants = :val")
                        .expressionAttributeValues(
                                Collections.singletonMap(
                                        ":val",
                                        AttributeValue.builder()
                                                .n(String.valueOf(newLiveParticipants))
                                                .build()))
                        .build();
        dynamodbClient.updateItem(updateItemRequest);
    }

    public void decrementLiveParticipants(String activityId) {
        int currentLiveParticipants = getLiveParticipants(activityId);
        int newLiveParticipants = currentLiveParticipants - 1;
        UpdateItemRequest updateItemRequest =
                UpdateItemRequest.builder()
                        .tableName(liveParticipantsTable)
                        .key(
                                Collections.singletonMap(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityId).build()))
                        .updateExpression("SET number_of_participants = :val")
                        .expressionAttributeValues(
                                Collections.singletonMap(
                                        ":val",
                                        AttributeValue.builder()
                                                .n(String.valueOf(newLiveParticipants))
                                                .build()))
                        .build();
        dynamodbClient.updateItem(updateItemRequest);
    }

    // add participant's state
    public void addParticipantState(String activityUuid, String userId, String state) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("activity_uuid", AttributeValue.builder().s(activityUuid).build());
        item.put("user_uuid", AttributeValue.builder().s(userId).build());
        item.put("participant_state", AttributeValue.builder().s(state).build());
        PutItemRequest request =
                PutItemRequest.builder().tableName(participantsStateTable).item(item).build();

        dynamodbClient.putItem(request);
    }

    // get participant's state before checkin or checkout
    public String getParticipantState(String activityUuid, String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("activity_uuid", AttributeValue.builder().s(activityUuid).build());
        key.put("user_uuid", AttributeValue.builder().s(userId).build());
        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(participantsStateTable)
                        .key(key)
                        .projectionExpression("participant_state")
                        .build();

        Map<String, AttributeValue> result = dynamodbClient.getItem(request).item();
        if (result == null || !result.containsKey("participant_state")) {
            return null;
        }
        return result.get("participant_state").s();
    }

    public int getLiveParticipants(String activityUuid) {
        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(liveParticipantsTable)
                        .key(
                                Collections.singletonMap(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build()))
                        .projectionExpression("number_of_participants")
                        .build();

        Map<String, AttributeValue> result = dynamodbClient.getItem(request).item();
        if (result == null || !result.containsKey("number_of_participants")) {
            return 0;
        }

        // return Integer.parseInt(result.get("number_of_participants").n());
        return result.get("number_of_participants").n() == null
                ? Integer.parseInt(result.get("number_of_participants").s())
                : Integer.parseInt(result.get("number_of_participants").n());
    }
}
