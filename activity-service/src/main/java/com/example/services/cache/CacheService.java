package com.example.services.cache;

import com.example.model.Activity;
import com.example.repository.ActivityRepository;
import java.util.List;
import java.util.UUID;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final CacheHandlerImpl cacheHandlerImpl;
    private final ActivityRepository activityRepository;

    public CacheService(RedissonClient redissonClient, ActivityRepository activityRepository) {
        this.cacheHandlerImpl = new CacheHandlerImpl(redissonClient);
        this.activityRepository = activityRepository;
    }

    public List<Activity> getActivityByTagId(UUID tagId) {
        List<Activity> activities = cacheHandlerImpl.get(tagId.toString());
        if (activities == null || activities.isEmpty()) {
            activities = activityRepository.findActivitiesByTagId(tagId);
            cacheHandlerImpl.putAsync(tagId.toString(), activities);
        }
        return activities;
    }
}
