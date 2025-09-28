package com.example.taskmanager.service;

import com.example.taskmanager.exception.UnauthorizedTaskAccessException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

   public Task saveTask(Task task, User user) {
       return Optional.ofNullable(task)
               .map(t -> {
                   t.setUser(Objects.requireNonNull(user, "User cannot be null"));
                   return taskRepository.save(t);
               })
               .orElseThrow(() -> new IllegalArgumentException("Task cannot be null"));
   }


    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }
    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));

        // Ensure task belongs to the logged-in user
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedTaskAccessException();
        }

        taskRepository.delete(task);
    }
    public boolean existsByTitleAndUser(String title, User user) {
        return taskRepository.existsByTitleAndUser(title, user);
    }


}
