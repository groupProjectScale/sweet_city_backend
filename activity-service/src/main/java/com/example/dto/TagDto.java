package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {
    private String tagDescription;
    private Integer numOfCreations;

    public TagDto() {}

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription.toLowerCase().replace(" ", "");
    }

    public Integer getNumOfCreations() {
        return numOfCreations;
    }

    public void setNumOfCreations(Integer numOfCreations) {
        this.numOfCreations = numOfCreations;
    }
}
