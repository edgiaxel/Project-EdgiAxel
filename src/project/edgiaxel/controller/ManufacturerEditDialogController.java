package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.Manufacturer;

public class ManufacturerEditDialogController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField countryField;
    @FXML
    private ComboBox<String> categoryComboBox;

    private Stage dialogStage;
    private Manufacturer manufacturer;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the category ComboBox with fixed values
        categoryComboBox.setItems(FXCollections.observableArrayList("Hypercar", "LMGT3"));
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the manufacturer to be edited in the dialog.
     *
     * @param manufacturer
     */
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;

        if (manufacturer.getManufacturerId() != 0) {
            idField.setText(String.valueOf(manufacturer.getManufacturerId()));
        } else {
            idField.setText("New Entry");
        }
        nameField.setText(manufacturer.getName());
        countryField.setText(manufacturer.getCountry());
        categoryComboBox.getSelectionModel().select(manufacturer.getCategory());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok (SAVE).
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Update the Manufacturer object properties
            manufacturer.setName(nameField.getText());
            manufacturer.setCountry(countryField.getText());
            manufacturer.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());

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
     *
     * * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid manufacturer name entered!\n";
        }
        if (countryField.getText() == null || countryField.getText().length() == 0) {
            errorMessage += "No valid country entered!\n";
        }
        if (categoryComboBox.getSelectionModel().isEmpty()) {
            errorMessage += "No category selected!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
