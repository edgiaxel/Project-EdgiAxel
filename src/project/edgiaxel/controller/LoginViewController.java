package project.edgiaxel.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.edgiaxel.dao.UserDAO;
import java.io.IOException;

public class LoginViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (UserDAO.getInstance().validateLogin(user, pass)) {
            loadDashboard(event);
        } else {
            errorLabel.setText("Access Denied: Invalid Credentials.");
        }
    }

    @FXML
    private void switchToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/RegisterView.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/Dashboard.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("FIA WEC SIMULATOR - " + project.edgiaxel.SessionManager.getCurrentUsername());
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
