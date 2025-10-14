package project.edgiaxel.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Button startButton;
    @FXML
    private Button viewDataButton;
    @FXML
    private Button viewChampionshipsButton;
    @FXML
    private Button exitButton;
    
    // Method to switch scenes, you'll use this a lot.
    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void handleStartChampionship(ActionEvent event) throws IOException {
        // You'll need to create StartChampionship.fxml later.
        System.out.println("Starting Championship...");
        // switchScene(event, "/project/edgiaxel/fxml/StartChampionship.fxml"); 
        // For now, just a placeholder.
    }

    @FXML
    void handleViewMasterData(ActionEvent event) throws IOException {
        System.out.println("Opening Master Data View...");
        switchScene(event, "/project/edgiaxel/fxml/MasterDataView.fxml");
    }

    @FXML
    void handleViewChampionships(ActionEvent event) throws IOException {
        switchScene(event, "/project/edgiaxel/fxml/ViewChampionships.fxml");
    }
    
    @FXML
    void handleExit(ActionEvent event) {
        System.out.println("Quitting.");
        Platform.exit();
    }
}