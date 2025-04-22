package Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private static final String SCREENSHOT_DIR = "test-output/screenshots";

    public ScreenshotUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        createScreenshotDirectory();
    }

    /**
     * Creates screenshot directory if it doesn't exist
     */
    private void createScreenshotDirectory() {
        try {
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
                log.info("Created screenshot directory: " + SCREENSHOT_DIR);
            }
        } catch (IOException e) {
            log.error("Failed to create screenshot directory: " + e.getMessage());
        }
    }

    /**
     * Takes screenshot with highlighted element
     * @param element Element to highlight
     * @param elementName Name of the element for the file name
     */
    public void captureElementScreenshot(WebElement element, String elementName) {
        try {
            // Scroll element into view
            js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);

            // Highlight element
            highlightElement(element);

            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Generate filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = String.format("screenshot_%s_%s.png", elementName, timestamp);
            Path destinationPath = Paths.get(SCREENSHOT_DIR, fileName);

            // Copy screenshot to destination
            try {
                Files.copy(screenshot.toPath(), destinationPath);
                log.info("Screenshot saved: " + destinationPath);
            } catch (IOException e) {
                log.error("Failed to save screenshot: " + e.getMessage());
            }

            // Remove highlight
            unhighlightElement(element);

        } catch (Exception e) {
            log.error("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Highlights an element
     * @param element Element to highlight
     */
    private void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            js.executeScript(
                    "arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');"
                            + "arguments[0].setAttribute('data-original-style', arguments[1]);",
                    element,
                    originalStyle
            );
        } catch (Exception e) {
            log.error("Failed to highlight element: " + e.getMessage());
        }
    }

    /**
     * Removes highlight from element
     * @param element Element to unhighlight
     */
    private void unhighlightElement(WebElement element) {
        try {
            String originalStyle = (String) js.executeScript(
                    "return arguments[0].getAttribute('data-original-style');",
                    element
            );
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    originalStyle != null ? originalStyle : ""
            );
        } catch (Exception e) {
            log.error("Failed to unhighlight element: " + e.getMessage());
        }
    }

    /**
     * Generates a unique filename for the screenshot
     * @param elementName Name of the element
     * @return String unique filename
     */
    private String generateUniqueFileName(String elementName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return String.format("screenshot_%s_%s.png", elementName, timestamp);
    }
}
