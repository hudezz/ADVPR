package ADVPR;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin Dashboard (TabPane): Upload Documents + Manage Knowledge Base.
 * Security guard is handled by SceneManager.switchToAdminDashboard().
 */
public class AdminDashboardController {

    // Upload tab
    @FXML private ListView<String> documentsListView;
    @FXML private Label selectedFileLabel;

    // KB tab
    @FXML private TextArea knowledgeBaseTextArea;

    // Top bar
    @FXML private Label adminWelcomeLabel;

    private final StorageService storage = new StorageService();

    private File selectedFile = null;

    @FXML
    public void initialize() {
        // Safe check for label existence
        if (adminWelcomeLabel != null) {
            String user = Session.getAdminUsername();
            adminWelcomeLabel.setText("Logged in as: " + (user == null ? "admin" : user));
        }

        // Load stored KB text
        try {
            if (knowledgeBaseTextArea != null) {
                knowledgeBaseTextArea.setText(storage.readKnowledgeBaseText());
            }
        } catch (IOException e) {
            if (knowledgeBaseTextArea != null) {
                knowledgeBaseTextArea.setText("(Failed to load knowledge base text: " + e.getMessage() + ")");
            }
        }

        refreshDocumentsList();
    }

    @FXML
    public void onChooseFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Admission Document");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supported Documents", "*.pdf", "*.docx", "*.txt"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Word", "*.docx"),
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            selectedFile = chosen;
            if (selectedFileLabel != null) {
                selectedFileLabel.setText(chosen.getAbsolutePath());
            }
        }
    }

    @FXML
    public void onUpload(ActionEvent event) {
        if (selectedFile == null) {
            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please choose a file first.");
            return;
        }

        try {
            Path saved = storage.saveUploadedDocument(selectedFile.toPath());
            showAlert(Alert.AlertType.INFORMATION, "Upload Successful", "Saved to: " + saved.toString());
            selectedFile = null;
            if (selectedFileLabel != null) {
                selectedFileLabel.setText("No file selected");
            }
            refreshDocumentsList();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Upload Failed", e.getMessage());
        }
    }

    @FXML
    public void onDeleteSelected(ActionEvent event) {
        String selected = documentsListView.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a document to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete document: " + selected + "?");
        confirm.setContentText("This removes the file from local storage.");

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    Path file = storage.documentsDir().resolve(selected);
                    Files.deleteIfExists(file);
                    refreshDocumentsList();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed", e.getMessage());
                }
            }
        });
    }

    @FXML
    public void onUpdateKnowledgeBase(ActionEvent event) {
        String text = knowledgeBaseTextArea.getText() == null ? "" : knowledgeBaseTextArea.getText();
        try {
            storage.writeKnowledgeBaseText(text);
            showAlert(Alert.AlertType.INFORMATION, "Knowledge Base Updated", "Saved to: " + storage.knowledgeBaseTextFile());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", e.getMessage());
        }
    }

    @FXML
    public void onReIndexPlaceholder(ActionEvent event) {
        // Placeholder for future integration
        showAlert(Alert.AlertType.INFORMATION, "Re-Index", "Use the Home Screen button for AI indexing.");
    }

    @FXML
    public void onLogout(ActionEvent event) {
        Session.logout();
        SceneManager.switchToHome();
    }

    private void refreshDocumentsList() {
        try {
            storage.ensureDirectories();
            if (documentsListView != null) {
                List<String> docs = Files.list(storage.documentsDir())
                        .filter(Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .sorted(Comparator.naturalOrder())
                        .collect(Collectors.toList());
                documentsListView.setItems(FXCollections.observableArrayList(docs));
            }
        } catch (IOException e) {
            if (documentsListView != null) {
                documentsListView.setItems(FXCollections.observableArrayList());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}