package com.tenpearls.contactmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpearls.contactmanagementsystem.dto.ChangePasswordRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginRequest;
import com.tenpearls.contactmanagementsystem.dto.RegisterRequest;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String testEmail;

    private String testPhone;

@BeforeEach
void setup() {

    testEmail =
            "junit-" + System.nanoTime() + "@example.com";

    testPhone =
            "0311" + (System.nanoTime() % 100000000);

    User testUser = User.builder()
            .firstName("JUnit")
            .lastName("User")
            .email(testEmail)
            .phoneNumber(testPhone)
            .password(
                    passwordEncoder.encode("123456")
            )
            .build();

    userRepository.save(testUser);
}

private UsernamePasswordAuthenticationToken
createAuthentication() {

    Long userId = userRepository
            .findByEmail(testEmail)
            .orElseThrow()
            .getId();

    return new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.emptyList()
    );
}


    @Test
    void register_ShouldReturnCreated() throws Exception {

        RegisterRequest request =
                new RegisterRequest();

        request.setFirstName("Another");
        request.setLastName("User");

        request.setEmail(
                "another-" +
                System.nanoTime() +
                "@example.com"
        );

        request.setPhoneNumber(
                "0322" +
                (System.nanoTime() % 100000000)
        );

        request.setPassword("123456");


        mockMvc.perform(
                post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.message")
                                .value("Registration Successful")
                );
    }


    @Test
    void login_ShouldReturnOk() throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setUsername(testEmail);
        request.setPassword("123456");


        mockMvc.perform(
                post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.message")
                                .value("Login Successful")
                );
    }


    @Test
    void changePassword_ShouldReturnOk() throws Exception {

        ChangePasswordRequest request =
                new ChangePasswordRequest();

        request.setOldPassword("123456");
        request.setNewPassword("654321");
        request.setConfirmPassword("654321");


        mockMvc.perform(
                put("/api/v1/auth/change-password")
                        .with(csrf())
                        .with(
                                authentication(
                                        createAuthentication()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "Password changed successfully"
                        )
                );
    }


    @Test
    void logout_ShouldReturnOk() throws Exception {

        mockMvc.perform(
                post("/api/v1/auth/logout")
                        .with(csrf())
                        .with(
                                authentication(
                                        createAuthentication()
                                )
                        )
        )
                .andExpect(status().is3xxRedirection());
    }
}