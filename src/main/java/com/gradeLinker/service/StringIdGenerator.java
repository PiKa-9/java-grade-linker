package com.gradeLinker.service;


import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StringIdGenerator implements IdGenerator<String> {
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int length = 6;

    public String generateId() {
        StringBuilder buffer = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; ++i) {
            int randomPosition = random.nextInt(0, alphabet.length());
            buffer.append(alphabet.charAt(randomPosition));
        }
        return buffer.toString();
    }
}
