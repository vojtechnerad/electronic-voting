package cz.vse.java.elektronicke_volby.gui;

import com.google.common.hash.Hashing;
import cz.vse.java.elektronicke_volby.logic.DatabaseManagement;
import cz.vse.java.elektronicke_volby.logic.Voter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;

public class SignUpScreenController extends AnchorPane implements Initializable {

    @FXML
    TextField firstname;
    @FXML
    TextField lastname;
    @FXML
    TextField birth_number;
    @FXML
    TextField email;
    @FXML
    ComboBox region;
    @FXML
    ComboBox day;
    @FXML
    ComboBox month;
    @FXML
    ComboBox year;
    @FXML
    TextField password;
    @FXML
    TextField password_repeat;
    @FXML
    Button sign_up;

    /**
     * Metoda pro naplnění comboboxů daty.
     */
    private void prepareDateComboBoxes() {
        ArrayList<Integer> days = new ArrayList<>();
        ArrayList<Integer> months = new ArrayList<>();
        ArrayList<Integer> years = new ArrayList<>();

        for (int i = 1; i < 32; i++) {
            days.add(i);
        }
        day.getItems().addAll(days);

        for (int i = 1; i < 13; i++) {
            months.add(i);
        }
        month.getItems().addAll(months);

        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= 1900 ; i--) {
            years.add(i);
        }
        year.getItems().addAll(years);
    }

    /**
     * Metoda pro registraci nového voliče.
     * @param actionEvent
     */
    public void register(ActionEvent actionEvent) {
        //Kontrola zadání jména
        if (firstname.getText().isEmpty()) {
            System.out.println("Name is empty");
            return;
        }

        //Kontrola zadání příjmení
        if (lastname.getText().isEmpty()) {
            System.out.println("Lastname is empty");
            return;
        }

        //Kontrola zadání rodného čísla
        if (birth_number.getText().isEmpty()) {
            System.out.println("Birth number is empty");
            return;
        }

        //Kontrola zadání emailu
        if (email.getText().isEmpty()) {
            System.out.println("Email is empty");
            return;
        }

        //Kontrola zadání kraje
        if (region.getValue() == null) {
            System.out.println("Region is empty");
            return;
        }

        //Kontrola zadání data narození
        if (day.getValue() == null) {
            System.out.println("Day is empty");
            return;
        } else if (month.getValue() == null) {
            System.out.println("Month is empty");
            return;
        } else if (year.getValue() == null) {
            System.out.println("Year is empty");
            return;
        }

        //Kontrola zadání hesla
        if (password.getText().isEmpty()) {
            System.out.println("Password is empty");
            return;
        } else if (password_repeat.getText().isEmpty()){
            System.out.println("Password repeat is empty");
            return;
        } else if (!password.getText().equals(password_repeat.getText())) {
            System.out.println("Password and password repeat does not match");
            return;
        }

        Voter newVoter = new Voter(
                firstname.getText(),
                lastname.getText(),
                birth_number.getText(),
                email.getText(),
                getRegionNumber(region.getValue().toString()),
                getBirthDate(),
                Hashing.sha256().hashString(password.getText(), StandardCharsets.UTF_8).toString()
        );


        newVoter.signUp();
    }

    /**
     * Metoda pro zjištění čísla (ID) kraje.
     * @param regionName
     * @return
     */
    public int getRegionNumber(String regionName) {
        int regionNumber = 0;
        for (Map.Entry<Integer, String> entry : DatabaseManagement.getRegions().entrySet()) {
            if (entry.getValue().toString().equals(regionName)) {
                regionNumber = entry.getKey();
            }
        }
        return regionNumber;
    }

    /**
     * Metoda pro zjištění data narození voliče.
     * @return
     */
    public String getBirthDate() {
        String date = "";

        date = date + year.getValue().toString() + "-";

        String monthString = month.getValue().toString();
        if (monthString.length() == 1) {
            date = date + "0" + monthString + "-";
        } else {
            date = date + monthString + "-";
        }

        String dayString = day.getValue().toString();
        if (dayString.length() == 1) {
            date = date + "0" + dayString;
        } else {
            date = date + dayString;
        }
        return date;
    }

    @FXML
    private AnchorPane rootPane;

    /**
     * Metoda pro přepnutí obrazovky na přihlašovací obrazovku voliče.
     * @throws IOException
     */
    @FXML
    private void loadSignIn() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/scenes/sign_in_screen.fxml"));
        rootPane.getChildren().setAll(pane);

    }

    /**
     * Metoda pro inicializaci třídy.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Naplnění ComboBoxu jednotlivými kraji z databáze
        region.getItems().addAll(DatabaseManagement.getRegions().values());

        //Naplnění ComboBoxů data narození příslušnými čísly
        prepareDateComboBoxes();
    }
}
