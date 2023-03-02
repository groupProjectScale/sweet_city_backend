package com.example.services;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

import com.example.dto.ActivityDto;
import com.example.dto.UserLoginDto;
import com.example.model.Activity;
import com.example.model.Address;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;
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
