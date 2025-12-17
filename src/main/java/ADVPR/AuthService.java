package ADVPR;

/**
 * Simple hardcoded authentication.
 * Adapted for ADVPR package.
 */
public class AuthService {

    // Hardcoded demo credentials
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "123";

    public boolean validateAdmin(String username, String password) {
        return ADMIN_USER.equals(username) && ADMIN_PASS.equals(password);
    }
}