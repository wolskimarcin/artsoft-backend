package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);
}
