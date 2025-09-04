package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    public String showTasks(Model model, HttpSession session) {
        // Get the logged-in user from session
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return "redirect:/login"; // not logged in → go to login page
        }

        User user = (User) userObj;

        List<Task> tasks = taskService.getTasksByUser(user);

        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());

        return "tasks";
    }

    @PostMapping
    public String addTask(@Valid @ModelAttribute("task") Task task, BindingResult result, HttpSession session, Model model) {

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            // If validation fails, show the form again with errors
            User user = (User) userObj;
            List<Task> tasks = taskService.getTasksByUser(user);
            model.addAttribute("tasks", tasks);
            return "tasks";
        }

        User user = (User) userObj;
        taskService.saveTask(task, user);

        return "redirect:/tasks"; // After adding, reload the tasks page
    }




    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return "redirect:/login";
        }

        User user = (User) userObj;
        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }

}
