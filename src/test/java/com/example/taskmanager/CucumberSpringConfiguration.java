package com.example.taskmanager;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {
    // This class can be empty
    // It just tells Cucumber to load Spring Boot context
}
