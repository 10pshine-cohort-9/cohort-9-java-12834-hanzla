package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.ChangePasswordRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginResponse;
import com.tenpearls.contactmanagementsystem.dto.RegisterRequest;
import com.tenpearls.contactmanagementsystem.dto.RegisterResponse;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(
        name = "User APIs",
        description = "Authentication APIs for user registration, login, logout and password management."
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final HttpSessionSecurityContextRepository
            securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @Operation(
            summary = "Register User",
            description = "Registers a new user in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User registered successfully"
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @Operation(
            summary = "Get CSRF Token",
            description = "Provides a CSRF token to the frontend."
    )
    @ApiResponse(
            responseCode = "200",
            description = "CSRF token returned successfully"
    )
    @GetMapping("/csrf")
    public ResponseEntity<String> csrf(CsrfToken csrfToken) {

        return ResponseEntity.ok(
                csrfToken.getToken()
        );
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates a user and creates a secure session."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        User user = userService.authenticate(request);

        /*
         * Create/obtain the HTTP session.
         */
        httpRequest.getSession(true);

        /*
         * Rotate session ID after successful authentication.
         */
        httpRequest.changeSessionId();

        /*
         * Create authenticated principal using user ID.
         */
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        null,
                        Collections.emptyList()
                );

        /*
         * Create SecurityContext.
         */
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        /*
         * Explicitly save SecurityContext to HTTP session.
         */
        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        LoginResponse response =
                LoginResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .message("Login Successful")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Change Password",
            description = "Changes the password of the authenticated user."
    )
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.changePassword(
                        userId,
                        request
                )
        );
    }

    @Operation(
            summary = "Logout User",
            description = "Logs out the authenticated user."
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest httpRequest) {

        SecurityContextHolder.clearContext();

        if (httpRequest.getSession(false) != null) {
            httpRequest.getSession(false).invalidate();
        }

        return ResponseEntity.ok(
                "Logout Successful"
        );
    }
}