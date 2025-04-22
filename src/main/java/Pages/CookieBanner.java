package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Handles the cookie consent banner interactions in the web application.
 * This class manages all cookie-related UI elements and user interactions.
 */
public class CookieBanner {
    private static final Logger log = LogManager.getLogger(CookieBanner.class);
    private static final int COOKIE_TIMEOUT = 10;
    private final WebDriverWait cookieWait;


    // Page elements using PageFactory annotations
    @FindAll({
            @FindBy(css = "*[class*='banner']"),
            @FindBy(css = "*[role*='dialog']"),
            @FindBy(css = "#cookie-consent"),
    })
    private WebElement bannerContainer;


    @FindAll({
            @FindBy(xpath = "//*[contains(text(), 'Accept')]"),
            @FindBy(id = "cc-allow-button"),
            @FindBy(css = "*[id*='accept']"),
            @FindBy(css = "*[data-testid*='accept']"),
    })
    private WebElement acceptAllButton;

    @FindAll({
            @FindBy(id = "cc-deny-button"),
            @FindBy(css = "*[id*='deny']"),
            @FindBy(css = "*[id*='reject']"),
            @FindBy(xpath = "//*[contains(text(), 'Decline')]"),
            @FindBy(xpath = "//*[contains(text(), 'Reject')]")
    })
    private WebElement rejectAllButton;

    @FindAll({
            @FindBy(css = "*[data-testid*='more']"),
            @FindBy(css = ".customize-cookies-btn"),
            @FindBy(xpath = "//button[contains(text(), 'Customize')]")
    })
    private WebElement customizeButton;


    /**
     * Constructs a new CookieBanner instance.
     *
     * @param driver WebDriver instance to interact with the browser
     * @throws IllegalArgumentException if driver is null
     */
    public CookieBanner(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.cookieWait = new WebDriverWait(driver, Duration.ofSeconds(COOKIE_TIMEOUT));
        log.info("Cookie Banner initialized");
    }

    /**
     * Waits for banner to be visible
     * @return true if banner is visible, false if timeout occurs
     */
    public boolean waitForBanner() {
        try {
            cookieWait.until(ExpectedConditions.visibilityOf(bannerContainer));
            log.info("Cookie banner is visible");
            return true;
        } catch (Exception e) {
            log.info("Cookie banner not present or already handled");
            return false;
        }
    }

    /**
     * Checks if cookie banner is currently displayed
     * @return true if banner is displayed
     */
    public boolean isBannerDisplayed() {
        try {
            boolean isDisplayed = bannerContainer.isDisplayed();
            log.debug("Cookie banner display status: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            log.debug("Cookie banner not found");
            return false;
        }
    }

    /**
     * Accepts all cookies
     */
    public void acceptAllCookies() {
        try {
            if (waitForBanner()) {
                log.info("Attempting to accept all cookies");
                cookieWait.until(ExpectedConditions.elementToBeClickable(acceptAllButton));
                acceptAllButton.click();
                log.info("Accepted all cookies successfully");
                waitForBannerToDisappear();
            }
        } catch (Exception e) {
            log.error("Failed to accept cookies: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Rejects all cookies
     */
    public void rejectAllCookies() {
        try {
            if (waitForBanner()) {
                log.info("Attempting to reject all cookies");
                cookieWait.until(ExpectedConditions.elementToBeClickable(rejectAllButton));
                rejectAllButton.click();
                log.info("Rejected all cookies successfully");
                waitForBannerToDisappear();
            }
        } catch (Exception e) {
            log.error("Failed to reject cookies: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Waits for banner to disappear after action
     */
    private void waitForBannerToDisappear() {
        try {
            cookieWait.until(ExpectedConditions.invisibilityOf(bannerContainer));
            log.info("Cookie banner closed");
        } catch (Exception e) {
            acceptAllCookies();
            log.warn("Cookie banner did not close as expected: {}", e.getMessage());
        }
    }
}
