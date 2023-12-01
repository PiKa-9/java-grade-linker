package com.gradeLinker.domain.course;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GradeSetImpl implements GradeSet {
    private Set<Grade> grades;

    public GradeSetImpl(Set<Grade> grades) {
        if (grades != null) { this.grades = new HashSet<>(); }
    }

    @Override
    public Set<Grade> getGrades() {
        return grades;
    }
    @Override
    /* Sorts the grades by date; then by type */
    public List<Grade> getGradesSorted() {
        return grades.stream().sorted().toList();
    }

    @Override
    public void addGrades(Grade... grades) {
        this.grades.addAll(Arrays.stream(grades).toList());
    }
}
