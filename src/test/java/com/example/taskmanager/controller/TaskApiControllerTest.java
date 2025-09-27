package com.example.taskmanager.controller;

import com.example.taskmanager.controller.TaskApiController;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskApiControllerTest {

    @Mock
    TaskService taskService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TaskApiController taskApiController;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddTask() {
        User testUser = new User();
        testUser.setUsername("Kaveesha");

        Task task = new Task();
        task.setTitle("Test Task");

        Mockito.when(userRepository.findByUsername("Kaveesha")).thenReturn(Optional.of(testUser));

        ResponseEntity<Map<String, String>> response = taskApiController.addTask(task);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Task created successfully", response.getBody().get("message"));
    }

    @Test
    void testGetTasks() {
        User testUser = new User();
        testUser.setUsername("Kaveesha");

        Task task1 = new Task();
        task1.setTitle("Task 1");

        Mockito.when(userRepository.findByUsername("Kaveesha")).thenReturn(Optional.of(testUser));
        Mockito.when(taskService.getTasksByUser(testUser)).thenReturn(List.of(task1));

        List<Task> tasks = taskApiController.getTasks();

        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Mockito.when(taskService.getAllTasks()).thenReturn(List.of(task1));

        List<Task> tasks = taskApiController.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }
}
