package project.edgiaxel.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.edgiaxel.dao.ManufacturerDAO;
import project.edgiaxel.dao.TeamDAO;
import project.edgiaxel.dao.CircuitDAO;
import project.edgiaxel.model.Manufacturer;
import project.edgiaxel.model.Team;
import project.edgiaxel.model.Driver;
import project.edgiaxel.model.Circuit;

import java.io.IOException;
import java.util.Optional;

public class MasterDataViewController {

    @FXML
    private TableView<Manufacturer> manufacturerTable;
    @FXML
    private TableView<Team> teamTable;
    @FXML
    private TableView<Driver> driverTable;
    @FXML
    private Label teamHeader;
    @FXML
    private Label driverHeader;
    @FXML
    private ImageView masterImageView;
    @FXML
    private Label imageStatusLabel;
    @FXML
    private TableView<Circuit> circuitTable;
    @FXML
    private ImageView circuitImageView;
    @FXML
    private Label circuitStatusLabel;

    private final ManufacturerDAO manufacturerDAO = ManufacturerDAO.getInstance();
    private final TeamDAO teamDAO = TeamDAO.getInstance();
    private final CircuitDAO circuitDAO = CircuitDAO.getInstance();

    @FXML
    private void initialize() {
        setupManufacturerTable();
        loadManufacturerData();
        manufacturerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showManufacturerDetails(newValue));

