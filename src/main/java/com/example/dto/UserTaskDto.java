package com.example.dto;

public class UserTaskDto {
    private UserDto userDto;
    private AddressDto addressDto;

    public UserTaskDto() {}

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }
}
