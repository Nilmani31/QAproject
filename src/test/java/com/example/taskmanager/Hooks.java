// com/example/taskmanager/Hooks.java
package com.example.taskmanager;

import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    @Before
    public void cleanDb() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }
}
