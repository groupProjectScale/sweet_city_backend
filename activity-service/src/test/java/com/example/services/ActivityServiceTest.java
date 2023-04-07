package com.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.BaseTest;
import com.example.dto.ActivityDto;
import com.example.dto.RequirementDto;
import com.example.dto.TagDto;
import com.example.model.Activity;
import com.example.model.Requirement;
import com.example.model.Tag;
import com.example.model.User;
import com.example.repository.ActivityRepository;
import com.example.repository.RequirementRepository;
import com.example.repository.TagRepository;
import com.example.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ActivityServiceTest extends BaseTest {
    @MockBean private ActivityRepository activityRepository;

    @MockBean private TagRepository tagRepository;

    @MockBean private RequirementRepository requirementRepository;

    @MockBean private UserRepository userRepository;

    @Autowired private ActivityService activityService;

    private static final String ACTIVITY_ID = UUID.randomUUID().toString();
    private static final String CREATOR_ID = UUID.randomUUID().toString();
    private static final UUID ACTIVITY_UUID = UUID.fromString(ACTIVITY_ID);
    private static final UUID NON_EXISTING_ACTIVITY_ID = UUID.fromString(CREATOR_ID);
    private static final UUID CREATOR_UUID = UUID.fromString(CREATOR_ID);
    private static LocalDateTime now = LocalDateTime.now();
    private static final Timestamp ACTIVITY_START_TIME = Timestamp.valueOf(now.plusDays(1));
    private static final Timestamp ACTIVITY_END_TIME = Timestamp.valueOf(now.plusDays(2));
    private static final String DESCRIPTION = "shopping";
    private static final String NAME = "test";
    private static ActivityDto validActivityDto;
    private static ActivityDto invalidActivityDto;
    private static Activity expectedActivity;
    private static User expectedCreator;
    private static RequirementDto requirementDto;
    private static Requirement expectedRequirement;
    private static TagDto tagDto;
    private static Tag expectedTag;

    @BeforeAll
    static void setUpTestData() {
        validActivityDto =
                new ActivityDto(NAME, CREATOR_UUID, ACTIVITY_START_TIME, ACTIVITY_END_TIME);
        invalidActivityDto =
                new ActivityDto(NAME, ACTIVITY_UUID, ACTIVITY_START_TIME, ACTIVITY_END_TIME);
        expectedActivity =
                new Activity(
                        ACTIVITY_UUID, NAME, CREATOR_UUID, ACTIVITY_START_TIME, ACTIVITY_END_TIME);

        requirementDto = new RequirementDto(DESCRIPTION);
        expectedRequirement = new Requirement(CREATOR_UUID, DESCRIPTION);

        tagDto = new TagDto(DESCRIPTION);
        expectedTag = new Tag(CREATOR_UUID, DESCRIPTION, 1);

        expectedCreator =
                new User(CREATOR_UUID, "test", "test", "test", "test@gmail.com", "password");
    }

    @Test
    public void testAddActivity_withInvalidActivityDto_shouldReturnNull() {
        when(userRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.empty());
        when(activityRepository.save(any(Activity.class))).thenReturn(expectedActivity);

        Activity actualActivity = activityService.addActivity(invalidActivityDto);
        assertThat(actualActivity).isNull();

        verify(activityRepository, times(0)).save(any(Activity.class));
    }

    @Test
    public void testAddActivity_withValidActivityDto_shouldReturnActivity() {
        when(userRepository.findById(CREATOR_UUID)).thenReturn(Optional.of(expectedCreator));
        when(activityRepository.save(any(Activity.class))).thenReturn(expectedActivity);

        Activity actualActivity = activityService.addActivity(validActivityDto);
        assertThat(actualActivity).isNotNull();
        assertThat(actualActivity).isEqualTo(expectedActivity);

        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    public void testGetActivityById_withExistingActivityId_shouldReturnActivity() {
        when(activityRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.of(expectedActivity));
        Optional<Activity> actualActivity = activityService.getActivityById(ACTIVITY_UUID);

        assertThat(actualActivity).isNotNull();
        assertThat(actualActivity.get()).usingRecursiveComparison().isEqualTo(expectedActivity);

        verify(activityRepository, times(1)).findById(ACTIVITY_UUID);
    }

    @Test
    public void testGetActivityById_withNonExistingActivityId_shouldReturnNull() {
        when(activityRepository.findById(NON_EXISTING_ACTIVITY_ID)).thenReturn(Optional.empty());

        Optional<Activity> actualActivity =
                activityService.getActivityById(NON_EXISTING_ACTIVITY_ID);
        assertThat(actualActivity).isEmpty();

        verify(activityRepository, times(1)).findById(NON_EXISTING_ACTIVITY_ID);
    }

    @Test
    public void testAddTagForActivity_activityNotExist_shouldReturnFalse() {
        when(activityRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.empty());

        boolean result = activityService.addTagForActivity(ACTIVITY_UUID, tagDto);

        assertThat(result).isFalse();
        verify(activityRepository, times(1)).findById(ACTIVITY_UUID);
        verify(activityRepository, times(0)).save(any(Activity.class));
        verify(tagRepository, times(0)).findByTagDescription(tagDto.getTagDescription());
        verify(tagRepository, times(0)).save(any(Tag.class));
    }

    @Test
    public void testAddTagForActivity_shouldReturnTrue() {
        when(activityRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.of(expectedActivity));
        when(tagRepository.findByTagDescription(tagDto.getTagDescription())).thenReturn(null);
        when(tagRepository.save(any(Tag.class))).thenReturn(expectedTag);

        boolean result = activityService.addTagForActivity(ACTIVITY_UUID, tagDto);

        assertThat(result).isTrue();
        assertThat(expectedActivity.getTags()).hasSize(1);
        verify(activityRepository, times(1)).findById(ACTIVITY_UUID);
        verify(activityRepository, times(1)).save(expectedActivity);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    public void testAddRequirementForActivity_activityNotExist_shouldReturnFalse() {
        when(activityRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.empty());
        boolean result = activityService.addRequirementForActivity(ACTIVITY_UUID, requirementDto);

        assertThat(result).isFalse();
        verify(activityRepository, times(0)).save(any(Activity.class));
    }

    @Test
    public void testAddRequirementForActivity_shouldReturnTrue() {

        when(activityRepository.findById(ACTIVITY_UUID)).thenReturn(Optional.of(expectedActivity));
        when(requirementRepository.findByDescription(requirementDto.getDescription()))
                .thenReturn(null);
        when(requirementRepository.save(any(Requirement.class))).thenReturn(expectedRequirement);

        boolean result = activityService.addRequirementForActivity(ACTIVITY_UUID, requirementDto);

        // Assert
        assertThat(result).isTrue();
        assertThat(expectedActivity.getRequirements()).hasSize(1);
        verify(activityRepository, times(1)).save(any(Activity.class));
    }
}
