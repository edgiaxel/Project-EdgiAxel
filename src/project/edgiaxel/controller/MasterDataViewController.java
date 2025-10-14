package project.edgiaxel.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Circuit;
import project.edgiaxel.model.Team;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.CarModel;
import project.edgiaxel.util.DBUtil;

public class MasterDataViewController {

    @FXML
    private TableView<Manufacturer> manufacturerTable;
    @FXML
    private TableView<Team> teamTable;
    @FXML
    private TableView<Driver> driverTable;
    @FXML
    private TableView<Circuit> circuitTable;

    // List to hold all teams, used for filtering by manufacturer
    private ObservableList<Team> allTeams = FXCollections.observableArrayList();
    private ObservableList<CarModel> allCarModels = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Step 1: Initialize all data loading
        loadAllData();

        // Step 2: Set up all columns
        setupManufacturerTable();
        setupTeamTable();
        setupDriverTable();
        setupCircuitTable();

        // Step 3: Set up event listeners (The logic glue)
        manufacturerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeamsByManufacturer(newValue));

        teamTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDriversForSelectedTeam(newValue));
    }

    // --- DATA LOADING & INITIAL SETUP ---
    // Inside MasterDataViewController.java
// --- DATA LOADING & INITIAL SETUP (UPDATED) ---
    private void loadAllData() {
        // Clear all lists before refreshing
        allTeams.clear();
        allCarModels.clear(); // <-- NEW LINE

        // Load and set data.
        manufacturerTable.setItems(DBUtil.getAllManufacturers());
        allTeams.addAll(DBUtil.getAllTeams());
        allCarModels.addAll(DBUtil.getAllCarModels()); // <-- NEW LINE
        circuitTable.setItems(DBUtil.getAllCircuits());

        // Clear filtered views
        teamTable.getItems().clear();
        driverTable.getItems().clear();

        System.out.println("Loaded/Refreshed All Master Data. Total teams loaded: " + allTeams.size());
    }

    private void setupManufacturerTable() {
        TableColumn<Manufacturer, Integer> mIdCol = new TableColumn<>("ID");
        mIdCol.setCellValueFactory(cellData -> cellData.getValue().manufacturerIdProperty().asObject());
        mIdCol.setPrefWidth(50);

        TableColumn<Manufacturer, String> mNameCol = new TableColumn<>("Manufacturer Name");
        mNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        mNameCol.setPrefWidth(200);

        TableColumn<Manufacturer, String> mCategoryCol = new TableColumn<>("Category");
        mCategoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        manufacturerTable.getColumns().addAll(mIdCol, mNameCol, mCategoryCol);
    }

    private void setupTeamTable() {
        TableColumn<Team, String> carNumCol = new TableColumn<>("Car #");
        carNumCol.setCellValueFactory(cellData -> cellData.getValue().carNumberProperty());
        carNumCol.setPrefWidth(60);

        TableColumn<Team, String> teamNameCol = new TableColumn<>("Team Name");
        teamNameCol.setCellValueFactory(cellData -> cellData.getValue().teamNameProperty());
        teamNameCol.setPrefWidth(250);

        TableColumn<Team, String> modelCol = new TableColumn<>("Car Model");
        // This column needs special handling to display the CarModel name instead of just the ID
        modelCol.setCellValueFactory(cellData -> {
            int carModelId = ((Team) cellData.getValue()).carModelId.get();
            CarModel model = DBUtil.getCarModelById(carModelId);
            return model != null ? model.modelNameProperty() : new SimpleStringProperty("UNKNOWN");
        });
        modelCol.setPrefWidth(200);

        TableColumn<Team, String> teamNatCol = new TableColumn<>("Nationality");
        teamNatCol.setCellValueFactory(cellData -> cellData.getValue().nationalityProperty());

        teamTable.getColumns().addAll(carNumCol, teamNameCol, modelCol, teamNatCol);
    }

    private void setupDriverTable() {
        TableColumn<Driver, String> dFirstCol = new TableColumn<>("First Name");
        dFirstCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<Driver, String> dLastCol = new TableColumn<>("Last Name");
        dLastCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Driver, String> dNatCol = new TableColumn<>("Nationality");
        dNatCol.setCellValueFactory(cellData -> cellData.getValue().nationalityProperty());

        driverTable.getColumns().addAll(dFirstCol, dLastCol, dNatCol);
    }

    private void setupCircuitTable() {
        TableColumn<Circuit, Integer> cIdCol = new TableColumn<>("ID");
        cIdCol.setCellValueFactory(cellData -> cellData.getValue().circuitIdProperty().asObject());
        cIdCol.setPrefWidth(50);

        TableColumn<Circuit, String> cNameCol = new TableColumn<>("Circuit Name");
        cNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        cNameCol.setPrefWidth(200);

        TableColumn<Circuit, Number> cLengthCol = new TableColumn<>("Length (km)");
        cLengthCol.setCellValueFactory(cellData -> cellData.getValue().lengthKmProperty());
        cLengthCol.setPrefWidth(100);

        TableColumn<Circuit, String> cRaceTypeCol = new TableColumn<>("Race Type");
        cRaceTypeCol.setCellValueFactory(cellData -> cellData.getValue().raceTypeProperty());

        circuitTable.getColumns().addAll(cIdCol, cNameCol, cLengthCol, cRaceTypeCol);
    }

    // --- HIERARCHICAL LISTENER LOGIC ---
    // 1. Filter teams based on selected manufacturer
    private void filterTeamsByManufacturer(Manufacturer manufacturer) {
        if (manufacturer == null) {
            teamTable.setItems(null);
            driverTable.setItems(null);
            return;
        }

        // Filter all teams to only show ones belonging to the selected manufacturer
        ObservableList<Team> filteredTeams = allTeams.filtered(
                team -> {
                    CarModel model = DBUtil.getCarModelById(team.carModelId.get());
                    return model != null && model.getManufacturerId() == manufacturer.getManufacturerId();
                }
        );
        teamTable.setItems(filteredTeams);
        driverTable.setItems(null); // Clear driver table when manufacturer changes
    }

    // 2. Show drivers based on selected team
    private void showDriversForSelectedTeam(Team team) {
        if (team == null) {
            driverTable.setItems(null);
            return;
        }

        // Fetch drivers from DB using the team ID
        ObservableList<Driver> drivers = DBUtil.getDriversByTeam(team.getTeamId());
        driverTable.setItems(drivers);

        // Throw a fit if the data is wrong
        if (drivers.size() < 3 || drivers.size() > 4) { // Real WEC usually uses 3, sometimes 4 for Le Mans or placeholders
            new Alert(Alert.AlertType.WARNING, "TEAM " + team.getCarNumber() + " HAS " + drivers.size() + " DRIVERS! YOU NEED 3 MINIMUM FOR THE SIMULATOR, FIX IT!").showAndWait();
        }
    }

    // --- NAVIGATION & CRUD PLACEHOLDERS (ADD THE NEW TEAM/DRIVER PLACEHOLDERS) ---
    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event, "/project/edgiaxel/fxml/Dashboard.fxml");
    }

    // MANUFACTURER CRUD
    // --- DIALOG HELPER ---
    /**
     * Opens a dialog to edit details for the specified Manufacturer.
     *
     * @param manufacturer the manufacturer object to edit, or null for a new
     * one.
     * @return true if the user clicked OK, false otherwise.
     */
    private boolean showManufacturerEditDialog(Manufacturer manufacturer) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/project/edgiaxel/fxml/ManufacturerEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(manufacturer == null ? "Add New Manufacturer" : "Edit Manufacturer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(manufacturerTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the manufacturer into the controller.
            ManufacturerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setManufacturer(manufacturer);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load dialog FXML, check path!").showAndWait();
            e.printStackTrace();
            return false;
        }
    }

