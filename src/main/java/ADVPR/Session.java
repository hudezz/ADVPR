package ADVPR; // <--- THIS IS THE IMPORTANT CHANGE

/**
 * Session holder for Admin.
 * Adapted for ADVPR package.
 */
public final class Session {
    private static boolean adminAuthenticated = false;
    private static String adminUsername = null;

    private Session() {}

    public static boolean isAdminAuthenticated() {
        return adminAuthenticated;
    }

    public static String getAdminUsername() {
        return adminUsername;
    }

    public static void authenticateAdmin(String username) {
        adminAuthenticated = true;
        adminUsername = username;
    }

    public static void logout() {
        adminAuthenticated = false;
        adminUsername = null;
    }
}