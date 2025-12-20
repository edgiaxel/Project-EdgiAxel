package project.edgiaxel.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import project.edgiaxel.dao.StandingsDAO;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.StandingEntry;

import java.io.IOException;
import java.util.Optional;

public class ViewChampionshipsController {

    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private ComboBox<Circuit> circuitComboBox;
    @FXML
    private Label statusLabel;

    @FXML
    private TableView<StandingEntry> driversOverallTable;
    @FXML
    private TableView<StandingEntry> overallTable;
    @FXML
    private TableView<StandingEntry> manufacturersTable;
    @FXML
    private TableView<StandingEntry> teamHyperTable;
    @FXML
    private TableView<StandingEntry> teamGT3Table;

    @FXML
    private TabPane championshipTabPane;
    @FXML
    private Tab raceResultsTab;

    private final StandingsDAO standingsDAO = StandingsDAO.getInstance();

    @FXML
    private void initialize() {
        setupTableColumns();
        yearComboBox.setItems(standingsDAO.getAvailableYears());

        championshipTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == raceResultsTab && circuitComboBox.getValue() == null) {
                statusLabel.setText("Please select a circuit from the dropdown to see results.");
            }
        });
    }

    private void setupTableColumns() {
        configureColumns(driversOverallTable, "position", "name", "carNumber", "points"); 
        configureColumns(overallTable, "position", "name", "points", null);
        configureColumns(teamHyperTable, "position", "name", "points", null);
        configureColumns(teamGT3Table, "position", "name", "points", null);
        configureColumns(manufacturersTable, "position", "name", "points", null);
    }

    private void configureColumns(TableView<StandingEntry> table, String pos, String name, String extra, String pts) {
        table.getColumns().clear();
        TableColumn<StandingEntry, Integer> pCol = new TableColumn<>("POS");
        pCol.setCellValueFactory(new PropertyValueFactory<>(pos));

        TableColumn<StandingEntry, String> nCol = new TableColumn<>("NAME / TEAM");
        nCol.setCellValueFactory(new PropertyValueFactory<>(name));
        nCol.setPrefWidth(300);

        table.getColumns().addAll(pCol, nCol);

        if (extra != null && pts != null) {
            TableColumn<StandingEntry, String> tCol = new TableColumn<>("TEAM");
            tCol.setCellValueFactory(new PropertyValueFactory<>(extra));
            tCol.setPrefWidth(200);
            TableColumn<StandingEntry, Integer> ptsCol = new TableColumn<>("PTS");
            ptsCol.setCellValueFactory(new PropertyValueFactory<>(pts));
            table.getColumns().addAll(tCol, ptsCol);
        } else { 
            TableColumn<StandingEntry, Integer> ptsCol = new TableColumn<>("PTS");
            ptsCol.setCellValueFactory(new PropertyValueFactory<>(extra != null ? extra : "points"));
            table.getColumns().add(ptsCol);
        }
    }

    @FXML
    private void handleYearSelection() {
        Integer selectedYear = yearComboBox.getValue();
        if (selectedYear == null) {
            return;
        }

        driversOverallTable.setItems(standingsDAO.getDriverStandings(selectedYear));
        overallTable.setItems(standingsDAO.getTeamStandings(selectedYear, "Overall"));
        manufacturersTable.setItems(standingsDAO.getManufacturerStandings(selectedYear));
        teamHyperTable.setItems(standingsDAO.getTeamStandings(selectedYear, "Hypercar"));
        teamGT3Table.setItems(standingsDAO.getTeamStandings(selectedYear, "LMGT3"));

        circuitComboBox.setItems(standingsDAO.getCircuitsForYear(selectedYear));
        statusLabel.setText("Showing standings for " + selectedYear);
    }

    @FXML
    private void handleCircuitSelection(ActionEvent event) {
        Circuit selectedCircuit = circuitComboBox.getValue();
        Integer year = yearComboBox.getValue();

        if (selectedCircuit == null || year == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/RaceResultDetailsTab.fxml"));
            Parent root = loader.load();

            RaceResultDetailsTabController controller = loader.getController();
            controller.loadCircuitResults(year, selectedCircuit);

            raceResultsTab.setContent(root);
            championshipTabPane.getSelectionModel().select(raceResultsTab);
            statusLabel.setText("Loaded results for " + selectedCircuit.getName());

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading circuit details.");
        }
    }

    @FXML
    private void handleResetSeason(ActionEvent event) {
        Integer year = yearComboBox.getValue();
        if (year == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Season");
        alert.setHeaderText("Permanently delete Season " + year + "?");
        alert.setContentText("This will wipe ALL results and allow you to re-create the season from scratch.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = standingsDAO.resetSeasonData(year);
            if (success) {
                yearComboBox.setItems(standingsDAO.getAvailableYears());
                yearComboBox.getSelectionModel().clearSelection();
                clearAllTables();
                statusLabel.setText("Season " + year + " deleted completely.");
            }
        }
    }

    private void clearAllTables() {
        driversOverallTable.getItems().clear();
        overallTable.getItems().clear();
        manufacturersTable.getItems().clear();
        teamHyperTable.getItems().clear();
        teamGT3Table.getItems().clear();
        circuitComboBox.getItems().clear();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/Dashboard.fxml"));
            yearComboBox.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
