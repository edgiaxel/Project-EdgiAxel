package project.edgiaxel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

public class ViewChampionshipsController implements Initializable {

    @FXML
    private ComboBox<?> yearComboBox;
    @FXML
    private TabPane championshipTabPane;
    @FXML
    private TableView<?> overallTable;
    @FXML
    private TableView<?> manufacturersTable;
    @FXML
    private TableView<?> teamHyperTable;
    @FXML
    private TableView<?> teamGT3Table;
    @FXML
    private ComboBox<?> circuitComboBox;
    @FXML
    private Tab raceResultsTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleYearSelection(ActionEvent event) {
    }

    @FXML
    private void handleCircuitSelection(ActionEvent event) {
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
    }
}
