package project.edgiaxel.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.edgiaxel.dao.ChampionshipDAO;
import project.edgiaxel.dao.CircuitDAO;
import project.edgiaxel.dao.StandingsDAO;
import project.edgiaxel.model.ChampionshipSeason;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.StandingEntry;
import project.edgiaxel.model.RaceResultEntry;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;

public class ViewChampionshipsController {

    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private ComboBox<Circuit> circuitComboBox;
    @FXML
    private TabPane championshipTabPane;
    @FXML
    private Tab raceResultsTab;

    @FXML
    private TableView<StandingEntry> overallTable;
    @FXML
    private TableView<StandingEntry> manufacturersTable;
    @FXML
    private TableView<StandingEntry> teamHyperTable;
    @FXML
    private TableView<StandingEntry> teamGT3Table;

    private final ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
    private final CircuitDAO circuitDAO = CircuitDAO.getInstance();
    private final StandingsDAO standingsDAO = StandingsDAO.getInstance();

    private List<Circuit> selectedSeasonCircuits; 
    
    @FXML
    private void initialize() {
        setupStandingsTables();

        loadSeasons();
    }

    private void setupStandingsTables() {
        TableColumn<StandingEntry, Integer> manufacturersPosCol = new TableColumn<>("POS");
        manufacturersPosCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        manufacturersPosCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> manufacturersNameCol = new TableColumn<>("MANUFACTURER");
        manufacturersNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        manufacturersNameCol.setPrefWidth(400);

        TableColumn<StandingEntry, Integer> manufacturersPtsCol = new TableColumn<>("POINTS");
        manufacturersPtsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        manufacturersPtsCol.setPrefWidth(100);

        manufacturersTable.getColumns().setAll(manufacturersPosCol, manufacturersNameCol, manufacturersPtsCol);

        PropertyValueFactory<StandingEntry, String> carNumFactory = new PropertyValueFactory<>("carNumber");
        PropertyValueFactory<StandingEntry, String> catFactory = new PropertyValueFactory<>("category");
        PropertyValueFactory<StandingEntry, String> nameFactory = new PropertyValueFactory<>("name");
        PropertyValueFactory<StandingEntry, Integer> posFactory = new PropertyValueFactory<>("position");
        PropertyValueFactory<StandingEntry, Integer> ptsFactory = new PropertyValueFactory<>("points");

        TableColumn<StandingEntry, Integer> hyperPosCol = new TableColumn<>("POS");
        hyperPosCol.setCellValueFactory(posFactory);
        hyperPosCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> hyperNumCol = new TableColumn<>("#");
        hyperNumCol.setCellValueFactory(carNumFactory);
        hyperNumCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> hyperNameCol = new TableColumn<>("TEAM NAME");
        hyperNameCol.setCellValueFactory(nameFactory);
        hyperNameCol.setPrefWidth(350);

        TableColumn<StandingEntry, Integer> hyperPtsCol = new TableColumn<>("POINTS");
        hyperPtsCol.setCellValueFactory(ptsFactory);
        hyperPtsCol.setPrefWidth(100);

        teamHyperTable.getColumns().setAll(hyperPosCol, hyperNumCol, hyperNameCol, hyperPtsCol);

        TableColumn<StandingEntry, Integer> gt3PosCol = new TableColumn<>("POS");
        gt3PosCol.setCellValueFactory(posFactory);
        gt3PosCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> gt3NumCol = new TableColumn<>("#");
        gt3NumCol.setCellValueFactory(carNumFactory);
        gt3NumCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> gt3NameCol = new TableColumn<>("TEAM NAME");
        gt3NameCol.setCellValueFactory(nameFactory);
        gt3NameCol.setPrefWidth(350);

        TableColumn<StandingEntry, Integer> gt3PtsCol = new TableColumn<>("POINTS");
        gt3PtsCol.setCellValueFactory(ptsFactory);
        gt3PtsCol.setPrefWidth(100);

        teamGT3Table.getColumns().setAll(gt3PosCol, gt3NumCol, gt3NameCol, gt3PtsCol);

        TableColumn<StandingEntry, Integer> overallPosCol = new TableColumn<>("POS");
        overallPosCol.setCellValueFactory(posFactory);
        overallPosCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> overallNumCol = new TableColumn<>("#");
        overallNumCol.setCellValueFactory(carNumFactory);
        overallNumCol.setPrefWidth(50);

        TableColumn<StandingEntry, String> overallNameCol = new TableColumn<>("ENTRY NAME");
        overallNameCol.setCellValueFactory(nameFactory);
        overallNameCol.setPrefWidth(300);

        TableColumn<StandingEntry, String> overallCatCol = new TableColumn<>("CAT");
        overallCatCol.setCellValueFactory(catFactory);
        overallCatCol.setPrefWidth(80);

        TableColumn<StandingEntry, Integer> overallPtsCol = new TableColumn<>("POINTS");
        overallPtsCol.setCellValueFactory(ptsFactory);
        overallPtsCol.setPrefWidth(100);

        overallTable.getColumns().setAll(overallPosCol, overallNumCol, overallNameCol, overallCatCol, overallPtsCol);
    }

