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
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "tag_id", updatable = false, nullable = false)
    private UUID tagId;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "tags")
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    @Column(name = "tag_description", nullable = false)
    private String tagDescription;

    @Column(name = "num_of_creations")
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
