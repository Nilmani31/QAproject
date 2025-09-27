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
    private static final String ACTION_1 = "User not found";
    private static final String ACTION_2 = "tasks";

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showTasks(Model model, Principal principal) {
        // Get logged-in username
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(ACTION_1));

        List<Task> tasks = taskService.getTasksByUser(user);

        model.addAttribute(ACTION_2, tasks);
        model.addAttribute("task", new Task());

        return ACTION_2;
    }

    @PostMapping
    public String addTask(@Valid @ModelAttribute("task") Task task, BindingResult result,
                          Principal principal, Model model) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(ACTION_1));

        if (result.hasErrors()) {
            List<Task> tasks = taskService.getTasksByUser(user);
            model.addAttribute(ACTION_2, tasks);
            return ACTION_2;
        }
        boolean exists = taskService.existsByTitleAndUser(task.getTitle(), user);
        if (exists) {
            // Add error message instead of saving duplicate
            result.rejectValue("title", "duplicate", "Task already exists");
            List<Task> tasks = taskService.getTasksByUser(user);
            model.addAttribute(ACTION_2, tasks);
            return ACTION_2;  // return to page instead of redirect
        }


        taskService.saveTask(task, user);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(ACTION_1));

        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }
}
