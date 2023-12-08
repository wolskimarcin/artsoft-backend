package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.model.LoginBody;
import com.busher.artsoftbackend.api.model.RegistrationBody;
import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.exception.UserAlreadyExistsException;
import com.busher.artsoftbackend.model.LocalUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final LocalUserRepository localUserRepository;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserService(LocalUserRepository localUserRepository, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserRepository = localUserRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
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
        return localUserRepository.save(user);
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
