package com.example.services;

import com.example.dto.ActivityDto;
import com.example.model.Activity;
import com.example.model.Address;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            UserRepository userRepository,
            AddressRepository addressRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
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

    public int getCurrentParticipant(UUID activityId) {
        Optional<Activity> activity = getActivityById(activityId);
        if (activity.isPresent() && !activity.isEmpty()) {
            return activity.get().getCurrentParticipants();
        }
        return -1;
    }

    public List<Activity> getActivityRanking(String userName) {
        // To do
        User user = getUserByUsername(userName);
        Address address = addressRepository.getAddressByUserId(user.getUserId());
        /*
        search in location table, based on address, get list of all activities_locations
        within 50 miles of address, then query activity table, get list of all activities
        sort by current_participants.
         */

        return null;
    }

    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    private boolean validateActivity(ActivityDto activityDto) {
        // TO DO
        return true;
    }
}
