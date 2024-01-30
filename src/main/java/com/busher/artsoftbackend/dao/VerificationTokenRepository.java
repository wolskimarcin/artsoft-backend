package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
