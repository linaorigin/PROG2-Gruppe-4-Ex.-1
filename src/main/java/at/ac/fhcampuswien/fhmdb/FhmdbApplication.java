package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.data.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        scene.getStylesheets()
             .add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css"))
                         .toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
        try {
            DatabaseManager db = DatabaseManager.getDatabaseManager();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}