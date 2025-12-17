package ADVPR;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private static Stage stage;
    private static Scene scene;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchScene(String fxml) {
        try {
            if (!fxml.endsWith(".fxml")) fxml += ".fxml";

            // --- UNIVERSAL SEARCH STRATEGY ---
            URL fileUrl = null;

            // 1. Check relative to the class (Standard)
            // Expects: target/classes/ADVPR/Home.fxml
            fileUrl = App.class.getResource(fxml);
            if (fileUrl != null) System.out.println("DEBUG: Found relative: " + fileUrl);

            // 2. Check strict package path
            // Expects: target/classes/ADVPR/Home.fxml
            if (fileUrl == null) {
                fileUrl = App.class.getResource("/ADVPR/" + fxml);
                if (fileUrl != null) System.out.println("DEBUG: Found at /ADVPR/: " + fileUrl);
            }

            // 3. Check ROOT (If you marked ADVPR folder as Source Root)
            // Expects: target/classes/Home.fxml
            if (fileUrl == null) {
                fileUrl = App.class.getResource("/" + fxml);
                if (fileUrl != null) System.out.println("DEBUG: Found at root /: " + fileUrl);
            }

            // 4. Check "view" folder (Old Member 3 Location)
            if (fileUrl == null) {
                fileUrl = App.class.getResource("/view/" + fxml);
                if (fileUrl != null) System.out.println("DEBUG: Found at /view/: " + fileUrl);
            }

            // CRITICAL FAILURE
            if (fileUrl == null) {
                System.err.println("❌ CRITICAL ERROR: Could not find '" + fxml + "' in any known location.");
                System.err.println("   Checked: Relative, /ADVPR/" + fxml + ", /" + fxml + ", and /view/" + fxml);
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            Parent root = fxmlLoader.load();

            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
            stage.sizeToScene();
            stage.show();

        } catch (IOException e) {
            System.err.println("❌ CRASHED while loading " + fxml);
            e.printStackTrace();
        }
    }

    public static void switchToHome() {
        switchScene("Home");
    }

    public static void switchToLogin() {
        switchScene("Login");
    }

    public static void switchToAdminDashboard() {
        if (Session.isLoggedIn()) {
            switchScene("AdminDashboard");
        } else {
            System.out.println("Access Denied: Admin not logged in.");
            switchToLogin();
        }
    }
}