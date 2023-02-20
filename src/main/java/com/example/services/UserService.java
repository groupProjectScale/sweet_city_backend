package com.example.services;

import com.example.model.User;
import com.example.repository.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public User getUserByUserId(UUID userId) {
        return userRepository.getUserByUserId(userId);
    }
}
