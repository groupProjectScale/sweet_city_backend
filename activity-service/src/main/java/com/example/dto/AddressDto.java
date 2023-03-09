package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private UUID userId;
    private String location;
    private double longitude;
    private double latitude;

    public AddressDto() {}

    public AddressDto(UUID userId, String location, double longitude, double latitude) {
        this.userId = userId;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
