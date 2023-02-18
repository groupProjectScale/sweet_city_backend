package com.example.model;

import java.util.UUID;

public class User {
    private UUID userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String hashPasswordWithSalt; // hashed with salt

    public static final class Builder {
        private UUID userId;
        private String userName;
        private String firstName;
        private String lastName;
        private String email;
        private String hashPasswordWithSalt;

        public Builder() {}

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withUserId(UUID val) {
            userId = val;
            return this;
        }

        public Builder withUserName(String val) {
            userName = val;
            return this;
        }

        public Builder withFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder withLastName(String val) {
            lastName = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withHashPasswordWithSalt(String val) {
            hashPasswordWithSalt = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User(Builder builder) {
        setUserId(builder.userId);
        setUserName(builder.userName);
        setFirstName(builder.firstName);
        setLastName(builder.lastName);
        setEmail(builder.email);
        setHashPasswordWithSalt(builder.hashPasswordWithSalt);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

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

    public void setHashPasswordWithSalt(String password) {
        this.hashPasswordWithSalt = password;
    }
}
