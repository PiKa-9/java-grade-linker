package com.gradeLinker.dto.web.request;

import java.util.List;

public class GradeTableRequest {
    private List<String> studentUsernames;
    private List<String> dates;
    private List<String> gradeCategories;
    private List<String> grades;

    public GradeTableRequest(List<String> studentUsernames, List<String> dates, List<String> gradeCategories, List<String> grades) {
        this.studentUsernames = studentUsernames;
        this.dates = dates;
        this.gradeCategories = gradeCategories;
        this.grades = grades;
    }

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }
    public List<String> getDates() {
        return dates;
    }
    public List<String> getGradeCategories() {
        return gradeCategories;
    }
    public List<String> getGrades() {
        return grades;
    }
}
