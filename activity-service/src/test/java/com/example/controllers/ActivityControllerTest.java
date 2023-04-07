package com.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.BaseTest;
import com.example.dto.ActivityDto;
import com.example.dto.RequirementDto;
import com.example.dto.TagDto;
import com.example.model.Activity;
import com.example.model.Requirement;
import com.example.model.Tag;
import com.example.services.ActivityService;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActivityControllerTest extends BaseTest {
    @MockBean private ActivityService activityService;

    @Autowired private MockMvc mvc;

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    private static ObjectMapper objectMapper = new ObjectMapper();

    // Serialize TimeStamp to the right format(timezone and structure)
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(dateFormat);
    }

    private static final String ACTIVITY_ID = UUID.randomUUID().toString();
    private static final String CREATOR_ID = UUID.randomUUID().toString();
    private static final UUID ACTIVITY_UUID = UUID.fromString(ACTIVITY_ID);
    private static final UUID CREATOR_UUID = UUID.fromString(CREATOR_ID);
    private static LocalDateTime now = LocalDateTime.now();
    private static ZoneId zone = ZoneOffset.UTC;
    private static final Timestamp ACTIVITY_START_TIME =
            Timestamp.from(Instant.from((now.plusDays(1).atZone(zone))));
    private static final Timestamp ACTIVITY_END_TIME =
            Timestamp.from(Instant.from((now.plusDays(2).atZone(zone))));
    private static final String DESCRIPTION = "shopping";
    private static final String NAME = "test";
    private static ActivityDto activityDto;
    private static Activity expectedActivity;
    private static RequirementDto requirementDto;
    private static Requirement expectedRequirement;
    private static TagDto tagDto;
    private static Tag expectedTag;

    @BeforeAll
    static void setUpTestData() {
        activityDto = new ActivityDto(NAME, CREATOR_UUID, ACTIVITY_START_TIME, ACTIVITY_END_TIME);
        expectedActivity = new Activity();
        BeanUtils.copyProperties(activityDto, expectedActivity);

        requirementDto = new RequirementDto(DESCRIPTION);
        expectedRequirement = new Requirement();
        BeanUtils.copyProperties(requirementDto, expectedRequirement);

        tagDto = new TagDto(DESCRIPTION);
        expectedTag = new Tag();
        BeanUtils.copyProperties(tagDto, expectedTag);
    }

    @Test
    void testCreateActivity_ReturnActivity() throws Exception {
        Mockito.when(activityService.addActivity(Mockito.any(ActivityDto.class)))
                .thenReturn(expectedActivity);
        Mockito.when(activityService.addActivity(activityDto)).thenReturn(expectedActivity);

        String requestJson = objectMapper.writeValueAsString(activityDto);

        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.post("/activity/create")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(expectedActivity);
        String actualResponse = result.getResponse().getContentAsString();
        actualResponse = actualResponse.replace("+00:00", "Z");
        System.out.println(actualResponse);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testGetActivityById_ReturnActivity() throws Exception {

        Mockito.when(activityService.getActivityById(ACTIVITY_UUID))
                .thenReturn(Optional.of(expectedActivity));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/activity/get/{activityId}", ACTIVITY_UUID))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(expectedActivity);
        String actualResponse = result.getResponse().getContentAsString();
        actualResponse = actualResponse.replace("+00:00", "Z");
        System.out.println(actualResponse);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testAddTagForActivity_ReturnTrue() throws Exception {

        Mockito.when(
                        activityService.addTagForActivity(
                                Mockito.eq(ACTIVITY_UUID), Mockito.any(TagDto.class)))
                .thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(tagDto);

        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.post(
                                                "/activity/add-tag/{activityId}", ACTIVITY_UUID)
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content(requestJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String expectedResponse = "true";
        String actualResponse = result.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testAddRequirementForActivity_ReturnTrue() throws Exception {

        Mockito.when(
                        activityService.addRequirementForActivity(
                                Mockito.eq(ACTIVITY_UUID), Mockito.any(RequirementDto.class)))
                .thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(requirementDto);

        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.post(
                                                "/activity/add-req/{activityId}", ACTIVITY_UUID)
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content(requestJson))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String expectedResponse = "true";
        String actualResponse = result.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
