package com.testforge.tests.ui.selenium;

import com.testforge.driver.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all Selenium UI tests.
 * Fresh driver per test method = full isolation, no state leakage between tests.
 */
public abstract class BaseUiTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
