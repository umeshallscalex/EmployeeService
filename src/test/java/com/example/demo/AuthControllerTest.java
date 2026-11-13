package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void separateLoginPageIsServed() throws Exception {
        mockMvc.perform(get("/login.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Employee Service")))
                .andExpect(content().string(containsString("location.assign(`${apiUrl}/`)")));
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Log in or create an account")))
                .andExpect(content().string(containsString("Log out")));
    }

    @Test
    void registrationStoresPasswordHashAndLoginChecksCredentials() throws Exception {
        String username = "test_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "safePass123";
        String credentials = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", password));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username));

        AppUser storedUser = appUserRepository.findByUsername(username).orElseThrow();
        assertNotEquals(password, storedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches(password, storedUser.getPasswordHash()));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful."))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertTrue(session != null);
        mockMvc.perform(get("/api/auth/session").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.username").value(username));

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/auth/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false));

        String wrongPassword = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", "wrongPass123"));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongPassword))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password."));
    }
}
