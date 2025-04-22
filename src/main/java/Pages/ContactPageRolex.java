package Pages;

import Constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object class representing the Contact Page for Rolex website.
 * This class contains all the web elements and methods specific to the Rolex contact page.
 */
public class ContactPageRolex extends BasePage {

    // Contact information fields
    private static final Logger log = LogManager.getLogger(ContactPageRolex.class);

    /**
     * Constructor for ContactPageRolex
     * Initializes the page elements using PageFactory from the parent class
     *
     * @param driver WebDriver instance to be used for this page
     */
    public ContactPageRolex(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        log.info("ContactPageRolex initialized");
    }

    /**
     * Address element with multiple locator strategies
     */
    @FindAll({
            @FindBy(css = "div[class*='address']"),
            @FindBy(css = "p[class*='body']"),
            @FindBy(css = "span[class*='address']"),
            @FindBy(xpath = "//div[contains(@class, 'location')]//p"),
            @FindBy(xpath = "//div[contains(@class, 'contact-info')]//address")
    })
    private WebElement addressElement;

    /**
     * Phone number element with multiple locator strategies
     */
    @FindAll({
            @FindBy(css = "a[href*='tel:']"),
            @FindBy(css = "span[class*='phone']"),
            @FindBy(css = "div[class*='phone']"),
            @FindBy(xpath = "//p[contains(@class, 'phone')]"),
            @FindBy(xpath = "//div[contains(@class, 'contact')]//a[contains(@href, 'tel')]")
    })
    private WebElement numberElement;

    /**
     * Opening hours element with multiple locator strategies
     */
    @FindAll({
            @FindBy(css = "div[class*='hours']"),
            @FindBy(css = "p[class*='hours']"),
            @FindBy(css = "div[class*='timing']"),
            @FindBy(xpath = "//div[contains(@class, 'store-hours')]"),
            @FindBy(xpath = "//div[contains(@class, 'opening-times')]")
    })
    private WebElement openingHoursElement;

    /**
     * Gets the address text from the page
     * @return String containing address
     */
    public String getAddress() {
        try {
            log.info("Attempting to get address");
            String address = addressElement.getText();
            log.info("Retrieved address: {}", address);
            return address;
        } catch (Exception e) {
            log.error("Failed to get address: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the phone number from the page
     * @return String containing phone number
     */
    public String getPhoneNumber() {
        try {
            log.info("Attempting to get phone number");
            String phoneNumber = numberElement.getText();
            log.info("Retrieved phone number: {} ", phoneNumber);
            return phoneNumber;
        } catch (Exception e) {
            log.error("Failed to get phone number: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the opening hours from the page
     * @return String containing opening hours
     */
    public String getOpeningHours() {
        try {
            log.info("Attempting to get opening hours");
            String hours = openingHoursElement.getText();
            log.info("Retrieved opening hours: {}", hours);
            return hours;
        } catch (Exception e) {
            log.error("Failed to get opening hours: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if all contact information elements are displayed
     * @return boolean indicating if all elements are displayed
     */
    public boolean areContactDetailsDisplayed() {
        try {
            log.info("Checking if all contact details are displayed");
            return waitForContactDetails();
        } catch (Exception e) {
            log.error("Error checking contact details display: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Waits for all contact information elements to be visible
     * @return boolean indicating if all elements became visible
     */
    public boolean waitForContactDetails() {
        try {
            log.info("Waiting for contact details to be visible");
            waitForElementPresence(addressElement, Constants.Elements.ROLEX_ADDRESS);
            waitForElementPresence(numberElement, Constants.Elements.ROLEX_PHONE_NUMBER);
            waitForElementPresence(openingHoursElement, Constants.Elements.ROLEX_OPENING_HOURS);
            log.info("All contact details are now visible");
            return true;
        } catch (Exception e) {
            log.error("Failed waiting for contact details: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Handles cookie consent for the contact page
     */
    public void handleContactPageCookies() {
        try {
            log.info("Handling contact page cookies");
            handleCookieConsent();
            log.info("Successfully handled cookies");
        } catch (Exception e) {
            log.error("Failed to handle cookies: {}", e.getMessage());
            throw e;
        }
    }

}
