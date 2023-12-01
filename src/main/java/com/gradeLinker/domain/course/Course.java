package com.gradeLinker.domain.course;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private String id;
    private String title;
    // { id of participant : data of participant }
    //     participants and grades - the data in course which was build by interactions of the course with participant (or admin)
    private Map<String, CourseParticipant> participants = new HashMap<>();
    private Map<String, GradeSet> grades = new HashMap<>();

    public Course(String id, String title, Map<String, CourseParticipant> participants, Map<String, GradeSet> grades) {
        this.id = id;
        this.title = title;
        if (participants != null) { this.participants = participants; }
        if (grades != null) { this.grades = grades; }
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
    public Map<String, GradeSet> getGrades() {
        return grades;
    }


    public void addParticipant(CourseParticipant participant) {
        participants.put(participant.getUsername(), participant);

        if (participant instanceof Student) {
            Student student = (Student) participant;
            grades.put(student.getUsername(), student.getGrades());
        }
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
