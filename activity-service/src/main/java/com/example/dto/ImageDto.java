package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/** The type Image dto. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDto {
    private String fileName;
    private String filePath;
    private String activityIdPlusFileName;

    public ImageDto() {}

    public ImageDto(String fileName, String filePath, String activityIdPlusFileName) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.activityIdPlusFileName = activityIdPlusFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getActivityIdPlusFileName() {
        return activityIdPlusFileName;
    }

    public void setActivityIdPlusFileName(String activityIdPlusFileName) {
        this.activityIdPlusFileName = activityIdPlusFileName;
    }
}
