package cz.vse.java.elektronicke_volby.gui;

import cz.vse.java.elektronicke_volby.logic.DatabaseManagement;
import cz.vse.java.elektronicke_volby.logic.Elections;
import cz.vse.java.elektronicke_volby.logic.Voter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController extends VBox implements Initializable {

    @FXML
    VBox electionsDashboard;
    @FXML
    ListView electionsOutput = new ListView();
    @FXML
    Label labelName;
    @FXML
    Label labelBirthNumber;
    @FXML
    VBox rootPane;

    public Voter loggedVoter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoggedUser(Voter voter) {
        loggedVoter = voter;
    }

    public void setName() {
        labelName.setText(loggedVoter.getFirstName() + " " + loggedVoter.getLastname());
        labelBirthNumber.setText("(" + loggedVoter.getBirthNumber() + ")");
    }

    public void renderElections() {
        ArrayList<Elections> elections = new ArrayList<>();
        elections = DatabaseManagement.listOfElections();

        for (Elections election : elections) {
            HBox hbox = new HBox();
            hbox.setSpacing(20);
            hbox.setAlignment(Pos.CENTER);

            Label labelName = new Label();
            labelName.setText(election.getName());
            labelName.setAlignment(Pos.CENTER_LEFT);

            Label labelDate = new Label();
            labelDate.setText(election.getDate());
            labelDate.setAlignment(Pos.CENTER);

            Label labelStatus = new Label();
            String status;
            switch (election.getStatus()) {
                case 1: status = "Čeká na otevření";
                case 2: status = "Otevřené";
            }

            Button button = new Button();
            button.setText("Zobrazit volby");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        showElectionsDetails(election);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            button.setAlignment(Pos.CENTER_RIGHT);
            hbox.getChildren().addAll(labelName, labelDate, button);
            electionsDashboard.getChildren().add(hbox);
        }
    }

    public void showElectionsDetails(Elections election) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/elections_details_screen.fxml"));
        VBox pane = loader.load();
        ElectionsDetailsScreenController controller = loader.getController();
        rootPane.getChildren().setAll(pane);
        controller.initializeScreen(loggedVoter, election);
    }

    public void signOut(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_screen.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
