package com.abcRestaurant.abcRestaurant.Automation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginSuccess() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("linukaofficial4@gmail.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("shehan12345");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Login successful! Now you are logged in as a"));
        Thread.sleep(3000);
    }

    @Test
    public void testLoginFailure() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("wrong@example.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("wrongpassword");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Invalid Email or Password...Try Again"));
        Thread.sleep(3000);
    }

    @Test
    public void testLoginFailureInWrongPassword() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("linukaofficial4@gmail.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("wrong");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Password must be longer than 6 characters"));
        Thread.sleep(3000);
    }
    @Test
    public void testLoginFailureWithoutEmail() throws InterruptedException {

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("shehan12345");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Email is required"));
        Thread.sleep(3000);
    }

    @Test
    public void testLoginFailureWithoutPassword() throws InterruptedException {

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("linukaofficial4@gmail.com");

        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        submitButton.click();

        WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".swal2-popup")));
        String actualText = alert.getText();
        assertTrue(actualText.contains("Password is required"));
        Thread.sleep(3000);
    }

}
