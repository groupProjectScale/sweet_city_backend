package com.example.repository;

import com.example.model.User;
import com.example.repository.mappers.UserMapper;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private Jdbi jdbi;
    private UserMapper userMapper;
    public UserRepository(Jdbi jdbi, UserMapper userMapper) {
        this.jdbi = jdbi;
        this.userMapper = userMapper;
    }

    private final String INSERT_CLIENT_SQL =
            "INSERT INTO CLIENT (username, firstname, lastname, email, password) " +
                    "VALUES (:username, :firstname, :lastname, :email, :password);";


    public User addUser(User user) {
        try (Handle handle = jdbi.open()) {
            return handle.createUpdate(INSERT_CLIENT_SQL)
                    .bind("username", user.getUserName())
                    .bind("firstname", user.getFirstName())
                    .bind("lastname", user.getLastName())
                    .bind("email", user.getEmail())
                    .bind("password", user.getHashPasswordWithSalt())
                    .executeAndReturnGeneratedKeys()
                    .map(new UserMapper())
                    .one();
            // return true;
        } catch (Exception e) {
            // log
            System.out.println(e);
            return null;
        }
    }
}
