package com.example.controllers;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        //System.out.println(newProduct.toString());
        return ResponseEntity.ok().body(newUser);
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
