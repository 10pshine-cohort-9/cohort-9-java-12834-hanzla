package com.tenpearls.contactmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email or Phone Number is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}