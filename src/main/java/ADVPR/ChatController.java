package ADVPR;

package ADVPR;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class ChatController {

    @FXML private VBox messageContainer;
    @FXML private TextField inputField;

    // 1. Connect to the Real AI Brain
    private final AIService aiService = new AIService();

    @FXML
    public void handleSend() {
        String userText = inputField.getText();
        if (userText.isEmpty()) return;

        // 2. Show User's Message immediately (Green Bubble)
        addMessage("User: " + userText, true);
        inputField.clear();

        // 3. Ask the AI (Run in background so the app doesn't freeze)
        new Thread(() -> {
            try {
                // This calls Ollama to get the answer
                String aiResponse = aiService.getAnswer(userText);

                // Update the screen with the answer (Gray Bubble)
                Platform.runLater(() -> addMessage("AI: " + aiResponse, false));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> addMessage("Error: Could not connect to AI. Make sure Ollama is running!", false));
            }
        }).start();
    }

    @FXML
    public void handleBack() {
        // Go back to the Dashboard
        SceneManager.switchScene("Home.fxml");
    }

    // Helper method to create the chat bubbles
    private void addMessage(String text, boolean isUser) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(350); // Keep bubbles from getting too wide

        if (isUser) {
            // User style: Light Green
            label.setStyle("-fx-background-color: lightgreen; -fx-padding: 10; -fx-background-radius: 10;");
        } else {
            // AI style: Light Gray
            label.setStyle("-fx-background-color: lightgray; -fx-padding: 10; -fx-background-radius: 10;");
        }

        messageContainer.getChildren().add(label);
    }
}