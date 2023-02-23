package com.example.services;

import com.example.dto.ActivityDto;
import com.example.model.Activity;
import com.example.repository.ActivityRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/** The type Activity service. */
@Service
public class ActivityService {

    /** The Activity repository. */
    private ActivityRepository activityRepository;

    /**
     * Initialize ActivityService.
     *
     * @param activityRepository the activity repository
     */
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Gets activity by activity id.
     *
     * @param activityId activity id
     * @return activity by activity id
     */
    public Optional<Activity> getActivityById(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        return activity;
    }

    /**
     * Find all activity.
     *
     * @return all activity
     */
    public List<Activity> getAllActivity() {
        List<Activity> lst = activityRepository.findAll();
        return lst;
    }

    /**
     * Create new activity TODO.
     *
     * @param activityDto activityDto
     * @return activity
     */
    public Activity addActivity(ActivityDto activityDto) {
        if (!validateActivity(activityDto)) {
            return null;
        }
        Activity a = new Activity();
        BeanUtils.copyProperties(activityDto, a);
        return activityRepository.save(a);
    }

    /**
     * validate activity TODO.
     *
     * @param activityDto activityDto
     * @return Whether activity is valid
     */
    public boolean validateActivity(ActivityDto activityDto) {
        // TO DO
        return true;
    }
}
