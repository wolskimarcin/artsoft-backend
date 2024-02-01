package com.busher.artsoftbackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {EncryptionService.class})
@SpringBootTest
class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void whenEncryptPassword_thenReturnsNonNullEncryptedPassword() {
        String password = "testPassword";
        String encryptedPassword = encryptionService.encryptPassword(password);

        assertNotNull(encryptedPassword, "Encrypted password should not be null.");
    }

    @Test
    void whenVerifyPassword_WithCorrectPassword_ThenReturnsTrue() {
        String password = "testPassword";
        String encryptedPassword = encryptionService.encryptPassword(password);

        assertTrue(encryptionService.verifyPassword(password, encryptedPassword),
                "Password verification should succeed with the correct password.");
    }

    @Test
    void whenVerifyPassword_WithIncorrectPassword_ThenReturnsFalse() {
        String correctPassword = "correctPassword";
        String encryptedPassword = encryptionService.encryptPassword(correctPassword);
        String incorrectPassword = "incorrectPassword";

        assertFalse(encryptionService.verifyPassword(incorrectPassword, encryptedPassword),
                "Password verification should fail with an incorrect password.");
    }
}