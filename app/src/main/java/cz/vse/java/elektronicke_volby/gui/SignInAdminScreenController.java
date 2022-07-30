package cz.vse.java.elektronicke_volby.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controller pro scénu pro přihlášení správce.
 */
public class SignInAdminScreenController extends AnchorPane {

    @FXML
    TextField login_code;
    @FXML
    TextField password;
    @FXML
    AnchorPane rootPane;

    /**
     * Metoda pro přihlášení správce do aplikace.
     * @throws IOException
     */
    @FXML
    public void signInAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/elections_data_management.fxml"));
        VBox pane = loader.load();
        rootPane.getChildren().clear();
        rootPane.getChildren().addAll(pane);
        System.out.println("XD");
    }

    /**
     * Metoda pro přepnutí obrazovky na přihlašovací obrazovku voliče.
     * @throws IOException
     */
    public void signInAsVoter() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_screen.fxml"));
        rootPane.getChildren().setAll(pane);

    }


}