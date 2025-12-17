package ADVPR;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage stage;

    // 1. We catch the stage when the App starts
    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    // 2. THIS IS THE MISSING METHOD
    // We allow other parts of the app (like HomeController) to grab the stage
    public static Stage getStage() {
        return stage;
    }

    // 3. Helper to switch screens
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML file: " + fxmlFile);
        }
    }
}