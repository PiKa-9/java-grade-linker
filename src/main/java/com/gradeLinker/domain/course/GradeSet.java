package com.gradeLinker.domain.course;

import java.util.List;
import java.util.Set;

// Basically GradeSet is just for grade display on the view page
public interface GradeSet {
    Set<Grade> getGrades();
    List<Grade> getGradesSorted();

    void addGrades(Grade... grades);
}
