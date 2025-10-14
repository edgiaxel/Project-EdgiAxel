package project.edgiaxel.controller;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.model.CarModel;
import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Team;
import project.edgiaxel.util.DBUtil;

public class TeamEditDialogController {

    @FXML
    private ComboBox<Manufacturer> manufacturerComboBox;
    @FXML
    private ComboBox<CarModel> carModelComboBox;
    @FXML
    private TextField carNumberField;
    @FXML
    private TextField teamNameField;
    @FXML
    private TextField nationalityField;
    @FXML
    private TextField categoryField;

    private Stage dialogStage;
    private Team team;
    private Manufacturer manufacturer; // The manufacturer this team belongs to
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // We'll load Car Models dynamically based on the set Manufacturer.
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the team entry and the parent manufacturer.
     */
    public void setTeamAndManufacturer(Team team, Manufacturer manufacturer, ObservableList<CarModel> allCarModels) {
        this.team = team;
        this.manufacturer = manufacturer;

        // 1. Set Manufacturer and Category (Disabled/Read-only fields)
        manufacturerComboBox.getItems().add(manufacturer);
        manufacturerComboBox.getSelectionModel().select(manufacturer);
        categoryField.setText(manufacturer.getCategory());

        // 2. Populate Car Models relevant to this manufacturer
        ObservableList<CarModel> filteredModels = allCarModels.filtered(
                model -> model.getManufacturerId() == manufacturer.getManufacturerId()
        );
        carModelComboBox.setItems(filteredModels);

        // 3. Populate existing team data if editing
        if (team != null) {
            carNumberField.setText(team.getCarNumber());
            teamNameField.setText(team.getTeamName());
            nationalityField.setText(team.getNationality());

            // Select the existing car model
            CarModel currentModel = DBUtil.getCarModelById(team.carModelIdProperty().get());
            if (currentModel != null) {
                carModelComboBox.getSelectionModel().select(currentModel);
            }
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            CarModel selectedModel = carModelComboBox.getSelectionModel().getSelectedItem();

            if (this.team == null) {
                // ADD operation: Create a new Team object
                this.team = new Team(
                        -1, // ID will be generated
                        carNumberField.getText().trim(),
                        teamNameField.getText().trim(),
                        selectedModel.getCarModelId(),
                        nationalityField.getText().trim(),
                        categoryField.getText()
                );
            } else {
                // EDIT operation: Update existing object
                team.carNumberProperty().set(carNumberField.getText().trim());
                team.teamNameProperty().set(teamNameField.getText().trim());
                team.carModelIdProperty().set(selectedModel.getCarModelId());
                team.nationalityProperty().set(nationalityField.getText().trim());
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
     * Returns the Team object. Used by the MasterDataViewController after a
     * successful ADD operation.
     *
     * * @return the Team object (newly created or modified)
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Validates user input.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (carNumberField.getText() == null || carNumberField.getText().trim().isEmpty()) {
            errorMessage += "Car Number is essential, you idiot!\n";
        }
        if (teamNameField.getText() == null || teamNameField.getText().trim().isEmpty()) {
            errorMessage += "Team Name is mandatory!\n";
        }
        if (nationalityField.getText() == null || nationalityField.getText().trim().isEmpty()) {
            errorMessage += "Team Nationality is mandatory!\n";
        }
        if (carModelComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Car Model must be selected or created!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Team Data");
            alert.setHeaderText("Fix these entry errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
