package Validations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpeningHoursComplianceCheck {
    private static final Logger log = LogManager.getLogger(OpeningHoursComplianceCheck.class);

    /**
     * Checks if retailer opening hours match Rolex opening hours and determines compliance level
     * @param rolexHours opening hours from Rolex website
     * @param retailerHours opening hours from Retailer website
     * @return true if compliant, false if not
     */
    public boolean isOpeningHoursCompliant(String rolexHours, String retailerHours) {
        log.info("Checking opening hours compliance");
        log.debug("Rolex hours: " + rolexHours);
        log.debug("Retailer hours: " + retailerHours);

        // Check if opening hours are available
        if (rolexHours == null || retailerHours == null ||
                rolexHours.trim().isEmpty() || retailerHours.trim().isEmpty()) {
            log.error("❌ NOT COMPLIANT: Opening hours are not available on the retailer's website");
            return false;
        }

        // Normalize opening hours for comparison
        String normalizedRolexHours = normalizeOpeningHours(rolexHours);
        String normalizedRetailerHours = normalizeOpeningHours(retailerHours);

        // Check for exact match after normalization
        if (normalizedRolexHours.equals(normalizedRetailerHours)) {
            log.info("✅ COMPLIANT: Opening hours match exactly");
            return true;
        }

        // Check if hours match when ignoring closing days in Rolex section
        if (hoursMatchIgnoringClosingDays(normalizedRolexHours, normalizedRetailerHours)) {
            log.info("✅ COMPLIANT: Opening hours match (closing days not reported in Rolex section)");
            return true;
        }

        // Check for different days/hours
        if (hasDifferentDaysOrHours(normalizedRolexHours, normalizedRetailerHours)) {
            log.error("❌ NOT COMPLIANT: Different days or opening hours detected");
            return false;
        }

        log.error("❌ NOT COMPLIANT: Opening hours do not match required criteria");
        return false;
    }

    /**
     * Normalizes opening hours string for comparison
     */
    private String normalizeOpeningHours(String hours) {
        return hours.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("(:|\\.|,)(?=\\d)", "h") // normalize time separators
                .replaceAll("(am|pm)", "") // remove AM/PM indicators
                .replaceAll("(monday|mon)", "monday")
                .replaceAll("(tuesday|tue|tues)", "tuesday")
                .replaceAll("(wednesday|wed)", "wednesday")
                .replaceAll("(thursday|thu|thurs)", "thursday")
                .replaceAll("(friday|fri)", "friday")
                .replaceAll("(saturday|sat)", "saturday")
                .replaceAll("(sunday|sun)", "sunday")
                .trim();
    }

    /**
     * Checks if hours match when ignoring closing days
     */
    private boolean hoursMatchIgnoringClosingDays(String hours1, String hours2) {
        // Extract operating hours for each day
        String[] days1 = extractDailyHours(hours1);
        String[] days2 = extractDailyHours(hours2);

        for (int i = 0; i < 7; i++) {
            if (!days1[i].contains("closed") && !days2[i].contains("closed")) {
                if (!days1[i].equals(days2[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Extracts daily hours into an array
     */
    private String[] extractDailyHours(String hours) {
        String[] daysOfWeek = new String[7];
        String[] lines = hours.split("\n");

        for (String line : lines) {
            line = line.toLowerCase().trim();
            if (line.contains("monday")) daysOfWeek[0] = extractHoursFromLine(line);
            if (line.contains("tuesday")) daysOfWeek[1] = extractHoursFromLine(line);
            if (line.contains("wednesday")) daysOfWeek[2] = extractHoursFromLine(line);
            if (line.contains("thursday")) daysOfWeek[3] = extractHoursFromLine(line);
            if (line.contains("friday")) daysOfWeek[4] = extractHoursFromLine(line);
            if (line.contains("saturday")) daysOfWeek[5] = extractHoursFromLine(line);
            if (line.contains("sunday")) daysOfWeek[6] = extractHoursFromLine(line);
        }

        return daysOfWeek;
    }

    /**
     * Extracts hours from a single line
     */
    private String extractHoursFromLine(String line) {
        // Remove day name and extract time
        return line.replaceAll("^.*?(?=\\d|closed)", "").trim();
    }

    /**
     * Checks for different days or hours
     */
    private boolean hasDifferentDaysOrHours(String hours1, String hours2) {
        String[] days1 = extractDailyHours(hours1);
        String[] days2 = extractDailyHours(hours2);

        for (int i = 0; i < 7; i++) {
            if (days1[i] != null && days2[i] != null && !days1[i].equals(days2[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates time format
     */
    private boolean isValidTimeFormat(String time) {
        // Check if time follows standard format (e.g., 09:00-18:00 or 9h00-18h00)
        return time.matches("\\d{1,2}[h:]\\d{2}-\\d{1,2}[h:]\\d{2}");
    }

    /**
     * Checks if the hours indicate a full day closure
     */
    private boolean isClosedDay(String hours) {
        return hours.toLowerCase().contains("closed") ||
                hours.toLowerCase().contains("fermé") ||
                hours.toLowerCase().contains("geschlossen");
    }

    /**
     * Validates if hours are within reasonable range
     */
    private boolean isReasonableHours(String hours) {
        // Extract hours from time string
        String[] times = hours.split("-");
        if (times.length != 2) return false;

        int openingHour = extractHour(times[0]);
        int closingHour = extractHour(times[1]);

        // Check if hours are within reasonable range (e.g., 6-23)
        return openingHour >= 6 && openingHour <= 23 &&
                closingHour >= 6 && closingHour <= 23 &&
                openingHour < closingHour;
    }

    /**
     * Extracts hour from time string
     */
    private int extractHour(String time) {
        // Extract numeric hour from time string (e.g., "09:00" or "9h00")
        return Integer.parseInt(time.split("[h:]")[0]);
    }
}
