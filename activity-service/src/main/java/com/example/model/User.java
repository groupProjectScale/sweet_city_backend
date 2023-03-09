package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Client")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false)
    private UUID userId;

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String hashPasswordWithSalt; // hashed with salt

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "attendees")
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    public User() {}

    public User(
            UUID userId,
            String userName,
            String firstName,
            String lastName,
            String email,
            String hashPasswordWithSalt) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashPasswordWithSalt = hashPasswordWithSalt;
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

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
