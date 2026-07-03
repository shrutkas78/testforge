package com.testforge.pages.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the saucedemo products (inventory) page.
 */
public class ProductsPage extends BasePage {

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(pageTitle) && "Products".equals(getText(pageTitle));
    }

    public int getProductCount() {
        waitForVisible(inventoryItems);
        return driver.findElements(inventoryItems).size();
    }

    public ProductsPage addProductToCart(String productName) {
        String id = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(id));
        return this;
    }

    public int getCartCount() {
        return isDisplayed(cartBadge) ? Integer.parseInt(getText(cartBadge)) : 0;
    }
}
