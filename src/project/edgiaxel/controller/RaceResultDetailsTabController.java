package project.edgiaxel.controller;

import javafx.collections.FXCollections;
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

import java.util.stream.Collectors;

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

    public void loadCircuitResults(int year, Circuit circuit) {
        if (circuit == null) {
            return;
        }

        circuitHeaderLabel.setText("RESULTS FOR " + circuit.getName().toUpperCase() + " - " + circuit.getRaceType());

        ObservableList<RaceResultEntry> raceResults = standingsDAO.getSessionResults(year, circuit.getCircuitId(), "RACE");
        ObservableList<RaceResultEntry> qualiResults = standingsDAO.getSessionResults(year, circuit.getCircuitId(), "QUALIFYING");
        ObservableList<RaceResultEntry> fpResults = standingsDAO.getSessionResults(year, circuit.getCircuitId(), "FP%");

        raceResultTable.setItems(raceResults);
        qualiResultTable.setItems(qualiResults);
        fpResultTable.setItems(fpResults);

        raceResultTable.refresh();
        qualiResultTable.refresh();
        fpResultTable.refresh();
    }

    private void setupResultTableColumns(TableView<RaceResultEntry> table) {
        table.getColumns().clear();

        TableColumn<RaceResultEntry, Integer> posCol = new TableColumn<>("POS");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        posCol.setPrefWidth(50);

        TableColumn<RaceResultEntry, String> numCol = new TableColumn<>("#");
        numCol.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        numCol.setPrefWidth(50);

        TableColumn<RaceResultEntry, String> teamCol = new TableColumn<>("TEAM");
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamCol.setPrefWidth(220);

        TableColumn<RaceResultEntry, String> modelCol = new TableColumn<>("CAR MODEL");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        modelCol.setPrefWidth(180);

        TableColumn<RaceResultEntry, String> timeCol = new TableColumn<>("TIME / LAPS");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("bestTimeOrLaps"));
        timeCol.setPrefWidth(120);

        TableColumn<RaceResultEntry, String> catCol = new TableColumn<>("CAT");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(80);

        table.getColumns().addAll(posCol, numCol, teamCol, modelCol, timeCol, catCol);
    }
}
