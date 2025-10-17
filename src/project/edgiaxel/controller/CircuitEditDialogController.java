package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.Circuit;

public class CircuitEditDialogController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField lengthField;
    @FXML
    private ComboBox<String> raceTypeComboBox;

    private Stage dialogStage;
    private Circuit circuit;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Initialize the ComboBox with the WEC defined race types
        raceTypeComboBox.setItems(FXCollections.observableArrayList("6 Hours", "8–10 Hours", "24 Hours"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;

        if (circuit.getCircuitId() != 0) {
            idField.setText(String.valueOf(circuit.getCircuitId()));
        } else {
            idField.setText("New Entry");
        }
        nameField.setText(circuit.getName());
        locationField.setText(circuit.getLocation());
        countryField.setText(circuit.getCountry());
        lengthField.setText(String.valueOf(circuit.getLengthKm()));
        raceTypeComboBox.getSelectionModel().select(circuit.getRaceType());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            circuit.setName(nameField.getText());
            circuit.setLocation(locationField.getText());
            circuit.setCountry(countryField.getText());
            circuit.setLengthKm(Double.parseDouble(lengthField.getText()));
            circuit.setRaceType(raceTypeComboBox.getSelectionModel().getSelectedItem());

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
            errorMessage += "No valid circuit name!\n";
        }
        if (locationField.getText() == null || locationField.getText().isEmpty()) {
            errorMessage += "No valid location!\n";
        }
        if (countryField.getText() == null || countryField.getText().isEmpty()) {
            errorMessage += "No valid country!\n";
        }

        String lengthText = lengthField.getText();
        if (lengthText == null || lengthText.length() == 0) {
            errorMessage += "No valid length entered!\n";
        } else {
            try {
                double length = Double.parseDouble(lengthText);
                if (length <= 0) {
                    errorMessage += "Length must be positive!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Length is not a valid number (use decimal point)! (e.g., 5.419)\n";
            }
        }

        if (raceTypeComboBox.getSelectionModel().isEmpty()) {
            errorMessage += "No race duration selected!\n";
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
