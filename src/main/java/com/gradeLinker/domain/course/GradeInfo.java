package com.gradeLinker.domain.course;

public class GradeInfo {
    private String category;
    private String date;

    public GradeInfo( String category, String date) {
        this.category = category;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }
    public String getDate() {
        return date;
    }
}
