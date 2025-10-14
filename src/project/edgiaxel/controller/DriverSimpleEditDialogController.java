package project.edgiaxel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.Driver;

public class DriverSimpleEditDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField nationalityField;

    private Stage dialogStage;
    private Driver newDriver;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public boolean isOkClicked() { return okClicked; }
    public Driver getNewDriver() { return newDriver; }
    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            newDriver = new Driver(
                -1, // ID will be generated
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                nationalityField.getText().trim()
            );
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() { dialogStage.close(); }

    private boolean isInputValid() {
        String errorMessage = "";
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() || nationalityField.getText().trim().isEmpty()) {
            errorMessage += "All driver fields are mandatory, stop messing around!\n";
        }
        
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Driver Data");
            alert.setHeaderText("Fix the driver's info, quick!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}