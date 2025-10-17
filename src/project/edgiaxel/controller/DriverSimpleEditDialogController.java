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
        // No initialization needed beyond default component states
    }

    /**
     * Sets the stage of this dialog.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the driver to be edited/created in the dialog.
     */
    public void setDriver(Driver driver) {
        this.driver = driver;

        // This is primarily for adding new drivers, so fields start empty/null
        firstNameField.setText(driver.getFirstName());
        lastNameField.setText(driver.getLastName());
        nationalityField.setText(driver.getNationality());
    }

    /**
     * Returns true if the user clicked OK.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok (OK).
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Update the Driver object properties
            driver.setFirstName(firstNameField.getText());
            driver.setLastName(lastNameField.getText());
            driver.setNationality(nationalityField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     */
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
