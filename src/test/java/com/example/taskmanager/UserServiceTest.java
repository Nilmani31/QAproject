package com.example.taskmanager;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        userService = new UserService(userRepo);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        // Use proper username without quotes
        User testUser = new User();
        testUser.setUsername("Kaveesha");

        // Hash password with BCrypt (like real service)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        testUser.setPassword(encoder.encode("1234"));

        when(userRepo.findByUsername("Kaveesha")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername("Kaveesha");

        assertEquals("Kaveesha", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));

        // Optionally verify password matches
        assertTrue(encoder.matches("1234", userDetails.getPassword()));
    }


    @Test
    void testLoadUserByUsernameFail() {
        when(userRepo.findByUsername("wrong")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("wrong"));
    }
}
