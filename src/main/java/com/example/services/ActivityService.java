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
    public Activity addAttendee(UUID activityId, UserLoginDto userLoginDto) {
        // check if user can join activity
        if (!validateAttendeeForJoin(activityId, userLoginDto)) {
            return null;
        }
        Activity activity = activityRepository.findById(activityId).get();
        User user = userRepository.findByUsername(userLoginDto.getUserName());
        // add user to activity
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activity.addAttendee(user);
        // save to database
        activityRepository.save(activity);
        return activity;
    }

    private boolean validateAttendeeForJoin(UUID activityId, UserLoginDto userLoginDto) {
        User user =
                userRepository.findByUserNameAndHashPasswordWithSalt(
                        userLoginDto.getUserName(), userLoginDto.getHashPasswordWithSalt());
        Optional<Activity> activity = activityRepository.findById(activityId);
        // activity and user must exists
        if (activity.isEmpty() || user == null) {
            return false;
        }
        // activity size is not full
        if (activity.get().getMaximumParticipants() != null
                && activity.get().getCurrentParticipants()
                        >= activity.get().getMaximumParticipants()) {
            return false;
        }
        // user did not join activity already
        if (activity.get().getAttendees().contains(user)) {
            return false;
        }
        return true;
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class},
            isolation = SERIALIZABLE)
    public boolean deleteAttendee(UUID activityId, UserLoginDto userLoginDto) {
        // check if user can quit activity
        if (!validateAttendeeForQuit(activityId, userLoginDto)) {
            return false;
        }
        Activity activity = activityRepository.findById(activityId).get();
        User user = userRepository.findByUsername(userLoginDto.getUserName());
        // remove user from activity
        activity.removeAttendee(user);
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        // save to the database
        activityRepository.save(activity);
        return true;
    }

    private boolean validateAttendeeForQuit(UUID activityId, UserLoginDto userLoginDto) {
        User user =
                userRepository.findByUserNameAndHashPasswordWithSalt(
                        userLoginDto.getUserName(), userLoginDto.getHashPasswordWithSalt());

        Optional<Activity> activity = activityRepository.findById(activityId);

        // activity and User must exists
        if (activity.isEmpty() || user == null) {
            return false;
        }

        // user must joined activity before
        if (!activity.get().getAttendees().contains(user)) {
            return false;
        }
        return true;
    }
}
