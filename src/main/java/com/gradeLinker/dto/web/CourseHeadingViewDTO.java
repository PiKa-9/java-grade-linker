package com.gradeLinker.dto.web;

public class CourseHeadingViewDTO {
    private String courseId;
    private String courseTitle;

    public CourseHeadingViewDTO(String courseId, String courseTitle) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public String getCourseId() {
        return courseId;
    }
    public String getCourseTitle() {
        return courseTitle;
    }
}
