package cz.vse.java.elektronicke_volby.gui;

import cz.vse.java.elektronicke_volby.logic.Candidate;
import cz.vse.java.elektronicke_volby.logic.DatabaseManagement;
import cz.vse.java.elektronicke_volby.logic.Elections;
import cz.vse.java.elektronicke_volby.logic.Voter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;

public class ElectionsDetailsScreenController {

    public Elections election;
    public Voter voter;
    @FXML
    Label name, date, hasUserVoted, areElectionsOpen;
    @FXML
    VBox rootPane, candidateDashboard;


    public void initializeScreen(Voter voter, Elections election) {
        candidateDashboard.getChildren().clear();
        this.voter = voter;
        this.election = election;
        name.setText(election.getName());
        date.setText(election.getDate());

        for (Candidate candidate : DatabaseManagement.getCandidates(election.getId())) {
            HBox hbox = new HBox();
            hbox.setSpacing(20);

            Label labelName = new Label();
            labelName.setText(candidate.name);

            Button button = new Button("Zvolit kandidáta");
            if (DatabaseManagement.hasUserVoted(election, voter)) {
                button.setDisable(true);
            } else {
                if (election.getStatus() == 0 || election.getStatus() == 2) {
                    button.setDisable(true);
                } else {
                    button.setDisable(false);
                }
            }
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DatabaseManagement.recordAVote(election, voter, candidate);
                    initializeScreen(voter, election);
                }
            });

            hbox.getChildren().addAll(labelName, button);
            candidateDashboard.getChildren().add(hbox);
        }

        if (DatabaseManagement.hasUserVoted(election, voter)) {
            hasUserVoted.setText("Uživatel již VOLIL");
        } else {
            hasUserVoted.setText("Uživatel zatím NEVOLIL");
        }

        switch (election.getStatus()) {
            case 0: areElectionsOpen.setText("Uzavřené (před volbami)");
            break;
            case 1: areElectionsOpen.setText("Otevřené)");
            break;
            case 2: areElectionsOpen.setText("Uzavřené (po volbách)");
            break;
        }
    }

    public void backToDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/dashboard_screen.fxml"));
        VBox pane = loader.load();
        DashboardController controller = loader.getController();
        rootPane.getChildren().setAll(pane);
        controller.setLoggedUser(voter);
        controller.setName();
        controller.renderElections();
    }

    public void signOut() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_screen.fxml"));
        rootPane.getChildren().setAll(pane);

    }
}
