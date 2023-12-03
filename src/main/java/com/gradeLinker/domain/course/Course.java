package com.gradeLinker.domain.course;

import java.util.HashMap;
import java.util.Map;

public interface Course {
    String getId();
    String getTitle();
    CourseParticipant getParticipantByUsername(String username);
    Map<String, CourseParticipant> getParticipants();
    CourseGrades getCourseGrades();

    void addParticipant(CourseParticipant participant);
    void setCourseGrades(CourseGrades courseGrades);
    void setTitle(String title);
}
