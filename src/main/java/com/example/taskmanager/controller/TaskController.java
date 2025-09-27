package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showTasks(Model model, Principal principal) {
        // Get logged-in username
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks = taskService.getTasksByUser(user);

        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());

        return "tasks";
    }

    @PostMapping
    public String addTask(@Valid @ModelAttribute("task") Task task, BindingResult result,
                          Principal principal, Model model) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (result.hasErrors()) {
            List<Task> tasks = taskService.getTasksByUser(user);
            model.addAttribute("tasks", tasks);
            return "tasks";
        }

        taskService.saveTask(task, user);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }
}
