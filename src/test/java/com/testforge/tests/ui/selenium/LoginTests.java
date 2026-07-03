package com.testforge.tests.ui.selenium;

import com.testforge.driver.DriverFactory;
import com.testforge.pages.selenium.LoginPage;
import com.testforge.pages.selenium.ProductsPage;
import com.testforge.utils.DataReader;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseUiTest {

    @Test(groups = {"smoke"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Standard user can log in and lands on the products page")
    public void standardUserCanLogin() {
        ProductsPage products = new LoginPage(DriverFactory.getDriver())
                .open()
                .loginAs("standard_user", "secret_sauce");

        Assert.assertTrue(products.isLoaded(), "Products page should load after valid login");
        Assert.assertEquals(products.getProductCount(), 6, "Expected 6 products in inventory");
    }

    @Test(groups = {"smoke"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Locked-out user sees the correct error message")
    public void lockedOutUserSeesError() {
        LoginPage login = new LoginPage(DriverFactory.getDriver())
                .open()
                .loginExpectingError("locked_out_user", "secret_sauce");

        Assert.assertTrue(login.isErrorDisplayed(), "Error banner should be visible");
        Assert.assertTrue(login.getErrorMessage().contains("locked out"),
                "Error should mention the user is locked out");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return DataReader.readJsonAsDataProvider("login-data.json",
                "username", "password", "expectedError");
    }

    @Test(groups = {"regression"}, dataProvider = "invalidCredentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Invalid credential combinations show the right error (data-driven)")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        LoginPage login = new LoginPage(DriverFactory.getDriver())
                .open()
                .loginExpectingError(username, password);

        Assert.assertTrue(login.getErrorMessage().contains(expectedError),
                "Expected error containing: " + expectedError
                        + " but got: " + login.getErrorMessage());
    }
}
