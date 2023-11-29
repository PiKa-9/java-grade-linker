package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class UserDTO {
    private String username;
    private int passwordHash;
    private Set<String> roles;
    private String firstName;
    private String lastName;
    private Set<String> courseIds;

    public UserDTO(@JsonProperty("username") String username, @JsonProperty("passwordHash") int passwordHash, @JsonProperty("roles") Set<String> roles, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("courseIds") Set<String> courseIds) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseIds = courseIds;
    }

    public String getUsername() {
        return username;
    }
    public int getPasswordHash() {
        return passwordHash;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Set<String> getCourseIds() {
        return courseIds;
    }
}
