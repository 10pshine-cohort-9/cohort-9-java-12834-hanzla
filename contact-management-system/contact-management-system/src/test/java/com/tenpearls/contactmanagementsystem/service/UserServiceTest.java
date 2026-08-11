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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private ChangePasswordRequest changePasswordRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();

        registerRequest.setFirstName("Hanzla");
        registerRequest.setLastName("Shehzad");
        registerRequest.setEmail("hanzla@gmail.com");
        registerRequest.setPhoneNumber("03001234567");
        registerRequest.setPassword("123456");


        loginRequest = new LoginRequest();

        loginRequest.setUsername("hanzla@gmail.com");
        loginRequest.setPassword("123456");


        changePasswordRequest = new ChangePasswordRequest();

        changePasswordRequest.setOldPassword("123456");
        changePasswordRequest.setNewPassword("654321");
        changePasswordRequest.setConfirmPassword("654321");


        user = User.builder()
                .id(1L)
                .firstName("Hanzla")
                .lastName("Shehzad")
                .email("hanzla@gmail.com")
                .phoneNumber("03001234567")
                .password("encodedPassword")
                .build();
    }


    @Test
    void register_ShouldRegisterSuccessfully() {

        when(userRepository.existsByEmail(
                registerRequest.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByPhoneNumber(
                registerRequest.getPhoneNumber()))
                .thenReturn(false);

        when(passwordEncoder.encode(
                registerRequest.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);


        RegisterResponse response =
                userService.register(registerRequest);


        assertNotNull(response);

        assertEquals(
                "Hanzla",
                response.getFirstName());

        assertEquals(
                "Registration Successful",
                response.getMessage());

        verify(userRepository)
                .save(any(User.class));
    }


    @Test
    void register_ShouldThrowEmailAlreadyExistsException() {

        when(userRepository.existsByEmail(
                registerRequest.getEmail()))
                .thenReturn(true);


        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(registerRequest));


        verify(userRepository, never())
                .save(any());
    }


    @Test
    void register_ShouldThrowPhoneAlreadyExistsException() {

        when(userRepository.existsByEmail(
                registerRequest.getEmail()))
                .thenReturn(false);

        when(userRepository.existsByPhoneNumber(
                registerRequest.getPhoneNumber()))
                .thenReturn(true);


        assertThrows(
                PhoneNumberAlreadyExistsException.class,
                () -> userService.register(registerRequest));


        verify(userRepository, never())
                .save(any());
    }


    @Test
    void authenticate_ShouldAuthenticateWithEmail() {

        when(userRepository.findByEmail(
                "hanzla@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(true);


        User authenticatedUser =
                userService.authenticate(loginRequest);


        assertNotNull(authenticatedUser);

        assertEquals(
                1L,
                authenticatedUser.getId());

        assertEquals(
                "Hanzla",
                authenticatedUser.getFirstName());


        verify(userRepository)
                .findByEmail("hanzla@gmail.com");
    }


    @Test
    void authenticate_ShouldAuthenticateWithPhone() {

        loginRequest.setUsername("03001234567");


        when(userRepository.findByEmail(
                "03001234567"))
                .thenReturn(Optional.empty());

        when(userRepository.findByPhoneNumber(
                "03001234567"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(true);


        User authenticatedUser =
                userService.authenticate(loginRequest);


        assertNotNull(authenticatedUser);

        assertEquals(
                1L,
                authenticatedUser.getId());


        verify(userRepository)
                .findByPhoneNumber("03001234567");
    }


    @Test
    void authenticate_ShouldThrowInvalidCredentials_WhenUserNotFound() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(userRepository.findByPhoneNumber(anyString()))
                .thenReturn(Optional.empty());


        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.authenticate(loginRequest));
    }


    @Test
    void authenticate_ShouldThrowInvalidCredentials_WhenPasswordWrong() {

        when(userRepository.findByEmail(
                loginRequest.getUsername()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(false);


        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.authenticate(loginRequest));
    }


    @Test
    void login_ShouldReturnLoginResponse() {

        when(userRepository.findByEmail(
                loginRequest.getUsername()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(true);


        LoginResponse response =
                userService.login(loginRequest);


        assertNotNull(response);

        assertEquals(
                1L,
                response.getId());

        assertEquals(
                "Hanzla",
                response.getFirstName());

        assertEquals(
                "Login Successful",
                response.getMessage());
    }


    @Test
    void changePassword_ShouldChangePasswordSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                changePasswordRequest.getOldPassword(),
                user.getPassword()))
                .thenReturn(true);

        when(passwordEncoder.encode(
                changePasswordRequest.getNewPassword()))
                .thenReturn("newEncodedPassword");


        String response =
                userService.changePassword(
                        1L,
                        changePasswordRequest);


        assertEquals(
                "Password changed successfully",
                response);


        verify(passwordEncoder)
                .encode("654321");

        verify(userRepository)
                .save(user);
    }


    @Test
    void changePassword_ShouldThrowUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                UserNotFoundException.class,
                () -> userService.changePassword(
                        1L,
                        changePasswordRequest));
    }


    @Test
    void changePassword_ShouldThrowInvalidPassword_WhenOldPasswordWrong() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                changePasswordRequest.getOldPassword(),
                user.getPassword()))
                .thenReturn(false);


        assertThrows(
                InvalidPasswordException.class,
                () -> userService.changePassword(
                        1L,
                        changePasswordRequest));


        verify(userRepository, never())
                .save(any(User.class));
    }


    @Test
    void changePassword_ShouldThrowInvalidPassword_WhenPasswordsDoNotMatch() {

        changePasswordRequest
                .setConfirmPassword("differentPassword");


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                changePasswordRequest.getOldPassword(),
                user.getPassword()))
                .thenReturn(true);


        assertThrows(
                InvalidPasswordException.class,
                () -> userService.changePassword(
                        1L,
                        changePasswordRequest));


        verify(userRepository, never())
                .save(any(User.class));
    }
}