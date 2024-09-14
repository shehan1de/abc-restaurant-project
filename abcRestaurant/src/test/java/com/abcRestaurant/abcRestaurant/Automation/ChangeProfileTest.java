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

public class ChangeProfileTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login to the application
        driver.get("http://localhost:3000/login");
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("linukaofficial4@gmail.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("shehan12345");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Login successful! Now you are logged in as a"));

        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPhoneNumberUpdateSuccess() throws InterruptedException {
        // Navigate to ChangeProfile page after logging in
        driver.get("http://localhost:3000/change-profile");

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        nameInput.clear();
        nameInput.sendKeys("NewTestUsername");

        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
        phoneInput.clear();
        phoneInput.sendKeys("0987654321");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("updateProfile")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-info")));

        String actualText = successMessage.getText();
        assertFalse(actualText.contains("Profile Updated Successfully"));

        Thread.sleep(3000);
    }
}
