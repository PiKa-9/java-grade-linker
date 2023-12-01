package com.gradeLinker.domain;

import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserFactory {
    public LoginUser createLoginUser(String username, int passwordHash) {
        return new LoginUser(username, passwordHash);
    }

    public User createUser(String username, int passwordHash, Set<String> roles, String firstName, String lastName, Set<String> courseIds) {
        return new User(username, passwordHash, roles, firstName, lastName, courseIds);
    }
}
