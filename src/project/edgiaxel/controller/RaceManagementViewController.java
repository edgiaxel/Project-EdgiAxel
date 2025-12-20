package project.edgiaxel.controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import project.edgiaxel.model.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import project.edgiaxel.dao.ResultDAO;
import java.io.IOException;
import project.edgiaxel.dao.ChampionshipDAO;
import project.edgiaxel.dao.StandingsDAO;

public class RaceManagementViewController {

    @FXML
    private Label seasonInfoLabel, circuitNameLabel, statusLabel;
    @FXML
    private TableView<RaceResultEntry> overallResultTable, hypercarResultTable, gt3ResultTable;
    @FXML
    private Button runSessionButton, nextRaceButton, autoRunButton;
    @FXML
    private ListView<String> sessionListView;
    @FXML
    private TitledPane resultPane;
    @FXML
    private ComboBox<Circuit> roundSelector;
    @FXML
    private Button finishChampionshipButton;

    private List<RaceResultEntry> hyperpoleContenders = new ArrayList<>();
    private int seasonId, year;
    private int currentRound = 0;
    private ObservableList<Circuit> calendar;
    private ObservableList<Team> activeTeams;
    private final List<String> SESSION_ORDER = Arrays.asList("FP1", "FP2", "FP3", "QUALIFYING", "HYPERPOLE", "RACE");
    private final ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
    private static final Map<String, Double> circuitBaseTimes = new HashMap<>();
    private int lastCompletedSessionIndex = -1;

    static {
        circuitBaseTimes.put("Lusail International Circuit", 100.0);
        circuitBaseTimes.put("Imola Circuit", 105.0);
        circuitBaseTimes.put("Spa Francorchamps", 130.0);
        circuitBaseTimes.put("LeMans", 200.0);
        circuitBaseTimes.put("Interlagos Circuit", 70.0);
        circuitBaseTimes.put("COTA Americas", 125.0);
        circuitBaseTimes.put("Fuji Speedway", 85.0);
        circuitBaseTimes.put("Bahrain International Circuit", 115.0);
    }

