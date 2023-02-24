package com.example.services;

import com.example.model.Address;
import com.example.model.User;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public User addUserWithAddress(User user, Address address)
            throws ExecutionException, InterruptedException {
        CompletableFuture<User> completableFuture =
                CompletableFuture.supplyAsync(() -> userRepository.save(user));
        CompletableFuture<Address> future =
                completableFuture.thenApply(
                        (newUser) -> {
                            address.setUserId(newUser.getUserId());
                            return addressRepository.save(address);
                        });
        Address address1 = future.get();
        return getUserByUserId(address1.getUserId());
    }

    public User getUserByUserId(UUID userId) {
        Optional user = userRepository.findById(userId);
        if (user.isPresent()) {
            return (User) user.get();
        }
        return null;
    }
}
