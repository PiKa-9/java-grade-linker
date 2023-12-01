package com.gradeLinker.domain.course;

public class IntGrade implements Grade {
    private Integer value;
    private String category;

    public IntGrade(Integer value, String category) {
        this.value = value;
        this.category = category;
    }

    public Integer getValue() {
        return value;
    }
    public String getGradeCategory() {
        return category;
    }
}
