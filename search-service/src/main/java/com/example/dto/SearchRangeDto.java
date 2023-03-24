package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SearchRangeDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double longitude;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double latitude;

    private long radius;

    public SearchRangeDto() {}

    public SearchRangeDto(long longitude, long latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }
}
