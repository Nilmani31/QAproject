package com.example.taskmanager;

import com.example.taskmanager.repository.UserRepository;
import io.cucumber.java.en.*;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.taskmanager.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskSteps {
    @Autowired private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    private User loggedUser;
    private Task addedTask;

    @Given("a user exists with username {string} and password {string}")
    public void user_exists(String username, String password) {

        userRepository.deleteByUsername(username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.save(user);
    }

    @When("the user logs in with username {string} and password {string}")
    public void user_logs_in(String username, String password) {
        Optional<User> user = userService.authenticate(username, password);
        assertTrue(user.isPresent());
        loggedUser = user.get();
    }

    @When("adds a task with title {string} and description {string}")
    public void adds_task(String title, String description) {
        Task task = new Task(title, description);
        addedTask = taskService.saveTask(task, loggedUser);
    }

    @Then("the task {string} should appear in the task list")
    public void task_should_appear(String title) {
        assertEquals(title, addedTask.getTitle());
    }
}
