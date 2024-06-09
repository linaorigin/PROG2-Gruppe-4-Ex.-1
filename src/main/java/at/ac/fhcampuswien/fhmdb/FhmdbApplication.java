package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.data.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HomeControllerFactory factory = new HomeControllerFactory();
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        fxmlLoader.setControllerFactory(factory);

        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        scene.getStylesheets()
             .add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css"))
                         .toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
        try {
            DatabaseManager db = DatabaseManager.getDatabaseManager();
        } catch (DatabaseException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}