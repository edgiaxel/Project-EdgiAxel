package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.edgiaxel.dao.ChampionshipDAO;
import project.edgiaxel.dao.CircuitDAO;
import project.edgiaxel.dao.ManufacturerDAO;
import project.edgiaxel.dao.TeamDAO;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Team;

import java.io.IOException;
import java.time.Year;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StartChampionshipViewController {

    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private Label statusLabel;
    @FXML
    private Button startChampionshipButton;

    @FXML
    private TableView<CircuitWrapper> circuitTable;

    @FXML
    private TableView<ManufacturerWrapper> manufacturerTable;
    @FXML
    private TableView<TeamWrapper> teamTable;
    @FXML
    private CheckBox autoSelectDrivers;

    private final CircuitDAO circuitDAO = CircuitDAO.getInstance();
    private final ManufacturerDAO manufacturerDAO = ManufacturerDAO.getInstance();
    private final TeamDAO teamDAO = TeamDAO.getInstance();
    private final ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();

    private ObservableList<CircuitWrapper> allCircuitWrappers;
    private ObservableList<ManufacturerWrapper> allManufacturerWrappers;
    private ObservableList<TeamWrapper> allTeamWrappers;

    public static class CircuitWrapper extends Circuit {

        private final BooleanProperty selected;

        public CircuitWrapper(Circuit circuit) {
            super(circuit.getCircuitId(), circuit.getName(), circuit.getLocation(), circuit.getCountry(), circuit.getLengthKm(), circuit.getRaceType());
            this.selected = new SimpleBooleanProperty(true);
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }
    }

    public static class ManufacturerWrapper extends Manufacturer {

        private final BooleanProperty selected;
        private ObservableList<TeamWrapper> teams;

        public ManufacturerWrapper(Manufacturer m) {
            super(m.getManufacturerId(), m.getName(), m.getCountry(), m.getCategory());
            this.selected = new SimpleBooleanProperty(true);
            this.teams = FXCollections.observableArrayList();

            this.selected.addListener((obs, oldVal, newVal) -> {
                for (TeamWrapper team : teams) {
                    team.setSelected(newVal);
                }
            });
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public ObservableList<TeamWrapper> getTeams() {
            return teams;
        }
    }

    public static class TeamWrapper extends Team {

        private final BooleanProperty selected;
        private final StartChampionshipViewController controller;

        public TeamWrapper(Team team, StartChampionshipViewController controller) {
            super(team.getTeamId(), team.getCarNumber(), team.getTeamName(), team.getManufacturerId(), team.getCarModelId(), team.getNationality(), team.getCategory());
            this.setCarModel(team.getCarModel());
            this.setDrivers(team.getDrivers());
            this.selected = new SimpleBooleanProperty(true);
            this.controller = controller;

            this.selected.addListener((obs, oldVal, newVal) -> controller.validateTeamSelection(this, newVal));
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public StringProperty driverCountProperty() {
            int count = getDrivers() != null ? getDrivers().size() : 0;
            return new SimpleStringProperty(count + "/4");
        }
    }

    @FXML
    private void initialize() {
        initYearComboBox();
        setupCircuitTable();
        setupManufacturerTable();
        setupTeamTable();

        loadInitialData();

        startChampionshipButton.setDisable(true);
        statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
        statusLabel.setText("STATUS: PENDING YEAR VALIDATION");
    }

    private void initYearComboBox() {
        int currentYear = Year.now().getValue();
        for (int y = currentYear - 5; y <= currentYear + 10; y++) {
            yearComboBox.getItems().add(y);
        }
        yearComboBox.getSelectionModel().select(currentYear + 1);
    }

    private void setupCircuitTable() {
        TableColumn<CircuitWrapper, Boolean> selectCol = new TableColumn<>("Include");
        selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);
        selectCol.setPrefWidth(70);

        TableColumn<CircuitWrapper, String> nameCol = new TableColumn<>("Circuit Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(300);

        TableColumn<CircuitWrapper, String> typeCol = new TableColumn<>("Duration");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("raceType"));
        typeCol.setPrefWidth(120);

        circuitTable.getColumns().addAll(selectCol, nameCol, typeCol);
        circuitTable.setEditable(true);
    }

    private void setupManufacturerTable() {
        TableColumn<ManufacturerWrapper, Boolean> selectCol = new TableColumn<>("Include");
        selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);
        selectCol.setPrefWidth(70);

        TableColumn<ManufacturerWrapper, String> nameCol = new TableColumn<>("Manufacturer");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<ManufacturerWrapper, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(120);

        manufacturerTable.getColumns().addAll(selectCol, nameCol, catCol);
        manufacturerTable.setEditable(true);

        manufacturerTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> filterTeamTable(newVal)
        );
    }

    private void setupTeamTable() {
        TableColumn<TeamWrapper, Boolean> selectCol = new TableColumn<>("Include");
        selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);
        selectCol.setPrefWidth(70);

        TableColumn<TeamWrapper, String> numCol = new TableColumn<>("#");
        numCol.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        numCol.setPrefWidth(50);

        TableColumn<TeamWrapper, String> nameCol = new TableColumn<>("Team Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        nameCol.setPrefWidth(250);

        TableColumn<TeamWrapper, String> driversCol = new TableColumn<>("Drivers");
        driversCol.setCellValueFactory(new PropertyValueFactory<>("driverCount"));
        driversCol.setPrefWidth(70);

        teamTable.getColumns().addAll(selectCol, numCol, nameCol, driversCol);
        teamTable.setEditable(true);
    }

    private void loadInitialData() {
        ObservableList<Circuit> circuits = circuitDAO.getAllCircuits();
        allCircuitWrappers = circuits.stream().map(CircuitWrapper::new).collect(Collectors.toCollection(FXCollections::observableArrayList));
        circuitTable.setItems(allCircuitWrappers);

        ObservableList<Manufacturer> manufacturers = manufacturerDAO.getAllManufacturers();
        allManufacturerWrappers = FXCollections.observableArrayList();
        allTeamWrappers = FXCollections.observableArrayList();

        for (Manufacturer m : manufacturers) {
            ManufacturerWrapper mWrapper = new ManufacturerWrapper(m);
            ObservableList<Team> teams = teamDAO.getTeamsByManufacturerId(m.getManufacturerId());
            for (Team t : teams) {
                TeamWrapper tWrapper = new TeamWrapper(t, this);
                mWrapper.getTeams().add(tWrapper);
                allTeamWrappers.add(tWrapper);
            }
            allManufacturerWrappers.add(mWrapper);
        }
        manufacturerTable.setItems(allManufacturerWrappers);
    }

    private void filterTeamTable(ManufacturerWrapper selectedManufacturer) {
        if (selectedManufacturer != null) {
            teamTable.setItems(selectedManufacturer.getTeams());
        } else {
            teamTable.getItems().clear();
        }
    }

    @FXML
    private void handleCheckYear() {
        Integer selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
        if (selectedYear == null) {
            String text = yearComboBox.getEditor().getText();
            if (text != null && !text.trim().isEmpty()) {
                try {
                    selectedYear = Integer.parseInt(text.trim());
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "The year you entered is not a valid number.");
                    startChampionshipButton.setDisable(true);
                    statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
                    statusLabel.setText("STATUS: ERROR - Invalid Year Format.");
                    return;
                }
            }
        }

        if (selectedYear == null) {
            showAlert("Validation Error", "Please select or enter a Championship Year.");
            startChampionshipButton.setDisable(true);
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
            statusLabel.setText("STATUS: PENDING YEAR VALIDATION");
            return;
        }
        if (championshipDAO.doesSeasonExist(selectedYear)) {
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f")); 
            statusLabel.setText("STATUS: ERROR - Season " + selectedYear + " Already Exists!");
            startChampionshipButton.setDisable(true);
        } else {
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#00b386")); 
            statusLabel.setText("STATUS: VALIDATION SUCCESS - Ready to Start " + selectedYear + " Season.");
            checkOverallReadyState(); 
        }
    }

    private void validateTeamSelection(TeamWrapper team, boolean isSelected) {
        if (isSelected) {
            if (team.getDrivers().size() < 3 || team.getDrivers().size() > 4) {
                if (team.getDrivers().isEmpty()) {
                    showAlert("Team Invalid", "Team #" + team.getCarNumber() + " (" + team.getTeamName() + ") cannot be included: It has no assigned drivers. Assign 3-4 drivers in Master Data.");
                } else {
                    showAlert("Team Invalid", "Team #" + team.getCarNumber() + " (" + team.getTeamName() + ") cannot be included: Requires 3-4 drivers, currently has " + team.getDrivers().size() + ".");
                }
                if (team.isSelected()) {
                }
            }
        }
        checkOverallReadyState();
    }

    private void checkOverallReadyState() {
        startChampionshipButton.setDisable(true);
        if (!statusLabel.getText().contains("SUCCESS")) {
            return; // Year not validated
        }

        long selectedCircuitCount = allCircuitWrappers.stream().filter(CircuitWrapper::isSelected).count();
        if (selectedCircuitCount < 1) {
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
            statusLabel.setText("STATUS: ERROR - Select at least 1 Circuit.");
            return;
        }

        ObservableList<TeamWrapper> selectedTeams = allTeamWrappers.stream()
                .filter(TeamWrapper::isSelected)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (selectedTeams.isEmpty()) {
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
            statusLabel.setText("STATUS: ERROR - Select at least 1 Team Entry.");
            return;
        }

        boolean invalidDrivers = selectedTeams.stream()
                .anyMatch(t -> t.getDrivers().size() < 3 || t.getDrivers().size() > 4);

        if (invalidDrivers) {
            statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#e50f0f"));
            statusLabel.setText("STATUS: ERROR - Selected Teams must have 3-4 drivers assigned.");
            return;
        }

        statusLabel.setTextFill(javafx.scene.paint.Color.valueOf("#00b386"));
        statusLabel.setText("STATUS: READY TO START " + yearComboBox.getSelectionModel().getSelectedItem() + " SEASON.");
        startChampionshipButton.setDisable(false);
    }

    @FXML
    private void handleStartChampionship(ActionEvent event) {
        if (!startChampionshipButton.isDisable()) {
            Integer year = yearComboBox.getSelectionModel().getSelectedItem();

            ObservableList<Circuit> finalCircuits = allCircuitWrappers.stream()
                    .filter(CircuitWrapper::isSelected)
                    .map(c -> (Circuit) c) 
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            ObservableList<Team> finalTeams = allTeamWrappers.stream()
                    .filter(TeamWrapper::isSelected)
                    .map(t -> (Team) t)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            int newSeasonId = championshipDAO.createNewSeason(year, finalCircuits);

            if (newSeasonId != -1) {
                showAlert("Success", "Season " + year + " created successfully! Proceeding to the first race.");

                handleSwitchToRaceManagement(event, newSeasonId, finalCircuits, finalTeams);
            } else {
                showAlert("System Error", "Failed to start championship. Database operation failed.");
            }
        }
    }

    private void handleSwitchToRaceManagement(ActionEvent event, int seasonId, ObservableList<Circuit> circuits, ObservableList<Team> teams) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/RaceManagementView.fxml"));
            Parent root = loader.load();

            RaceManagementViewController controller = loader.getController();

            stage.setTitle("WEC Race Weekend - Round 1");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load Race Management scene: " + e.getMessage());
            e.printStackTrace();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
