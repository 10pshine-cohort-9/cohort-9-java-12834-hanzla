package com.tenpearls.contactmanagementsystem.service;

import com.tenpearls.contactmanagementsystem.dto.ChangePasswordRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginRequest;
import com.tenpearls.contactmanagementsystem.dto.LoginResponse;
import com.tenpearls.contactmanagementsystem.dto.RegisterRequest;
import com.tenpearls.contactmanagementsystem.dto.RegisterResponse;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.exception.EmailAlreadyExistsException;
import com.tenpearls.contactmanagementsystem.exception.InvalidCredentialsException;
import com.tenpearls.contactmanagementsystem.exception.InvalidPasswordException;
import com.tenpearls.contactmanagementsystem.exception.PhoneNumberAlreadyExistsException;
import com.tenpearls.contactmanagementsystem.exception.UserNotFoundException;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {

        logger.info("Registering user {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);

        logger.info("User registered successfully");

        return RegisterResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .message("Registration Successful")
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        logger.info("Login request received for {}", request.getUsername());

        User user = userRepository.findByEmail(request.getUsername())
                .or(() -> userRepository.findByPhoneNumber(request.getUsername()))
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email/phone or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email/phone or password");
        }

        logger.info("Login successful");

return LoginResponse.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .message("Login Successful")
        .build();
    }

    public String changePassword(ChangePasswordRequest request) {

        logger.info("Password change requested for {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException(
                    "Old password is incorrect");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new InvalidPasswordException(
                    "Passwords do not match");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        userRepository.save(user);

        logger.info("Password updated successfully");

        return "Password changed successfully";
    }
    public String logout() {

    logger.info("User logged out successfully");

    return "Logout Successful";
}
}