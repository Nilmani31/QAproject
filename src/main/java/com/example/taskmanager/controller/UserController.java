package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
        this.passwordEncoder=new BCryptPasswordEncoder();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        User user = userService.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Login successful → redirect to dashboard/tasks page
            return "redirect:/tasks"; // replace with your actual page
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        try {
            userService.save(newUser);
        } catch (Exception e) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        return "redirect:/users/login";
    }
}
