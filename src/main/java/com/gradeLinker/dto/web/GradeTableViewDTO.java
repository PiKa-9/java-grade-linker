package com.gradeLinker.dto.web;

import java.util.List;

public class GradeTableViewDTO {
    private List<String> studentUsernames;
    private List<String> studentFullNames;
    private List<String> dates;
    private List<String> gradeCategories;
    private List<List<String>> grades;

    public GradeTableViewDTO(List<String> studentUsernames, List<String> studentFullNames, List<String> dates, List<String> gradeCategories, List<List<String>> grades) {
        this.studentUsernames = studentUsernames;
        this.studentFullNames = studentFullNames;
        this.dates = dates;
        this.gradeCategories = gradeCategories;
        this.grades = grades;
    }

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }
    public List<String> getStudentFullNames() {
        return studentFullNames;
    }
    public List<String> getDates() {
        return dates;
    }
    public List<String> getGradeCategories() {
        return gradeCategories;
    }
    public List<List<String>> getGrades() {
        return grades;
    }
}
