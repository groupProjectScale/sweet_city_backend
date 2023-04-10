package com.example.repository;

import com.example.model.Activity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Activity findActivityByLocationId(UUID locationId);

    @Query(
            "SELECT a FROM Activity a LEFT JOIN FETCH a.attendees LEFT JOIN FETCH a.tags LEFT JOIN"
                    + " FETCH a.requirements WHERE a.locationId = :locationId")
    Activity findActivityByLocationId(@Param("locationId") UUID locationId);

    @Query(
            "SELECT DISTINCT a FROM Activity a LEFT JOIN FETCH a.attendees "
                    + "LEFT JOIN FETCH a.tags LEFT JOIN"
                    + " FETCH a.requirements WHERE LOWER(a.name) LIKE %:query%")
    List<Activity> searchByName(@Param("query") String query);

    @Query(
            "SELECT a FROM Activity a JOIN a.tags t WHERE t.tagDescription IN ?1 GROUP BY a HAVING"
                    + " COUNT(DISTINCT t) = ?2")
    List<Activity> findByAllTagsWithCount(List<String> tags, long count);

    default List<Activity> findByAllTags(List<String> tags) {
        return findByAllTagsWithCount(tags, tags.size());
    }

    @Query(
            "SELECT DISTINCT a FROM Activity a LEFT JOIN FETCH a.attendees "
                    + "LEFT JOIN FETCH a.tags t LEFT JOIN"
                    + " FETCH a.requirements WHERE t.tagId = ?1")
    List<Activity> findActivitiesByTagId(UUID tagId);
}
