package Ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
@Tag("Ui")

public class LoginUITest {

    @Test
    public void testLoginSuccess() {
        // Use manual EdgeDriver path
        System.setProperty("webdriver.edge.driver", "C:\\drivers\\msedgedriver.exe");

        WebDriver driver = new EdgeDriver();

        try {
            // Open login page
            driver.get("http://localhost:8080/users/login");

            // Fill in username and password
            driver.findElement(By.name("username")).sendKeys("Kaveesha");
            driver.findElement(By.name("password")).sendKeys("1234");

            // Click login button
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Check if redirected to tasks page
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains("/tasks"));
            assertTrue(driver.getCurrentUrl().contains("/tasks"), "User should be redirected to /tasks");



        } finally {
            // Close the browser after test
            driver.quit();
        }
    }
}
