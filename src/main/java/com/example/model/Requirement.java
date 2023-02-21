package com.example.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Requirement {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID requirementId;
}