        setupTeamTable();
        teamTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTeamDetails(newValue));

        setupDriverTable();

        setupCircuitTable();
        loadCircuitData();
        circuitTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCircuitDetails(newValue));
    }
    
    
    
    
    

    private void setupManufacturerTable() {
        manufacturerTable.getColumns().clear();
        TableColumn<Manufacturer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("manufacturerId"));
        idCol.setPrefWidth(50);
        TableColumn<Manufacturer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        TableColumn<Manufacturer, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(100);
        manufacturerTable.getColumns().setAll(idCol, nameCol, categoryCol);
    }

    private void setupTeamTable() {
        teamTable.getColumns().clear();
        TableColumn<Team, String> carNumberCol = new TableColumn<>("#");
        carNumberCol.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        carNumberCol.setPrefWidth(50);
        TableColumn<Team, String> teamNameCol = new TableColumn<>("Team Name");
        teamNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamNameCol.setPrefWidth(250);
        TableColumn<Team, String> carModelCol = new TableColumn<>("Car Model");
        carModelCol.setCellValueFactory(new PropertyValueFactory<>("carModelName"));
        carModelCol.setPrefWidth(200);
        teamTable.getColumns().setAll(carNumberCol, teamNameCol, carModelCol);
    }

    private void setupDriverTable() {
        driverTable.getColumns().clear();
        TableColumn<Driver, Integer> driverIdCol = new TableColumn<>("ID");
        driverIdCol.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        driverIdCol.setPrefWidth(50);
        TableColumn<Driver, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(250);
        TableColumn<Driver, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        nationalityCol.setPrefWidth(150);
        driverTable.getColumns().setAll(driverIdCol, nameCol, nationalityCol);
    }

    private void setupCircuitTable() {
        circuitTable.getColumns().clear();
        TableColumn<Circuit, String> nameCol = new TableColumn<>("Circuit Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);
        TableColumn<Circuit, Double> lengthCol = new TableColumn<>("Length (km)");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("lengthKm"));
        lengthCol.setPrefWidth(120);
        TableColumn<Circuit, String> raceTypeCol = new TableColumn<>("Race Type");
        raceTypeCol.setCellValueFactory(new PropertyValueFactory<>("raceType"));
        raceTypeCol.setPrefWidth(150);
        circuitTable.getColumns().setAll(nameCol, lengthCol, raceTypeCol);
    }
    
    
    
    
    
    

    private void loadManufacturerData() {
        manufacturerTable.setItems(manufacturerDAO.getAllManufacturers());
        teamTable.getItems().clear();
        driverTable.getItems().clear();
        teamHeader.setText("2. TEAM ENTRIES (Select Manufacturer Above)");
        driverHeader.setText("3. DRIVERS (Current Roster for Selected Team)");
        masterImageView.setImage(null);
        imageStatusLabel.setText("Select Manufacturer or Team for Image");
    }

    private void loadCircuitData() {
        circuitTable.setItems(circuitDAO.getAllCircuits());
        circuitImageView.setImage(null);
        circuitStatusLabel.setText("Select a Circuit to view its map and stats.");
    }

    private void showManufacturerDetails(Manufacturer manufacturer) {
        if (manufacturer != null) {
            teamHeader.setText("2. TEAM ENTRIES for: " + manufacturer.getName());
            
            teamTable.setItems(teamDAO.getTeamsByManufacturerId(manufacturer.getManufacturerId()));
            
            driverTable.getItems().clear();
            driverHeader.setText("3. DRIVERS (Current Roster for Selected Team)");
            
            try {
                Image logo = new Image(getClass().getResourceAsStream(manufacturer.getLogoPath()));
                masterImageView.setImage(logo);
                imageStatusLabel.setText(manufacturer.getName() + " Image.");
            } catch (Exception e) {
                masterImageView.setImage(null);
                imageStatusLabel.setText("Could not load logo for " + manufacturer.getName() + ".");
            }
        } else {
            loadManufacturerData();
        }
    }

    private void showTeamDetails(Team team) {
        if (team != null) {
            driverHeader.setText("3. DRIVERS for: " + team.getTeamName() + " #" + team.getCarNumber());
            
            driverTable.setItems(team.getDrivers()); 
            
            /*try {
                Image livery = new Image(getClass().getResourceAsStream(team.getLiveryPath()));
                masterImageView.setImage(livery);
                imageStatusLabel.setText(team.getTeamName() + " Livery #" + team.getCarNumber() + ".");
            } catch (Exception e) {
                masterImageView.setImage(null);
                imageStatusLabel.setText("Could not load livery for " + team.getTeamName() + ".");
            }*/
        } else {
             driverTable.getItems().clear();
        }
    }
    
    private void showCircuitDetails(Circuit circuit) {
        if (circuit != null) {
            try {
                Image map = new Image(getClass().getResourceAsStream(circuit.getMapPath()));
                circuitImageView.setImage(map);
                circuitStatusLabel.setText(circuit.getName() + " Circuit Map.\nLength: " + circuit.getLengthKm() + "km, Race: " + circuit.getRaceType() + ".");
            } catch (Exception e) {
                circuitImageView.setImage(null);
                circuitStatusLabel.setText("Could not load map for " + circuit.getName() + ".");
            }
        } else {
              circuitImageView.setImage(null);
              circuitStatusLabel.setText("Select a Circuit to view its map and stats.");
        }
    }
    
    
    
    
    
    

    @FXML
    private void handleAddManufacturer() {
        Manufacturer newManufacturer = new Manufacturer(0, "", "", "Hypercar"); 
        boolean okClicked = showManufacturerEditDialog(newManufacturer);
        if (okClicked) {
            if (manufacturerDAO.insertManufacturer(newManufacturer)) {
                loadManufacturerData(); 
            } else {
                showAlert("Error", "Failed to add manufacturer. Check logs for FK violations or DB connection.");
            }
        }
    }

    @FXML
    private void handleEditManufacturer() {
        Manufacturer selectedManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selectedManufacturer != null) {
            boolean okClicked = showManufacturerEditDialog(selectedManufacturer);
            if (okClicked) {
                if (manufacturerDAO.updateManufacturer(selectedManufacturer)) {
                    manufacturerTable.getSelectionModel().select(selectedManufacturer);
                    manufacturerTable.refresh();
                } else {
                    showAlert("Error", "Failed to update manufacturer.");
                }
            }
        } else {
            showAlert("No Selection", "Select a manufacturer to edit.");
        }
    }

    @FXML
    private void handleDeleteManufacturer() {
        Manufacturer selectedManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selectedManufacturer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Manufacturer: " + selectedManufacturer.getName());
            alert.setContentText("Are you sure you want to delete this manufacturer? This may affect associated teams/cars!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (manufacturerDAO.deleteManufacturer(selectedManufacturer)) {
                    loadManufacturerData();
                } else {
                    showAlert("Error", "Failed to delete manufacturer. Check if any teams/cars are still linked.");
                }
            }
        } else {
            showAlert("No Selection", "Select a manufacturer to delete.");
        }
    }
    
    
    
    @FXML
    private void handleAddTeam() {
        Manufacturer selectedManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selectedManufacturer == null) {
            showAlert("No Manufacturer Selected", "Please select a Manufacturer first to add a new Team entry.");
            return;
        }

        Team newTeam = new Team(0, "", "", selectedManufacturer.getManufacturerId(), 0, "", selectedManufacturer.getCategory());
        boolean okClicked = showTeamEditDialog(newTeam);
        if (okClicked) {
            if (teamDAO.insertTeam(newTeam)) {
                 showManufacturerDetails(selectedManufacturer); 
            } else {
                showAlert("Error", "Failed to add team. Check if car number is unique or if car model is selected.");
            }
        }
    }
    
    @FXML
    private void handleEditTeam() {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            boolean okClicked = showTeamEditDialog(selectedTeam);
            if (okClicked) {
                if (teamDAO.updateTeam(selectedTeam)) {
                    teamTable.refresh();
                } else {
                     showAlert("Error", "Failed to update team.");
                }
            }
        } else {
            showAlert("No Selection", "Select a team entry to edit.");
        }
    }
    
    @FXML
    private void handleDeleteTeam() {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        Manufacturer selectedManufacturer = manufacturerTable.getSelectionModel().getSelectedItem();
        if (selectedTeam != null && selectedManufacturer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Team: #" + selectedTeam.getCarNumber() + " " + selectedTeam.getTeamName());
            alert.setContentText("Are you sure you want to delete this team entry? All driver assignments will be removed.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (teamDAO.deleteTeam(selectedTeam)) {
                    showManufacturerDetails(selectedManufacturer);
                } else {
                    showAlert("Error", "Failed to delete team.");
                }
            }
        } else {
            showAlert("No Selection", "Select a team entry to delete.");
        }
    }
    
    @FXML 
    private void handleAddDriver() {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            showDriverAssignmentDialog(selectedTeam);
            selectedTeam.setDrivers(teamDAO.getDriversForTeam(selectedTeam.getTeamId()));
            driverTable.setItems(selectedTeam.getDrivers());
            driverTable.refresh();
        } else {
            showAlert("No Selection", "Select a team entry first to manage drivers.");
        }
    }
    
    
    
    
    

    @FXML
    private void handleAddCircuit() {
        Circuit newCircuit = new Circuit(0, "", "", "", 0.0, "6 Hours"); 
        boolean okClicked = showCircuitEditDialog(newCircuit);
        if (okClicked) { 
           if (circuitDAO.insertCircuit(newCircuit)) { 
               loadCircuitData(); 
           } else {
                showAlert("Error", "Failed to add circuit. Check database connection.");
           }
        }
    }
    
    @FXML
    private void handleEditCircuit() {
         Circuit selectedCircuit = circuitTable.getSelectionModel().getSelectedItem();
        if (selectedCircuit != null) {
            boolean okClicked = showCircuitEditDialog(selectedCircuit);
            if (okClicked) { 
               if (circuitDAO.updateCircuit(selectedCircuit)) { 
                   circuitTable.refresh(); 
               } else {
                   showAlert("Error", "Failed to update circuit.");
               }
            }
        } else {
            showAlert("No Selection", "Select a circuit to edit.");
        }
    }
    
    @FXML
    private void handleDeleteCircuit() {
         Circuit selectedCircuit = circuitTable.getSelectionModel().getSelectedItem();
        if (selectedCircuit != null) {
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Circuit: " + selectedCircuit.getName());
            alert.setContentText("Are you sure you want to delete this circuit? This action is permanent.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (circuitDAO.deleteCircuit(selectedCircuit)) { 
                    loadCircuitData(); 
                } else {
                    showAlert("Error", "Failed to delete circuit. Check if it's currently used in a championship (future feature).");
                }
            }
        } else {
            showAlert("No Selection", "Select a circuit to delete.");
        }
    }
    
    
    
    
    

    private boolean showManufacturerEditDialog(Manufacturer manufacturer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/ManufacturerEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Manufacturer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(manufacturerTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ManufacturerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setManufacturer(manufacturer);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean showTeamEditDialog(Team team) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/TeamEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(team.getTeamId() == 0 ? "Add New Team" : "Edit Team");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(teamTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TeamEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTeam(team);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean showCircuitEditDialog(Circuit circuit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/CircuitEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(circuit.getCircuitId() == 0 ? "Add New Circuit" : "Edit Circuit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(circuitTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CircuitEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCircuit(circuit);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDriverAssignmentDialog(Team team) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/edgiaxel/fxml/DriverAssignmentDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Driver Assignment for #" + team.getCarNumber());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(teamTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DriverAssignmentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTeam(team);

            dialogStage.showAndWait();
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
