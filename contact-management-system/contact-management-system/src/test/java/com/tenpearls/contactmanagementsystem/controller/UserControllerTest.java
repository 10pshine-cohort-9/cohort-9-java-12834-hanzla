package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.ChangePasswordRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginResponse;
import com.tenpearls.contactmanagementsystem.dto.RegisterRequest;
import com.tenpearls.contactmanagementsystem.dto.RegisterResponse;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private CsrfToken csrfToken;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @InjectMocks
    private UserController controller;

    @Test
    void shouldRegisterUser() {

        RegisterRequest request = new RegisterRequest();
        RegisterResponse response = new RegisterResponse(
        1L,
        "John",
        "Doe",
        "john@example.com",
        "123456789",
        "Registration Successful"
);

        when(userService.register(request))
                .thenReturn(response);

        ResponseEntity<RegisterResponse> result =
                controller.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(userService).register(request);
    }

    @Test
    void shouldReturnCsrfToken() {

        when(csrfToken.getToken())
                .thenReturn("test-csrf-token");

        ResponseEntity<String> result =
                controller.csrf(csrfToken);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("test-csrf-token", result.getBody());

        verify(csrfToken).getToken();
    }

    @Test
    void shouldLoginUser() {

        LoginRequest request = new LoginRequest();

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("123456789");

        when(userService.authenticate(request))
                .thenReturn(user);

        when(httpRequest.getSession(true))
                .thenReturn(null);

        ResponseEntity<LoginResponse> result =
                controller.login(
                        request,
                        httpRequest,
                        httpResponse
                );

        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNotNull(result.getBody());

        assertEquals(1L, result.getBody().getId());
        assertEquals("John", result.getBody().getFirstName());
        assertEquals("Doe", result.getBody().getLastName());
        assertEquals(
                "john@example.com",
                result.getBody().getEmail()
        );
        assertEquals(
                "123456789",
                result.getBody().getPhoneNumber()
        );
        assertEquals(
                "Login Successful",
                result.getBody().getMessage()
        );

        verify(userService).authenticate(request);
       verify(httpRequest, atLeastOnce()).getSession(true);
        verify(httpRequest).changeSessionId();
    }

    @Test
    void shouldChangePassword() {

        ChangePasswordRequest request =
                new ChangePasswordRequest();

        when(authentication.getPrincipal())
                .thenReturn(1L);

        when(userService.changePassword(
                1L,
                request
        )).thenReturn("Password changed successfully");

        ResponseEntity<String> result =
                controller.changePassword(
                        request,
                        authentication
                );

        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertEquals(
                "Password changed successfully",
                result.getBody()
        );

        verify(userService).changePassword(
                1L,
                request
        );
    }
}