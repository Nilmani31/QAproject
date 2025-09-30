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
public class LoginUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // WebDriverManager automatically downloads and sets up ChromeDriver
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options for better CI compatibility
        ChromeOptions options = new ChromeOptions();

        // Run in headless mode in CI environment for faster execution
        if (System.getenv("CI") != null) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        // Add additional options for stability
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSuccess() {
        try {
            // Open login page
            driver.get("http://localhost:8080/users/login");

            // Fill in username and password using explicit waits
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Kaveesha");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys("1234");

            // Click login button
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Check if redirected to tasks page
            wait.until(ExpectedConditions.urlContains("/tasks"));
            assertTrue(driver.getCurrentUrl().contains("/tasks"), "User should be redirected to /tasks");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}