package com.example.services;

import com.example.model.Activity;
import com.example.repository.ActivityRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final ActivityRepository activityRepository;

    public SearchService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> searchByTags(List<String> tagNames) {
        return activityRepository.findByAllTags(tagNames);
    }
}
