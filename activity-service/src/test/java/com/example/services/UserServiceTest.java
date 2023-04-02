package com.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.BaseTest;
import com.example.model.Address;
import com.example.model.Location;
import com.example.model.User;
import com.example.repository.AddressRepository;
import com.example.repository.LocationRepository;
import com.example.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class UserServiceTest extends BaseTest {
    private static final String USER_ID = "f7c72c1d-5c5e-4ed9-902e-541d8c1621d2";
    private static final UUID USER_UUID = UUID.fromString(USER_ID);
    private static final User EXPECTED_USER =
            new User(
                    USER_UUID,
                    "hellobobbychabby",
                    "bobby1",
                    "charles",
                    "bd@gmail.com",
                    "passwokld");
    private static final Address EXPECTED_ADDRESS =
            new Address(USER_UUID, "home", 46.608013, -122.335167);

    @MockBean private UserRepository userRepository;

    @MockBean private AddressRepository addressRepository;

    @MockBean private LocationRepository locationRepository;

    @Autowired private UserService userService;

    @Test
    public void testAddUserWithAddress_returnUser() {

        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER);

        User actualUser = userService.addUserWithAddress(EXPECTED_USER, EXPECTED_ADDRESS);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(EXPECTED_USER);

        verify(userRepository, times(1)).save(EXPECTED_USER);
        verify(addressRepository, times(1)).save(EXPECTED_ADDRESS);
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void testGetUserByUserId_returnUser() {
        when(userRepository.findById(USER_UUID)).thenReturn(Optional.of(EXPECTED_USER));
        User actualUser = userService.getUserByUserId(USER_UUID);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(EXPECTED_USER);

        verify(userRepository, times(1)).findById(USER_UUID);
    }

    @Test
    public void testGetUserByUserId_returnNull() {

        User actualUser = userService.getUserByUserId(null);
        assertThat(actualUser).isNull();
        verify(userRepository, times(1)).findById(null);
    }
}
