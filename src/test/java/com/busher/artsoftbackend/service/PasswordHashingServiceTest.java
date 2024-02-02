package com.busher.artsoftbackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {PasswordHashingService.class})
@SpringBootTest
class PasswordHashingServiceTest {

    @Autowired
    private PasswordHashingService passwordHashingService;

    @Test
    void whenHashPassword_thenReturnsNonNullHashedPassword() {
        String password = "testPassword";
        String hashedPassword = passwordHashingService.hashPassword(password);

        assertNotNull(hashedPassword, "Hashed password should not be null.");
    }

    @Test
    void whenVerifyPassword_WithCorrectPassword_ThenReturnsTrue() {
        String password = "testPassword";
        String hashedPassword = passwordHashingService.hashPassword(password);

        assertTrue(passwordHashingService.verifyPassword(password, hashedPassword),
                "Password verification should succeed with the correct password.");
    }

    @Test
    void whenVerifyPassword_WithIncorrectPassword_ThenReturnsFalse() {
        String correctPassword = "correctPassword";
        String hashedPassword = passwordHashingService.hashPassword(correctPassword);
        String incorrectPassword = "incorrectPassword";

        assertFalse(passwordHashingService.verifyPassword(incorrectPassword, hashedPassword),
                "Password verification should fail with an incorrect password.");
    }
}