// --- MANUFACTURER CRUD IMPLEMENTATION ---
    @FXML
    void handleAddManufacturer(ActionEvent event) {
        // 1. Create a dummy object to hold new data
        Manufacturer newManufacturer = new Manufacturer(-1, "", "", "");

        // 2. Show the dialog and check for OK click
        if (showManufacturerEditDialog(newManufacturer)) {
            try {
                // 3. Save to DB
                DBUtil.insertManufacturer(newManufacturer);
                new Alert(Alert.AlertType.INFORMATION, "Manufacturer " + newManufacturer.getName() + " added!").showAndWait();

                // 4. Reload the data tables
                loadAllData();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during INSERT: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void handleEditManufacturer(ActionEvent event) {
        Manufacturer selected = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a manufacturer first, dumbass.").showAndWait();
            return;
        }

        // 1. Show the dialog with the existing object
        if (showManufacturerEditDialog(selected)) {
            try {
                // 2. Update the DB with the modified object
                DBUtil.updateManufacturer(selected);
                new Alert(Alert.AlertType.INFORMATION, "Manufacturer " + selected.getName() + " updated!").showAndWait();

                // 3. Force table refresh (since we edited the existing object)
                manufacturerTable.getColumns().get(0).setVisible(false);
                manufacturerTable.getColumns().get(0).setVisible(true);

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during UPDATE: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void handleDeleteManufacturer(ActionEvent event) {
        Manufacturer selected = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pick something to delete, or are you just wasting time?").showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("DANGER ZONE");
        confirmation.setHeaderText("ARE YOU SURE, AXEL?!");
        confirmation.setContentText("Deleting " + selected.getName() + " will DELETE ALL ASSOCIATED TEAMS AND CARS. THIS IS PERMANENT.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DBUtil.deleteManufacturer(selected.getManufacturerId());
                new Alert(Alert.AlertType.INFORMATION, "Manufacturer " + selected.getName() + " and its teams have been WIPED OUT.").showAndWait();

                // Reload everything since the deletion was cascading
                loadAllData();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during DELETE: " + e.getMessage()).showAndWait();
            }
        }
    }

    // TEAM CRUD
    /**
     * Opens a dialog to edit details for the specified Team.
     */
    private Optional<Team> showTeamEditDialog(Team team, Manufacturer manufacturer) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/project/edgiaxel/fxml/TeamEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(team == null ? "Add New Team" : "Edit Team");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(teamTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the team, manufacturer, and all car models into the controller.
            TeamEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTeamAndManufacturer(team, manufacturer, allCarModels);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                // If adding, the controller created the object; return it.
                return Optional.ofNullable(team == null ? controller.getTeam() : team);
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load Team dialog FXML, check path!").showAndWait();
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @FXML
    void handleAddTeam(ActionEvent event) {
        Manufacturer parentManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (parentManufacturer == null) {
            new Alert(Alert.AlertType.WARNING, "Select a Manufacturer first! Every team needs a brand.").showAndWait();
            return;
        }

        // Show dialog and get back the new Team object wrapped in Optional
        Optional<Team> result = showTeamEditDialog(null, parentManufacturer);
        result.ifPresent(newTeam -> {
            try {
                DBUtil.insertTeam(newTeam);
                new Alert(Alert.AlertType.INFORMATION, "Team " + newTeam.getTeamName() + " added!").showAndWait();
                loadAllData();
                // Re-select the parent manufacturer to show the new team
                manufacturerTable.getSelectionModel().select(parentManufacturer);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during TEAM INSERT: " + e.getMessage()).showAndWait();
            }
        });
    }

    @FXML
    void handleEditTeam(ActionEvent event) {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        Manufacturer parentManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            new Alert(Alert.AlertType.WARNING, "Select a team to edit, you slowpoke.").showAndWait();
            return;
        }

        Optional<Team> result = showTeamEditDialog(selectedTeam, parentManufacturer);
        result.ifPresent(updatedTeam -> {
            try {
                DBUtil.updateTeam(updatedTeam);
                new Alert(Alert.AlertType.INFORMATION, "Team #" + updatedTeam.getCarNumber() + " updated!").showAndWait();

                // Refresh the Team table view
                teamTable.getColumns().get(0).setVisible(false);
                teamTable.getColumns().get(0).setVisible(true);

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during TEAM UPDATE: " + e.getMessage()).showAndWait();
            }
        });
    }

    @FXML
    void handleDeleteTeam(ActionEvent event) {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            new Alert(Alert.AlertType.WARNING, "Select a team to delete first!").showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("TEAM DELETION");
        confirmation.setHeaderText("WIPING OUT CAR #" + selectedTeam.getCarNumber());
        confirmation.setContentText("Deleting this team will REMOVE all associated drivers from this car. Continue?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DBUtil.deleteTeam(selectedTeam.getTeamId());
                new Alert(Alert.AlertType.INFORMATION, "Team #" + selectedTeam.getCarNumber() + " deleted.").showAndWait();
                loadAllData();
                // Re-select the parent manufacturer to refresh the filtered team list
                manufacturerTable.getSelectionModel().select(manufacturerTable.getSelectionModel().getSelectedItem());

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during TEAM DELETE: " + e.getMessage()).showAndWait();
            }
        }
    }

    // DRIVER CRUD (Only need add/assign driver here, editing/deleting individual drivers is complex)
    /**
     * Opens the dialog to manage drivers for the selected team.
     */
    private void showDriverAssignmentDialog(Team team) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/project/edgiaxel/fxml/DriverAssignmentDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Manage Roster for Car #" + team.getCarNumber());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(driverTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the team object into the controller.
            DriverAssignmentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTeam(team);

            dialogStage.showAndWait();

            // Reload the drivers for the current team after the dialog closes
            showDriversForSelectedTeam(team);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load Driver Assignment dialog FXML, check path!").showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddDriver(ActionEvent event) {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            new Alert(Alert.AlertType.WARNING, "Select a team first! You can't assign a driver to thin air.").showAndWait();
            return;
        }

        showDriverAssignmentDialog(selectedTeam);
    }

    // CIRCUIT CRUD
    // Inside MasterDataViewController.java (Add this helper method)
    /**
     * Opens a dialog to edit details for the specified Circuit.
     */
    private boolean showCircuitEditDialog(Circuit circuit) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/project/edgiaxel/fxml/CircuitEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(circuit == null ? "Add New Circuit" : "Edit Circuit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(circuitTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the circuit into the controller.
            CircuitEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCircuit(circuit);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load Circuit dialog FXML, check path!").showAndWait();
            e.printStackTrace();
            return false;
        }
    }

