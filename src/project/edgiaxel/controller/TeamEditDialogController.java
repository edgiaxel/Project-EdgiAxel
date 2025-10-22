package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.edgiaxel.dao.CarModelDAO;
import project.edgiaxel.dao.ManufacturerDAO;
import project.edgiaxel.model.CarModel;
import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Team;

import java.util.Optional;

public class TeamEditDialogController {

    @FXML private ComboBox<Manufacturer> manufacturerComboBox;
    @FXML private ComboBox<CarModel> carModelComboBox;
    @FXML private TextField carNumberField;
    @FXML private TextField teamNameField;
    @FXML private TextField nationalityField;
    @FXML private TextField categoryField;

    private Stage dialogStage;
    private Team team;
    private boolean okClicked = false;
    private final CarModelDAO carModelDAO = CarModelDAO.getInstance();

    @FXML
    private void initialize() {
        manufacturerComboBox.setItems(ManufacturerDAO.getInstance().getAllManufacturers());

        manufacturerComboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    carModelComboBox.setItems(carModelDAO.getCarModelsByManufacturerId(newVal.getManufacturerId()));
                    categoryField.setText(newVal.getCategory());
                }
            }
        );
        
        carModelComboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    Manufacturer manufacturer = manufacturerComboBox.getSelectionModel().getSelectedItem();
                    if (manufacturer != null) {
                        categoryField.setText(manufacturer.getCategory());
                    }
                }
            }
        );
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTeam(Team team) {
        this.team = team;

        Manufacturer manufacturer = ManufacturerDAO.getInstance().getAllManufacturers().stream()
                .filter(m -> m.getManufacturerId() == team.getManufacturerId())
                .findFirst().orElse(null);
        
        manufacturerComboBox.getSelectionModel().select(manufacturer);
        
        if (manufacturer != null) {
            carModelComboBox.setItems(carModelDAO.getCarModelsByManufacturerId(manufacturer.getManufacturerId()));
        }

        carNumberField.setText(team.getCarNumber());
        teamNameField.setText(team.getTeamName());
        nationalityField.setText(team.getNationality());
        categoryField.setText(team.getCategory());
        
        if (team.getCarModelId() != 0) {
            CarModel model = carModelDAO.getCarModelById(team.getCarModelId());
            carModelComboBox.getSelectionModel().select(model);
        }
        
        manufacturerComboBox.setDisable(team.getTeamId() != 0);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            CarModel selectedModel = carModelComboBox.getSelectionModel().getSelectedItem();
            
            team.setCarNumber(carNumberField.getText());
            team.setTeamName(teamNameField.getText());
            team.setNationality(nationalityField.getText());
            
            team.setManufacturerId(manufacturerComboBox.getSelectionModel().getSelectedItem().getManufacturerId());
            team.setCarModelId(selectedModel.getCarModelId());
            team.setCategory(categoryField.getText()); 

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

        if (manufacturerComboBox.getSelectionModel().isEmpty()) {
            errorMessage += "Manufacturer must be selected!\n";
        }
        if (carModelComboBox.getSelectionModel().isEmpty()) {
            errorMessage += "Car Model must be selected!\n";
        }
        if (carNumberField.getText() == null || !carNumberField.getText().matches("\\d{1,3}")) {
            errorMessage += "Car Number must be 1-3 digits!\n"; 
        }
        if (teamNameField.getText() == null || teamNameField.getText().length() < 3) {
            errorMessage += "Team Name too short!\n"; 
        }
        if (nationalityField.getText() == null || nationalityField.getText().length() < 3) {
            errorMessage += "Nationality too short!\n"; 
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