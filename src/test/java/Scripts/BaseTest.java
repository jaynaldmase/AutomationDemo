package Scripts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import Constants.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Base test class containing common test configuration and setup
 */
public class BaseTest {
    protected WebDriver driver;
    protected Properties urlProperties;
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private static final String PROPERTIES_PATH = "src/test/java/TestData/";

    /**
     * Sets up WebDriver and loads properties before each test method
     * @param browser browser type to initialize
     * @param propertyFile name of the property file to load
     */
    @BeforeMethod
    @Parameters({"browser", "propertyFile"})
    public void setup(@Optional("chrome") String browser, String propertyFile) {
        loadProperties(propertyFile);
        initializeDriver(browser);
    }

    /**
     * Loads properties from specified configuration file
     * @param propertyFileName name of the property file to load
     */
    private void loadProperties(String propertyFileName) {
        try (FileInputStream inputStream = new FileInputStream(PROPERTIES_PATH + propertyFileName + ".properties")) {
            urlProperties = new Properties();
            urlProperties.load(inputStream);
            validateRequiredProperties();
            logger.info("Properties loaded successfully from: {}", propertyFileName);
        } catch (IOException e) {
            logger.error("Failed to load properties file {}: {}", propertyFileName, e.getMessage());
            throw new RuntimeException("Failed to load properties file: " + propertyFileName, e);
        }
    }


    /**
     * Validates that all required properties are present
     */
    private void validateRequiredProperties() {
        if (!urlProperties.containsKey(Constants.URLs.RETAILER_URL) ||
                !urlProperties.containsKey(Constants.URLs.ROLEX_URL)) {
            logger.error(Constants.LogMessages.MISSING_PROPERTIES);
            throw new RuntimeException(Constants.LogMessages.MISSING_PROPERTIES);
        }
    }

    /**
     * Initializes WebDriver with specified browser
     * @param browser browser type to initialize
     */
    private void initializeDriver(String browser) {
        logger.info("Setting up WebDriver for browser: {}", browser);
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--start-maximized");
                    firefoxOptions.addArguments("--disable-notifications");
                    driver = new FirefoxDriver(firefoxOptions);
                    // Firefox requires explicit maximize
                    driver.manage().window().maximize();
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--remote-allow-origins=*");
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    logger.error("Unsupported browser type: {}", browser);
                    throw new RuntimeException("Unsupported browser type: " + browser);
            }

            // Set common timeouts and configurations
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            logger.info("{} WebDriver initialized successfully", browser);

        } catch (Exception e) {
            logger.error("Failed to setup WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to setup WebDriver", e);
        }
    }

    /**
     * Cleans up WebDriver after each test method
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down WebDriver");
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error during driver cleanup: {}", e.getMessage());
            }
        }
    }

    /**
     * Gets the current WebDriver instance
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
}
