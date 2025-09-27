package com.example.taskmanager.controller;


import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserApiController userApiController;

    @Test
    void testGetUserSuccess() {
        User testUser = new User();
        testUser.setUsername("john");

        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(testUser));

        Map<String, String> response = userApiController.getUser("john");

        assertEquals("john", response.get("username"));
    }

    @Test
    void testGetUserNotFound() {
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        try {
            userApiController.getUser("john");
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }
}
