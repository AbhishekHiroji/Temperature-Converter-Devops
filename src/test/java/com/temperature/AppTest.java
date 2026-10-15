
package com.temperature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.get("file:///" + System.getProperty("user.dir")
                .replace("\\", "/")
                + "/src/main/resources/index.html");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void celsiusToFahrenheitConversion() {
        driver.findElement(By.id("temperature")).sendKeys("100");
        driver.findElement(By.id("convert")).click();

        String result = driver.findElement(By.id("result")).getText();
        assertEquals("Result: 212 °F", result);
    }

    @Test
    void fahrenheitToCelsiusConversion() {
        driver.findElement(By.id("temperature")).sendKeys("32");

        driver.findElement(By.id("conversion"))
                .findElement(By.cssSelector("option[value='FtoC']"))
                .click();

        driver.findElement(By.id("convert")).click();

        String result = driver.findElement(By.id("result")).getText();
        assertEquals("Result: 0 °C", result);
    }

    @Test
    void emptyInputShowsError() {
        driver.findElement(By.id("convert")).click();

        String result = driver.findElement(By.id("result")).getText();
        assertTrue(result.contains("Please enter a temperature"));
    }
}
