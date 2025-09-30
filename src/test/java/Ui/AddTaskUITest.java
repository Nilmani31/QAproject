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

        // Always run headless in CI
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        // Increase wait time for CI
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testAddTask() {
        try {
            driver.get("http://localhost:8080/users/login");

            // Ensure login page fully loaded
            wait.until(ExpectedConditions.urlContains("/users/login"));

            // Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Kaveesha");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys("1234");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Wait until redirected to task page
            wait.until(ExpectedConditions.urlMatches(".*tasks.*"));

            // Add a task
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title"))).sendKeys("Do Home");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("description"))).sendKeys("homework");
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();

            // Wait for task to appear in the list
            By taskItem = By.xpath("//ul[contains(@class,'list-group')]/li[contains(., 'Do Home')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(taskItem));

            // Verify task is displayed
            assertTrue(driver.findElement(taskItem).getText().contains("Do Home"));

        } catch (Exception e) {
            // Optional: print page source for debugging in CI
            System.out.println("Test failed. Page source:\n" + driver.getPageSource());
            throw e;
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
