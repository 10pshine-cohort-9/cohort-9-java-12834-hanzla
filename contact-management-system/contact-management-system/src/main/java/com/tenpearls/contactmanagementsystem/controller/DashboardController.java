package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.DashboardResponse;
import com.tenpearls.contactmanagementsystem.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            Authentication authentication) {

        /*
         * Make sure Spring Security has authenticated
         * the current request.
         */
        if (authentication == null
                || !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        /*
         * Our application stores the authenticated user's
         * database ID as the Authentication principal.
         */
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Long userId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                dashboardService.getDashboard(userId)
        );
    }
}