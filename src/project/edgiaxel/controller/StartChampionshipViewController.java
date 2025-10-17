package project.edgiaxel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

public class StartChampionshipViewController implements Initializable {

    @FXML
    private ComboBox<?> yearComboBox;
    @FXML
    private Label statusLabel;
    @FXML
    private CheckBox autoSelectDrivers;
    @FXML
    private TableView<?> circuitTable;
    @FXML
    private TableView<?> manufacturerTable;
    @FXML
    private TableView<?> teamTable;
    @FXML
    private Button startChampionshipButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void handleCheckYear(ActionEvent event) {
    }

    @FXML
    private void handleStartChampionship(ActionEvent event) {
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
    }
}