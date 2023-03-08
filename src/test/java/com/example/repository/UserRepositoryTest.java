package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.BaseTest;
import com.example.model.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends BaseTest {

    @Autowired private UserRepository userRepository;
    private static final UUID USER_UUID = UUID.fromString("f7c72c1d-5c5e-4ed9-902e-541d8c1621d2");
    private static final String PASSWORD = "passwokld";
    private User expectedUser;

    @BeforeEach
    public void setup() {
        String username = UUID.randomUUID().toString();
        expectedUser = new User(USER_UUID, username, "bobby1", "charles", "bd@gmail.com", PASSWORD);
    }

    @Test
    void testFindByUsername_returnUser() {
        userRepository.save(expectedUser);

        User result = userRepository.findByUsername(expectedUser.getUserName());

        assertThat(result).isNotNull();
        assertThat(result.getUserName()).isEqualTo(expectedUser.getUserName());
    }

    @Test
    void testFindByUserNameAndHashPasswordWithSalt_returnUser() {
        userRepository.save(expectedUser);
        User result =
                userRepository.findByUserNameAndHashPasswordWithSalt(
                        expectedUser.getUserName(), PASSWORD);

        assertThat(result).isNotNull();
        assertThat(result.getUserName()).isEqualTo(expectedUser.getUserName());
        assertThat(result.getHashPasswordWithSalt()).isEqualTo(PASSWORD);
    }

    @Test
    void testSave_returnSavedUser() {
        User result = userRepository.save(expectedUser);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getUserName()).isEqualTo(expectedUser.getUserName());
    }
}
