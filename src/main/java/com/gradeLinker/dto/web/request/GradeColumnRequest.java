package com.gradeLinker.dto.web.request;

public class GradeColumnRequest {
    private String date;

    public GradeColumnRequest(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
