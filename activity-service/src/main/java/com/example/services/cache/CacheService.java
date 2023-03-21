package com.example.services.cache;

import com.example.model.Activity;
import com.example.repository.ActivityRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final CacheHandlerImpl cacheHandlerImpl;
    private final ActivityRepository activityRepository;
    private static final int FUTURE_TIME_OUT = 1000;
    private static final TimeUnit FUTURE_TIME_OUT_TIME_UNIT = TimeUnit.MILLISECONDS;

    public CacheService(RedissonClient redissonClient, ActivityRepository activityRepository) {
        this.cacheHandlerImpl = new CacheHandlerImpl(redissonClient);
        this.activityRepository = activityRepository;
    }

    public List<Activity> getActivityByTagId(UUID tagId) {
        Future<List<Activity>> futureActivities = cacheHandlerImpl.getAsync(tagId.toString());
        try {
            List<Activity> activities =
                    futureActivities.get(FUTURE_TIME_OUT, FUTURE_TIME_OUT_TIME_UNIT);
            if (activities == null || activities.isEmpty()) {
                activities = activityRepository.findActivitiesByTagId(tagId);
                cacheHandlerImpl.putAsync(tagId.toString(), activities);
            }
            return activities;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
