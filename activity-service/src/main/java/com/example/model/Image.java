package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/** {@summary This is valid java doc.} */
@Entity
@Table(name = "image")
public class Image {
    @Id
    @Column(name = "image_id", updatable = false, nullable = false)
    private UUID imageId;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToMany(mappedBy = "images")
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    public Image() {}

    public Image(UUID imageId, String url) {
        this.imageId = imageId;
        this.url = url;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
