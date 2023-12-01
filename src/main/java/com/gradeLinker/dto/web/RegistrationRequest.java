package com.gradeLinker.dto.web;

public class RegistrationRequest {
    private String username;
    private String type;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;

    public RegistrationRequest(String username, String type, String firstName, String lastName, String password, String confirmPassword) {
        this.username = username;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }


    public String getUsername() {
        return username;
    }
    public String getType() {
        return type;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
