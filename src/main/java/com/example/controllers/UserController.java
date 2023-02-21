package com.example.controllers;

import com.example.dto.AddressDto;
import com.example.dto.UserDto;
import com.example.model.User;
import com.example.services.UserService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** The type User controller. */
@RestController
public class UserController {
    /** The User service. */
    private UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add user response entity.
     *
     * @param userTask the requestBody.
     * @return the response entity
     */
    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody UserTask userTask) {
        if (validateUserDto(userTask.userDto) == false) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = toUser(userTask.userDto);
        User newUser = userService.addUser(user);

        return ResponseEntity.ok().body(newUser);
    }

    /**
     * Gets user by user id.
     *
     * @param userId the user id
     * @return the user by user id
     */
    @GetMapping("/get-user/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable("userId") UUID userId) {
        User getUser = userService.getUserByUserId(userId);
        return ResponseEntity.ok().body(getUser);
    }

    /**
     * Validate user dto boolean.
     *
     * @param userDto the user dto
     * @return the boolean
     */
    private boolean validateUserDto(UserDto userDto) {
        if (userDto == null || userDto.getUserName() == null) {
            return false;
        }
        if (userDto.getFirstName() == null || userDto.getFirstName().length() <= 5) {
            return false;
        }
        return true;
    }

    /**
     * To user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    private User toUser(UserDto userDto) {
        return User.Builder.newBuilder()
                .withUserName(userDto.getUserName())
                .withFirstName(userDto.getFirstName())
                .withLastName(userDto.getLastName())
                .withEmail(userDto.getEmail())
                .withHashPasswordWithSalt(userDto.getPassword())
                .build();
    }

    /** The type User task. */
    public class UserTask {
        /** The User dto. */
        public UserDto userDto;
        /** The Address dto. */
        public AddressDto addressDto;
    }
}
