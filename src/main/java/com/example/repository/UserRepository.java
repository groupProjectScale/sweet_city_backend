package com.example.repository;

import com.example.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** The interface User repository. */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM client WHERE user_name = ?1", nativeQuery = true)
    User findByUsername(String userName);

    @Query(
            value = "SELECT * FROM client WHERE user_name = ?1 AND hash_password_with_salt = ?2",
            nativeQuery = true)
    User findByUserNameAndHashPasswordWithSalt(String userName, String hashPasswordWithSalt);
}
