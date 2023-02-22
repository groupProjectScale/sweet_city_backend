package com.example.controllers;

import com.example.dto.ActivityDto;
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


/** The type Activity controller. */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    /** The Activity service. */
    private ActivityService activityService;

    /**
     * Instantiates a new Activity Controller.
     *
     * @param activityService activity service
     */
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Gets activity by activity id.
     *
     * @param activityId the activity id
     * @return the user by user id
     */
    @GetMapping("/get/{activityId}")
    public ResponseEntity<Optional<Activity>> getActivityById(@PathVariable UUID activityId) {
        Optional<Activity> activity = activityService.getActivityById(activityId);
        return ResponseEntity.ok(activity);
    }

    /**
     * Find all activity.
     *
     * @return all activities
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<Activity>> getAllActivity() {
        List<Activity> lst = activityService.getAllActivity();
        return ResponseEntity.ok(lst);
    }

    /**
     * Create new activity.
     *
     * @param activityDto the activity dto
     * @return the activity
     */
    @PostMapping("/create")
    // TO DO, For Test Only Now
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityDto activityDto) {
        Activity activity = activityService.addActivity(activityDto);
        return ResponseEntity.ok(activity);
    }
}
