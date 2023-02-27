package com.example.repository;

import com.example.model.Requirement;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Activity repository. */
@Repository
public interface RequirementRepository extends JpaRepository<Requirement, UUID> {}
