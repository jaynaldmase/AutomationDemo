package Scripts;

import Pages.ContactPageRolex;
import Pages.ContactPageRetailer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import Constants.Constants;

public class ValidateRetailerInformationTest extends BaseTest {
    private static final Logger log = LogManager.getLogger(ValidateRetailerInformationTest.class);

    @Test(description = "Validate retailer information matches between Rolex and Retailer websites")
    public void validateRetailerInfo() {
        ContactPageRolex rolexPage;
        ContactPageRetailer retailerPage;

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
                    rolexPage.waitForContactDetails(),
                    Constants.ErrorMessages.CONTACT_DETAILS_ERROR
            );

            // Capture Rolex information
            log.info("Capturing Rolex contact information");
            String rolexAddress = rolexPage.getAddress();
            String rolexNumber = rolexPage.getPhoneNumber();
            String rolexHours = rolexPage.getOpeningHours();

            log.info("Rolex Address: {}", rolexAddress);
            log.info("Rolex Phone: {}", rolexNumber);
            log.info("Rolex Hours: {}", rolexHours);

            // Navigate to Retailer website and handle cookies
            log.info("Navigating to Retailer website");
            driver.get(urlProperties.getProperty(Constants.URLs.RETAILER_URL));
            retailerPage.handleContactPageCookies();

            // Wait for and verify Retailer contact details are displayed
            Assert.assertTrue(
                    retailerPage.waitForContactDetails(),
                    Constants.ErrorMessages.CONTACT_DETAILS_ERROR
            );

            // Capture Retailer information
            log.info("Capturing Retailer contact information");
            String retailerAddress = retailerPage.getAddress();
            String retailerNumber = retailerPage.getPhoneNumber();
            String retailerHours = retailerPage.getOpeningHours();

            log.info("Retailer Address: {}", retailerAddress);
            log.info("Retailer Phone: {}", retailerNumber);
            log.info("Retailer Hours: {}", retailerHours);

            // Compare information
            log.info("Comparing contact information between Rolex and Retailer websites");

            Assert.assertEquals(
                    retailerAddress.trim(),
                    rolexAddress.trim(),
                    "Address mismatch between Rolex and Retailer websites"
            );

            Assert.assertEquals(
                    retailerNumber.trim(),
                    rolexNumber.trim(),
                    "Phone number mismatch between Rolex and Retailer websites"
            );

            Assert.assertEquals(
                    retailerHours.trim(),
                    rolexHours.trim(),
                    "Opening hours mismatch between Rolex and Retailer websites"
            );

            log.info("All contact information matches between Rolex and Retailer websites");

        } catch (Exception e) {
            log.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }
}
