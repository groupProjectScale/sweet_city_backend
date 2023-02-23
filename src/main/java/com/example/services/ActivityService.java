package com.example.services;

import com.example.dto.ActivityDto;
import com.example.model.Activity;
import com.example.repository.ActivityRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Optional<Activity> getActivityById(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        return activity;
    }

    public List<Activity> getAllActivity() {
        List<Activity> lst = activityRepository.findAll();
        return lst;
    }

    public Activity addActivity(ActivityDto activityDto) {
        if (!validateActivity(activityDto)) {
            return null;
        }
        Activity a = new Activity();
        BeanUtils.copyProperties(activityDto, a);
        return activityRepository.save(a);
    }

    public boolean validateActivity(ActivityDto activityDto) {
        // TO DO
        return true;
    }
}
