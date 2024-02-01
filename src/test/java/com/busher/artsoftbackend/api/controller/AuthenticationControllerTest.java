package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.api.model.RegistrationBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper;
    private RegistrationBody body;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        body = new RegistrationBody();
        body.setEmail("test@test.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("Password123");
        body.setUsername("Username");
    }

    @Test
    void whenRegisterWithValidInputs_thenReturnsOkRequest() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void whenRegisterWithInvalidInputs_thenReturnsBadRequest() throws Exception {
        testFieldWithInvalidValues("username", new String[]{null, ""});

        testFieldWithInvalidValues("email", new String[]{null, "", "invalidemail"});

        testFieldWithInvalidValues("password", new String[]{null, "", "short"});

        testFieldWithInvalidValues("firstName", new String[]{null, ""});

        testFieldWithInvalidValues("lastName", new String[]{null, ""});

        body.setEmail("invalidemail@");
        performPostWithBody(mapper.writeValueAsString(body));
    }

    private void testFieldWithInvalidValues(String fieldName, String[] values) throws Exception {
        for (String value : values) {
            switch (fieldName) {
                case "username":
                    body.setUsername(value);
                    break;
                case "email":
                    body.setEmail(value);
                    break;
                case "password":
                    body.setPassword(value);
                    break;
                case "firstName":
                    body.setFirstName(value);
                    break;
                case "lastName":
                    body.setLastName(value);
                    break;
            }
            performPostWithBody(mapper.writeValueAsString(body));
        }
    }

    private void performPostWithBody(String content) throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

}