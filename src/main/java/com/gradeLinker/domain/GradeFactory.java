package com.gradeLinker.domain;

import com.gradeLinker.domain.course.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeFactory {
    public CourseGrades createCourseGrades(List<String> usernames) {
        return new CourseGradesImpl(
                usernames,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
