package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/*
* TaskManagerin UserCreated -luokka, jossa User crated -ikkuna suljetaan, kun OK-nappia painetaan.
 */
public class TaskManagerUserCreatedController {

    @FXML
    private Button ButtonOK;

    // Metodi, joka sulkee User created -ikkunan, kun OK-nappia painetaan.
    public void suljekirjautuminen() {
    // Sulkee OK-ikkunan
        Stage stage = (Stage) ButtonOK.getScene().getWindow();
        stage.close();


    }
}
