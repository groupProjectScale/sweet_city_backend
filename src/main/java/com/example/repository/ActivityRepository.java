package com.example.repository;

import com.example.repository.mappers.ActivityMapper;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityRepository {
    private Jdbi jdbi;
    private ActivityMapper activityMapper;

    public ActivityRepository(Jdbi jdbi, ActivityMapper activityMapper) {
        this.jdbi = jdbi;
        this.activityMapper = activityMapper;
    }
}
