package com.example.services;

import com.example.configurations.DynamodbConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
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

    public void incrementLiveParticipants(String activityUuid) {
        int currentLiveParticipants = getLiveParticipants(activityUuid);
        deleteLiveParticipants(activityUuid, currentLiveParticipants);
        updateLiveParticipants(activityUuid, ++currentLiveParticipants);
    }

    public void decrementLiveParticipants(String activityUuid) {
        int currentLiveParticipants = getLiveParticipants(activityUuid);
        deleteLiveParticipants(activityUuid, currentLiveParticipants);
        updateLiveParticipants(activityUuid, --currentLiveParticipants);
    }

    private void deleteLiveParticipants(String activityUuid, int currentLiveParticipants) {
        DeleteItemRequest deleteRequest =
                DeleteItemRequest.builder()
                        .tableName("live_participants_table")
                        .key(
                                Map.of(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build(),
                                        "number_of_participants",
                                        AttributeValue.builder()
                                                .n(Integer.toString(currentLiveParticipants))
                                                .build()))
                        .build();

        try {
            dynamodbClient.deleteItem(deleteRequest);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    private void updateLiveParticipants(String activityUuid, int currentLiveParticipants) {
        PutItemRequest putRequest =
                PutItemRequest.builder()
                        .tableName("live_participants_table")
                        .item(
                                Map.of(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build(),
                                        "number_of_participants",
                                        AttributeValue.builder()
                                                .n(Integer.toString(currentLiveParticipants))
                                                .build()))
                        .build();

        try {
            dynamodbClient.putItem(putRequest);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    // add participant's state
    public void addParticipantState(String activityUuid, String userUuid, String state) {
        PutItemRequest request =
                PutItemRequest.builder()
                        .tableName(participantsStateTable)
                        .item(
                                Map.of(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build(),
                                        "user_uuid",
                                        AttributeValue.builder().s(userUuid).build(),
                                        "participant_state",
                                        AttributeValue.builder().s(state).build()))
                        .build();

        try {
            dynamodbClient.putItem(request);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    // get participant's state before checkin or checkout
    public String getParticipantState(String activityUuid, String userId) {
        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(participantsStateTable)
                        .key(
                                Map.of(
                                        "activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build(),
                                        "user_uuid",
                                        AttributeValue.builder().s(userId).build()))
                        .projectionExpression("participant_state")
                        .build();

        Map<String, AttributeValue> result = dynamodbClient.getItem(request).item();
        if (result == null || !result.containsKey("participant_state")) {
            return null;
        }
        return result.get("participant_state").s();
    }

    public int getLiveParticipants(String activityUuid) {
        QueryRequest queryRequest =
                QueryRequest.builder()
                        .tableName("live_participants_table")
                        .keyConditionExpression("activity_uuid = :activity_uuid")
                        .expressionAttributeValues(
                                Map.of(
                                        ":activity_uuid",
                                        AttributeValue.builder().s(activityUuid).build()))
                        .scanIndexForward(false) // in descending order
                        .limit(1)
                        .build();

        try {
            QueryResponse response = dynamodbClient.query(queryRequest);
            List<Map<String, AttributeValue>> items = response.items();
            if (!items.isEmpty() && items.get(0).containsKey("number_of_participants")) {
                // System.out.println(items.get(0).get("number_of_participants").n());
                return Integer.parseInt(items.get(0).get("number_of_participants").n());
            } else {
                return 0;
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    // update participant's state
    public void updateParticipantState(String activityUuid, String userUuid, String newState) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("activity_uuid", AttributeValue.builder().s(activityUuid).build());
        key.put("user_uuid", AttributeValue.builder().s(userUuid).build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(participantsStateTable)
                        .key(key)
                        .updateExpression("SET participant_state = :val")
                        .expressionAttributeValues(
                                Collections.singletonMap(
                                        ":val", AttributeValue.builder().s(newState).build()))
                        .build();
        try {
            dynamodbClient.updateItem(request);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }
}
