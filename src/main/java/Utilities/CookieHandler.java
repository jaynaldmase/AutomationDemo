package Utilities;

/**
 * Handles cookie consent management and user preferences for different types of cookies.
 * This class implements cookie management following GDPR compliance requirements.
 *
 * @author James Reynald Mase
 * @version 1.0
 */
public class CookieHandler {
    // Cookie preference flags
    private boolean essentialCookiesAccepted;
    private boolean marketingCookiesAccepted;
    private boolean analyticsCookiesAccepted;
    private boolean preferencesSet;

    /**
     * Initializes a new CookieHandler with default settings.
     * Essential cookies are accepted by default as they are required for basic functionality.
     * All other cookie types are disabled until user provides explicit consent.
     */
    public CookieHandler() {
        this.essentialCookiesAccepted = true; // Essential cookies are always accepted
        this.marketingCookiesAccepted = false;
        this.analyticsCookiesAccepted = false;
        this.preferencesSet = false;
    }

    /**
     * Sets specific cookie preferences based on user selection.
     *
     * @param acceptMarketing true if marketing cookies are accepted
     * @param acceptAnalytics true if analytics cookies are accepted
     */
    public void setCookiePreferences(boolean acceptMarketing, boolean acceptAnalytics) {
        this.marketingCookiesAccepted = acceptMarketing;
        this.analyticsCookiesAccepted = acceptAnalytics;
        this.preferencesSet = true;
    }

    /**
     * Accepts all optional cookies.
     * This method is typically called when user clicks "Accept All" button.
     */
    public void acceptAllCookies() {
        this.marketingCookiesAccepted = true;
        this.analyticsCookiesAccepted = true;
        this.preferencesSet = true;
    }

    /**
     * Rejects all optional cookies while maintaining essential cookies.
     * This method is typically called when user clicks "Reject All" or "Essential Only" button.
     */
    public void rejectOptionalCookies() {
        this.marketingCookiesAccepted = false;
        this.analyticsCookiesAccepted = false;
        this.preferencesSet = true;
    }

    /**
     * Checks if marketing cookies are accepted.
     *
     * @return true if marketing cookies are accepted
     */
    public boolean isMarketingCookiesAccepted() {
        return marketingCookiesAccepted;
    }

    /**
     * Checks if analytics cookies are accepted.
     *
     * @return true if analytics cookies are accepted
     */
    public boolean isAnalyticsCookiesAccepted() {
        return analyticsCookiesAccepted;
    }

    /**
     * Checks if user has set their cookie preferences.
     *
     * @return true if preferences have been set
     */
    public boolean arePreferencesSet() {
        return preferencesSet;
    }

    /**
     * Determines if the cookie consent banner should be displayed to the user.
     * Banner should be shown if user hasn't set their preferences yet.
     *
     * @return true if cookie banner should be displayed
     */
    public boolean shouldShowCookieBanner() {
        return !preferencesSet;
    }

    /**
     * Resets all cookie preferences to default state.
     * This method can be used when user wants to clear their preferences
     * or when preferences need to be renewed.
     */
    public void resetPreferences() {
        this.marketingCookiesAccepted = false;
        this.analyticsCookiesAccepted = false;
        this.preferencesSet = false;
    }

    /**
     * Generates a formatted string containing the current status of all cookie preferences.
     *
     * @return String representation of current cookie consent status
     */
    public String getCookieConsentStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Cookie Preferences:\n");
        status.append("Essential Cookies: Always Accepted\n");
        status.append("Marketing Cookies: ")
                .append(marketingCookiesAccepted ? "Accepted" : "Rejected")
                .append("\n");
        status.append("Analytics Cookies: ")
                .append(analyticsCookiesAccepted ? "Accepted" : "Rejected");
        return status.toString();
    }
}
