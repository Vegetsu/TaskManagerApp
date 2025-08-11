package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class TaskManagerNewUserController {

    @FXML
    private Button ButtonLuoKayttaja;

    @FXML
    private CheckBox CheckBoxLuoSalasana;

    @FXML
    private CheckBox CheckBoxVarmistaSalasana;

    @FXML
    private Text KirjautumisError;

    @FXML
    private PasswordField PasswordFieldLuoHidden;

    @FXML
    private PasswordField PasswordFieldVarmistusHidden;

    @FXML
    private TextField TextFieldLuoNormaalli;

    @FXML
    private TextField TextFieldSalasanaUudelleen;

    @FXML
    private TextField TextFieldTunnus;


    Connection conn = null;

    public void luokayttaja() throws SQLException, IOException {
        conn =SQLTietokanta.Openconnection();


        String tunnus = TextFieldTunnus.getText();
        String salasana = CheckBoxLuoSalasana.isSelected()
                ? TextFieldLuoNormaalli.getText()
                : PasswordFieldLuoHidden.getText();
        String varmistus = TextFieldSalasanaUudelleen.isVisible()
                ? TextFieldSalasanaUudelleen.getText()
                : PasswordFieldVarmistusHidden.getText();

        if (tunnus.isEmpty()){
            KirjautumisError.setText("Käyttäjänimi puuttuu!");
        }
        else if (salasana.equals(varmistus)){
            if (SQLTietokanta.Onkokayttajaolemassa(tunnus,conn)){
                SQLTietokanta.lisaaKayttaja(tunnus, UserService.hashPassword(salasana), conn);

                // Sulkee käyttäjänluomisikkunan
                Stage stage = (Stage) ButtonLuoKayttaja.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerUserCreated.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                Stage uusiStage = new Stage();
                uusiStage.setScene(scene);
                uusiStage.setTitle("User Created");
                uusiStage.show();

            }
            else{
                KirjautumisError.setText("Käyttäjä on jo olemassa!");
            }

        }

        else {
            KirjautumisError.setText("Salasanat eivät täsmää!");
        }


    }

    @FXML
    private void ChangeHidden() {
    if (CheckBoxLuoSalasana.isSelected()){
        TextFieldLuoNormaalli.setText(PasswordFieldLuoHidden.getText());
        TextFieldLuoNormaalli.setVisible(true);
        PasswordFieldLuoHidden.setVisible(false);
        return;
    }

    PasswordFieldLuoHidden.setText(TextFieldLuoNormaalli.getText());
    PasswordFieldLuoHidden.setVisible(true);
    TextFieldLuoNormaalli.setVisible(false);
    }

    @FXML
    private void ChangeHiddenVarmistus() {
        if (CheckBoxVarmistaSalasana.isSelected()){
            TextFieldSalasanaUudelleen.setText(PasswordFieldVarmistusHidden.getText());
            TextFieldSalasanaUudelleen.setVisible(true);
            PasswordFieldVarmistusHidden.setVisible(false);
            return;
        }

        PasswordFieldVarmistusHidden.setText(TextFieldSalasanaUudelleen.getText());
        PasswordFieldVarmistusHidden.setVisible(true);
        TextFieldSalasanaUudelleen.setVisible(false);
    }

}
