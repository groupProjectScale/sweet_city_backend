package com.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.ActivityServiceApplication;
import com.example.BaseTest;
import com.example.dto.AddressDto;
import com.example.dto.UserDto;
import com.example.dto.UserTaskDto;
import com.example.model.Address;
import com.example.model.User;
import com.example.services.UserService;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ActivityServiceApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {
    @MockBean private UserService userService;
    @Captor private ArgumentCaptor<User> userCaptor;
    @Captor private ArgumentCaptor<Address> addressCaptor;
    @Autowired private MockMvc mvc;

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final UUID USER_ID = UUID.fromString("f7c72c1d-5c5e-4ed9-902e-541d8c1621d2");
    private static final String USER_NAME = "bobbyisthebest";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "bobby";
    private static final String LAST_NAME = "isbest";
    private static final String EMAIL = "bd@gmail.com";
    private static final String LOCATION = "home";
    private static final double LATITUDE = 47.608013;
    private static final double LONGITUDE = -121.335167;

    private static UserDto userDto;
    private static AddressDto addressDto;
    private static UserTaskDto userTaskDto;
    private static User expectedUser;
    private static Address expectedAddress;

    @BeforeAll
    static void setUpTestData() {
        userDto = new UserDto(USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
        addressDto = new AddressDto(USER_ID, LOCATION, LATITUDE, LONGITUDE);
        userTaskDto = new UserTaskDto();
        userTaskDto.setUserDto(userDto);
        userTaskDto.setAddressDto(addressDto);

        expectedUser = new User();
        BeanUtils.copyProperties(userDto, expectedUser);
        expectedAddress = new Address();
        BeanUtils.copyProperties(addressDto, expectedAddress);
    }

    @Test
    void testAddUser_returnNewUser() throws Exception {
        // Setup mock service
        Mockito.when(
                        userService.addUserWithAddress(
                                Mockito.any(User.class), Mockito.any(Address.class)))
                .thenReturn(expectedUser);

        String json = objectMapper.writeValueAsString(userTaskDto);

        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.post("/add-user")
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        // Verify the response
        String expectedResponse = objectMapper.writeValueAsString(expectedUser);
        String actualResponse = result.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualTo(expectedResponse);

        // Verify the service method calls
        verify(userService).addUserWithAddress(userCaptor.capture(), addressCaptor.capture());
        verify(userService, times(1))
                .addUserWithAddress(Mockito.any(User.class), Mockito.any(Address.class));

        User capturedUser = userCaptor.getValue();
        Address capturedAddress = addressCaptor.getValue();
        assertThat(capturedUser)
                .usingRecursiveComparison()
                .ignoringFields("userId")
                .isEqualTo(expectedUser);
        assertThat(capturedAddress).usingRecursiveComparison().isEqualTo(expectedAddress);
    }

    @Test
    void testGetUserByUserId_returnUser() throws Exception {
        expectedUser.setUserId(USER_ID);
        Mockito.when(userService.getUserByUserId(USER_ID)).thenReturn(expectedUser);

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/get-user/{userId}", USER_ID))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        // Verify the response
        String expectedResponse = objectMapper.writeValueAsString(expectedUser);
        String actualResponse = result.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testGetUserByUserId_NotFound() throws Exception {
        Mockito.when(userService.getUserByUserId(USER_ID)).thenReturn(null);

        MvcResult result =
                mvc.perform(
                                MockMvcRequestBuilders.get("/get-user/" + USER_ID)
                                        .contentType(APPLICATION_JSON_UTF8))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertThat(actualResponse).isEmpty();
    }
}
