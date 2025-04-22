package Pages;

import Utilities.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.apache.logging.log4j.LogManager.*;

/**
 * Base page class containing common functionality for all page objects
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected CookieBanner cookieBanner;
    protected JavascriptExecutor js;
    protected ScreenshotUtils screenshotUtils;
    private static final Logger log = getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * Constructor for BasePage
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        if (driver == null) {
            log.error("Driver instance cannot be null");
            throw new IllegalArgumentException("Driver instance cannot be null");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.cookieBanner = new CookieBanner(driver);
        log.info("Base page initialized");
        this.js = (JavascriptExecutor) driver;
        this.screenshotUtils = new ScreenshotUtils(driver);


    }

    /**
     * Waits for element to be clickable
     * @param element WebElement to wait for
     * @return WebElement that is now clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        try {
            log.info("Waiting for element to be clickable");
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            log.error("Element not clickable: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Waits for element presence and captures screenshot
     * @param element WebElement to wait for
     * @param elementName Name of element for screenshot
     */
    protected void waitForElementPresence(WebElement element, String elementName) {
        try {
            log.info("Waiting for element presence: " + elementName);

            // Wait for element visibility
            wait.until(ExpectedConditions.visibilityOf(element));

            // Capture screenshot with highlight
            screenshotUtils.captureElementScreenshot(element, elementName);

        } catch (Exception e) {
            log.error("Element not present: " + elementName + " - " + e.getMessage());
            throw e;
        }
    }



    /**
     * Handles cookie consent banner if present on the page
     */
    protected void handleCookieConsent() {
        if (cookieBanner.isBannerDisplayed()) {
            log.info("Handling cookie consent");
            try {
                cookieBanner.acceptAllCookies();
            } catch (Exception e) {
                log.error("Failed to handle cookie consent: " + e.getMessage());
                throw e;
            }
        }
    }


    /**
     * Checks if page is loaded
     * @return true if page is loaded, false otherwise
     */
    public boolean isPageLoaded() {
        try {
            boolean loaded = driver.getCurrentUrl() != null && !driver.getCurrentUrl().isEmpty();
            log.info("Page load check completed");
            return loaded;
        } catch (Exception e) {
            log.error("Error checking page load status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the current page URL
     * @return current page URL
     */
    public String getCurrentUrl() {
        try {
            String url = driver.getCurrentUrl();
            log.info("Current URL: " + url);
            return url;
        } catch (Exception e) {
            log.error("Error getting current URL: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the current page title
     * @return current page title
     */
    public String getPageTitle() {
        try {
            String title = driver.getTitle();
            log.info("Page title: " + title);
            return title;
        } catch (Exception e) {
            log.error("Error getting page title: " + e.getMessage());
            throw e;
        }
    }
}