    private void loadSeasons() {
        ObservableList<ChampionshipSeason> seasons = championshipDAO.getAllSeasons();

        ObservableList<Integer> years = seasons.stream()
                .map(ChampionshipSeason::getYear)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        yearComboBox.setItems(years);

        if (!years.isEmpty()) {
            yearComboBox.getSelectionModel().select(0);
            handleYearSelection();
        }
    }

    // --- Event Handlers ---
    @FXML
    private void handleYearSelection() {
        Integer selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
        if (selectedYear == null) {
            return;
        }

        selectedSeasonCircuits = circuitDAO.getAllCircuits(); 
        circuitComboBox.setItems(FXCollections.observableArrayList(selectedSeasonCircuits));
        circuitComboBox.getSelectionModel().clearSelection();

        loadStandingsData(selectedYear);
    }

    private void loadStandingsData(int year) {
        int mockSeasonId = 2025;

        manufacturersTable.setItems(standingsDAO.getChampionshipStandings(mockSeasonId, "MANUFACTURER"));

        ObservableList<StandingEntry> hyperTeams = standingsDAO.getChampionshipStandings(mockSeasonId, "HYPERCAR_TEAM");
        ObservableList<StandingEntry> gt3Teams = standingsDAO.getChampionshipStandings(mockSeasonId, "LMGT3_TEAM");

        teamHyperTable.setItems(hyperTeams);
        teamGT3Table.setItems(gt3Teams);

        ObservableList<StandingEntry> overallTeams = FXCollections.observableArrayList();

        // Copy all entries from both lists
        overallTeams.addAll(hyperTeams.stream()
                .map(e -> new StandingEntry(e.getPosition(), e.getName(), e.getPoints(), e.getCategory(), e.getCarNumber()))
                .collect(Collectors.toList()));

        overallTeams.addAll(gt3Teams.stream()
                .map(e -> new StandingEntry(e.getPosition(), e.getName(), e.getPoints(), e.getCategory(), e.getCarNumber()))
                .collect(Collectors.toList()));

        overallTeams.sort(Comparator.comparing(StandingEntry::getPoints).reversed());

        for (int i = 0; i < overallTeams.size(); i++) {
            overallTeams.get(i).setPosition(i + 1);
        }

        overallTable.setItems(overallTeams);

        raceResultsTab.setContent(new Label("Select a Circuit from the dropdown above to view Race Results (FP, Quali, Race)."));
        raceResultsTab.setText("RACE RESULTS (Select Circuit)");
        championshipTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void handleCircuitSelection() {
        Circuit selectedCircuit = circuitComboBox.getSelectionModel().getSelectedItem();
        Integer selectedYear = yearComboBox.getSelectionModel().getSelectedItem();

        if (selectedCircuit == null || selectedYear == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/RaceResultDetailsTab.fxml"));
            AnchorPane content = loader.load();

            RaceResultDetailsTabController controller = loader.getController();
            controller.loadRaceData(selectedYear, selectedCircuit);

            raceResultsTab.setContent(content);
            raceResultsTab.setText(selectedCircuit.getName() + " Race Results");
            championshipTabPane.getSelectionModel().select(raceResultsTab);

        } catch (IOException e) {
            System.err.println("Failed to load Race Result Details: " + e.getMessage());
        }
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
