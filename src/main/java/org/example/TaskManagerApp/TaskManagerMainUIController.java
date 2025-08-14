package org.example.TaskManagerApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/*
* Taskmanagerin pääikkunan controller-luokka.
*  -Määrittelee mitä tietoja käyttäjälle näytetään tietokannasta.
*  -Huolehtii että oikean käyttäjän tehtävät näytetään tietokannasta
*  -Määrittelee lisäys-, muokkaus- ja poistotoiminnot käyttäjälle näytettävästä taulusta,
*   ja samalla muokkaa tietoja tietokannassa.
 */

public class TaskManagerMainUIController {


    @FXML
    private AnchorPane AnchorPaneLeft;

    @FXML
    private Button ButtonSave;

    @FXML
    private Button ButtonPoista;

    @FXML
    private HBox HboxTop;

    @FXML
    private TableColumn<Tehtava, String> TableColumnKuvaus;

    @FXML
    private TableColumn<Tehtava, String> TableColumnNimi;

    @FXML
    private TableColumn<Tehtava, Integer> TableColumnId;

    @FXML
    private TableView<Tehtava> TableViewCenter;

    @FXML
    private Text TextTehtavalista;

    @FXML
    private TextField TextFieldId;

    @FXML
    private TextField TextFieldNimi;

    @FXML
    private Text TextId;

    @FXML
    private Text TextKuvaus;

    @FXML
    private Text TextNimi;

    @FXML
    private TextField TextOtsikko;

    @FXML
    private TextField TextFieldKuvaus;

    @FXML
    private Text TextIlmoitusAdd;

    @FXML
    private Text TextIlmoitusDelete;

    @FXML
    private StackPane StackPaneAdd;

    @FXML
    private StackPane StackPaneDelete;

    // Tietokannan tiedot sisältävä lista
    ObservableList<Tehtava> tehtavalist;

    // Yhteys MySQL:ään
    Connection conn = null;


