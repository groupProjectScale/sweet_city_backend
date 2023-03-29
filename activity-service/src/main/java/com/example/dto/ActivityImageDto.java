package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

/** The type ActivityImage dto. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityImageDto {
    private UUID activityId;
    private UUID imageId;

    public ActivityImageDto() {
    }

    public ActivityImageDto(UUID activityId, UUID imageId) {
        this.activityId = activityId;
        this.imageId = imageId;
    }

    public UUID getActivityId() {
        return activityId;
    }

    public void setActivityId(UUID activityId) {
        this.activityId = activityId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
