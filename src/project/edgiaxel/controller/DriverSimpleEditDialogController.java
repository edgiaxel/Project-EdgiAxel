package project.edgiaxel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.Driver;

public class DriverSimpleEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField nationalityField;

    private Stage dialogStage;
    private Driver driver;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;

        firstNameField.setText(driver.getFirstName());
        lastNameField.setText(driver.getLastName());
        nationalityField.setText(driver.getNationality());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            driver.setFirstName(firstNameField.getText());
            driver.setLastName(lastNameField.getText());
            driver.setNationality(nationalityField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().trim().length() < 2) {
            errorMessage += "First Name is too short or missing!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().trim().length() < 2) {
            errorMessage += "Last Name is too short or missing!\n";
        }
        if (nationalityField.getText() == null || nationalityField.getText().trim().length() < 3) {
            errorMessage += "Nationality is too short or missing!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Driver Details");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
