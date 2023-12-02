package com.gradeLinker.domain.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class User extends LoginUser {
    private String firstName;
    private String lastName;
    private Set<String> courseIds = new HashSet<>(); // ids of courses with which interaction is allowed

    public User(String username, int passwordHash, Set<String> roles, String firstName, String lastName, Set<String> courseIds) {
        super(username, passwordHash, roles);
        this.firstName = firstName;
        this.lastName = lastName;
        if (courseIds != null) { this.courseIds = courseIds; }
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public boolean hasCourseId(String courseId) {return courseIds.contains(courseId);}
    public Set<String> getCourseIds() {
        return courseIds;
    }


    public void addCourseIds(String... courseIds) {
        this.courseIds.addAll(Arrays.stream(courseIds).toList());
    }


    public void removeCourseIds(String... courseIds) {
        // idea recommended to make faster
        Arrays.stream(courseIds).toList().forEach(this.courseIds::remove);
    }
}
