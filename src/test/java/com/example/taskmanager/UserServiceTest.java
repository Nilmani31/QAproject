package com.example.taskmanager;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class); // create mock
        userService = new UserService(userRepo); // inject mock
    }

    @Test
    void testAuthenticateSuccess() {
        User testUser = new User();
        testUser.setUsername("chamsha");
        testUser.setPassword("1234");

        when(userRepo.findByUsername("chamsha")).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.authenticate("chamsha", "1234");
        assertTrue(user.isPresent(), "User should be authenticated");
    }

    @Test
    void testAuthenticateFail() {
        when(userRepo.findByUsername("wrong")).thenReturn(Optional.empty());

        Optional<User> user = userService.authenticate("wrong", "pass");
        assertTrue(user.isEmpty(), "Invalid credentials should fail");
    }
}
