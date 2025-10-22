package project.edgiaxel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class RaceManagementViewController implements Initializable {

    @FXML
    private Label seasonInfoLabel;
    @FXML
    private Label circuitNameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ListView<?> sessionListView;
    @FXML
    private Button runSessionButton;
    @FXML
    private Button nextRaceButton;
    @FXML
    private Button autoRunButton;
    @FXML
    private TitledPane resultPane;
    @FXML
    private TableView<?> overallResultTable;
    @FXML
    private TableView<?> hypercarResultTable;
    @FXML
    private TableView<?> gt3ResultTable;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void handleRunSession(ActionEvent event) {
    }

    @FXML
    private void handleNextRace(ActionEvent event) {
    }

    @FXML
    private void handleFinishChampionship(ActionEvent event) {
    }

    @FXML
    private void handleAutoRunAll(ActionEvent event) {
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            stage.setTitle("FIA WEC SIMULATOR");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}