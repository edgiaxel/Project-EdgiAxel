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
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import project.edgiaxel.dao.ChampionshipDAO;
import project.edgiaxel.dao.StandingsDAO;
import project.edgiaxel.dao.TeamDAO;
import project.edgiaxel.model.ChampionshipSeason;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.Team;

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
    private final ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        checkDatabaseConnection();
    }

    private void checkDatabaseConnection() {
        try (Connection conn = DBConnector.getConnection()) {
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
        ObservableList<ChampionshipSeason> ongoingList = championshipDAO.getOngoingSeasons();

        if (ongoingList.isEmpty()) {
            showInfoAlert("No ongoing championships found! Why not start a brand new one? ✨");
            return;
        }

        ChampionshipSeason selectedSeason;

        if (ongoingList.size() == 1) {
            selectedSeason = ongoingList.get(0);
        } else {
            ChoiceDialog<ChampionshipSeason> dialog = new ChoiceDialog<>(ongoingList.get(0), ongoingList);
            dialog.setTitle("Resume Championship");
            dialog.setHeaderText("Multiple ongoing seasons detected!");
            dialog.setContentText("Which year do you want to continue, Axel?");

            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            
            Optional<ChampionshipSeason> result = dialog.showAndWait();
            if (result.isPresent()) {
                selectedSeason = result.get();
            } else {
                return; 
            }
        }

        if (selectedSeason != null) {
            ObservableList<Circuit> circuits = StandingsDAO.getInstance().getCircuitsForYear(selectedSeason.getYear());
            ObservableList<Team> teams = TeamDAO.getInstance().getAllTeams();

            switchToRaceManagement(event, selectedSeason.getSeasonId(), selectedSeason.getYear(), circuits, teams);
        }
    }

    private void switchToRaceManagement(ActionEvent event, int seasonId, int year, ObservableList<Circuit> circuits, ObservableList<Team> teams) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/RaceManagementView.fxml"));
            Parent root = loader.load();

            RaceManagementViewController controller = loader.getController();
            controller.initData(seasonId, year, circuits, teams);

            stage.setTitle("WEC Race Management - " + year);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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

    @FXML
    private void handleLogout(ActionEvent event) {
        project.edgiaxel.SessionManager.logout(); 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/LoginView.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("FIA WEC SIMULATOR - LOGIN");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
