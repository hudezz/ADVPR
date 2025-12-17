package ADVPR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Login screen controller.
 * Adapted for ADVPR package.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML // <--- This tag was missing in the old code!
    public void onLogin(ActionEvent event) {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (authService.validateAdmin(username, password)) {
            Session.authenticateAdmin(username);
            if (errorLabel != null) errorLabel.setText("");
            SceneManager.switchToAdminDashboard();
        } else {
            showError("Invalid credentials. Try admin / 123.");
        }
    }

    @FXML // <--- This tag was missing in the old code!
    public void onBack(ActionEvent event) {
        SceneManager.switchToHome();
    }

    private void showError(String msg) {
        if (errorLabel != null) {
            errorLabel.setText(msg);
        }
    }
}