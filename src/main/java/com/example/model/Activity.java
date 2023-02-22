package com.example.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/** {@summary This is valid java doc.} */
@Entity
@Table(name = "activity")
public class Activity {

    @OneToMany private final Set<Tag> tags = new HashSet<>();

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "activity_id", updatable = false, nullable = false)
    private UUID activityId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private UUID creatorId; // linked back to client table

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "location_id", nullable = true)
    private UUID locationId;

    @Column(name = "price", nullable = true)
    private double price;

    @Column(name = "current_participants", nullable = true)
    private Integer currentParticipants;

    @Column(name = "minimum_participants", nullable = true)
    private Integer minimumParticipants;

    @Column(name = "maximum_participants", nullable = true)
    private Integer maximumParticipants;

    @OneToMany private final Set<Requirement> requirements = new HashSet<>();

    @OneToMany private final Set<User> attendees = new HashSet<>();

    public Activity() {}

    /**
     * Add attendee.
     *
     * @param user the user
     */
    public void addAttendee(User user) {
        this.attendees.add(user);
    }

    /**
     * Gets attendees.
     *
     * @return the attendees
     */
    public Set<User> getAttendees() {
        return attendees;
    }

    /**
     * Remove attendee.
     *
     * @param user the user
     */
    public void removeAttendee(User user) {
        this.attendees.remove(user.getUserId());
    }

    /**
     * Gets activity id.
     *
     * @return the activity id
     */
    public UUID getActivityId() {
        return activityId;
    }

    /**
     * Sets activity id.
     *
     * @param activityId the activity id
     */
    public void setActivityId(UUID activityId) {
        this.activityId = activityId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets creator id.
     *
     * @return the creator id
     */
    public UUID getCreatorId() {
        return creatorId;
    }

    /**
     * Sets creator id.
     *
     * @param creatorId the creator id
     */
    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets location id.
     *
     * @return the location id
     */
    public UUID getLocationId() {
        return locationId;
    }

    /**
     * Sets location id.
     *
     * @param locationId the location id
     */
    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets current participants.
     *
     * @return the current participants
     */
    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    /**
     * Sets current participants.
     *
     * @param currentParticipants the current participants
     */
    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    /**
     * Gets minimum participants.
     *
     * @return the minimum participants
     */
    public Integer getMinimumParticipants() {
        return minimumParticipants;
    }

    /**
     * Sets minimum participants.
     *
     * @param minimumParticipants the minimum participants
     */
    public void setMinimumParticipants(Integer minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    /**
     * Gets maximum participants.
     *
     * @return the maximum participants
     */
    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    /**
     * Sets maximum participants.
     *
     * @param maximumParticipants the maximum participants
     */
    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Gets requirements.
     *
     * @return the requirements
     */
    public Set<Requirement> getRequirements() {
        return requirements;
    }
}
