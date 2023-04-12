package com.example.services;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

import com.example.dto.ActivityDto;
import com.example.dto.RequirementDto;
import com.example.dto.TagDto;
import com.example.dto.UserLoginDto;
import com.example.model.Activity;
import com.example.model.Address;
import com.example.model.Location;
import com.example.model.Requirement;
import com.example.model.Tag;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.AddressRepository;
import com.example.repository.LocationRepository;
import com.example.repository.RequirementRepository;
import com.example.repository.TagRepository;
import com.example.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proto.HeartbeatRequest;
import proto.HeartbeatResponse;
import proto.MonitoringServiceGrpc;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TagRepository tagRepository;
    private final RequirementRepository requirementRepository;
    private final LocationRepository locationRepository;
    private final String SERVICE_NAME = "activity";
    private static final int RANKING = 5;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static final Logger logger = LogManager.getLogger(ActivityService.class);

    @GrpcClient("sweetcity")
    MonitoringServiceGrpc.MonitoringServiceStub stub;

    public ActivityService(
            ActivityRepository activityRepository,
            UserRepository userRepository,
            AddressRepository addressRepository,
            LocationRepository locationRepository,
            TagRepository tagRepository,
            RequirementRepository requirementRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
        this.tagRepository = tagRepository;
        this.requirementRepository = requirementRepository;
        sendHeartBeat();
    }

    private void sendHeartBeat() {
        scheduler.scheduleAtFixedRate(this::sendHeartBeatMessage, 10, 10, TimeUnit.SECONDS);
    }

    private void sendHeartBeatMessage() {
        try {
            HeartbeatRequest request =
                    HeartbeatRequest.newBuilder()
                            .setName(SERVICE_NAME)
                            .setIsRunning(true)
                            .setTimeStamp(System.currentTimeMillis())
                            .build();
            stub.send(
                    request,
                    new StreamObserver<HeartbeatResponse>() {
                        @Override
                        public void onNext(HeartbeatResponse response) {}

                        @Override
                        public void onError(Throwable t) {
                            logger.error(t.getMessage());
                        }

                        @Override
                        public void onCompleted() {}
                    });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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

    public List<Activity> getActivityRanking(String userName, Optional<Integer> top) {
        User user = getUserByUsername(userName);
        Address address = addressRepository.getAddressByUserId(user.getUserId());

        List<Location> locations =
                locationRepository.getNearByLocations(
                        null,
                        LocationRepository.DEFAULT_SEARCH_RANGE,
                        address.getLongitude(),
                        address.getLatitude());

        List<Activity> activities = new ArrayList<>();
        for (Location location : locations) {
            Activity activity =
                    activityRepository.findActivityByLocationId(location.getLocationId());
            if (activity != null) {
                activities.add(activity);
            }
        }
        activities.sort(Comparator.comparingInt(Activity::getCurrentParticipants).reversed());
        if (top.isPresent()) {
            return activities.subList(0, Math.min(top.get(), activities.size()));
        }
        return activities.subList(0, Math.min(RANKING, activities.size()));
    }

    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    private boolean validateActivity(ActivityDto activityDto) {
        String name = activityDto.getName();
        UUID userId = activityDto.getCreatorId();
        Timestamp startTime = activityDto.getStartTime();
        Timestamp endTime = activityDto.getEndTime();
        // 1. activityDto must contain: activityName, creator, startTime, endTime
        if (name == null || userId == null || startTime == null || endTime == null) {
            return false;
        }
        // 1. check creator: must be user
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) {
            return false;
        }
        // 2. check time: activity endTime > startTime && startTime > now
        if (startTime.after(endTime)
                || startTime.before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        // 3. check activityName
        if (name.length() <= 2) {
            return false;
        }
        return true;
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class},
            isolation = SERIALIZABLE)
    public Boolean addAttendee(UUID activityId, UserLoginDto userLoginDto) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        User user = userRepository.findByUsername(userLoginDto.getUserName());
        // activity or user do not exists
        if (activity.isEmpty() || user == null) {
            return false;
        }
        Integer currParticipants = activity.get().getCurrentParticipants();
        Integer maxParticipants = activity.get().getMaximumParticipants();
        Set<User> attendees = activity.get().getAttendees();

        // activity size is full
        if (maxParticipants != null && currParticipants >= maxParticipants) {
            return false;
        }
        // user joined activity before
        if (attendees.contains(user)) {
            return false;
        }
        // add user to activity
        activity.get().addAttendee(user);
        activityRepository.save(activity.get());
        activityRepository.addOneParticipant(activityId);
        return true;
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class},
            isolation = SERIALIZABLE)
    public boolean deleteAttendee(UUID activityId, UserLoginDto userLoginDto) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        User user = userRepository.findByUsername(userLoginDto.getUserName());
        // activity and User must exists
        if (activity.isEmpty() || user == null) {
            return false;
        }
        Set<User> attendees = activity.get().getAttendees();
        // user must joined activity before
        if (!attendees.contains(user)) {
            return false;
        }
        // remove user from activity
        activity.get().removeAttendee(user);
        activityRepository.save(activity.get());
        activityRepository.removeOneParticipant(activityId);
        return true;
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class},
            isolation = SERIALIZABLE)
    public boolean addTagForActivity(UUID activityId, TagDto tagDto) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) {
            return false;
        }
        Tag tag = tagRepository.findByTagDescription(tagDto.getTagDescription());
        // do not exist this tag before, store tagDescription
        if (tag == null) {
            tag = addTag(tagDto);
        }
        // tag already exists in this activity
        if (activity.get().getTags().contains(tag)) {
            return false;
        }
        activity.get().addTag(tag);
        activityRepository.save(activity.get());
        tagRepository.addOneCreation(tag.getTagId());
        return true;
    }

    @Transactional(isolation = SERIALIZABLE)
    Tag addTag(TagDto tagDto) {
        Tag tag = tagRepository.findByTagDescription(tagDto.getTagDescription());
        if (tag != null) {
            return tag;
        }
        Tag t = new Tag();
        BeanUtils.copyProperties(tagDto, t);
        return tagRepository.save(t);
    }

    @Transactional
    public boolean addRequirementForActivity(UUID activityId, RequirementDto requirementDto) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) {
            return false;
        }
        Requirement r = requirementRepository.findByDescription(requirementDto.getDescription());
        // do not exist this requirement before, store requirement
        if (r == null) {
            r = addRequirement(requirementDto);
        }
        // requirement already exists in this activity
        if (activity.get().getRequirements().contains(r)) {
            return false;
        }
        activity.get().addRequirements(r);
        activityRepository.save(activity.get());
        return true;
    }

    @Transactional(isolation = SERIALIZABLE)
    Requirement addRequirement(RequirementDto requirementDto) {
        Requirement r = new Requirement();
        Requirement req = requirementRepository.findByDescription(requirementDto.getDescription());
        if (req != null) {
            return req;
        }
        BeanUtils.copyProperties(requirementDto, r);
        return requirementRepository.save(r);
    }

    public boolean validateTagId(String tagId) {
        return tagRepository.findById(UUID.fromString(tagId)).isPresent();
    }

    public int getNumberOfCreationsForTag(String tagId) {
        return tagRepository.getNumberOfCreationsForTag(tagId);
    }

    public boolean isActivityExist(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) {
            return false;
        }
        return true;
    }
}
