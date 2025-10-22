package project.edgiaxel.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.edgiaxel.dao.StandingsDAO;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.RaceResultEntry;

public class RaceResultDetailsTabController {

    @FXML
    private Label circuitHeaderLabel;
    @FXML
    private TableView<RaceResultEntry> raceResultTable;
    @FXML
    private TableView<RaceResultEntry> qualiResultTable;
    @FXML
    private TableView<RaceResultEntry> fpResultTable;
    @FXML
    private TabPane resultsTabPane;

    private final StandingsDAO standingsDAO = StandingsDAO.getInstance();

    @FXML
    private void initialize() {
        setupResultTableColumns(raceResultTable);
        setupResultTableColumns(qualiResultTable);
        setupResultTableColumns(fpResultTable);
    }

    private void setupResultTableColumns(TableView<RaceResultEntry> table) {
        TableColumn<RaceResultEntry, Integer> posCol = new TableColumn<>("POS");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        posCol.setPrefWidth(50);

        TableColumn<RaceResultEntry, String> numCol = new TableColumn<>("#");
        numCol.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        numCol.setPrefWidth(50);

        TableColumn<RaceResultEntry, String> teamCol = new TableColumn<>("TEAM");
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamCol.setPrefWidth(250);

        TableColumn<RaceResultEntry, String> modelCol = new TableColumn<>("CAR MODEL");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        modelCol.setPrefWidth(200);

        TableColumn<RaceResultEntry, String> timeCol = new TableColumn<>("TIME / LAPS");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("bestTimeOrLaps"));
        timeCol.setPrefWidth(150);

        TableColumn<RaceResultEntry, String> catCol = new TableColumn<>("CATEGORY");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(100);

        table.getColumns().addAll(posCol, numCol, teamCol, modelCol, timeCol, catCol);
    }

    public void loadRaceData(int year, Circuit circuit) {
        circuitHeaderLabel.setText(String.format("RESULTS FOR %s, %s (%s)",
                circuit.getName(),
                circuit.getCountry(),
                circuit.getRaceType()));

        ObservableList<RaceResultEntry> raceResults = standingsDAO.getRaceResults(year, circuit.getCircuitId(), "RACE_OVERALL");
        raceResultTable.setItems(raceResults);

        ObservableList<RaceResultEntry> qualiResults = standingsDAO.getRaceResults(year, circuit.getCircuitId(), "QUALIFYING");
        qualiResultTable.setItems(qualiResults);

        ObservableList<RaceResultEntry> fpResults = standingsDAO.getRaceResults(year, circuit.getCircuitId(), "FREE_PRACTICE");
        fpResultTable.setItems(fpResults);
    }
}
