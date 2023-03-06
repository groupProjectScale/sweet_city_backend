package com.example.services;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

import com.example.dto.ActivityDto;
import com.example.dto.UserLoginDto;
import com.example.model.Activity;
import com.example.model.Address;
import com.example.model.Location;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.AddressRepository;
import com.example.repository.LocationRepository;
import com.example.repository.RequirementRepository;
import com.example.repository.TagRepository;
import com.example.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final TagRepository tagRepository;
    private static final int RANKING = 5;
    private final RequirementRepository requirementRepository;

    public ActivityService(
        ActivityRepository activityRepository,
        UserRepository userRepository,
        AddressRepository addressRepository,
        LocationRepository locationRepository,
        TagRepository tagRepository, RequirementRepository requirementRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
        this.tagRepository = tagRepository;
        this.requirementRepository = requirementRepository;
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
        // TODO
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

}
