package cz.vse.java.elektronicke_volby.gui;

import com.google.common.hash.Hashing;
import cz.vse.java.elektronicke_volby.logic.DatabaseManagement;
import cz.vse.java.elektronicke_volby.logic.Voter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class SignInScreenController extends AnchorPane implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    TextField birthNumber;
    @FXML
    PasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void loadSignUp() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_up_screen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * Metoda pro přihlášení uživatele.
     * @param actionEvent
     * @throws IOException
     */
    public void signIn(ActionEvent actionEvent) throws IOException {
        String passwordHash = Hashing.sha256().hashString(password.getText(), StandardCharsets.UTF_8).toString();

        if (DatabaseManagement.verifyUsersCredentials(birthNumber.getText(), passwordHash)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/dashboard_screen.fxml"));
            VBox pane = loader.load();
            DashboardController controller = loader.getController();
            Voter voter = DatabaseManagement.getVotersInstance(birthNumber.getText());
            rootPane.getChildren().setAll(pane);
            controller.setLoggedUser(voter);
            controller.setName();
            controller.renderElections();
        } else {
            System.out.println("Nesprávné přihlašovací údaje");
        }
    }

    public void signInAsAdmin(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_admin_screen.fxml"));
        rootPane.getChildren().setAll(pane);

    }
}
