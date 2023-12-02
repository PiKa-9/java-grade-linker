package com.gradeLinker.domain.course;

import java.util.Arrays;
import java.util.Set;

public class CourseParticipant {
    private String username;
    private String fullName;
    private Set<String> roles; // course-roles

    public CourseParticipant(String username, String fullName, Set<String> roles) {
        this.username = username;
        this.fullName = fullName;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }
    public String getFullName() {
        return fullName;
    }
    public boolean hasRoles(String... roles) {
        return this.roles.containsAll(Arrays.stream(roles).toList());
    }
    public Set<String> getRoles() {
        return roles;
    }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void addRoles(String... roles) {
        this.roles.addAll(Arrays.stream(roles).toList());
    }
}
