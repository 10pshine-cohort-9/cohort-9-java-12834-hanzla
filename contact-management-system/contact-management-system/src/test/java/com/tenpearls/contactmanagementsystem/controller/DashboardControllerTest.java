package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.DashboardResponse;
import com.tenpearls.contactmanagementsystem.service.DashboardService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DashboardController controller;

    @Test
    void shouldReturnDashboardForAuthenticatedUser() {

        DashboardResponse response = new DashboardResponse();

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(1L);
        when(dashboardService.getDashboard(1L))
                .thenReturn(response);

        ResponseEntity<DashboardResponse> result =
                controller.getDashboard(authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(dashboardService).getDashboard(1L);
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticationIsNull() {

        ResponseEntity<DashboardResponse> result =
                controller.getDashboard(null);

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                result.getStatusCode()
        );

        assertNull(result.getBody());

        verifyNoInteractions(dashboardService);
    }

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() {

        when(authentication.isAuthenticated())
                .thenReturn(false);

        ResponseEntity<DashboardResponse> result =
                controller.getDashboard(authentication);

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                result.getStatusCode()
        );

        verifyNoInteractions(dashboardService);
    }

    @Test
    void shouldReturnForbiddenWhenPrincipalIsNotLong() {

        when(authentication.isAuthenticated())
                .thenReturn(true);

        when(authentication.getPrincipal())
                .thenReturn("invalid-principal");

        ResponseEntity<DashboardResponse> result =
                controller.getDashboard(authentication);

        assertEquals(
                HttpStatus.FORBIDDEN,
                result.getStatusCode()
        );

        verifyNoInteractions(dashboardService);
    }
}