package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class TaskManagerLoginController {

    @FXML
    private TextField TextFieldSalasana;

    @FXML
    private TextField TextFieldTunnus;

    @FXML
    private Button ButtonLogin;

    @FXML
    private Button temporary;

    @FXML
    private Text KirjautumisError;

    Connection conn = null;


/*
    private void Login(){
        if (!TextFieldSalasana.getText().equals() || TextFieldTunnus.getText().isEmpty()){

        }
    }*/


    public void avaaluokayttaja() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerNewUser.fxml"));
        Parent parent = fxmlLoader.load();

        Stage primarystage = new Stage();
        primarystage.setTitle("New User");
        primarystage.setScene(new Scene(parent));
        primarystage.setResizable(false);
        //primarystage.show();
        primarystage.initModality(Modality.APPLICATION_MODAL);

        primarystage.showAndWait();
    }


    public void varmistakayttaja() throws SQLException, IOException {
        conn =SQLTietokanta.Openconnection();

        String salasana = TextFieldSalasana.getText();
        String tunnus = TextFieldTunnus.getText();

        if (SQLTietokanta.verifyKayttaja(tunnus,salasana,conn)){

            // Sulkee kirjautumisikkunan
            Stage stage = (Stage) ButtonLogin.getScene().getWindow();
            stage.close();

            try {



                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerMainUI.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                Stage uusiStage = new Stage();
                uusiStage.setScene(scene);
                uusiStage.setTitle("Task Manager");
                uusiStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            KirjautumisError.setText("Käyttäjätunnus tai salasana väärin!");
        }


    }

}
