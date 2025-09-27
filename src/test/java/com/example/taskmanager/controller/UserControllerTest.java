package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    Model model;

    @InjectMocks
    UserController userController;

    @Test
    void testLoginPage() {
        String viewName = userController.loginPage();
        assertEquals("login", viewName);
    }

    @Test
    void testRegisterPage() {
        String viewName = userController.registerPage();
        assertEquals("register", viewName);
    }

    @Test
    void testRegisterSuccess() {
        // Mock save() to return the saved user
        User mockUser = new User();
        mockUser.setUsername("john");
        mockUser.setPassword("password123");

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(mockUser);

        String viewName = userController.register("john", "password123", model);

        assertEquals("redirect:/users/login", viewName);
    }

    @Test
    void testRegisterUsernameExists() throws Exception {
        Mockito.doThrow(new RuntimeException("Username exists"))
                .when(userService).save(Mockito.any(User.class));

        String viewName = userController.register("john", "password123", model);

        assertEquals("register", viewName);
        Mockito.verify(model).addAttribute("error", "Username already exists!");
    }
}
