package com.tenpearls.contactmanagementsystem.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {

        user = User.builder()
                .firstName("Hanzla")
                .lastName("Shehzad")
                .email("controller@gmail.com")
                .phoneNumber("03995555555")
                .password("123456")
                .build();

        user = userRepository.save(user);
    }

    private void authenticateUser() {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        null,
                        Collections.emptyList()
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void createContact_ShouldReturnCreated() throws Exception {

        authenticateUser();

        ContactRequest request = new ContactRequest();

        request.setFirstName("Ali");
        request.setLastName("Khan");
        request.setTitle("Software Engineer");
        request.setEmail("ali@gmail.com");
        request.setPhoneNumber("03001111111");

        mockMvc.perform(
                post("/api/v1/contacts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName")
                        .value("Ali"));
    }

    @Test
    void getAllContacts_ShouldReturnOk() throws Exception {

        authenticateUser();

        mockMvc.perform(
                get("/api/v1/contacts")
        )
                .andExpect(status().isOk());
    }

    @Test
    void searchContacts_ShouldReturnOk() throws Exception {

        authenticateUser();

        mockMvc.perform(
                get("/api/v1/contacts/search")
                        .param("keyword", "Ali")
        )
                .andExpect(status().isOk());
    }
}