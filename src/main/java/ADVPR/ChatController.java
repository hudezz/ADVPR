package ADVPR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;

public class ChatController {

    @FXML
    private TextArea historyTextArea;

    @FXML
    private TextField questionField;

    // Connect to the PDFManager (The Brain), NOT directly to AIService
    private final PDFManager pdfManager = new PDFManager();

    @FXML
    public void initialize() {
        if(historyTextArea != null) {
            historyTextArea.setText("Welcome! Ask a question about the uploaded PDF.\n\n");
        }
    }

    @FXML
    public void handleAsk(ActionEvent event) {
        String question = questionField.getText();

        if (question == null || question.trim().isEmpty()) {
            return;
        }

        // 1. Show User's Question
        historyTextArea.appendText("YOU: " + question + "\n");
        questionField.clear();

        // 2. Get AI Answer in background (Prevent freezing)
        new Thread(() -> {
            String answer;
            try {
                // This calls the method we created in PDFManager
                answer = pdfManager.askQuestion(question);
            } catch (Exception e) {
                answer = "Error: " + e.getMessage();
            }

            // 3. Update Screen
            String finalAnswer = answer;
            Platform.runLater(() -> {
                historyTextArea.appendText("AI: " + finalAnswer + "\n\n");
            });
        }).start();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        SceneManager.switchToHome();
    }
}