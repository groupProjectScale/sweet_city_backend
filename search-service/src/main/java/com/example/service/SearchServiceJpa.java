package com.example.service;

import com.example.model.Activity;
import com.example.model.Location;
import com.example.repository.ActivityRepository;
import com.example.repository.LocationRepository;
import com.example.service.cache.annotations.Cached;
import com.example.service.cache.annotations.CachedAsync;
import com.example.services.DynamodbService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import proto.HeartbeatRequest;
// import proto.HeartbeatResponse;
// import proto.MonitoringServiceGrpc;

@Service
public class SearchServiceJpa {
    private static final long DEFAULT_RADIUS = 100000000000L;
    private final LocationRepository locationRepository;
    private final ActivityRepository activityRepository;
    private final DynamodbService dynamodbService;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final String SERVICE_NAME = "search";
    private static final Logger logger = LogManager.getLogger(SearchServiceJpa.class);

    //    @GrpcClient("search")
    //    MonitoringServiceGrpc.MonitoringServiceStub stub;

    public SearchServiceJpa(
            LocationRepository locationRepository,
            ActivityRepository activityRepository,
            DynamodbService dynamodbService) {
        this.locationRepository = locationRepository;
        this.activityRepository = activityRepository;
        this.dynamodbService = dynamodbService;
        sendHeartBeat();
    }

    private void sendHeartBeat() {
        //        scheduler.scheduleAtFixedRate(this::sendHeartBeatMessage, 0, 10000,
        // TimeUnit.MILLISECONDS);
    }

    //    private void sendHeartBeatMessage() {
    //        try {
    //            HeartbeatRequest request = HeartbeatRequest.newBuilder()
    //
    // .setName(SERVICE_NAME).setIsRunning(true).setTimeStamp(System.currentTimeMillis()).build();
    //            stub.send(request, new StreamObserver<HeartbeatResponse>() {
    //                @Override
    //                public void onNext(HeartbeatResponse response) {
    //                }
    //
    //                @Override
    //                public void onError(Throwable t) {
    //                    logger.error(t.getMessage());
    //                }
    //
    //                @Override
    //                public void onCompleted() {
    //                }
    //            });
    //        } catch (Exception e) {
    //            logger.error(e.getMessage());
    //        }
    //    }

    // @Cached(key = "searchResults:{#query}")
    @Cached
    @Transactional
    public List<Activity> searchByActivityName(String query) {
        List<Activity> result = activityRepository.searchByName(query);
        return result;
    }

    @CachedAsync
    @Transactional
    public CompletableFuture<List<Activity>> searchByNearbyLocation(
            double longitude, double latitude, long radius) {
        List<Activity> activities = getNearbyActivities(longitude, latitude, radius);
        List<Activity> liveActivities = rankLiveActivities(activities);

        List<Activity> top10LiveActivities =
                liveActivities.stream().limit(2).collect(Collectors.toList());

        return CompletableFuture.completedFuture(top10LiveActivities);
    }

    private List<Activity> rankLiveActivities(List<Activity> activities) {
        List<UUID> activityIds =
                activities.stream().map(Activity::getActivityId).collect(Collectors.toList());

        Map<UUID, Integer> activityParticipants = new HashMap<>();
        activityIds.stream()
                .forEach(
                        activityId -> {
                            int liveParticipants =
                                    dynamodbService.getLiveParticipants(activityId.toString());
                            activityParticipants.put(activityId, liveParticipants);
                        });

        activities.sort(
                Comparator.comparing(
                        activity ->
                                activityParticipants.getOrDefault(activity.getActivityId(), 0)));
        Collections.reverse(activities);
        return activities;
    }

    private List<Activity> getNearbyActivities(double longitude, double latitude, long radius) {

        long searchRadius = Optional.ofNullable(radius).filter(r -> r != 0).orElse(DEFAULT_RADIUS);

        List<Location> locations =
                locationRepository.getNearByLocations(
                        searchRadius, locationRepository.DEFAULT_SEARCH_RANGE, longitude, latitude);

        List<Activity> activities = new ArrayList<>();
        for (Location location : locations) {
            Activity activity =
                    activityRepository.findActivityByLocationId(location.getLocationId());
            if (activity != null) {
                activities.add(activity);
            }
        }

        return activities;
    }

    public List<Activity> searchByTags(List<String> tagNames) {
        return activityRepository.findByAllTags(tagNames);
    }

    @Cached
    @Transactional
    public List<Activity> getActivityByTagId(UUID tagId) {
        List<Activity> result = activityRepository.findActivitiesByTagId(tagId);
        return result;
    }
}
