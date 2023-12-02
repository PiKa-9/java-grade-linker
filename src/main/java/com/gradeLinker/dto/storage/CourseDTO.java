package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public class CourseDTO {
    private String id;
    private String title;
    private Set<CourseParticipantDTO> participants;
    private CourseGradesDTO courseGrades;

    public CourseDTO(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("participants") Set<CourseParticipantDTO> participants, @JsonProperty("courseGrades") CourseGradesDTO courseGrades) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.courseGrades = courseGrades;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Set<CourseParticipantDTO> getParticipants() {
        return participants;
    }
    public CourseGradesDTO getCourseGrades() {
        return courseGrades;
    }
}
