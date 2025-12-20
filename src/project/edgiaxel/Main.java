package project.edgiaxel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/project/edgiaxel/fxml/LoginView.fxml"; // Default
        String title = "FIA WEC SIMULATOR - LOGIN";

        // Check for persistent session
        if (SessionManager.isUserLoggedIn()) {
            fxmlFile = "/project/edgiaxel/fxml/Dashboard.fxml";
            title = "FIA WEC SIMULATOR - " + SessionManager.getCurrentUsername();
        }

        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
