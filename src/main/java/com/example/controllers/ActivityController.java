package com.example.controllers;

import com.example.dto.ActivityDto;
import com.example.dto.UserDto;
import com.example.model.Activity;
import com.example.services.ActivityService;
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

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
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
}
