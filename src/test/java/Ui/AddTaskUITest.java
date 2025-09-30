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

import java.time.Duration;
import java.util.List;

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
        options.addArguments("--headless=new"); // headless mode for CI
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(50)); // longer timeout
    }

    @Test
    public void testAddTask() {
        String taskTitle = "Do Home";
        String taskDescription = "homework";

        // 1️⃣ Login
        driver.get("http://localhost:" + port + "/users/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Kaveesha");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys("1234");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
        wait.until(ExpectedConditions.urlContains("/tasks"));

        // 2️⃣ Add task
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys(taskTitle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("description"))).sendKeys(taskDescription);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

        // 3️⃣ Verify task is added
        boolean taskFound = false;
        for (int i = 0; i < 10; i++) { // retry loop
            try {
                List<WebElement> tasks = driver.findElements(By.xpath("//ul[@class='list-group']/li//strong"));
                for (WebElement t : tasks) {
                    if (t.getText().equals(taskTitle)) {
                        taskFound = true;
                        break;
                    }
                }
                if (taskFound) break;
                Thread.sleep(1000); // wait a bit before retry
            } catch (StaleElementReferenceException | InterruptedException e) {
                // retry
            }
        }

        assertTrue(taskFound, "Task should appear in the task list");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
