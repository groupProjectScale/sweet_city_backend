package com.example.services;

import com.example.model.Address;
import com.example.model.Location;
import com.example.model.User;
import com.example.repository.AddressRepository;
import com.example.repository.LocationRepository;
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
    private final LocationRepository locationRepository;

    public UserService(
            UserRepository userRepository,
            AddressRepository addressRepository,
            LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
    }

    public User addUserWithAddress(User user, Address address)
            throws ExecutionException, InterruptedException {
        CompletableFuture<User> userFuture =
                CompletableFuture.supplyAsync(() -> userRepository.save(user));

        address.setUserId(userFuture.get().getUserId());
        addressRepository.save(address);

        locationRepository.save(
                new Location(address.getLocation(), address.getLongitude(), address.getLatitude()));

        return getUserByUserId(userFuture.get().getUserId());
    }

    public User getUserByUserId(UUID userId) {
        Optional user = userRepository.findById(userId);
        if (user.isPresent()) {
            return (User) user.get();
        }
        return null;
    }
}
