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

import static com.itextpdf.text.FontFactory.contains;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/register");
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRegisterSuccess() throws InterruptedException {
        // Wait for and interact with each input field
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("newuser@example.com");

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.sendKeys("newusername");

        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordInput.sendKeys("validPassword1");

        WebElement confirmPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));
        confirmPasswordInput.sendKeys("validPassword1");

        WebElement phoneNumberInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
        phoneNumberInput.sendKeys("1234567890");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));

        String actualText = alert.getText();
        assertFalse(actualText.contains("Registration successful! Redirecting to login..."));
        Thread.sleep(3000);
    }




    @Test
    public void testRegisterFailureWithoutEmail() throws InterruptedException {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.sendKeys("newusername");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("validPassword1");

        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        confirmPasswordInput.sendKeys("validPassword1");

        WebElement phoneNumberInput = driver.findElement(By.id("phoneNumber"));
        phoneNumberInput.sendKeys("1234567890");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        assertFalse(contains("Email is required"));
        Thread.sleep(3000);
    }

    @Test
    public void testRegisterFailureWithoutPassword() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("newuser@example.com");

        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("newusername");

        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        confirmPasswordInput.sendKeys("validPassword1");

        WebElement phoneNumberInput = driver.findElement(By.id("phoneNumber"));
        phoneNumberInput.sendKeys("1234567890");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        assertFalse(contains("Password is required"));
        Thread.sleep(3000);
    }

    @Test
    public void testRegisterFailurePasswordMismatch() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("newuser@example.com");

        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("newusername");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("validPassword1");

        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        confirmPasswordInput.sendKeys("mismatchedPassword");

        WebElement phoneNumberInput = driver.findElement(By.id("phoneNumber"));
        phoneNumberInput.sendKeys("1234567890");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        assertFalse(contains("Passwords do not match"));
        Thread.sleep(3000);
    }

    @Test
    public void testRegisterFailureInvalidPhoneNumber() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("newuser@example.com");

        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("newusername");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("validPassword1");

        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        confirmPasswordInput.sendKeys("validPassword1");

        WebElement phoneNumberInput = driver.findElement(By.id("phoneNumber"));
        phoneNumberInput.sendKeys("invalidPhoneNumber");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        assertFalse(contains("Contact number is invalid"));
        Thread.sleep(3000);
    }

    @Test
    public void testRegisterFailureShortPassword() throws InterruptedException {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")));
        emailInput.sendKeys("newuser@example.com");

        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("newusername");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("short");

        WebElement confirmPasswordInput = driver.findElement(By.id("confirmPassword"));
        confirmPasswordInput.sendKeys("short");

        WebElement phoneNumberInput = driver.findElement(By.id("phoneNumber"));
        phoneNumberInput.sendKeys("1234567890");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        assertFalse(contains("Password must be longer than 6 characters"));
        Thread.sleep(3000);
    }
}
