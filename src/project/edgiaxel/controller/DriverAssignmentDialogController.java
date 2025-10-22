package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.edgiaxel.dao.DriverDAO;
import project.edgiaxel.dao.TeamDAO;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.Team;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class DriverAssignmentDialogController {

    @FXML
    private Label teamLabel;
    @FXML
    private ListView<Driver> availableDriverList;
    @FXML
    private ListView<Driver> teamDriverList;
    @FXML
    private Label driverCountLabel;

    private Stage dialogStage;
    private Team team;
    private ObservableList<Driver> allDrivers;
    private ObservableList<Driver> currentTeamDrivers;
    private final TeamDAO teamDAO = TeamDAO.getInstance();
    private final DriverDAO driverDAO = DriverDAO.getInstance();

    @FXML
    private void initialize() {
        availableDriverList.setCellFactory(lv -> new ListCell<Driver>() {
            @Override
            protected void updateItem(Driver driver, boolean empty) {
                super.updateItem(driver, empty);
                setText(empty ? null : driver.getFullName() + " (" + driver.getNationality() + ")");
            }
        });
        teamDriverList.setCellFactory(lv -> new ListCell<Driver>() {
            @Override
            protected void updateItem(Driver driver, boolean empty) {
                super.updateItem(driver, empty);
                setText(empty ? null : driver.getFullName() + " (" + driver.getNationality() + ")");
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTeam(Team team) {
        this.team = team;
        teamLabel.setText("Selected Team: #" + team.getCarNumber() + " " + team.getTeamName() + " - " + team.getCategory());

        loadDriverData();
        updateLists();
    }

    private void loadDriverData() {
        allDrivers = driverDAO.getAllDrivers();
        currentTeamDrivers = FXCollections.observableArrayList(teamDAO.getDriversForTeam(team.getTeamId()));
    }

    private void updateLists() {
        ObservableList<Driver> available = FXCollections.observableArrayList(allDrivers);
        available.removeAll(currentTeamDrivers);

        availableDriverList.setItems(available);
        teamDriverList.setItems(currentTeamDrivers);
        updateDriverCount();
    }

    private void updateDriverCount() {
        int count = currentTeamDrivers.size();
        driverCountLabel.setText("Total: " + count + " / 4 (Min 3 / Max 4)");

        if (count < 3 || count > 4) {
            driverCountLabel.setStyle("-fx-text-fill: #e50f0f; -fx-font-weight: bold;"); 
        } else {
            driverCountLabel.setStyle("-fx-text-fill: #00b386; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleAssignDriver() {
        Driver selectedDriver = availableDriverList.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            if (currentTeamDrivers.size() < 4) {
                currentTeamDrivers.add(selectedDriver);
                updateLists();
            } else {
                showAlert("Limit Reached", "A team cannot have more than 4 drivers.");
            }
        }
    }

    @FXML
    private void handleRemoveDriver() {
        Driver selectedDriver = teamDriverList.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            currentTeamDrivers.remove(selectedDriver);
            updateLists();
        }
    }

    @FXML
    private void handleNewDriver() {
        Driver newDriver = new Driver(0, "", "", "");
        boolean okClicked = showDriverSimpleEditDialog(newDriver);
        if (okClicked) {
            if (driverDAO.insertDriver(newDriver)) {
                loadDriverData(); 
                updateLists();
            } else {
                showAlert("Error", "Failed to add new driver to the database.");
            }
        }
    }

    @FXML
    private void handleSave() {
        int count = currentTeamDrivers.size();
        if (count < 3 || count > 4) {
            showAlert("Driver Count Error", "A team must have between 3 and 4 drivers to be saved!");
            return;
        }

        if (teamDAO.saveTeamDrivers(team.getTeamId(), currentTeamDrivers)) {
            showAlert("Success", "Driver assignments saved successfully.");
            dialogStage.close();
        } else {
            showAlert("Error", "Failed to save driver assignments to the database.");
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showDriverSimpleEditDialog(Driver driver) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/DriverSimpleEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Driver");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.dialogStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DriverSimpleEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDriver(driver);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
