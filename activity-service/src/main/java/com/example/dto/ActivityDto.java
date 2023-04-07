package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.UUID;

/** The type Activity dto. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto {

    private String name;

    private UUID creatorId;

    private Timestamp startTime;

    private Timestamp endTime;

    private UUID locationId;

    private double price;

    private Integer currentParticipants;

    private Integer minimumParticipants;

    private Integer maximumParticipants;

    public ActivityDto() {}

    public ActivityDto(String name, UUID creatorId, Timestamp startTime, Timestamp endTime) {
        this.name = name;
        this.creatorId = creatorId;
        this.startTime = startTime;
        this.endTime = endTime;
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
}
