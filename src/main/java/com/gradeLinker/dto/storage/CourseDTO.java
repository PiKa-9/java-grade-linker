package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public class CourseDTO {
    private String id;
    private String title;
    private Set<CourseParticipantDTO> participants;
    private Map<String, GradeSetDTO> grades;

    public CourseDTO(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("participants") Set<CourseParticipantDTO> participants, @JsonProperty("grades") Map<String, GradeSetDTO> grades) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.grades = grades;
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
    public Map<String, GradeSetDTO> getGrades() {
        return grades;
    }
}
