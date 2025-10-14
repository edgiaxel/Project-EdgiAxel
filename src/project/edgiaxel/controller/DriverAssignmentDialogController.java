package project.edgiaxel.controller;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.Team;
import project.edgiaxel.util.DBUtil;

public class DriverAssignmentDialogController {

    @FXML
    private Label teamLabel;
    @FXML
    private Label driverCountLabel;
    @FXML
    private ListView<Driver> availableDriverList;
    @FXML
    private ListView<Driver> teamDriverList;

    private Stage dialogStage;
    private Team team;
    private ObservableList<Driver> allDrivers;
    private ObservableList<Driver> currentRoster;
    private boolean changesMade = false;

    // --- SETUP ---
    @FXML
    private void initialize() {
        // Driver count constraints for WEC
        updateDriverCountLabel(0);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTeam(Team team) {
        this.team = team;
        teamLabel.setText(String.format("Selected Team: #%s %s", team.getCarNumber(), team.getTeamName()));

        // 1. Load All Drivers (Mutable Master List)
        allDrivers = DBUtil.getAllDrivers();

        // 2. Load Current Roster (Mutable List)
        // NOTE: This MUST be a mutable copy, not the one directly from DBUtil if the DBUtil returns an immutable list.
        currentRoster = DBUtil.getDriversByTeam(team.getTeamId());

        // 3. Create a SEPARATE, MUTABLE list for the Available Drivers
        ObservableList<Driver> mutableAvailableDrivers = FXCollections.observableArrayList(allDrivers);

        // 4. Remove current team members from the mutable list.
        // This is much safer than relying on FilteredList for a two-list transfer.
        mutableAvailableDrivers.removeAll(currentRoster);

        // 5. Set the lists to the ListViews
        availableDriverList.setItems(mutableAvailableDrivers);
        teamDriverList.setItems(currentRoster);

        updateDriverCountLabel(currentRoster.size());
    }

    // --- LOGIC ---
    private void updateDriverCountLabel(int count) {
        driverCountLabel.setText(String.format("Total: %d/3 (Min/Max)", count));
        if (count < 3 || count > 4) { // Warning if not 3 or 4, 4 is usually Le Mans/placeholder
            driverCountLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            driverCountLabel.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    private void handleAssignDriver() {
        Driver selectedDriver = availableDriverList.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            // Move driver from available to team roster
            currentRoster.add(selectedDriver);
            availableDriverList.getItems().remove(selectedDriver);
            updateDriverCountLabel(currentRoster.size());
            changesMade = true;
        }
    }

    @FXML
    private void handleRemoveDriver() {
        Driver selectedDriver = teamDriverList.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            // Move driver from team roster back to available
            availableDriverList.getItems().add(selectedDriver);
            teamDriverList.getItems().remove(selectedDriver);
            updateDriverCountLabel(currentRoster.size());
            changesMade = true;
        }
    }

    @FXML
    private void handleNewDriver() {
        // Open the small dialog to add a new driver globally
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/DriverSimpleEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage newDriverStage = new Stage();
            newDriverStage.setTitle("Add New Global Driver");
            newDriverStage.initModality(Modality.WINDOW_MODAL);
            newDriverStage.initOwner(dialogStage);
            newDriverStage.setScene(new Scene(page));

            DriverSimpleEditDialogController controller = loader.getController();
            controller.setDialogStage(newDriverStage);

            newDriverStage.showAndWait();

            if (controller.isOkClicked()) {
                Driver newDriver = controller.getNewDriver();
                DBUtil.insertDriver(newDriver);

                // Add new driver to both master list and available list
                allDrivers.add(newDriver);
                availableDriverList.getItems().add(newDriver);
                availableDriverList.getSelectionModel().select(newDriver); // Select the new driver
            }
        } catch (IOException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add new driver: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    // --- SAVE/CANCEL ---
    @FXML
    private void handleSave() {
        if (currentRoster.size() < 3) {
            new Alert(Alert.AlertType.WARNING, "Roster must have at least 3 drivers for WEC!").showAndWait();
            return;
        }

        if (changesMade) {
            try {
                // Perform the commit: DELETE old roster and INSERT new one
                DBUtil.updateTeamRoster(team.getTeamId(), currentRoster);
                team.setDrivers(currentRoster); // Update the underlying model list
                new Alert(Alert.AlertType.INFORMATION, "Team Roster for #" + team.getCarNumber() + " updated successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save roster changes: " + e.getMessage()).showAndWait();
                return;
            }
        }
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
