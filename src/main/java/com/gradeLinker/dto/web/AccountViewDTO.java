package com.gradeLinker.dto.web;

public class AccountViewDTO {
    private String username;
    private String firstName;
    private String lastName;

    public AccountViewDTO(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
}
