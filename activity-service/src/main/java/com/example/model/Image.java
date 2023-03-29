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

    @ManyToMany(mappedBy = "images")
    @JsonIgnore
    private Set<Activity> activities = new HashSet<>();

    public Image() {}

    public Image(UUID imageId) {
        this.imageId = imageId;
    }
}
