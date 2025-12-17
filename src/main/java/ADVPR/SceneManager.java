package ADVPR;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Universal SceneManager.
 * Handles both "Generic" switching (your code) and "Specific" switching (Member 3's code).
 */
public class SceneManager {

    private static Scene scene;

    // This allows us to access the stage if needed for popups
    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    // --- 1. THE GENERIC SWITCHER (Your Original Logic) ---
    public static void switchScene(String fxml) {
        try {
            // Check if string ends with .fxml, if not, add it (Safety)
            if (!fxml.endsWith(".fxml")) {
                fxml = fxml + ".fxml";
            }

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
            Parent root = fxmlLoader.load();

            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
            stage.sizeToScene(); // Optional: Resizes window to fit new content

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Could not load FXML file: " + fxml);
            e.printStackTrace();
        }
    }

    // --- 2. MEMBER 3'S SPECIFIC HELPERS (The New Logic) ---

    public static void switchToHome() {
        switchScene("Home.fxml");
    }

    public static void switchToLogin() {
        switchScene("Login.fxml");
    }

    /**
     * Security Guard: Only lets you pass if you are logged in.
     */
    public static void switchToAdminDashboard() {
        if (Session.isAdminAuthenticated()) {
            switchScene("AdminDashboard.fxml");
        } else {
            System.out.println("Access Denied: Admin not authenticated.");
            switchToLogin();
        }
    }
}