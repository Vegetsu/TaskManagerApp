package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TaskManagerUserCreatedController {

    @FXML
    private Button ButtonOK;


    public void suljekirjautuminen() {
    // Sulkee OK-ikkunan
        Stage stage = (Stage) ButtonOK.getScene().getWindow();
        stage.close();


    }
}
