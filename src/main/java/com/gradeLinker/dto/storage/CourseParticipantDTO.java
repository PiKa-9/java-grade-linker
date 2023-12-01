package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class CourseParticipantDTO {
    private String username;
    private Set<String> roles;

    public CourseParticipantDTO(@JsonProperty("username") String username, @JsonProperty("roles") Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }
    public Set<String> getRoles() {
        return roles;
    }
}
