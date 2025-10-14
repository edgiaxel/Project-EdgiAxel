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

    @FXML
    private void initialize() {
        // Initialize the ComboBox options. Don't fuck this up.
        categoryComboBox.setItems(FXCollections.observableArrayList("Hypercar", "LMGT3"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the manufacturer to be edited in the dialog. If manufacturer is
     * null, it's a new entry (Add).
     */
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;

        if (manufacturer != null) {
            idField.setText(String.valueOf(manufacturer.getManufacturerId()));
            nameField.setText(manufacturer.getName());
            countryField.setText(manufacturer.getCountry());
            categoryComboBox.getSelectionModel().select(manufacturer.getCategory());
        } else {
            // For new entries, clear the ID field and set defaults
            idField.setText("");
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Handles the OK button click. Validates and stores the data.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Update the Manufacturer object with new data
            if (this.manufacturer == null) {
                // This is an ADD operation (new Manufacturer object will be created in MasterDataController)
            } else {
                // This is an EDIT operation (update the existing object)
                manufacturer.setName(nameField.getText());
                manufacturer.setCountry(countryField.getText());
                manufacturer.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Handles the Cancel button click.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input. YOU BETTER DO THIS!
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "No Manufacturer Name, you idiot!\n";
        }
        if (countryField.getText() == null || countryField.getText().trim().isEmpty()) {
            errorMessage += "No Country specified!\n";
        }
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You MUST select a Category (Hypercar or LMGT3)!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct these garbage inputs:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
