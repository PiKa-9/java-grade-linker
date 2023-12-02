package com.gradeLinker.domain.course;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Student extends CourseParticipant {
    private List<Double> studentGrades;

    public Student(String username, String fullName, Set<String> roles, List<Double> studentGrades) {
        super(username, fullName, roles);
        this.studentGrades = studentGrades;
    }

    public List<Double> getStudentGrades() {
        return studentGrades;
    }
}
