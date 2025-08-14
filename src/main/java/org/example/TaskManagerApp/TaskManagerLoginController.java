package org.example.TaskManagerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/*
* TaskManagerin LoginController -luokka, jota käytetään käyttäjän kirjautumiseen liittyvissä toiminnoissa.
*  -Avataan uuden käyttäjän luonti -ikkuna, jos luo käyttäjä nappia painetaan.
*  -Varmistetaan kirjautuvan käyttäjän tiedot, ja jos ne ovat oikein, avataan pääikkunanäkymä ja jos ne ovat väärät,
*   heitetään virheviesti.
*  -Talletetaan kayttaja-muuttujaan valitun käyttäjän id, jota käytetään myöhemmin apuna pääikkunanäkymässä oikean
*   käyttäjän tietojen näyttämiseen.
 */

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

    @FXML
    private PasswordField passwordlogin;

    @FXML
    private CheckBox CheckBoxPassword;


    Connection conn = null;

    // Yleinen muuttuja, jota käytetään apuna pääikkunanäkymässä oikean käyttäjän tietojen hakemisessa TableViewiin.
    public static int kayttaja;

/*
* Metodi joka avaa uuden käyttäjän luomis -ikkunan kun painetaan luo käyttäjä -nappia.
 */
    public void avaaluokayttaja() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerNewUser.fxml"));
        Parent parent = fxmlLoader.load();

        Stage primarystage = new Stage();
        primarystage.setTitle("New User");
        primarystage.setScene(new Scene(parent));
        primarystage.setResizable(false);
        primarystage.initModality(Modality.APPLICATION_MODAL);

        primarystage.showAndWait();
    }

/*
* Metodi jolla luetaan käyttäjän syöttämä tunnus ja salasana. Sitten käytetään tietokantaluokan verifyKayttaja -metodia
* oikean käyttäjän varmistukseen, ja avataan pääikkuna jos tiedot ovat oikein ja heitetään virheviesti jos tiedot ovat
* väärin.
 */
    public void varmistakayttaja() throws SQLException, IOException {
        conn =SQLTietokanta.Openconnection();


        /*
         * salasana-muuttuja on luotu näillä ehdoilla siksi, että salasanan luku onnistuisi oikein. Jos
         * salasana on piilotetussa muodossa, luetaan suojauksen takana olevaa oikeaa salasanaa mutta jos salasana
         * on näkyvissä, luetaan näkyvää salasanaa.
         */
        String salasana = CheckBoxPassword.isSelected()
                ? TextFieldSalasana.getText()
                : passwordlogin.getText();
        String tunnus = TextFieldTunnus.getText();

        // Tietokantaluokan metodi, jolla varmistetaan käyttäjän olemassaolo tietokannassa.
        if (SQLTietokanta.verifyKayttaja(tunnus,salasana,conn)){

            /* Asetetaan yleiseen kayttaja-muuttujaan valitun käyttäjän id, jota tullaan käyttämään TableView:ssä oikeiden
            * tietojen näyttämiseen.
            */
            kayttaja = SQLTietokanta.valitseKayttaja(tunnus,conn);

            // Sulkee kirjautumisikkunan
            Stage stage = (Stage) ButtonLogin.getScene().getWindow();
            stage.close();

            try {

                // Avaa päänäkymän jos käyttäjän tiedot ovat oikein.
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
            // Heitetään virhe jos käyttäjätiedot ovat väärin.
            KirjautumisError.setText("Käyttäjätunnus tai salasana väärin!");
        }


    }

    // Metodi jota käytetään yleisen muuttujan kayttaja lähettämiseen TaskManagerMainUiController -luokkaan.
    public static int valittukayttaja(){
        return kayttaja;
    }


    /* Metodi, jolla CheckBox-ruudussa ollessa ruksi salasana muuttuu näkyväksi ja CheckBox-ruudun ollessa tyhjänä
     * salasana näkyy palleroina.
     */
    @FXML
    private void ChangeHiddenLogin() {
        if (CheckBoxPassword.isSelected()) {
            TextFieldSalasana.setText(passwordlogin.getText());
            TextFieldSalasana.setVisible(true);
            passwordlogin.setVisible(false);
            return;
        }
        passwordlogin.setText(TextFieldSalasana.getText());
        passwordlogin.setVisible(true);
        TextFieldSalasana.setVisible(false);
    }


}
