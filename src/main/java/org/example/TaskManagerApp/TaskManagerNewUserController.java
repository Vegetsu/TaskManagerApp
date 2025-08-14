package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/*
* TaskManagerin NewUserController -luokka, joka määrittelee toiminnallisuudet uuden käyttäjän luomiselle.
*  -Lukee käyttäjän syöttämät tiedot käyttäjätunnus- ja salasana-tekstikentistä, vertailee näitä tietokannan jo luotuihin
*   käyttäjiin, ja luo uuden käyttäjän tietokantaan, jos käyttäjää ei vielä aiemmin ollut olemassa.
 */


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


    /*
    * Metodi, joka lukee käyttäjän syötteet käyttäjätunnus- ja salasana-ruuduista, ja luo uuden käyttäjän näiden tietojen
    * perusteella. Jos käyttäjän luominen onnistuu, niin palataan avataan ikkuna jossa ilmoitetaan uuden käyttäjän
    * onnistuneesta luonnista ja palataan takaisin kirjautumisruutuun, jossa käyttäjä voi nyt luoduilla tunnuksilla
    * kirjautua sisään.
     */
    public void luokayttaja() throws SQLException, IOException {
        conn =SQLTietokanta.Openconnection();


        /*
        * Salasana pitää kirjoittaa kahdesti oikein salasana- ja varmistusruutuihin, jotta uuden käyttäjän luonti
        * hyväksytään. Salasanalaatikot ovat suojattu niin, että kirjainten tai numeroiden sijasta salasanakenttiin
        * kirjoittuu vain ympyröitä, jotta ulkopuoliset eivät näe salasanan kirjoittamista. Salasanalaatikoiden vieressä
        * on napit, joilla oikean salasanan saa näkyviin.
         */
        String tunnus = TextFieldTunnus.getText();

        /*
        * salasana ja varmistus muuttujat on luotu näillä ehdoilla siksi, että salasanan luku onnistuisi oikein. Jos
        * salasana on piilotetussa muodossa, luetaan suojauksen takana olevaa oikeaa salasanaa mutta jos salasana
        * on näkyvissä, luetaan näkyvää salasanaa.
         */
        String salasana = CheckBoxLuoSalasana.isSelected()
                ? TextFieldLuoNormaalli.getText()
                : PasswordFieldLuoHidden.getText();
        String varmistus = TextFieldSalasanaUudelleen.isVisible()
                ? TextFieldSalasanaUudelleen.getText()
                : PasswordFieldVarmistusHidden.getText();

        if (tunnus.isEmpty()){
            KirjautumisError.setText("Käyttäjänimi puuttuu!");
        }

        /*Jos molemmat salasanat ovat oikein, niin lisätään luotu käyttäjätunnus tietokantaan ja avataan uuden käyttäjän
        * luonnin varmistus -ikkuna.
         */
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


    /* Metodi, jolla CheckBox-ruudussa ollessa ruksi salasana muuttuu näkyväksi ja CheckBox-ruudun ollessa tyhjänä
    * salasana näkyy palleroina.
     */
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

    /* Metodi, jolla CheckBox-ruudussa ollessa ruksi salasanavarimustus muuttuu näkyväksi ja CheckBox-ruudun ollessa tyhjänä
     * salasanavarmistus näkyy palleroina.
     */
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
