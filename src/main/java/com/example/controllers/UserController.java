package com.example.controllers;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        if (validateUserDto(userDto) == false) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = ToUser(userDto);
        User newUser = userService.addUser(user);
        return ResponseEntity.ok().body(newUser);
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable("userId") UUID userId) {
        User getUser = userService.getUserByUserId(userId);
        return ResponseEntity.ok().body(getUser);
    }




    private boolean validateUserDto(UserDto userDto) {
        if (userDto == null || userDto.getUserName() == null) {
            return false;
        }
        if (userDto.getFirstName() == null || userDto.getFirstName().length() <= 5) {
            return false;
        }
        return true;
    }

    private User ToUser(UserDto userDto) {
        return User.Builder.newBuilder()
                .withUserName(userDto.getUserName())
                .withFirstName(userDto.getFirstName())
                .withLastName(userDto.getLastName())
                .withEmail(userDto.getEmail())
                .withHashPasswordWithSalt(userDto.getPassword())
                .build();
    }
}
