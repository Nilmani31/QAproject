package Ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Ui")
public class AddTaskUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Updated headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Increase timeout for CI/CD
    }

    @Test
    public void testAddTask() {
        try {
            driver.get("http://localhost:8080/users/login");

            // Wait for login page
            wait.until(ExpectedConditions.urlContains("/users/login"));

            // Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Kaveesha");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys("1234");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Wait for redirect to tasks page
            wait.until(ExpectedConditions.urlMatches(".*tasks.*"));

            // Add a task
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Do Home");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("description"))).sendKeys("homework");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Wait for the task to appear using explicit wait for specific text
            By taskLocator = By.xpath("//ul[@class='list-group']/li[contains(.,'Do Home')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(taskLocator));

            // Verify
            assertTrue(driver.findElement(taskLocator).getText().contains("Do Home"));

        } catch (Exception e) {
            System.out.println("Test failed. Page source:\n" + driver.getPageSource());
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
