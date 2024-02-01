package com.busher.artsoftbackend.api.security;

import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {

    private static final String AUTHENTICATED_PATH = "/auth/me";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String SAMPLE_TOKEN = "sample.token";
    private static final String USERNAME = "user";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JWTService jwtService;
    @MockBean
    private LocalUserRepository localUserRepository;

    @Test
    public void shouldRejectUnauthenticatedRequest() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void shouldRejectRequestWithInvalidToken() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH)
                        .header("Authorization", TOKEN_PREFIX + "bad.token"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void shouldRejectRequestForNonexistentUser() throws Exception {
        when(jwtService.getUsername(SAMPLE_TOKEN)).thenReturn(USERNAME);
        when(localUserRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());

        mvc.perform(get(AUTHENTICATED_PATH)
                        .header("Authorization", TOKEN_PREFIX + SAMPLE_TOKEN))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void shouldAllowRequestWithValidToken() throws Exception {
        LocalUser user = new LocalUser();
        when(jwtService.getUsername(SAMPLE_TOKEN)).thenReturn(USERNAME);
        when(localUserRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
        user.setEmailVerified(true);

        mvc.perform(get(AUTHENTICATED_PATH)
                        .header("Authorization", TOKEN_PREFIX + SAMPLE_TOKEN))
                .andExpect(status().isOk());
    }
}
