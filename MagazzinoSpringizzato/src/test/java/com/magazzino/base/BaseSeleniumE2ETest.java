package com.magazzino.base;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.magazzino.util.ScreenshotService;

public abstract class BaseSeleniumE2ETest {

    protected final static String GECKODRIVER_PATH = "C:\\Users\\loren\\Downloads\\geckodriver-v0.34.0-win32\\geckodriver.exe";
    protected String BASE_URL = "http://localhost:8080/all";
    protected WebDriver driver;
    protected WebDriverWait waitDriver;

    private ScreenshotService screenshotService;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.gecko.driver", GECKODRIVER_PATH);
    }

    @BeforeEach
    void setUpBeforeEach() {
        driver = new FirefoxDriver(new FirefoxOptions().setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe"));
        screenshotService = new ScreenshotService();
        Duration d = Duration.ofSeconds(5);
        waitDriver = new WebDriverWait(driver, d);
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    protected void captureScreenshot() {
        screenshotService.captureScreenShot(driver, this.getClass());
    }
}
