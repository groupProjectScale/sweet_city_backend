package com.example.repository;

import com.example.model.Requirement;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** The interface Activity repository. */
@Repository
public interface RequirementRepository extends JpaRepository<Requirement, UUID> {
    @Query(value = "SELECT * FROM requirement WHERE description = ?1", nativeQuery = true)
    Requirement findByDescription(String description);
}
