package com.tenpearls.contactmanagementsystem.repository;

import com.tenpearls.contactmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_ShouldReturnUser() {

        User user = User.builder()
                .firstName("Hanzla")
                .lastName("Shehzad")
                .email("repo1001@gmail.com")
                .phoneNumber("03990000001")
                .password("123456")
                .build();

        userRepository.save(user);

        assertTrue(
                userRepository.findByEmail("repo1001@gmail.com")
                        .isPresent());
    }

    @Test
    void findByPhoneNumber_ShouldReturnUser() {

        User user = User.builder()
                .firstName("Ali")
                .lastName("Khan")
                .email("repo1002@gmail.com")
                .phoneNumber("03990000002")
                .password("123456")
                .build();

        userRepository.save(user);

        assertTrue(
                userRepository.findByPhoneNumber("03990000002")
                        .isPresent());
    }

    @Test
    void existsByEmail_ShouldReturnTrue() {

        User user = User.builder()
                .firstName("Ahmed")
                .lastName("Raza")
                .email("repo1003@gmail.com")
                .phoneNumber("03990000003")
                .password("123456")
                .build();

        userRepository.save(user);

        assertTrue(
                userRepository.existsByEmail("repo1003@gmail.com"));
    }

    @Test
    void existsByPhoneNumber_ShouldReturnTrue() {

        User user = User.builder()
                .firstName("Hasnain")
                .lastName("Shehzad")
                .email("repo1004@gmail.com")
                .phoneNumber("03990000004")
                .password("123456")
                .build();

        userRepository.save(user);

        assertTrue(
                userRepository.existsByPhoneNumber("03990000004"));
    }
}