// Inside MasterDataViewController.java (Replace the placeholder CRUD methods)
// --- CIRCUIT CRUD IMPLEMENTATION ---
    @FXML
    void handleAddCircuit(ActionEvent event) {
        // 1. Create a dummy object to hold new data
        Circuit newCircuit = new Circuit(-1, "", "", "", 0.0, "6 Hours");

        // 2. Show the dialog and check for OK click
        if (showCircuitEditDialog(newCircuit)) {
            try {
                // 3. Save to DB
                DBUtil.insertCircuit(newCircuit);
                new Alert(Alert.AlertType.INFORMATION, "Circuit " + newCircuit.getName() + " added!").showAndWait();

                // 4. Reload the circuit data
                circuitTable.setItems(DBUtil.getAllCircuits());

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during CIRCUIT INSERT: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void handleEditCircuit(ActionEvent event) {
        Circuit selected = circuitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a circuit first, you massive fool.").showAndWait();
            return;
        }

        if (showCircuitEditDialog(selected)) {
            try {
                DBUtil.updateCircuit(selected);
                new Alert(Alert.AlertType.INFORMATION, "Circuit " + selected.getName() + " updated!").showAndWait();

                // Force table refresh
                circuitTable.getColumns().get(0).setVisible(false);
                circuitTable.getColumns().get(0).setVisible(true);

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during CIRCUIT UPDATE: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void handleDeleteCircuit(ActionEvent event) {
        Circuit selected = circuitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pick a circuit to delete!").showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Circuit Deletion");
        confirmation.setHeaderText("Confirm Deletion of " + selected.getName());
        confirmation.setContentText("Are you sure you want to permanently delete this circuit? This may affect existing championships.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DBUtil.deleteCircuit(selected.getCircuitId());
                new Alert(Alert.AlertType.INFORMATION, "Circuit " + selected.getName() + " deleted.").showAndWait();

                // Reload the circuit table
                circuitTable.setItems(DBUtil.getAllCircuits());

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error during CIRCUIT DELETE: " + e.getMessage()).showAndWait();
            }
        }
    }
}
