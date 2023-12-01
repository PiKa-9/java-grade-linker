package com.gradeLinker.dto.web;

public class CourseLinkViewDTO {
    private String courseId;
    private String text;

    public CourseLinkViewDTO(String courseId, String text) {
        this.courseId = courseId;
        this.text = text;
    }

    public String getCourseId() {
        return courseId;
    }
    public String getText() {
        return text;
    }
}
