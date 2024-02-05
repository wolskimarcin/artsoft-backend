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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final LocalUserRepository localUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordHashingService passwordHashingService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(LocalUserRepository localUserRepository, VerificationTokenRepository verificationTokenRepository, PasswordHashingService passwordHashingService, JWTService jwtService, EmailService emailService) {
        this.localUserRepository = localUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordHashingService = passwordHashingService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {
        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setUsername(registrationBody.getUsername());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setPassword(passwordHashingService.hashPassword(registrationBody.getPassword()));
        user.setIsEmailVerified(true); // TODO: temporarily disabled email verification until AWS completes DNS domain verification.
        logger.info("Email verification is temporarily disabled until AWS completes DNS domain verification. Email is verified by default, please proceed to login.");
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return localUserRepository.save(user);
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (passwordHashingService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.getIsEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                localUserRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<LocalUser> opUser = localUserRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetEmail(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetBody body) {
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<LocalUser> opUser = localUserRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            user.setPassword(passwordHashingService.hashPassword(body.getPassword()));
            localUserRepository.save(user);
        }
    }

}
