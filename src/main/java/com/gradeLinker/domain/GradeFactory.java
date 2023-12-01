package com.gradeLinker.domain;

import com.gradeLinker.domain.course.Grade;
import com.gradeLinker.domain.course.GradeSet;
import com.gradeLinker.domain.course.GradeSetImpl;
import com.gradeLinker.domain.course.IntGrade;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GradeFactory {
    public IntGrade createIntGrade(Integer value, String category) {
        return new IntGrade(value, category);
    }

    public GradeSet createGradeSet(Set<Grade> grades) {return new GradeSetImpl(grades);}
}
