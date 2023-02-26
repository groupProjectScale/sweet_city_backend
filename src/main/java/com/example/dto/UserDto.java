package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String hashPasswordWithSalt;

    public UserDto() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPasswordWithSalt() {
        return hashPasswordWithSalt;
    }

    public void setHashPasswordWithSalt(String hashPasswordWithSalt) {
        this.hashPasswordWithSalt = hashPasswordWithSalt;
    }
}
