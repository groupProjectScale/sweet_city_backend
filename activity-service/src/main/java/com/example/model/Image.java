package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/** {@summary This is valid java doc.} */
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "image_id", updatable = false, nullable = false)
    private UUID imageId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "activity_id_plus_file_name", nullable = false)
    private String activityIdPlusFileName;

    @ManyToMany(mappedBy = "images")
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    public Image() {}

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getActivityIdPlusFileName() {
        return activityIdPlusFileName;
    }

    public void setActivityIdPlusFileName(String activityIdPlusFileName) {
        this.activityIdPlusFileName = activityIdPlusFileName;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
