package com.gradeLinker.domain.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* User which can login, and has roles */
/* Following User classes should extend this class */
public class LoginUser {
    private String username;
    private int passwordHash;
    private Set<String> roles = new HashSet<>(); // general-roles

    public LoginUser(String username, int passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
    public LoginUser(String username, int passwordHash, Set<String> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        if (roles != null) { this.roles = roles; }
    }

    public String getUsername() {
        return username;
    }
    public int getPasswordHash() {
        return passwordHash;
    }
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
    public Set<String> getRoles() {
        return roles;
    }


    public void addRoles(String... roles) {
        this.roles.addAll(Arrays.stream(roles).toList());
    }


    public void removeRoles(String... roles) {
        // idea recommended to make faster
        Arrays.stream(roles).toList().forEach(this.roles::remove);
    }
}
