package Validations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressComplianceCheck {
    private static final Logger log = LogManager.getLogger(AddressComplianceCheck.class);

    /**
     * Checks if retailer address matches Rolex address and determines compliance level
     * @param rolexAddress address from Rolex website
     * @param retailerAddress address from Retailer website
     * @return true if compliant, false if not
     */
    public boolean isAddressCompliant(String rolexAddress, String retailerAddress) {
        log.info("Checking address compliance");
        log.debug("Rolex address: " + rolexAddress);
        log.debug("Retailer address: " + retailerAddress);

        // Check if either address is null or empty
        if (rolexAddress == null || retailerAddress == null ||
                rolexAddress.trim().isEmpty() || retailerAddress.trim().isEmpty()) {
            log.error("Essential information is missing - Address is null or empty");
            return false;
        }

        // Normalize addresses for comparison
        String normalizedRolexAddress = normalizeAddress(rolexAddress);
        log.info("Normalized Rolex Address: {}", normalizedRolexAddress);
        String normalizedRetailerAddress = normalizeAddress(retailerAddress);
        log.info("Normalized Retailer Address: {}", normalizedRetailerAddress);

        // Check for exact match after normalization
        if (normalizedRolexAddress.equals(normalizedRetailerAddress)) {
            log.info("✅ COMPLIANT: Addresses match exactly");
            return true;
        }

        // Check if addresses contain same information but different format
        if (containsSameInformation(normalizedRolexAddress, normalizedRetailerAddress)) {
            if (isFormatDifference(rolexAddress, retailerAddress)) {
                log.info("✅ COMPLIANT: Addresses match with different formatting");
                return true;
            }
        }

        // Check for specific non-compliance issues
        if (!hasRequiredComponents(normalizedRetailerAddress)) {
            log.error("❌ NOT COMPLIANT: Essential address components are missing");
            return false;
        }

        if (hasStreetNameMismatch(normalizedRolexAddress, normalizedRetailerAddress)) {
            log.error("❌ NOT COMPLIANT: Street name mismatch detected");
            return false;
        }

        if (!hasMatchingPostalCode(normalizedRolexAddress, normalizedRetailerAddress)) {
            log.error("❌ NOT COMPLIANT: ZIP code mismatch or missing");
            return false;
        }

        if (hasShoppingCentreMismatch(rolexAddress, retailerAddress)) {
            log.error("❌ NOT COMPLIANT: Shopping centre name inconsistency");
            return false;
        }

        log.error("❌ NOT COMPLIANT: Address information does not match required criteria");
        return false;
    }

    /**
     * Normalizes address string for comparison
     */
    private String normalizeAddress(String address) {
        return address.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("straße", "strasse")
                .trim();
    }

    /**
     * Checks if addresses contain the same core information
     */
    private boolean containsSameInformation(String address1, String address2) {
        String[] components1 = address1.split("[,\\s]+");
        String[] components2 = address2.split("[,\\s]+");

        int matchingComponents = 0;
        for (String comp1 : components1) {
            for (String comp2 : components2) {
                if (comp1.equals(comp2)) {
                    matchingComponents++;
                    break;
                }
            }
        }

        return (double) matchingComponents / Math.max(components1.length, components2.length) > 0.7;
    }

    /**
     * Checks if differences are only in formatting
     */
    private boolean isFormatDifference(String address1, String address2) {
        String stripped1 = address1.replaceAll("[^a-zA-Z0-9]", "");
        String stripped2 = address2.replaceAll("[^a-zA-Z0-9]", "");
        return stripped1.equalsIgnoreCase(stripped2);
    }

    /**
     * Checks if address has all required components
     */
    private boolean hasRequiredComponents(String address) {
        boolean hasStreetNumber = address.matches(".*\\d+.*");
        boolean hasBasicComponents = address.matches(".*[a-zA-Z]+.*\\d+.*[a-zA-Z]+.*");
        return hasStreetNumber && hasBasicComponents;
    }

    /**
     * Checks for street name mismatches
     */
    private boolean hasStreetNameMismatch(String address1, String address2) {
        String street1 = address1.split(",")[0];
        String street2 = address2.split(",")[0];
        return !containsSameInformation(street1, street2);
    }

    /**
     * Checks for matching postal codes
     */
    private boolean hasMatchingPostalCode(String address1, String address2) {
        String postalCode1 = extractPostalCode(address1);
        String postalCode2 = extractPostalCode(address2);
        return postalCode1 != null && postalCode2 != null && postalCode1.equals(postalCode2);
    }

    /**
     * Extracts postal code from address string
     */
    private String extractPostalCode(String address) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b\\d{5}\\b");
        java.util.regex.Matcher matcher = pattern.matcher(address);
        return matcher.find() ? matcher.group() : null;
    }

    /**
     * Checks for shopping centre name consistency
     */
    private boolean hasShoppingCentreMismatch(String address1, String address2) {
        boolean hasShoppingCentre1 = address1.toLowerCase().contains("centre") ||
                address1.toLowerCase().contains("center") ||
                address1.toLowerCase().contains("mall");
        boolean hasShoppingCentre2 = address2.toLowerCase().contains("centre") ||
                address2.toLowerCase().contains("center") ||
                address2.toLowerCase().contains("mall");
        return hasShoppingCentre1 != hasShoppingCentre2;
    }
}
