package cz.vse.java.elektronicke_volby.logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Metoda která zjišťuje, zdali se dá připojit k databázi, a poté pouští aplikaci.
 */
public class Core extends Application {

    public static void startApplication() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (DatabaseManagement.connect() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba databáze");
            alert.setHeaderText("Nepovedlo se připojit k databázovému systému");
            alert.setContentText("Aplikaci se nepodařilo navázat spojení s databází, aplikaci proto nelze spustit.");
            alert.showAndWait();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/scenes/sign_in_screen.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Elektronické volby");
            primaryStage.show();
        }
    }
}
