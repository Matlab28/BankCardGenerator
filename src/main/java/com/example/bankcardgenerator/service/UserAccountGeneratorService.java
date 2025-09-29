package com.example.bankcardgenerator.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

@Service
public class UserAccountGeneratorService {

    private final Random random = new SecureRandom();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERS_UPPER = LETTERS.toUpperCase(Locale.ROOT);
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%&*()-_=+[]{}";

    /**
     * Generate a username from first + last name + random digits. Lowercased.
     * If names null/empty, fallback to random name.
     */
    public String generateUsername(String firstName, String lastName) {
        String base;
        if (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank()) {
            base = (firstName + "." + lastName).replaceAll("\\s+", "");
        } else if (firstName != null && !firstName.isBlank()) {
            base = firstName.replaceAll("\\s+", "");
        } else {
            base = "user";
        }
        int suffix = 100 + random.nextInt(900); // three-digit suffix
        return (base + suffix).toLowerCase(Locale.ROOT);
    }

    /**
     * Generate a secure random password with given length and options.
     */
    public String generatePassword(int length, boolean includeSymbols, boolean includeUppercase) {
        if (length < 6) throw new IllegalArgumentException("password too short");
        StringBuilder pool = new StringBuilder(LETTERS).append(DIGITS);
        if (includeUppercase) pool.append(LETTERS_UPPER);
        if (includeSymbols) pool.append(SYMBOLS);

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(pool.length());
            password.append(pool.charAt(idx));
        }
        return password.toString();
    }

    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
}
