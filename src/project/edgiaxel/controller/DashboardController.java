package project.edgiaxel.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.edgiaxel.DBConnector;
import project.edgiaxel.Main; // Assuming Main class manages the initial Stage

import java.io.IOException;
import java.sql.Connection;
import javafx.scene.control.Alert;

public class DashboardController {

    @FXML
    private Button startButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button viewChampionshipsButton;
    @FXML
    private Button viewDataButton;
    @FXML
    private Button exitButton;

    // A placeholder to hold the primary stage reference if needed, though Scene switching is more common.
    private Stage primaryStage;

    // Initialize (optional, but good for checks)
    @FXML
    private void initialize() {
        // Optional: Check DB connection status here and disable buttons if disconnected
        checkDatabaseConnection();
        // The 'Resume Last Season' button should be disabled if no ongoing season exists.
        // resumeButton.setDisable(true); 
    }

    // Helper to check DB status - Crucial for OOP project
    private void checkDatabaseConnection() {
        try ( Connection conn = DBConnector.getConnection()) {
            if (conn == null || conn.isClosed()) {
                showErrorAlert("DB Connection FAILED. Disabling all functions.");
                // Disable all functional buttons if DB fails
                startButton.setDisable(true);
                resumeButton.setDisable(true);
                viewChampionshipsButton.setDisable(true);
                viewDataButton.setDisable(true);
            }
        } catch (Exception e) {
            System.err.println("DB Connection check failed: " + e.getMessage());
            startButton.setDisable(true);
            resumeButton.setDisable(true);
            viewChampionshipsButton.setDisable(true);
            viewDataButton.setDisable(true);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("Connection Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to switch scenes
    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // --- Event Handlers from FXML ---
    @FXML
    private void handleStartChampionship(ActionEvent event) {
        // Load StartChampionshipView.fxml
        switchScene(event, "/project/edgiaxel/fxml/StartChampionshipView.fxml", "Start New WEC Championship");
    }

    @FXML
    private void handleResumeChampionship(ActionEvent event) {
        // Implement logic to check and load the ongoing season state later.
        // For now, it will probably switch to the RaceManagementView for the ongoing season.
        System.out.println("RESUME: Feature to be implemented.");
        // switchScene(event, "/project/edgiaxel/fxml/RaceManagementView.fxml", "WEC Race Weekend");
    }

    @FXML
    private void handleViewChampionships(ActionEvent event) {
        // Load ViewChampionships.fxml
        switchScene(event, "/project/edgiaxel/fxml/ViewChampionships.fxml", "WEC Championship Standings");
    }

    @FXML
    private void handleViewMasterData(ActionEvent event) {
        // Load MasterDataView.fxml
        switchScene(event, "/project/edgiaxel/fxml/MasterDataView.fxml", "WEC Master Data Management");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Exit the JavaFX application gracefully
        Platform.exit();
        System.exit(0);
    }
}
