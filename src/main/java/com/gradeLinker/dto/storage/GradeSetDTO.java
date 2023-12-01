package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GradeSetDTO {
    private Set<GradeDTO> grades;

    public GradeSetDTO(@JsonProperty("grades") Set<GradeDTO> grades) {
        this.grades = grades;
    }

    public Set<GradeDTO> getGrades() {
        return grades;
    }
}
