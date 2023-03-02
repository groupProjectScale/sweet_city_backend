package com.example.controllers;

import com.example.dto.ActivityDto;
import com.example.dto.UserDto;
import com.example.model.Activity;
import com.example.services.ActivityService;
import com.example.services.DynamodbService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private ActivityService activityService;
    private DynamodbService dynamodbService;

    public ActivityController(ActivityService activityService, DynamodbService dynamodbService) {
        this.activityService = activityService;
        this.dynamodbService = dynamodbService;
    }

    @GetMapping("/get/{activityId}")
    public ResponseEntity<Optional<Activity>> getActivityById(@PathVariable UUID activityId) {
        Optional<Activity> activity = activityService.getActivityById(activityId);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Activity>> getAllActivity() {
        List<Activity> lst = activityService.getAllActivity();
        return ResponseEntity.ok(lst);
    }

    @PostMapping("/create")
    // TO DO, For Test Only Now
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityDto activityDto) {
        Activity activity = activityService.addActivity(activityDto);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/get/{activityId}/current-participant")
    public ResponseEntity<Integer> getCurrentParticipant(@PathVariable UUID activityId) {
        int currentParticipant = activityService.getCurrentParticipant(activityId);
        if (currentParticipant == -1) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(currentParticipant);
    }

    @GetMapping("/get/ranking")
    public ResponseEntity<List<Activity>> getActivityRanking(@RequestBody UserDto userDto) {
        // To do
        List<Activity> activities = activityService.getActivityRanking(userDto.getUserName());
        return ResponseEntity.ok().body(activities);
    }

    @PostMapping("/{activityId}/{userId}/checkin")
    public ResponseEntity<Boolean> checkIn(
            @PathVariable String activityId, @PathVariable String userId) {
        if (dynamodbService.getParticipantState(activityId, userId).equals("joined")) {
            dynamodbService.incrementLiveParticipants(activityId);
            dynamodbService.updateParticipantState(activityId, userId, "checkIn");
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/{activityId}/{userId}/checkout")
    public ResponseEntity<Boolean> checkOut(
            @PathVariable String activityId, @PathVariable String userId) {
        if (dynamodbService.getParticipantState(activityId, userId).equals("checkIn")) {
            dynamodbService.decrementLiveParticipants(activityId);
            dynamodbService.updateParticipantState(activityId, userId, "checkOut");
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    /* for testing only
    @PostMapping("/{activityId}/{userId}/update")
    public ResponseEntity<Boolean> update(@PathVariable String activityId
        , @PathVariable String userId) {
        dynamodbService.updateParticipantState(activityId, userId, "joined");
        return ResponseEntity.ok(true);
    }
    @PostMapping("/{activityId}/{userId}/add")
    public ResponseEntity<Boolean> add(@PathVariable String activityId
        , @PathVariable String userId) {
        dynamodbService.addParticipantState(activityId, userId, "joined");
        return ResponseEntity.ok(true);
    }
    */
}