    /*
    * Alustaa pääikkunan, kun se avataan.
    *  -Hakee tietokannasta oikean käyttäjän tiedot ja lisää ne listaan jota käytetään TableView:ssä
    *  -Asettaa TableView:n näyttämään oikeita arvoja ja asettaa tiedot viemään puolet ja puolet TableView:n tilasta.
    *  -Määrittää muokkausmahdollisuudet TableView:n arvoille sekä vie päivitykset tietokantaan.
     */
    @FXML
    private void initialize() throws SQLException, IOException {

        // Avataan tietokantayhteys ja asetetaan haetut arvot TableView:iin.
        conn =SQLTietokanta.Openconnection();
        tehtavalist =SQLTietokanta.Tehtavatiedot(TaskManagerLoginController.valittukayttaja());
        TableViewCenter.setItems(tehtavalist);

        //Asettaa TableView:n solut saamaan arvot TextFieldId-tekstiboksista, TextFieldNimi-tekstiboksista ja TextFieldKuvaus-tekstiboksista
        TableColumnNimi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavanimi()));
        TableColumnKuvaus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavakuvaus()));

        //Asettaa TableView:n solut viemään puolet ja puolet Tableview:n koosta
        TableColumnNimi.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.5));
        TableColumnKuvaus.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.5));

        //Metodi jossa muutetaan TableView:n Nimi-sarakkeen tietoja
        TableColumnNimi.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()) {
            private final Text text = new Text();

            {
                //Asettaa sarakkeen leveyden niin, että jos teksti menee yli reunojen, niin se pinoutuu seuraavalle riville
                text.wrappingWidthProperty().bind(TableColumnNimi.widthProperty().subtract(10));
            }

            //Päivittää solun tekstin uudeksi, jos sitä muokataan
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        });

        //Kun solua klikataan, niin sitä voidaan muokata, ja uusi teksti asetataan soluun
        TableColumnNimi.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tehtava, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Tehtava, String> tehtavaStringCellEditEvent) {
                Tehtava tehtava = tehtavaStringCellEditEvent.getRowValue();
                tehtava.setTehtavanimi(tehtavaStringCellEditEvent.getNewValue());

                try {
                    SQLTietokanta.PaivitaNimi(tehtavaStringCellEditEvent.getNewValue(),tehtava.getTehtavaId(),conn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }



        });

        //Metodi jossa muutetaan TableView:n Kuvaus-sarakkeen tietoja
        TableColumnKuvaus.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()) {
            private final Text text = new Text();

            {
                //Asettaa sarakkeen leveyden niin, että jos teksti menee yli reunojen, niin se pinoutuu seuraavalle riville
                text.wrappingWidthProperty().bind(TableColumnKuvaus.widthProperty().subtract(10));
            }

            //Päivittää solun tekstin uudeksi, jos sitä muokataan
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        });

        //Kun solua klikataan, niin sitä voidaan muokata, ja uusi teksti asetataan soluun
        TableColumnKuvaus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tehtava, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Tehtava, String> tehtavaStringCellEditEvent) {
                Tehtava tehtava = tehtavaStringCellEditEvent.getRowValue();
                tehtava.setTehtavakuvaus(tehtavaStringCellEditEvent.getNewValue());
                try {
                    SQLTietokanta.PaivitaKuvaus(tehtavaStringCellEditEvent.getNewValue(),tehtava.getTehtavaId(),conn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }

    /*Metodi, jossa tekstikentistä luetaan käyttäjän syöte ja TableView:iin lisätyt tiedot lisätään myös tietokantaan.
    * oikean käyttäjän id:n mukaan.
     */
    @FXML
    private void AddTehtava() {

        String nimi = TextFieldNimi.getText();
        String kuvaus = TextFieldKuvaus.getText();

        //Jos TextField tyhjä, niin error-viesti
        if (TextFieldNimi.getText().isEmpty() || TextFieldKuvaus.getText().isEmpty()){
            TextIlmoitusAdd.setText("Kaikissa tekstikentissä pitää olla tekstiä!");
        }

        else{
        //Muuten lisätään uusi tehtävä oiekan käyttäjän id:n mukaan ja tyhjennetään tekstikentät
        try {


            int uusiId = SQLTietokanta.lisaatehtava(nimi, kuvaus, TaskManagerLoginController.valittukayttaja(), conn);
            if (uusiId != -1) {
                TableViewCenter.getItems().add(new Tehtava(uusiId, nimi, kuvaus, TaskManagerLoginController.valittukayttaja()));
            }



            // Tyhjennetään kentät
            TextFieldNimi.clear();
            TextFieldKuvaus.clear();
            TextIlmoitusAdd.setText("");

        } catch (NumberFormatException e) {
            // Error jos yritetään lisätä id:ksi jotain muuta kuin kokonaislukua.
            TextIlmoitusAdd.setText("Tehtävän ID:n täytyy olla kokonaisluku!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
    }

    //Metodi, jossa tehtävä poistetaan
    @FXML
    private void DeleteTehtava() {

        //Klikataan TableView:stä jokin solu
        TableView.TableViewSelectionModel<Tehtava> selectionModel = TableViewCenter.getSelectionModel();

        //Jos solu on tyhjä, niin error-viesti
        if (selectionModel.isEmpty()){
            TextIlmoitusDelete.setText("Valitse poistettava solu!");
        }
        else {

            try {

                //Ladataan Confirmation screen -stage, joka avataan ruudulle, kun Poista tehtävä -nappia painetaan
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerConfirmation.fxml"));
                Parent parent = fxmlLoader.load();

                //Haetaan Confirmation screenin controller käytettäväksi tälle luokalle
                TaskManagerConfirmationController TaskManagerConfirmationKontrolli= fxmlLoader.getController();
                TaskManagerConfirmationKontrolli.haeTaskManagerMainUIKontrolli(this);

                Stage primarystage = new Stage();
                primarystage.setTitle("Confirmation screen");
                primarystage.setScene(new Scene(parent));
                primarystage.setResizable(false);
                primarystage.initModality(Modality.APPLICATION_MODAL);

                //Asetetaan valitun tehtävän nimi näkyväksi Confirmation screen -ikkunaan
                TaskManagerConfirmationKontrolli.asetaTehtavaNimi(TableViewCenter.getSelectionModel().getSelectedItem().getTehtavanimi());

                primarystage.showAndWait();


                /* Luetaan käyttäjän syöte "tehtävän poiston varmistus" -ikkunasta, ja jos käyttäjä
                * valitsee kyllä, niin poistetaan valittu tehtävä TableView:stä sekä tietokannasta.
                 */
                if (TaskManagerConfirmationKontrolli.getUserChoice()){
                    Tehtava valittu = TableViewCenter.getSelectionModel().getSelectedItem();
                    int id = valittu.getTehtavaId();
                    SQLTietokanta.poistaTehtava(id,conn);
                    selectionModel.clearSelection(id);
                    TableViewCenter.getItems().remove(valittu);
                }

                //Jos käyttäjä valitsee Ei-confirmation screenissä, niin ikkuna vain suljetaan eikä muuta tehdä
                else {

                    primarystage.close();
                }
            }

            catch (IOException ioError) {
                ioError.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }




}
