package com.example.controllers;

import com.example.dto.ActivityDto;
import com.example.dto.RequirementDto;
import com.example.dto.TagDto;
import com.example.dto.UserDto;
import com.example.dto.UserLoginDto;
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
    private final ActivityService activityService;
    private final DynamodbService dynamodbService;

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
    // TODO, For Test Only
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityDto activityDto) {
        Activity activity = activityService.addActivity(activityDto);
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/add-tag/{activityId}")
    public ResponseEntity<Boolean> addTagForActivity(
            @PathVariable(value = "activityId") UUID activityId, @RequestBody TagDto tagDto) {
        boolean res = activityService.addTagForActivity(activityId, tagDto);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add-req/{activityId}")
    public ResponseEntity<Boolean> addRequirementForActivity(
            @PathVariable(value = "activityId") UUID activityId,
            @RequestBody RequirementDto requirementDto) {
        boolean res = activityService.addRequirementForActivity(activityId, requirementDto);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/join/{activityId}")
    public ResponseEntity<Boolean> joinActivity(
            @PathVariable("activityId") UUID activityId, @RequestBody UserLoginDto userLoginDto) {
        boolean res = activityService.addAttendee(activityId, userLoginDto);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/quit/{activityId}")
    public ResponseEntity<Boolean> quitActivity(
            @PathVariable("activityId") UUID activityId, @RequestBody UserLoginDto userLoginDto) {
        boolean res = activityService.deleteAttendee(activityId, userLoginDto);
        return ResponseEntity.ok(res);
    }

    @GetMapping("get/activity/frequency/{tagId}")
    public ResponseEntity<String> getNumberOfCreationsForTag(@PathVariable String tagId) {
        if (!activityService.validateTagId(tagId)) {
            return ResponseEntity.badRequest().body("bad request, this is an invalid tagId");
        }
        int num = activityService.getNumberOfCreationsForTag(tagId);
        return ResponseEntity.ok(Integer.toString(num));
    }

    @GetMapping("/get/{activityId}/current-participant")
    public ResponseEntity<Integer> getCurrentParticipant(@PathVariable String activityId) {
        int currentParticipant = dynamodbService.getLiveParticipants(activityId);
        if (currentParticipant == -1) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(currentParticipant);
    }

    @GetMapping("/get/ranking")
    public ResponseEntity<List<Activity>> getActivityRanking(@RequestBody UserDto userDto) {
        List<Activity> activities =
                activityService.getActivityRanking(userDto.getUserName(), Optional.of(2));
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
}
