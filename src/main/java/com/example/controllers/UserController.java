package com.example.controllers;

import com.example.dto.AddressDto;
import com.example.dto.UserDto;
import com.example.model.Address;
import com.example.model.User;
import com.example.services.UserService;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<User> addUser(@RequestBody UserTask userTask)
            throws ExecutionException, InterruptedException {
        if (validateUserDto(userTask.userDto) == false) {
            return ResponseEntity.badRequest().body(null);
        }

        if (validateAddressDto(userTask.addressDto) == false) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = new User();
        BeanUtils.copyProperties(userTask.userDto, user);
        Address address = new Address();
        BeanUtils.copyProperties(userTask.addressDto, address);

        User newUser = userService.addUserWithAddress(user, address);
        return ResponseEntity.ok().body(newUser);
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable("userId") UUID userId) {
        User getUser = userService.getUserByUserId(userId);
        return ResponseEntity.ok().body(getUser);
    }

    private boolean validateAddressDto(AddressDto addressDto) {
        // To do
        return true;
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

    public static class UserTask {
        public UserDto userDto;
        public AddressDto addressDto;
    }
}
