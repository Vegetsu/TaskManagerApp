package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/*
* TaskManagerin tehtävän poiston varmistuksen controller -luokka.
*  -Alustaa kyllä- ja ei-napeille oikeat palautettavat arvot, joiden mukaan toimitaan tietyllä tavalla
*   TaskManagerMainUIController -luokassa.
*  -Yhdistää ConfirmationScreenin MainScreeniin.
 */
public class TaskManagerConfirmationController {

    private boolean userChoice = false;

    private TaskManagerMainUIController haeTaskManagerMainUIKontrolli;

    @FXML
    private Button ButtonEi;

    @FXML
    private Button ButtonKylla;

    @FXML
    private Text TehtavaConfirmation;

    @FXML
    private Text TextConfirmation;

//Metodi jota käytetään Main screenin controllerissa, jolla lähetetään käyttäjän valinta Confirmation screenillä
    public boolean getUserChoice() {
        return userChoice;
    }


    //Alustetaan napit, kun ikkuna avataan
    @FXML
    private void initialize() {

        //Jos käyttäjä painaa nappia kyllä, niin userChoice arvoksi tulee true
        ButtonKylla.setOnAction(event -> {
            userChoice = true;
            closeWindow();
        });

        //Jos käyttäjä painaa nappia ei, niin userChoice arvoksi tulee false
        ButtonEi.setOnAction(event -> {
            userChoice = false;
            closeWindow();
        });


    }

    //Metodi jonka avulla Confirmation screen -ikkuna sulkeutuu
    private void closeWindow() {
        Stage stage = (Stage) ButtonEi.getScene().getWindow();
        stage.close();
    }

    //Yhdistetään Confirmation screen Main screenin kontrolleriin
    public void haeTaskManagerMainUIKontrolli(TaskManagerMainUIController TaskManagerMainKontrolli) {
        this.haeTaskManagerMainUIKontrolli = TaskManagerMainKontrolli;

    }

    //Metodi jota käytetään Main screen -ikkunassa asettamaan Confirmation screeniin valitun tehtävän nimi
    public void asetaTehtavaNimi(String nimi) {
        TehtavaConfirmation.setText(nimi);
    }

}
