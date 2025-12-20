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

public class RegisterViewController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField regUsernameField;
    @FXML
    private PasswordField regPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label statusLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        if (regPasswordField.getText().equals(confirmPasswordField.getText())) {
            if (UserDAO.getInstance().checkUsernameExists(regUsernameField.getText())) {
                statusLabel.setText("Username already taken!");
                return;
            }

            boolean success = UserDAO.getInstance().registerUser(
                    fullNameField.getText(),
                    emailField.getText(),
                    regUsernameField.getText(),
                    regPasswordField.getText()
            );

            if (success) {
                handleCancel(event);
            } else {
                statusLabel.setText("Database Error.");
            }
        } else {
            statusLabel.setText("Passwords do not match!");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/LoginView.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
