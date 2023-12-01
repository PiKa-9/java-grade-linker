package com.gradeLinker.domain.course;

import java.util.Set;

public class Student extends CourseParticipant {
    private GradeSet grades;

    public Student(String username, Set<String> roles, GradeSet grades) {
        super(username, roles);
        if (grades != null) { this.grades = grades; }
    }

    public GradeSet getGrades() {
        return grades;
    }
}
