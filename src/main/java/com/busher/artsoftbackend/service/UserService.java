package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.model.LoginBody;
import com.busher.artsoftbackend.api.model.RegistrationBody;
import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.dao.VerificationTokenRepository;
import com.busher.artsoftbackend.exception.EmailFailureException;
import com.busher.artsoftbackend.exception.UserAlreadyExistsException;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.VerificationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {

    private final LocalUserRepository localUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final EmailService emailService;

    public UserService(LocalUserRepository localUserRepository, VerificationTokenRepository verificationTokenRepository, EncryptionService encryptionService, JWTService jwtService, EmailService emailService) {
        this.localUserRepository = localUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.encryptionService = encryptionService;
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
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationTokenRepository.save(verificationToken);
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

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> optionalUser = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (optionalUser.isPresent()) {
            LocalUser user = optionalUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}
