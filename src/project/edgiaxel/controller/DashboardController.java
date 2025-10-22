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

    private Stage primaryStage;

    @FXML
    private void initialize() {
        checkDatabaseConnection();
    }

    private void checkDatabaseConnection() {
        try ( Connection conn = DBConnector.getConnection()) {
            if (conn == null || conn.isClosed()) {
                showErrorAlert("DB Connection FAILED. Disabling all functions.");
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

    @FXML
    private void handleStartChampionship(ActionEvent event) {
        switchScene(event, "/project/edgiaxel/fxml/StartChampionshipView.fxml", "Start New WEC Championship");
    }

    @FXML
    private void handleResumeChampionship(ActionEvent event) {
        System.out.println("RESUME: Feature to be implemented.");
    }

    @FXML
    private void handleViewChampionships(ActionEvent event) {
        switchScene(event, "/project/edgiaxel/fxml/ViewChampionships.fxml", "WEC Championship Standings");
    }

    @FXML
    private void handleViewMasterData(ActionEvent event) {
        switchScene(event, "/project/edgiaxel/fxml/MasterDataView.fxml", "WEC Master Data Management");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
