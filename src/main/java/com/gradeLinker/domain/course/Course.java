package com.gradeLinker.domain.course;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private String id;
    private String title;
    private Map<String, CourseParticipant> participants = new HashMap<>();
    private CourseGrades courseGrades;

    public Course(String id, String title, Map<String, CourseParticipant> participants, CourseGrades courseGrades) {
        this.id = id;
        this.title = title;
        if (participants != null) { this.participants = participants; }
        this.courseGrades = courseGrades;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public CourseParticipant getParticipantByUsername(String username) {
        return participants.get(username);
    }
    public Map<String, CourseParticipant> getParticipants() {
        return participants;
    }
    public CourseGrades getCourseGrades() {
        return courseGrades;
    }

    public void addParticipant(CourseParticipant participant) {
        participants.put(participant.getUsername(), participant);

        if (participant instanceof Student) {
            courseGrades.addStudent((Student) participant);
        }
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
