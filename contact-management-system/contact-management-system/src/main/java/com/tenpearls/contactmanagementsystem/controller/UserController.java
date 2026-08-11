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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates a user using email or phone number and creates a session."
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

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        null,
                        Collections.emptyList()
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        HttpSessionSecurityContextRepository
                securityContextRepository =
                new HttpSessionSecurityContextRepository();

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
            description = "Changes the password of the currently authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully"
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
            description = "Logs out the currently authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Logout successful"
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
                "Logout Successful"
        );
    }
}