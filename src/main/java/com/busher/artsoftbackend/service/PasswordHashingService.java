package com.busher.artsoftbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashingService {

    @Value("${hashing.salt.rounds}")
    private int saltRounds;

    public String hashPassword(String password) {
        String salt = BCrypt.gensalt(saltRounds);
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}