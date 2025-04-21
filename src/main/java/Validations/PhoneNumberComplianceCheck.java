package Validations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhoneNumberComplianceCheck {
    private static final Logger log = LogManager.getLogger(PhoneNumberComplianceCheck.class);

    /**
     * Checks if retailer phone number matches Rolex phone number and determines compliance level
     * @param rolexPhoneNumber phone number from Rolex website
     * @param retailerPhoneNumber phone number from Retailer website
     * @return true if compliant, false if not
     */
    public boolean isPhoneNumberCompliant(String rolexPhoneNumber, String retailerPhoneNumber) {
        log.info("Checking phone number compliance");
        log.debug("Rolex phone number: " + rolexPhoneNumber);
        log.debug("Retailer phone number: " + retailerPhoneNumber);

        // Check if either phone number is null or empty
        if (rolexPhoneNumber == null || retailerPhoneNumber == null ||
                rolexPhoneNumber.trim().isEmpty() || retailerPhoneNumber.trim().isEmpty()) {
            log.error("❌ NOT COMPLIANT: Phone number is not available");
            return false;
        }

        // Normalize phone numbers for comparison
        String normalizedRolexNumber = normalizePhoneNumber(rolexPhoneNumber);
        String normalizedRetailerNumber = normalizePhoneNumber(retailerPhoneNumber);

        // Check for exact match after normalization
        if (normalizedRolexNumber.equals(normalizedRetailerNumber)) {
            log.info("✅ COMPLIANT: Phone numbers match exactly");
            return true;
        }

        // Check if numbers match without country code
        if (numbersMatchWithoutCountryCode(normalizedRolexNumber, normalizedRetailerNumber)) {
            log.info("✅ COMPLIANT: Phone numbers match (country code difference only)");
            return true;
        }

        // If numbers are different
        if (!numbersMatchWithoutCountryCode(normalizedRolexNumber, normalizedRetailerNumber)) {
            log.error("❌ NOT COMPLIANT: Different phone numbers detected");
            return false;
        }

        log.error("❌ NOT COMPLIANT: Phone numbers do not match required criteria");
        return false;
    }

    /**
     * Normalizes phone number for comparison by removing all non-numeric characters
     */
    private String normalizePhoneNumber(String phoneNumber) {
        // Remove all non-numeric characters
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    /**
     * Checks if phone numbers match without considering country code
     */
    private boolean numbersMatchWithoutCountryCode(String number1, String number2) {
        // Remove potential country codes (assuming they're at the start and 1-3 digits)
        String num1WithoutCountryCode = removeCountryCode(number1);
        String num2WithoutCountryCode = removeCountryCode(number2);

        return num1WithoutCountryCode.equals(num2WithoutCountryCode);
    }

    /**
     * Removes country code from phone number
     */
    private String removeCountryCode(String number) {
        // If number starts with + or 00, remove it and up to 3 digits following it
        if (number.length() > 8) { // Ensure we're not dealing with a local number
            if (number.startsWith("00")) {
                number = number.substring(2);
                // Remove up to 3 digits of country code
                return number.substring(3);
            } else {
                // Remove up to 3 digits assuming they're country code
                return number.substring(3);
            }
        }
        return number;
    }

    /**
     * Validates phone number format
     */
    private boolean isValidPhoneNumberFormat(String phoneNumber) {
        // Basic phone number format validation
        String normalized = normalizePhoneNumber(phoneNumber);
        // Check if it has a reasonable length for a phone number (min 8, max 15 digits)
        return normalized.length() >= 8 && normalized.length() <= 15;
    }

    /**
     * Checks if the number appears to be a mobile number
     */
    private boolean isMobileNumber(String phoneNumber) {
        String normalized = normalizePhoneNumber(phoneNumber);
        // Common mobile number prefixes (can be expanded based on country)
        return normalized.matches(".*(07|06|15|16|17)\\d+");
    }

    /**
     * Checks if the number appears to be a landline
     */
    private boolean isLandlineNumber(String phoneNumber) {
        String normalized = normalizePhoneNumber(phoneNumber);
        // Common landline prefixes (can be expanded based on country)
        return normalized.matches(".*(01|02|03|04|05)\\d+");
    }
}
