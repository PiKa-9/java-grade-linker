package com.gradeLinker.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordHashingService implements PasswordHasher {
    @Override
    public int hash(String password) {
        return password.hashCode();
    }
}
