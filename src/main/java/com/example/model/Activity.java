package com.example.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/** {@summary This is valid java doc.} */
@Entity
public class Activity {
    @OneToMany private final Set<Tag> tags = new HashSet<>();

    private String name;

    private UUID creatorId; // linked back to client table

    private long startTime;

    private long endTime;

    private UUID locationId;

    private double price;

    private Integer currentParticipants;

    private Integer minimumParticipants;

    private Integer maximumParticipants;

    @OneToMany private final Set<Requirement> requirements = new HashSet<>();

    @OneToMany private final Set<User> attendees = new HashSet<>();

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID activityId;

    public Activity() {}

    /**
     * Instantiates a new Activity.
     *
     * @param builder the builder
     */
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
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(long endTime) {
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

    /** this is a Builder. */
    public static final class Builder {
        /** The Activity id. */
        private UUID activityId;
        /** The Name. */
        private String name;
        /** The Creator id. */
        private UUID creatorId;
        /** The Start time. */
        private long startTime;
        /** The End time. */
        private long endTime;
        /** The Location id. */
        private UUID locationId;
        /** The Price. */
        private double price;
        /** The Current participants. */
        private Integer currentParticipants;
        /** The Minimum participants. */
        private Integer minimumParticipants;
        /** The Maximum participants. */
        private Integer maximumParticipants;
        /** The Tags. */
        private HashSet<Tag> tags;
        /** The Requirements. */
        private HashSet<Requirement> requirements;

        /** Instantiates a new Builder. */
        private Builder() {}

        /**
         * New builder builder.
         *
         * @return the builder
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * With activity id builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withActivityId(UUID val) {
            activityId = val;
            return this;
        }

        /**
         * With name builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withName(String val) {
            name = val;
            return this;
        }

        /**
         * With creator id builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withCreatorId(UUID val) {
            creatorId = val;
            return this;
        }

        /**
         * With start time builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withStartTime(long val) {
            startTime = val;
            return this;
        }

        /**
         * With end time builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withEndTime(long val) {
            endTime = val;
            return this;
        }

        /**
         * With location id builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withLocationId(UUID val) {
            locationId = val;
            return this;
        }

        /**
         * With price builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withPrice(double val) {
            price = val;
            return this;
        }

        /**
         * With current participants builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withCurrentParticipants(int val) {
            currentParticipants = val;
            return this;
        }

        /**
         * With minimum participants builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withMinimumParticipants(int val) {
            minimumParticipants = val;
            return this;
        }

        /**
         * With maximum participants builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withMaximumParticipants(int val) {
            maximumParticipants = val;
            return this;
        }

        /**
         * With tags builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withTags(HashSet<Tag> val) {
            tags = val;
            return this;
        }

        /**
         * With requirements builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder withRequirements(HashSet<Requirement> val) {
            requirements = val;
            return this;
        }

        /**
         * Build activity.
         *
         * @return the activity
         */
        public Activity build() {
            return new Activity(this);
        }
    }
}
