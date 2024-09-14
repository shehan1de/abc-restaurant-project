package com.abcRestaurant.abcRestaurant.Automation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FeedbackTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFeedbackSuccess() throws InterruptedException {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nameInput.sendKeys("Test name");

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("test@example.com");

        WebElement phoneInput = driver.findElement(By.id("phoneNumber"));
        phoneInput.sendKeys("1234567890");

        WebElement subjectInput = driver.findElement(By.id("subject"));
        subjectInput.sendKeys("Feedback Subject");

        WebElement messageInput = driver.findElement(By.id("message"));
        messageInput.sendKeys("This is a feedback message.");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submitFeedback")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Feedback submitted successfully!"));

        Thread.sleep(3000);
    }


    @Test
    public void testFeedbackFailureWithEmptyFields() throws InterruptedException {
        // Scroll to the bottom of the page
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))); // Adjust XPath if needed
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        Thread.sleep(3000);
    }

}
