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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/reservation");
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testReservationSuccess() throws InterruptedException {

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nameInput.sendKeys("John Doe");

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("johndoe@example.com");

        WebElement phoneInput = driver.findElement(By.id("phoneNumber"));
        phoneInput.sendKeys("1234567890");

        WebElement branchSelectElement = driver.findElement(By.id("branch"));
        Select branchSelect = new Select(branchSelectElement);
        branchSelect.selectByVisibleText("Colombo Main Branch");

        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys("2024-09-10");

        WebElement timeInput = driver.findElement(By.id("time"));
        timeInput.sendKeys("08:00", "PM");

        WebElement personsInput = driver.findElement(By.id("persons"));
        personsInput.sendKeys("4");

        WebElement requestInput = driver.findElement(By.id("request"));
        requestInput.sendKeys("A window seat, please");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submitReservation")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swal2-popup")));


        String actualText = alert.getText();
        assertTrue(actualText.contains("Reservation submitted successfully!"));

        Thread.sleep(3000);
    }

    @Test
    public void testReservationFailureWithEmptyFields() throws InterruptedException {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submitReservation")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        Thread.sleep(3000);
    }

}

