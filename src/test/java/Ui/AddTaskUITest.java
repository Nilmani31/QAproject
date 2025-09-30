package Ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.example.taskmanager.TaskmanagerApplication.class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("Ui")
public class AddTaskUITest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    @Transactional
    public void prepareUser() {
        // Ensure the test user exists
        if (!userRepository.existsByUsername("Kaveesha")) {
            User user = new User();
            user.setUsername("Kaveesha");
            user.setPassword("1234");
            userRepository.save(user);
        }
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Headless CI-friendly options
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-first-run");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    @Test
    public void testAddTask() {
        try {
            driver.get("http://localhost:" + port + "/users/login");

            // Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Kaveesha");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys("1234");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            wait.until(ExpectedConditions.urlContains("/tasks"));

            // Add task
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Do Home");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("description"))).sendKeys("homework");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Wait for task to appear in the list
            By taskLocator = By.xpath("//ul[@class='list-group']/li//strong[text()='Do Home']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(taskLocator));

            assertTrue(driver.findElement(taskLocator).getText().contains("Do Home"));
        } catch (Exception e) {
            // Take a screenshot on failure
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(screenshot.toPath(), Path.of("AddTaskUITest_failure.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
