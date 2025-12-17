package ADVPR;

public class Session {

    private static String adminUsername;

    // Log the user in
    public static void authenticateAdmin(String username) {
        adminUsername = username;
    }

    // Check if logged in (The method SceneManager needs!)
    public static boolean isLoggedIn() {
        return adminUsername != null;
    }

    // Get the name
    public static String getAdminUsername() {
        return adminUsername;
    }

    // Log out
    public static void logout() {
        adminUsername = null;
    }
}