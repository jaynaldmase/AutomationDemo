package Scripts;

import Pages.ContactPageRetailer;
import Pages.ContactPageRolex;
import Constants.Constants;
import Validations.AddressComplianceCheck;
import Validations.PhoneNumberComplianceCheck;
import Validations.OpeningHoursComplianceCheck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidateRetailerInformationTest extends BaseTest {
    private static final Logger log = LogManager.getLogger(ValidateRetailerInformationTest.class);
    private AddressComplianceCheck addressCheck;
    private PhoneNumberComplianceCheck phoneCheck;
    private OpeningHoursComplianceCheck hoursCheck;

    @Test(description = "Validate retailer information matches between Rolex and Retailer websites")
    public void validateRetailerInfo() {
        ContactPageRolex rolexPage;
        ContactPageRetailer retailerPage;

        // Initialize validation classes
        addressCheck = new AddressComplianceCheck();
        phoneCheck = new PhoneNumberComplianceCheck();
        hoursCheck = new OpeningHoursComplianceCheck();

        try {
            // Initialize page objects
            log.info("Initializing page objects");
            rolexPage = new ContactPageRolex(driver);
            retailerPage = new ContactPageRetailer(driver);

            // Navigate to Rolex website first and handle cookies
            log.info("Navigating to Rolex website");
            driver.get(urlProperties.getProperty(Constants.URLs.ROLEX_URL));
            rolexPage.handleContactPageCookies();

            // Wait for and verify Rolex contact details are displayed
            Assert.assertTrue(
                    rolexPage.areContactDetailsDisplayed(),
                    Constants.ErrorMessages.CONTACT_DETAILS_ERROR
            );

            // Capture Rolex information
            log.info("Capturing Rolex contact information");
            String rolexAddress = rolexPage.getAddress();
            String rolexPhone = rolexPage.getPhoneNumber();
            String rolexHours = rolexPage.getOpeningHours();

            // Navigate to retailer website and handle cookies
            log.info("Navigating to retailer website");
            driver.get(urlProperties.getProperty(Constants.URLs.RETAILER_URL));
            retailerPage.handleContactPageCookies();

            // Wait for and verify retailer contact details are displayed
            Assert.assertTrue(
                    retailerPage.areContactDetailsDisplayed(),
                    Constants.ErrorMessages.CONTACT_DETAILS_ERROR
            );

            // Capture retailer information
            log.info("Capturing retailer contact information");
            String retailerAddress = retailerPage.getAddress();
            String retailerPhone = retailerPage.getPhoneNumber();
            String retailerHours = retailerPage.getOpeningHours();

            // Validate Address
            log.info("Validating address compliance");
            boolean isAddressValid = addressCheck.isAddressCompliant(rolexAddress, retailerAddress);
            Assert.assertTrue(isAddressValid, Constants.ErrorMessages.ADDRESS_MISMATCH);

            // Validate Phone Number
            log.info("Validating phone number compliance");
            boolean isPhoneValid = phoneCheck.isPhoneNumberCompliant(rolexPhone, retailerPhone);
            Assert.assertTrue(isPhoneValid, Constants.ErrorMessages.PHONE_MISMATCH);

            // Validate Opening Hours
            log.info("Validating opening hours compliance");
            boolean isHoursValid = hoursCheck.isOpeningHoursCompliant(rolexHours, retailerHours);
            Assert.assertTrue(isHoursValid, Constants.ErrorMessages.HOURS_MISMATCH);

            // Log validation summary
            logValidationSummary(isAddressValid, isPhoneValid, isHoursValid);

        } catch (Exception e) {
            log.error("Test failed: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Logs validation summary
     */
    private void logValidationSummary(boolean addressValid, boolean phoneValid, boolean hoursValid) {
        log.info("\n=== Validation Summary ===");
        log.info("Address: " + (addressValid ? "✅ PASS" : "❌ FAIL"));
        log.info("Phone: " + (phoneValid ? "✅ PASS" : "❌ FAIL"));
        log.info("Hours: " + (hoursValid ? "✅ PASS" : "❌ FAIL"));
        log.info("Overall: " + ((addressValid && phoneValid && hoursValid) ? "✅ PASS" : "❌ FAIL"));
        log.info("=====================");
    }

    /**
     * Individual test for address validation
     */
    @Test(description = "Validate retailer address compliance")
    public void validateAddress() {
        try {
            ContactPageRolex rolexPage = new ContactPageRolex(driver);
            ContactPageRetailer retailerPage = new ContactPageRetailer(driver);
            addressCheck = new AddressComplianceCheck();

            String rolexAddress = rolexPage.getAddress();
            String retailerAddress = retailerPage.getAddress();

            boolean isValid = addressCheck.isAddressCompliant(rolexAddress, retailerAddress);
            Assert.assertTrue(isValid, "Address validation failed");
        } catch (Exception e) {
            log.error("Address validation test failed: " + e.getMessage());
            Assert.fail("Address validation test failed: " + e.getMessage());
        }
    }

    /**
     * Individual test for phone number validation
     */
    @Test(description = "Validate retailer phone number compliance")
    public void validatePhoneNumber() {
        try {
            ContactPageRolex rolexPage = new ContactPageRolex(driver);
            ContactPageRetailer retailerPage = new ContactPageRetailer(driver);
            phoneCheck = new PhoneNumberComplianceCheck();

            String rolexPhone = rolexPage.getPhoneNumber();
            String retailerPhone = retailerPage.getPhoneNumber();

            boolean isValid = phoneCheck.isPhoneNumberCompliant(rolexPhone, retailerPhone);
            Assert.assertTrue(isValid, "Phone number validation failed");
        } catch (Exception e) {
            log.error("Phone validation test failed: " + e.getMessage());
            Assert.fail("Phone validation test failed: " + e.getMessage());
        }
    }

    /**
     * Individual test for opening hours validation
     */
    @Test(description = "Validate retailer opening hours compliance")
    public void validateOpeningHours() {
        try {
            ContactPageRolex rolexPage = new ContactPageRolex(driver);
            ContactPageRetailer retailerPage = new ContactPageRetailer(driver);
            hoursCheck = new OpeningHoursComplianceCheck();

            String rolexHours = rolexPage.getOpeningHours();
            String retailerHours = retailerPage.getOpeningHours();

            boolean isValid = hoursCheck.isOpeningHoursCompliant(rolexHours, retailerHours);
            Assert.assertTrue(isValid, "Opening hours validation failed");
        } catch (Exception e) {
            log.error("Opening hours validation test failed: " + e.getMessage());
            Assert.fail("Opening hours validation test failed: " + e.getMessage());
        }
    }
}
