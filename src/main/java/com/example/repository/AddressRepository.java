package com.example.repository;

import com.example.model.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query(value = "SELECT * FROM address WHERE user_id = ?1", nativeQuery = true)
    Address getAddressByUserId(UUID userId);
}
