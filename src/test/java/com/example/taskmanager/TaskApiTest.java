package com.example.taskmanager;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskApiTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    SessionFilter session;

    @BeforeEach
    void setUpUserAndLogin() {
        // Ensure test user exists
        userRepo.findByUsername("chamsha").orElseGet(() -> {
            User nu = new User();
            nu.setUsername("chamsha");
            nu.setPassword("1234");
            return userRepo.save(nu);
        });

        RestAssured.port = port;
        session = new SessionFilter();
    }

    @Test
    void testAddTaskApi() {
        // Create a task
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test task description");

        given()
                .filter(session)
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post("/api/tasks") // Updated path
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON) // Response is JSON now
                .body("message", equalTo("Task created successfully")); // Match JSON key
    }

    @Test
    void testGetTasksApi() {
        // Create a task first
        Task task = new Task();
        task.setTitle("Another Task");
        task.setDescription("Another task description");

        given()
                .filter(session)
                .contentType(ContentType.JSON)
                .body(task)
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(201)
                .body("message", equalTo("Task created successfully"));

        // Fetch tasks
        Response resp = given()
                .filter(session)
                .when()
                .get("/api/tasks") // Updated path
                .thenReturn();

        assertThat(resp.statusCode(), is(200));
        assertThat(resp.asString(), containsString("Another Task"));
    }
}
