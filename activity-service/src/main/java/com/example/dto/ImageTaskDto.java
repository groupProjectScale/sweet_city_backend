package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

/** The type ActivityImage dto. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageTaskDto {
    private UUID activityId;
    private UUID imageId;
    private String url;

    public ImageTaskDto() {}

    public ImageTaskDto(UUID activityId, UUID imageId, String url) {
        this.activityId = activityId;
        this.imageId = imageId;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
