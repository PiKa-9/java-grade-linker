package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GradeInfoDTO {
    private String category;
    private String date;

    public GradeInfoDTO(@JsonProperty("category") String category, @JsonProperty("date") String date) {
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
