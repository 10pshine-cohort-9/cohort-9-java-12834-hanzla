package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.DashboardResponse;
import com.tenpearls.contactmanagementsystem.service.DashboardService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    void getDashboard_ShouldReturnOk_WhenAuthenticated() throws Exception {

        DashboardResponse response =
                DashboardResponse.builder()
                        .totalContacts(5L)
                        .favoriteContacts(2L)
                        .totalUsers(10L)
                        .recentContacts(Collections.emptyList())
                        .build();

        when(dashboardService.getDashboard(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/v1/dashboard")
                        .with(
                                authentication(
                                        new UsernamePasswordAuthenticationToken(
                                                1L,
                                                null,
                                                Collections.emptyList()
                                        )
                                )
                        )
        )
                .andExpect(status().isOk());
    }

    @Test
    void getDashboard_ShouldReturnForbidden_WhenNotAuthenticated()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/dashboard")
        )
                .andExpect(status().isForbidden());
    }
}