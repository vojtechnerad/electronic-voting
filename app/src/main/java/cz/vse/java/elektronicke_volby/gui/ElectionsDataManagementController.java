package cz.vse.java.elektronicke_volby.gui;

import cz.vse.java.elektronicke_volby.logic.DatabaseManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controller pro správcovcké menu, skrze které se vytvářejí, upravují a mažou jednotlivé volby.
 */
public class ElectionsDataManagementController extends VBox {

    @FXML
    TextField name;
    @FXML
    TextField date;
    @FXML
    VBox rootPane;

    /**
     * Metoda která po zadání jména a data (měsíce/roku) konání založí volby v databázi.
     * @param actionEvent
     */
    public void createElections(ActionEvent actionEvent) {
        DatabaseManagement.createElections(name.getText(), date.getText());
    }

    /**
     * Metoda která odhlásí aktuálně přihlášeného správce.
     * @throws IOException
     */
    public void signOut() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_admin_screen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
