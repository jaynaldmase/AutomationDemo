package Constants;

/**
 * Class containing all constant variables used across the test framework
 */
public class Constants {

    /**
     * URL Properties Keys
     */
    public static class URLs {
        public static final String RETAILER_URL = "retailer.url";
        public static final String ROLEX_URL = "rolex.url";
    }

    /**
     * Element IDs for Contact Pages
     */
    public static class Elements {
        public static final String ADDRESS = "address";
        public static final String PHONE_NUMBER = "phone-number";
        public static final String OPENING_HOURS = "opening-hours";
        public static final String RETAILER_ADDRESS = "Retailer-Address";
        public static final String RETAILER_PHONE_NUMBER = "Retailer-Phone-Number";
        public static final String RETAILER_OPENING_HOURS = "Retailer-Opening-Hours";
        public static final String ROLEX_ADDRESS = "Rolex-Address";
        public static final String ROLEX_PHONE_NUMBER = "Rolex-Phone-Number";
        public static final String ROLEX_OPENING_HOURS = "Rolex-Opening-Hours";
    }

    /**
     * Log Messages
     */
    public static class LogMessages {
        public static final String MISSING_PROPERTIES = "Required URL properties are missing";
        public static final String PROPERTY_LOAD_FAIL = "Failed to load properties file";
    }

    /**
     * Error Messages
     */
    public static class ErrorMessages {
        public static final String CONTACT_DETAILS_ERROR = "Failed to load contact details";
        public static final String ADDRESS_MISMATCH = "Address mismatch between Retailer and Rolex websites";
        public static final String PHONE_MISMATCH = "Phone number mismatch between Retailer and Rolex websites";
        public static final String HOURS_MISMATCH = "Opening hours mismatch between Retailer and Rolex websites";
    }
}
