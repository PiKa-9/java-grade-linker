package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseGradesDTO {
    private List<String> studentUsernames;
    private List<GradeInfoDTO> gradeInfoList;
    private List<List<Double>> grades;

    public CourseGradesDTO(@JsonProperty("studentUsernames") List<String> studentUsernames, @JsonProperty("gradeInfoList") List<GradeInfoDTO> gradeInfoList, @JsonProperty("grades") List<List<Double>> grades) {
        this.studentUsernames = studentUsernames;
        this.gradeInfoList = gradeInfoList;
        this.grades = grades;
    }

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }
    public List<GradeInfoDTO> getGradeInfoList() {
        return gradeInfoList;
    }
    public List<List<Double>> getGrades() {
        return grades;
    }
}
