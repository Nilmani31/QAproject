package com.example.taskmanager.controller;

import com.example.taskmanager.controller.TaskController;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskService taskService;

    @Mock
    UserRepository userRepository;

    @Mock
    Model model;

    @Mock
    Principal principal;
    @Mock
    BindingResult bindingResult;

    @InjectMocks
    TaskController taskController;

    @Test
    void testShowTasks() {
        User testUser = new User();
        testUser.setUsername("testuser");

        Mockito.when(principal.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        Mockito.when(taskService.getTasksByUser(testUser)).thenReturn(List.of());

        String viewName = taskController.showTasks(model, principal);

        assertEquals("tasks", viewName);
    }

    @Test
    void testAddTaskSuccess() {
        User testUser = new User();
        testUser.setUsername("testuser");

        Task task = new Task();
        task.setTitle("New Task");

        Mockito.when(principal.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        Mockito.when(taskService.existsByTitleAndUser("New Task", testUser)).thenReturn(false);

        // Mock BindingResult

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        String result = taskController.addTask(task, bindingResult, principal, model);

        assertEquals("redirect:/tasks", result);
        Mockito.verify(taskService).saveTask(task, testUser);
    }

}
