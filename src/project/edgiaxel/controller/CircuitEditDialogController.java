package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.Circuit;

public class CircuitEditDialogController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField countryField;
    @FXML private TextField lengthField;
    @FXML private ComboBox<String> raceTypeComboBox;

    private Stage dialogStage;
    private Circuit circuit;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Initialize the ComboBox options based on WEC rules
        raceTypeComboBox.setItems(FXCollections.observableArrayList("6 Hours", "8–10 Hours", "24 Hours"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;

        if (circuit != null) {
            idField.setText(String.valueOf(circuit.getCircuitId()));
            nameField.setText(circuit.getName());
            locationField.setText(circuit.getLocation());
            countryField.setText(circuit.getCountry());
            lengthField.setText(String.format("%.3f", circuit.getLengthKm()));
            raceTypeComboBox.getSelectionModel().select(circuit.getRaceType());
        } else {
            idField.setText("");
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Update the Circuit object with new data
            double length = Double.parseDouble(lengthField.getText().trim());

            if (this.circuit == null) {
                // This is an ADD operation (new Circuit object created in MasterDataController)
            } else {
                // This is an EDIT operation
                circuit.setName(nameField.getText());
                circuit.setLocation(locationField.getText());
                circuit.setCountry(countryField.getText());
                circuit.setLengthKm(length);
                circuit.setRaceType(raceTypeComboBox.getSelectionModel().getSelectedItem());
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input, especially the length field.
     */
    private boolean isInputValid() {
        String errorMessage = "";
        String lengthText = lengthField.getText();

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Circuit Name is mandatory!\n";
        }
        if (locationField.getText() == null || locationField.getText().trim().isEmpty()) {
            errorMessage += "Circuit Location (City) is mandatory!\n";
        }
        if (countryField.getText() == null || countryField.getText().trim().isEmpty()) {
            errorMessage += "Circuit Country is mandatory!\n";
        }
        if (raceTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Race Duration (e.g., 6 Hours) must be selected!\n";
        }
        
        // --- NUMERIC VALIDATION ---
        if (lengthText == null || lengthText.trim().isEmpty()) {
            errorMessage += "Circuit Length is mandatory!\n";
        } else {
            // Try to parse the length as a double
            try {
                double length = Double.parseDouble(lengthText.trim());
                if (length <= 0) {
                    errorMessage += "Circuit Length must be a positive number, dumbass.\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Circuit Length must be a valid number (e.g., 5.419)!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Circuit Data");
            alert.setHeaderText("Fix these errors or I'll smash your keyboard:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}