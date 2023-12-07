package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.model.RegistrationBody;
import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.exception.UserAlreadyExistsException;
import com.busher.artsoftbackend.model.LocalUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LocalUserRepository localUserRepository;

    public UserService(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
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
        //TODO: Encrypt passwords!!
        user.setPassword(registrationBody.getPassword());
        return localUserRepository.save(user);
    }
}
