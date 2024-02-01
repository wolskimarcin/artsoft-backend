package com.busher.artsoftbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.busher.artsoftbackend.model.LocalUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {JWTService.class})
@SpringBootTest
@TestPropertySource(properties = {
        "jwt.algorithm.key=testKey",
        "jwt.issuer=testIssuer",
        "jwt.expiryInSeconds=3600"
})
class JWTServiceTest {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Autowired
    private JWTService jwtService;

    private LocalUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new LocalUser();
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@example.com");
    }

    @Test
    void whenGenerateJWT_thenCreatesValidTokenWithUsername() {
        String token = jwtService.generateJWT(testUser);
        assertNotNull(token);

        assertDoesNotThrow(() -> JWT.require(Algorithm.HMAC256("testKey"))
                .withIssuer("testIssuer")
                .build()
                .verify(token));

        assertEquals(testUser.getUsername(), JWT.decode(token).getClaim("USERNAME").asString());
    }

    @Test
    void whenGenerateVerificationJWT_thenCreatesValidTokenWithEmail() {
        String token = jwtService.generateVerificationJWT(testUser);
        assertNotNull(token);

        assertDoesNotThrow(() -> JWT.require(Algorithm.HMAC256("testKey"))
                .withIssuer("testIssuer")
                .build()
                .verify(token));

        assertEquals(testUser.getEmail(), JWT.decode(token).getClaim("EMAIL").asString());
    }

    @Test
    void whenGetUsername_thenReturnsCorrectUsernameFromToken() {
        String token = jwtService.generateJWT(testUser);

        String username = jwtService.getUsername(token);
        assertEquals(testUser.getUsername(), username);
    }

    @Test
    public void whenGetUsername_withJWTNotGeneratedByUs_thenThrowException() {
        String token =
                JWT.create().withClaim("USERNAME", testUser.getUsername()).sign(Algorithm.HMAC256(
                        "fake-secret"));
        Assertions.assertThrows(SignatureVerificationException.class,
                () -> jwtService.getUsername(token));
    }

    @Test
    public void whenGetUsername_withCorrectJWTButWithoutIssuer_thenThrowException() {
        String token =
                JWT.create().withClaim("USERNAME", testUser.getUsername())
                        .sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class,
                () -> jwtService.getUsername(token));
    }

}