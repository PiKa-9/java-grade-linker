package com.gradeLinker.domain;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseImpl;
import com.gradeLinker.domain.course.CourseParticipant;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CourseFactory {
    public Course createCourse(String id, String title, Map<String, CourseParticipant> participants, CourseGrades courseGrades) {
        return new CourseImpl(
                id,
                title,
                participants,
                courseGrades
        );
    }
}
