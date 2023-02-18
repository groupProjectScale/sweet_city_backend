package com.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Activity {
    private UUID activityId;
    private String name;
    private UUID creatorId; //linked back to client table
    private long startTime;
    private long endTime;
    private UUID locationId;
    private double price;
    private Integer currentParticipants;
    private Integer minimumParticipants;
    private Integer maximumParticipants;
    private final Map<UUID, User> attendees = new HashMap<>();


    public static final class Builder {
        private UUID activityId;
        private String name;
        private UUID creatorId;
        private long startTime;
        private long endTime;
        private UUID locationId;
        private double price;
        private Integer currentParticipants;
        private Integer minimumParticipants;
        private Integer maximumParticipants;

        private Builder() {}

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withActivityId(UUID val) {
            activityId = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withCreatorId(UUID val) {
            creatorId = val;
            return this;
        }

        public Builder withStartTime(long val) {
            startTime = val;
            return this;
        }

        public Builder withEndTime(long val) {
            endTime = val;
            return this;
        }

        public Builder withLocationId(UUID val) {
            locationId = val;
            return this;
        }

        public Builder withPrice(double val) {
            price = val;
            return this;
        }

        public Builder withCurrentParticipants(int val) {
            currentParticipants = val;
            return this;
        }

        public Builder withMinimumParticipants(int val) {
            minimumParticipants = val;
            return this;
        }

        public Builder withMaximumParticipants(int val) {
            maximumParticipants = val;
            return this;
        }

        public Activity build() {
            return new Activity(this);
        }
    }

    public Activity(Builder builder) {
        setActivityId(builder.activityId);
        setName(builder.name);
        setCreatorId(builder.creatorId);
        setStartTime(builder.startTime);
        setEndTime(builder.endTime);
        setLocationId(builder.locationId);
        setPrice(builder.price);
        setCurrentParticipants(builder.currentParticipants);
        setMaximumParticipants(builder.maximumParticipants);
        setMinimumParticipants(builder.minimumParticipants);
    }

    public void addAttendee(User user) {
        this.attendees.put(user.getUserId(), user);
    }
    public void removeAttendee(User user) {
        this.attendees.remove(user.getUserId());
    }

    public UUID getActivityId() {
        return activityId;
    }

    public void setActivityId(UUID activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCreatorId() { return creatorId;}

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Integer getMinimumParticipants() {
        return minimumParticipants;
    }

    public void setMinimumParticipants(Integer minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }
    public Map<UUID, User> getAttendees() { return attendees; }

}
