package com.example.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    // Proper relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key column
    private User user;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task() {}

}
