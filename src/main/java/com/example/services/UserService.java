package com.example.services;

import com.example.model.User;
import com.example.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** The type User service. */
@Service
public class UserService {
    /** The User repository. */
    private final UserRepository userRepository;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add user user.
     *
     * @param user the user
     * @return the user
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Gets user by user id.
     *
     * @param userId the user id
     * @return the user by user id
     */
    public User getUserByUserId(UUID userId) {
        Optional user = userRepository.findById(userId);
        if (user.isPresent()) {
            return (User) user.get();
        }
        return null;
    }
}
