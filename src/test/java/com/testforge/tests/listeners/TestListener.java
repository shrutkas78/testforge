package com.testforge.tests.listeners;

import com.testforge.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * Attaches a screenshot to the Allure report whenever a UI test fails.
 * Wired in via the <listeners> block in the TestNG suite XML.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(
                    "Failure screenshot: " + result.getName(),
                    new ByteArrayInputStream(screenshot));
        } catch (IllegalStateException ignored) {
            // API tests have no driver — nothing to capture.
        }
    }
}