    @FXML
    public void initData(int seasonId, int year, ObservableList<Circuit> circuits, ObservableList<Team> teams) {
        this.seasonId = seasonId;
        this.year = year;
        this.calendar = circuits;
        this.activeTeams = FXCollections.observableArrayList(teams);

        // --- ADD THESE LISTENERS ---
        // 1. Listen for Round (Circuit) Changes
        roundSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleCircuitChange(newVal);
                // Also reset session selection to top for the new circuit
                sessionListView.getSelectionModel().select(0);
            }
        });

        // 2. Listen for Session (FP1, RACE, etc.) Selection
        sessionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshTableFromDatabase(); // Show data if it exists
                validateSessionButtons(newVal); // Grey out buttons if out of order
            }
        });

        roundSelector.getSelectionModel().select(0); // Trigger first round
        sessionListView.getSelectionModel().select(0); // Trigger FP1

        setupTables();
        // 1. Setup UI components
        roundSelector.setItems(calendar);
        sessionListView.setItems(FXCollections.observableArrayList(SESSION_ORDER));
        setupTables();

        // 2. Initialize the Event Listeners
        setupListeners();

        // 3. Sync state from Database (Resume Logic)
        syncProgressFromDB();

        // 4. Force initial validation for the current selection
        validateSessionButtons(sessionListView.getSelectionModel().getSelectedItem());
        if (!calendar.isEmpty()) {
            Circuit first = calendar.get(currentRound);
            circuitNameLabel.setText("CIRCUIT: " + first.getName() + " (" + first.getRaceType() + ")");
            seasonInfoLabel.setText("SEASON: " + year + " | ROUND: " + (currentRound + 1) + "/" + calendar.size());
        }
    }

    private void setupListeners() {
        roundSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleCircuitChange(newVal);
            }
        });

        sessionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                refreshTableFromDatabase();
                validateSessionButtons(newVal);
            }
        });
    }

    private void syncProgressFromDB() {
        int resumedRound = 0;
        int resumedSessionIdx = -1;

        for (int i = 0; i < calendar.size(); i++) {
            List<RaceResultEntry> raceCheck = StandingsDAO.getInstance()
                    .getSessionResults(year, calendar.get(i).getCircuitId(), "RACE");
            if (raceCheck.isEmpty()) {
                resumedRound = i;
                break;
            }
            resumedRound = i;
        }

        Circuit activeCircuit = calendar.get(resumedRound);
        for (int j = 0; j < SESSION_ORDER.size(); j++) {
            List<RaceResultEntry> sessionCheck = StandingsDAO.getInstance()
                    .getSessionResults(year, activeCircuit.getCircuitId(), SESSION_ORDER.get(j));
            if (!sessionCheck.isEmpty()) {
                resumedSessionIdx = j;
            } else {
                break;
            }
        }

        this.currentRound = resumedRound;
        this.lastCompletedSessionIndex = resumedSessionIdx;

        roundSelector.getSelectionModel().select(resumedRound);
        if (resumedSessionIdx < SESSION_ORDER.size() - 1) {
            sessionListView.getSelectionModel().select(resumedSessionIdx + 1);
        } else {
            sessionListView.getSelectionModel().select("RACE");
        }
    }

    private void handleCircuitChange(Circuit circuit) {
        int selectedRoundIdx = calendar.indexOf(circuit);
        circuitNameLabel.setText("CIRCUIT: " + circuit.getName() + " (" + circuit.getRaceType() + ")");
        seasonInfoLabel.setText("SEASON: " + year + " | ROUND: " + (selectedRoundIdx + 1) + "/" + calendar.size());

        if (selectedRoundIdx < currentRound) {
            statusLabel.setText("VIEWING HISTORY");
            sessionListView.getSelectionModel().select("RACE");
        } else {
            statusLabel.setText("CURRENT ROUND");
        }
        refreshTableFromDatabase();
    }

    private void refreshTableFromDatabase() {
        Circuit selectedCircuit = roundSelector.getValue();
        String selectedSession = sessionListView.getSelectionModel().getSelectedItem();
        if (selectedCircuit == null || selectedSession == null) {
            return;
        }

        List<RaceResultEntry> results = StandingsDAO.getInstance()
                .getSessionResults(year, selectedCircuit.getCircuitId(), selectedSession);
        updateTableViews(results);
    }

    private void validateSessionButtons(String selectedSession) {
        int selectedIdx = SESSION_ORDER.indexOf(selectedSession);
        int selectedRoundIdx = calendar.indexOf(roundSelector.getValue());

        if (selectedRoundIdx < currentRound) {
            runSessionButton.setDisable(true);
            autoRunButton.setDisable(true);
            return;
        }

        boolean isCurrentTurn = (selectedIdx == lastCompletedSessionIndex + 1);
        runSessionButton.setDisable(!isCurrentTurn);
        autoRunButton.setDisable(!isCurrentTurn);

        if (lastCompletedSessionIndex >= SESSION_ORDER.size() - 1) {
            runSessionButton.setDisable(true);
            autoRunButton.setDisable(true);
        }
    }

    private void loadRoundHistory(Circuit circuit) {
        int selectedRoundIndex = calendar.indexOf(circuit);
        if (selectedRoundIndex < currentRound) {
            statusLabel.setText("VIEWING HISTORY: ROUND " + (selectedRoundIndex + 1));
            runSessionButton.setDisable(true);
            autoRunButton.setDisable(true);
            List<RaceResultEntry> history = StandingsDAO.getInstance()
                    .getSessionResults(year, circuit.getCircuitId(), "RACE");
            updateTableViews(history);
        } else {
            statusLabel.setText("STATUS: READY FOR ROUND " + (currentRound + 1));
            runSessionButton.setDisable(false);
            autoRunButton.setDisable(false);
            loadRound(currentRound);
        }
    }

    private void loadRound(int index) {
        if (index < calendar.size()) {
            this.currentRound = index;
            Circuit c = calendar.get(index);
            circuitNameLabel.setText("CIRCUIT: " + c.getName() + " (" + c.getRaceType() + ")");
            statusLabel.setText("STATUS: READY FOR ROUND " + (index + 1));
            sessionListView.getSelectionModel().select(0);
            runSessionButton.setDisable(false);
            nextRaceButton.setDisable(true);
        }
    }

    // FIXED: Added missing method
    private void saveResultsToDB(List<RaceResultEntry> results, String sessionType) {
        // Yuuna: "Using the global seasonId so the SQL doesn't freak out!"
        ResultDAO.getInstance().saveSessionResult(this.seasonId, calendar.get(currentRound).getCircuitId(), results, sessionType);
    }

    private List<RaceResultEntry> simulateSession(String sessionType) {
        List<RaceResultEntry> results = new ArrayList<>();
        Random rand = new Random();

        // Yuura: "Iterating through activeTeams... make sure this list isn't empty!"
        for (Team team : activeTeams) {
            double baseTime = circuitBaseTimes.getOrDefault(calendar.get(currentRound).getName(), 100.0);

            // BoP and Category Logic
            double bopEffect = (100 - team.getCarModel().getBaseRating()) * 0.2;
            double categoryOffset = team.getCategory().equalsIgnoreCase("LMGT3") ? baseTime * 0.25 : 0;
            double variability = rand.nextDouble() * 2.0;
            double lapTime = baseTime + bopEffect + categoryOffset + variability;

            // DNF Logic
            boolean isDnf = sessionType.equals("RACE") && rand.nextDouble() < 0.05;
            String resultStr = isDnf ? "DNF" : formatTime(lapTime);

            // Yuura: "Passing team.getTeamId() here!"
            results.add(new RaceResultEntry(
                    0,
                    team.getCarNumber(),
                    team.getTeamName(),
                    team.getCarModel().getModelName(),
                    resultStr,
                    team.getCategory(),
                    team.getTeamId() // <--- NEW ARGUMENT
            ));
        }

        // Sort: Hypercars (Faster) then GT3
        results.sort((a, b) -> {
            if (a.getBestTimeOrLaps().equals("DNF")) {
                return 1;
            }
            if (b.getBestTimeOrLaps().equals("DNF")) {
                return -1;
            }
            return Double.compare(parseTime(a.getBestTimeOrLaps()), parseTime(b.getBestTimeOrLaps()));
        });

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setPosition(i + 1);
        }
        return results;
    }

    private String formatTime(double seconds) {
        int mins = (int) (seconds / 60);
        double secs = seconds % 60;
        return String.format("%d:%06.3f", mins, secs);
    }

    private double parseTime(String timeStr) {
        if (timeStr == null || timeStr.equals("DNF")) {
            return 9999.9;
        }
        try {
            String[] parts = timeStr.split(":");
            return (Integer.parseInt(parts[0]) * 60) + Double.parseDouble(parts[1]);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void handleRoundChange(Circuit circuit) {
        int selectedRoundIndex = calendar.indexOf(circuit);

        // Update Labels immediately
        circuitNameLabel.setText("CIRCUIT: " + circuit.getName() + " (" + circuit.getRaceType() + ")");
        seasonInfoLabel.setText("SEASON: " + year + " | ROUND: " + (selectedRoundIndex + 1) + "/" + calendar.size());

        if (selectedRoundIndex < currentRound) {
            statusLabel.setText("VIEWING HISTORY: ROUND " + (selectedRoundIndex + 1));
            List<RaceResultEntry> history = StandingsDAO.getInstance().getSessionResults(year, circuit.getCircuitId(), "RACE");
            updateTableViews(history);
            runSessionButton.setDisable(true);
            autoRunButton.setDisable(true);
            nextRaceButton.setDisable(true);
        } else {
            statusLabel.setText("STATUS: READY FOR " + SESSION_ORDER.get(lastCompletedSessionIndex + 1));
            loadRound(currentRound);
            validateSessionButtons(sessionListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void handleRunSession() {
        String session = sessionListView.getSelectionModel().getSelectedItem();
        if (session == null) {
            return;
        }

        List<RaceResultEntry> results = simulateSession(session);

        // CRITICAL: Save to DB before refreshing
        saveResultsToDB(results, session);

        lastCompletedSessionIndex = SESSION_ORDER.indexOf(session);

        if (session.equals("HYPERPOLE")) {
            awardPolePoint(results);
        }

        if (session.equals("RACE")) {
            ResultDAO.getInstance().applyRacePoints(seasonId, calendar.get(currentRound).getCircuitId(),
                    calendar.get(currentRound).getRaceType(), results);

            if (currentRound >= calendar.size() - 1) {
                statusLabel.setText("SEASON COMPLETE! CLICK FINISH!");
                finishChampionshipButton.setDisable(false);
                nextRaceButton.setDisable(true);
            } else {
                nextRaceButton.setDisable(false);
            }
        }

        // Refresh tables and move to next session
        updateTableViews(results);
        autoSelectNextSession();
    }

    private void autoSelectNextSession() {
        int currentIndex = SESSION_ORDER.indexOf(sessionListView.getSelectionModel().getSelectedItem());
        if (currentIndex != -1 && currentIndex < SESSION_ORDER.size() - 1) {
            sessionListView.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    private void handleAutoRunAll(ActionEvent event) {
        // Yuuna: "Calculate exactly how many sessions are left so we don't loop forever!"
        int currentIdx = sessionListView.getSelectionModel().getSelectedIndex();
        int totalSessions = SESSION_ORDER.size();

        for (int i = currentIdx; i < totalSessions; i++) {
            // Ensure we are selecting the right session before running
            sessionListView.getSelectionModel().select(i);
            handleRunSession();

            // If we just finished the RACE, break immediately
            if (SESSION_ORDER.get(i).equals("RACE")) {
                break;
            }
        }
        statusLabel.setText("AUTO-RUN COMPLETE: WEEKEND FINISHED");
    }

    @FXML
    private void handleNextRace(ActionEvent event) {
        if (currentRound < calendar.size() - 1) {
            // Increment the round index
            this.currentRound++;

            // Reset the session progress for the new weekend
            this.lastCompletedSessionIndex = -1;

            // FIXED: Programmatically update the ComboBox selection.
            // This triggers the listener we set up in initData() -> loadRoundHistory()
            roundSelector.getSelectionModel().select(currentRound);

            // Reset the session list view to the top (FP1)
            sessionListView.getSelectionModel().select(0);

            // Update the main round info label
            seasonInfoLabel.setText("SEASON: " + year + " | ROUND: " + (currentRound + 1) + "/" + calendar.size());

            // Disable the button again until the next RACE is finished
            nextRaceButton.setDisable(true);

            statusLabel.setText("STATUS: ARRIVED AT " + calendar.get(currentRound).getName());
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/project/edgiaxel/fxml/Dashboard.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFinishChampionship(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("End Championship");
        alert.setHeaderText("Finalizing the " + year + " Season");
        alert.setContentText("This will lock the standings and mark the season as Finished. Proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            championshipDAO.updateSeasonStatus(seasonId, "Finished");
            statusLabel.setText("CHAMPIONSHIP OFFICIALLY CONCLUDED!");
            runSessionButton.setDisable(true);
            autoRunButton.setDisable(true);
            nextRaceButton.setDisable(true);
            finishChampionshipButton.setDisable(true);

            // Transition to Standings View
            handleViewStandings(event);
        }
    }

// FIXED: Added missing navigation method
    private void handleViewStandings(ActionEvent event) {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/ViewChampionships.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleHyperpoleTransition(List<RaceResultEntry> qualiResults) {
        // Separate and get top 10 from each category
        List<RaceResultEntry> topHypercars = qualiResults.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("Hypercar") && !r.getBestTimeOrLaps().equals("DNF"))
                .limit(10)
                .collect(Collectors.toList());

        List<RaceResultEntry> topGT3 = qualiResults.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("LMGT3") && !r.getBestTimeOrLaps().equals("DNF"))
                .limit(10)
                .collect(Collectors.toList());

        hyperpoleContenders.clear();
        hyperpoleContenders.addAll(topHypercars);
        hyperpoleContenders.addAll(topGT3);

        statusLabel.setText("QUALIFYING DONE: TOP 10 ADVANCE TO HYPERPOLE");
    }

// Update your handleRunSession to award the Pole Point
    private void awardPolePoint(List<RaceResultEntry> hyperpoleResults) {
        // Top car in each category gets +1 point
        RaceResultEntry hyperPole = hyperpoleResults.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("Hypercar"))
                .findFirst().orElse(null);

        RaceResultEntry gt3Pole = hyperpoleResults.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("LMGT3"))
                .findFirst().orElse(null);

        if (hyperPole != null) {
            ResultDAO.getInstance().applyBonusPoint(seasonId, hyperPole.getCarNumber(), "Pole");
        }
        if (gt3Pole != null) {
            ResultDAO.getInstance().applyBonusPoint(seasonId, gt3Pole.getCarNumber(), "Pole");
        }
    }

    private void updateTableViews(List<RaceResultEntry> data) {
        overallResultTable.setItems(FXCollections.observableArrayList(data));
        hypercarResultTable.setItems(data.stream().filter(r -> r.getCategory().equalsIgnoreCase("Hypercar")).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        gt3ResultTable.setItems(data.stream().filter(r -> r.getCategory().equalsIgnoreCase("LMGT3")).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        overallResultTable.refresh();
        hypercarResultTable.refresh();
        gt3ResultTable.refresh();
    }

    private void setupTables() {
        setupColumns(overallResultTable);
        setupColumns(hypercarResultTable);
        setupColumns(gt3ResultTable);
    }

    private void setupColumns(TableView<RaceResultEntry> t) {
        t.getColumns().clear();
        TableColumn<RaceResultEntry, Integer> p = new TableColumn<>("POS");
        p.setCellValueFactory(new PropertyValueFactory<>("position"));
        TableColumn<RaceResultEntry, String> n = new TableColumn<>("#");
        n.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        TableColumn<RaceResultEntry, String> tm = new TableColumn<>("TEAM");
        tm.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        TableColumn<RaceResultEntry, String> ct = new TableColumn<>("CAT");
        ct.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<RaceResultEntry, String> r = new TableColumn<>("RESULT");
        r.setCellValueFactory(new PropertyValueFactory<>("bestTimeOrLaps"));
        t.getColumns().addAll(p, n, tm, ct, r);
    }
}
