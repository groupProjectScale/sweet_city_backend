package com.example.repository;

import com.example.model.Activity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** The interface Activity repository. */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    @Modifying
    @Query(
            "update Activity a set a.currentParticipants = a.currentParticipants + 1 where"
                    + " a.activityId =?1")
    void addOneParticipant(UUID activityId);

    @Modifying
    @Query(
            "update Activity a set a.currentParticipants = a.currentParticipants - 1 where"
                    + " a.activityId =?1")
    void removeOneParticipant(UUID activityId);

    Activity findActivityByLocationId(UUID locationId);
}
