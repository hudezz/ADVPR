package ADVPR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class HomeController {

    // 1. Connect to the PDF Brain (Your Logic)
    private final PDFManager pdfManager = new PDFManager();

    // 2. Admin Button (Member 3's Logic)
    @FXML
    public void onAdminClick(ActionEvent event) {
        // This takes us to the new Login screen
        SceneManager.switchScene("Login.fxml");
    }

    // 3. Chat Button (Merged: Their Name + Your Logic)
    @FXML
    public void onOpenChat(ActionEvent event) {
        // This was empty in Member 3's code. We added your switch logic back.
        SceneManager.switchScene("Chat.fxml");
    }

    // 4. Re-Index Button (Merged: Their Name + Your Logic)
    // Note: Member 3 might have named this onReIndexPlaceholder or similar in their FXML.
    // If clicking the button does nothing, check the FXML file for the method name!
    @FXML
    public void handleReIndex(ActionEvent event) {
        try {
            pdfManager.updateIndex();

            // Show Success Message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF Re-Indexed Successfully!");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            // Show Error Message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to index PDF: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // 5. FAQ Button (Placeholder for now)
    @FXML
    public void onOpenFAQ(ActionEvent event) {
        System.out.println("FAQ button clicked - Feature coming soon.");
    }
}