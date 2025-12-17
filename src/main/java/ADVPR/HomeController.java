
package ADVPR;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.File;

public class HomeController {

    // Dependency: The PDF tool Member 5 built
    private final DocumentManager documentManager = new DocumentManager();

    // --- BUTTON 1: Start Chat ---
    @FXML
    public void handleStartChat() {
        System.out.println("Start Chat clicked! (We will build the chat screen next)");
        // Later, we will tell SceneManager to switch to the Chat view here.
    }

    // --- BUTTON 2: Re-Index PDF ---
    @FXML
    public void handleUpload() {
        System.out.println("--- Button Clicked: Starting Upload Process ---");

        // 1. Open the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a PDF File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        Stage stage = SceneManager.getStage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("User selected: " + selectedFile.getName());

            // 2. Save the file
            File savedFile = documentManager.saveDocument(selectedFile);

            if (savedFile != null) {
                // 3. Extract text
                String text = documentManager.extractText(savedFile);

                // 4. Save extracted text
                documentManager.saveExtractedText(text, savedFile.getName());

                // 5. Show Success Message
                showAlert("Success", "PDF Processed!", "Extracted " + text.length() + " characters.");
            }
        }
    }

    // Helper for popup messages
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}