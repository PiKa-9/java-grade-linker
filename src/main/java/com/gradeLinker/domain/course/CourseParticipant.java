package com.gradeLinker.domain.course;

import java.util.Arrays;
import java.util.Set;

public class CourseParticipant {
    private String username;
    private Set<String> roles; // course-roles

    public CourseParticipant(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }
    public boolean hasRoles(String... roles) {
        return this.roles.containsAll(Arrays.stream(roles).toList());
    }
    public Set<String> getRoles() {
        return roles;
    }

    public void addRoles(String... roles) {
        this.roles.addAll(Arrays.stream(roles).toList());
    }
}
