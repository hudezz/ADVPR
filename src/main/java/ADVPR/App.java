package ADVPR;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        // 1. Give the stage to the SceneManager
        SceneManager.setStage(stage);
        stage.setTitle("ADVPR Chatbot");

        // 2. Open the first screen
        // FIXED: Used "switchScene" and added ".fxml"
        SceneManager.switchScene("Home.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}