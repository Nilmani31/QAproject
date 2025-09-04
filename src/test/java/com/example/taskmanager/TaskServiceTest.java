package com.example.taskmanager;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testSaveTask() {
        User user = new User();
        user.setId(1L);
        user.setUsername("chamsha");

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        // Mock repository save
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task savedTask = taskService.saveTask(task, user);

        // Verify that the task has the correct user
        assertEquals(user, savedTask.getUser());
        assertEquals("Test Task", savedTask.getTitle());

        // Verify repository interaction
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTasksByUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("chamsha");

        Task task1 = new Task("Task 1", "Desc 1");
        task1.setUser(user);
        Task task2 = new Task("Task 2", "Desc 2");
        task2.setUser(user);

        when(taskRepository.findByUser(user)).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getTasksByUser(user);

        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));

        verify(taskRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetTaskByIdFound() {
        Task task = new Task("Task 1", "Desc 1");
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(task, result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
        assertEquals("Task not found with id: 1", exception.getMessage());

        verify(taskRepository, times(1)).findById(1L);
    }
}
