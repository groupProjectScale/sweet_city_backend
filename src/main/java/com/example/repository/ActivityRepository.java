package com.example.repository;

import com.example.model.Activity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Activity repository. */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {}
