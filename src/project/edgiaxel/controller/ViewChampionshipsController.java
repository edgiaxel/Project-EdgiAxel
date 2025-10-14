package project.edgiaxel.controller;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import project.edgiaxel.model.Standing;
import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Team; // Placeholder models needed here
import project.edgiaxel.model.Driver;
import project.edgiaxel.util.DBUtil; // Placeholder DAO methods

public class ViewChampionshipsController {

    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private TableView<Standing> driverHyperTable; // Placeholder model, needs a Standing class later
    @FXML
    private TableView<Standing> manufacturerHyperTable;
    @FXML
    private TableView<Standing> teamGT3Table;

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        // Dummy year list
        ObservableList<Integer> years = FXCollections.observableArrayList(2025, 2024, 2023, 2022, 2021, 2020);
        yearComboBox.setItems(years);

        // Auto-select the 2024 season
        if (years.contains(2024)) {
            yearComboBox.getSelectionModel().select(Integer.valueOf(2024));
        }

        // --- Setup Table Columns ---
        setupDriverTable();
        setupManufacturerTable();
        setupTeamTable();

        // Load data for the initial selection (2024)
        loadStandingsForSelectedYear();
    }

    @FXML
    private void handleYearSelection(ActionEvent event) {
        loadStandingsForSelectedYear();
    }

    private void loadStandingsForSelectedYear() {
        Integer selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
        if (selectedYear == null) {
            manufacturerHyperTable.getItems().clear();
            return;
        }

        // Load Manufacturer Standings (The only one implemented so far)
        ObservableList<Standing> mStandings = DBUtil.getManufacturerStandings(selectedYear);
        manufacturerHyperTable.setItems(mStandings);
        System.out.println("Loaded " + mStandings.size() + " Hypercar Manufacturer Standings for " + selectedYear);

        // Clear the rest for now since they are not implemented.
        driverHyperTable.getItems().clear();
        teamGT3Table.getItems().clear();
    }

    @FXML
    private void handleViewRaceResults(ActionEvent event) throws IOException {
        if (yearComboBox.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.WARNING, "Select a championship year first, you useless artist!").showAndWait();
            return;
        }
        System.out.println("Opening Race Results View...");
        // This is where you'd select a circuit/round and open the detailed results FXML
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event, "/project/edgiaxel/fxml/Dashboard.fxml");
    }

    // --- TABLE SETUP METHODS (Using the generic Standing model) ---
    private void setupManufacturerTable() {
        TableColumn<Standing, Integer> posCol = new TableColumn<>("POS");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        posCol.setPrefWidth(50);

        TableColumn<Standing, String> nameCol = new TableColumn<>("Manufacturer");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Standing, Integer> ptsCol = new TableColumn<>("Points");
        ptsCol.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        ptsCol.setPrefWidth(100);

        manufacturerHyperTable.getColumns().addAll(posCol, nameCol, ptsCol);
    }

    private void setupDriverTable() {
        TableColumn<Standing, Integer> posCol = new TableColumn<>("POS");
        TableColumn<Standing, String> nameCol = new TableColumn<>("Driver Trio");
        TableColumn<Standing, Integer> ptsCol = new TableColumn<>("Points");
        driverHyperTable.getColumns().addAll(posCol, nameCol, ptsCol);
    }

    private void setupTeamTable() {
        TableColumn<Standing, Integer> posCol = new TableColumn<>("POS");
        TableColumn<Standing, String> nameCol = new TableColumn<>("Team/Car #");
        TableColumn<Standing, String> modelCol = new TableColumn<>("Car Model");
        TableColumn<Standing, Integer> ptsCol = new TableColumn<>("Points");
        teamGT3Table.getColumns().addAll(posCol, nameCol, modelCol, ptsCol);
    }
}
