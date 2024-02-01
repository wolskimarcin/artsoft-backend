package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.model.LoginBody;
import com.busher.artsoftbackend.api.model.PasswordResetBody;
import com.busher.artsoftbackend.api.model.RegistrationBody;
import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.dao.VerificationTokenRepository;
import com.busher.artsoftbackend.exception.EmailFailureException;
import com.busher.artsoftbackend.exception.EmailNotFoundException;
import com.busher.artsoftbackend.exception.UserAlreadyExistsException;
import com.busher.artsoftbackend.exception.UserNotVerifiedException;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.VerificationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private LocalUserRepository localUserRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private EncryptionService encryptionService;
    @Mock
    private JWTService jwtService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void whenRegisterUserWithNewEmail_thenSuccess() throws Exception {
        RegistrationBody registrationBody = createTestRegistrationBody();
        when(localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail())).thenReturn(Optional.empty());
        when(localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername())).thenReturn(Optional.empty());
        when(encryptionService.encryptPassword(registrationBody.getPassword())).thenReturn("encryptedPassword");
        when(localUserRepository.save(any(LocalUser.class))).thenAnswer(invocation -> {
            LocalUser user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        LocalUser result = userService.registerUser(registrationBody);

        assertUserFields(result, registrationBody);
        verify(emailService).sendVerificationEmail(any(VerificationToken.class));
    }

    @Test
    void whenRegisterUserAndUserAlreadyExists_thenThrowsException() {
        RegistrationBody registrationBody = createTestRegistrationBody();
        LocalUser existingUser = new LocalUser();
        existingUser.setEmail(registrationBody.getEmail());
        when(localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registrationBody));
    }

    @Test
    public void whenLoginWithVerifiedEmail_thenSuccess() throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testUser");
        loginBody.setPassword("correctPassword");

        LocalUser user = new LocalUser();
        user.setEmailVerified(true);
        user.setPassword("encryptedPassword");

        when(localUserRepository.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.of(user));
        when(encryptionService.verifyPassword("correctPassword", "encryptedPassword")).thenReturn(true);
        when(jwtService.generateJWT(user)).thenReturn("generatedJWTToken");

        String token = userService.loginUser(loginBody);

        assertNotNull(token);
        assertEquals("generatedJWTToken", token);
    }

    @Test
    public void whenLoginWithUnverifiedEmail_thenResendVerificationEmail() throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("user");
        loginBody.setPassword("password");

        LocalUser unverifiedUser = new LocalUser();
        unverifiedUser.setEmailVerified(false);
        unverifiedUser.setPassword("encryptedPassword");

        when(localUserRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(unverifiedUser));
        when(encryptionService.verifyPassword("password", "encryptedPassword")).thenReturn(true);

        doNothing().when(emailService).sendVerificationEmail(any(VerificationToken.class));

        assertThrows(UserNotVerifiedException.class, () -> userService.loginUser(loginBody));

        verify(emailService).sendVerificationEmail(any(VerificationToken.class));
    }

    @Test
    public void whenLoginWithIncorrectPassword_thenFailAuthentication() throws UserNotVerifiedException, EmailFailureException {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("user");
        loginBody.setPassword("wrongPassword");

        LocalUser user = new LocalUser();
        user.setEmailVerified(true);
        user.setPassword("encryptedPassword");

        when(localUserRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));
        when(encryptionService.verifyPassword("wrongPassword", "encryptedPassword")).thenReturn(false);

        assertNull(userService.loginUser(loginBody), "Expected null due to incorrect password");
    }

    @Test
    public void whenLoginAndUserDoesNotExist_thenFailAuthentication() throws UserNotVerifiedException, EmailFailureException {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("nonexistentUser");
        loginBody.setPassword("password");

        when(localUserRepository.findByUsernameIgnoreCase("nonexistentUser")).thenReturn(Optional.empty());

        assertNull(userService.loginUser(loginBody), "Expected null when user does not exist");
    }

    @Test
    public void whenVerifyUserWithValidToken_thenVerifySuccessfully() {
        String validToken = "validToken";
        VerificationToken verificationToken = new VerificationToken();
        LocalUser user = new LocalUser();
        user.setEmailVerified(false);

        verificationToken.setUser(user);
        when(verificationTokenRepository.findByToken(validToken)).thenReturn(Optional.of(verificationToken));

        boolean result = userService.verifyUser(validToken);

        assertTrue(result);
        assertTrue(user.isEmailVerified());
        verify(verificationTokenRepository).deleteByUser(user);
    }

    @Test
    public void whenVerifyUserAndTokenNotFound_thenVerificationFails() {
        String invalidToken = "invalidToken";
        when(verificationTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        boolean result = userService.verifyUser(invalidToken);

        assertFalse(result, "Expected verifyUser to return false when token is not found.");
    }

    @Test
    public void whenVerifyUserAndUserAlreadyVerified_thenNoActionTaken() {
        String token = "existingToken";
        LocalUser alreadyVerifiedUser = new LocalUser();
        alreadyVerifiedUser.setEmailVerified(true);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(alreadyVerifiedUser);

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        boolean result = userService.verifyUser(token);

        assertFalse(result, "Expected verifyUser to return false when user is already verified.");
    }

    private RegistrationBody createTestRegistrationBody() {
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("testUser");
        registrationBody.setEmail("test@example.com");
        registrationBody.setPassword("password123");
        registrationBody.setFirstName("Test");
        registrationBody.setLastName("User");
        return registrationBody;
    }

    private void assertUserFields(LocalUser user, RegistrationBody registrationBody) {
        assertNotNull(user);
        assertEquals(registrationBody.getUsername(), user.getUsername());
        assertEquals(registrationBody.getEmail(), user.getEmail());
        assertEquals("encryptedPassword", user.getPassword());
        assertEquals(registrationBody.getFirstName(), user.getFirstName());
        assertEquals(registrationBody.getLastName(), user.getLastName());
    }

    @Test
    public void whenForgotPasswordWithValidEmail_thenPasswordResetEmailSent() throws EmailNotFoundException, EmailFailureException {
        String email = "test@example.com";
        LocalUser user = new LocalUser();
        user.setEmail(email);

        when(localUserRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(jwtService.generatePasswordResetJWT(user)).thenReturn("resetToken");

        userService.forgotPassword(email);

        verify(emailService).sendPasswordResetEmail(eq(user), eq("resetToken"));
    }

    @Test
    public void whenResetPasswordWithValidTokenAndEmail_thenPasswordResetSuccessful() {
        String token = "resetToken";
        String email = "test@example.com";
        String newPassword = "newPassword";

        PasswordResetBody body = new PasswordResetBody();
        body.setToken(token);
        body.setPassword(newPassword);

        LocalUser user = new LocalUser();
        user.setEmail(email);

        when(jwtService.getResetPasswordEmail(token)).thenReturn(email);
        when(localUserRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(encryptionService.encryptPassword(newPassword)).thenReturn("encryptedPassword");

        userService.resetPassword(body);

        verify(localUserRepository).save(user);
        assertEquals("encryptedPassword", user.getPassword());
    }

}