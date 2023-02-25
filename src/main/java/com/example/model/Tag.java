package com.example.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false)
    private UUID tagId;

    @ManyToMany(mappedBy = "tags")
    private Set<Activity> activities = new HashSet<>();

    private String tagDescription;
    private Integer numOfCreations;

    public Tag() {}

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Integer getNumOfCreations() {
        return numOfCreations;
    }

    public void setNumOfCreations(Integer numOfCreations) {
        this.numOfCreations = numOfCreations;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
