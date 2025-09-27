package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskApiController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addTask(@RequestBody Task task) {
        // Use the test user "Kaveesha"
        User user = userRepository.findByUsername("Kaveesha")
                .orElseThrow(() -> new RuntimeException("User not found"));

        taskService.saveTask(task, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Task created successfully"));
    }

    @GetMapping
    public List<Task> getTasks() {
        // Use the test user "Kaveesha"
        User user = userRepository.findByUsername("Kaveesha")
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskService.getTasksByUser(user);
    }
}
