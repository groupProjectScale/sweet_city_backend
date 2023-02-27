package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginDto {
    private String userName;
    private String hashPasswordWithSalt;

    public UserLoginDto() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashPasswordWithSalt() {
        return hashPasswordWithSalt;
    }

    public void setHashPasswordWithSalt(String hashPasswordWithSalt) {
        this.hashPasswordWithSalt = hashPasswordWithSalt;
    }
}
