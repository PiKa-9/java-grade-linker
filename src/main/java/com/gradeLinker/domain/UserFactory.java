package com.gradeLinker.domain;

import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public LoginUser createLoginUser(String username, int passwordHash) {
        return new LoginUser(username, passwordHash);
    }

    public User createUser(String username, int passwordHash, String firstName, String lastName) {
        return new User(username, passwordHash, firstName, lastName);
    }
}
