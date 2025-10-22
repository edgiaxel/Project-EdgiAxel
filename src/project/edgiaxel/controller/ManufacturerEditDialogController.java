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
        categoryComboBox.setItems(FXCollections.observableArrayList("Hypercar", "LMGT3"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
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

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            manufacturer.setName(nameField.getText());
            manufacturer.setCountry(countryField.getText());
            manufacturer.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());

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
