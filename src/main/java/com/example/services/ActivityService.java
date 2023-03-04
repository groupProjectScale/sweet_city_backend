package com.example.services;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

import com.example.dto.ActivityDto;
import com.example.dto.RequirementDto;
import com.example.dto.TagDto;
import com.example.dto.UserLoginDto;
import com.example.model.Activity;
import com.example.model.Address;
import com.example.model.Requirement;
import com.example.model.Tag;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.AddressRepository;
import com.example.repository.RequirementRepository;
import com.example.repository.TagRepository;
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
    private final TagRepository tagRepository;
    private final RequirementRepository requirementRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            UserRepository userRepository,
            AddressRepository addressRepository,
            TagRepository tagRepository,
            RequirementRepository requirementRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
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
}
