package org.example.TaskManagerApp;

import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.Arrays;

public class TaskManagerMainUIController {

   // static  TaskManagerConfirmation TaskManagerConfirmationkontrolleri;

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

    private Integer tehtavanykyId;


    public class CustomIntegerStringConverter extends IntegerStringConverter{
        private final IntegerStringConverter converter = new IntegerStringConverter();

        @Override
        public String toString(Integer object) {
            try {
                return converter.toString(object);
            } catch (NumberFormatException e) {
                TextIlmoitusAdd.setText("Kaikissa tekstikentissä pitää olla tekstiä!");
            }
            return null;
        }

        @Override
        public Integer fromString(String string) {
            try {
                return converter.fromString(string);
            } catch (NumberFormatException e) {
                TextIlmoitusAdd.setText("Tehtävän ID:n täytyy olla kokonaisluku!");
            }
            return null;
        }
    }

    //AlustaaTableView:n eri tietoja, kun ikkuna avataan
    @FXML
    private void initialize() {

        //Asettaa TableView:n solut saamaan arvot TextFieldId-tekstiboksista, TextFieldNimi-tekstiboksista ja TextFieldKuvaus-tekstiboksista
        TableColumnId.setCellValueFactory(data -> data.getValue().tehtavaIdProperty().asObject());
        TableColumnNimi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavanimi()));
        TableColumnKuvaus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavakuvaus()));

        //Asettaa TableView:n solut viemään puolet ja puolet Tableview:n koosta
        TableColumnId.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.33));
        TableColumnNimi.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.33));
        TableColumnKuvaus.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.33));

        //Metodi jossa muutetaan TableView:n ID-sarakkeen tietoja
        TableColumnId.setCellFactory(column -> new TextFieldTableCell<Tehtava, Integer>(new CustomIntegerStringConverter()) {
            private final Text text = new Text();

            {
                //Asettaa sarakkeen leveyden niin, että jos teksti menee yli reunojen, niin se pinoutuu seuraavalle riville
                text.wrappingWidthProperty().bind(TableColumnId.widthProperty().subtract(10));
            }

            //Päivittää solun tekstin uudeksi, jos sitä muokataan
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item.toString());
                    setGraphic(text);
                }
            }
        });

        //Kun solua klikataan, niin sitä voidaan muokata, ja uusi teksti asetataan soluun
        TableColumnId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tehtava, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Tehtava, Integer> tehtavaIntegerCellEditEvent) {
                Tehtava tehtava = tehtavaIntegerCellEditEvent.getRowValue();
                Integer uusiArvo = tehtavaIntegerCellEditEvent.getNewValue();
                //tehtavanykyId = tehtavaIntegerCellEditEvent.getNewValue();
                if (uusiArvo != null) {
                    tehtava.setTehtavaId(uusiArvo);
                    TextIlmoitusAdd.setText("");
                }
                else {
                    TableViewCenter.refresh();
                }
            }
        });

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
            }
        });

        //Metodi jossa muutetaan TableView:n Nimi-sarakkeen tietoja
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
            }
        });



    }

    //Metodi, jossa tekstikentistä otetaan tekstiä ja uusi tehtävä lisätään TableView:iin
    @FXML
    private void AddTehtava() {

        String idString = TextFieldId.getText();
        String nimi = TextFieldNimi.getText();
        String kuvaus = TextFieldKuvaus.getText();

        //Jos TextField tyhjä, niin error-viesti
        if (TextFieldId.getText().isEmpty() || TextFieldNimi.getText().isEmpty() || TextFieldKuvaus.getText().isEmpty()){
            TextIlmoitusAdd.setText("Kaikissa tekstikentissä pitää olla tekstiä!");
        }

        else{
        //Muuten lisätään uusi tehtävä ja tyhjennetään tekstikentät
        try {
            int tehtavaid = Integer.parseInt(idString);

            TableViewCenter.getItems().add(new Tehtava(tehtavaid, nimi, kuvaus));

            // Tyhjennetään kentät
            TextFieldId.clear();
            TextFieldNimi.clear();
            TextFieldKuvaus.clear();
            TextIlmoitusAdd.setText("");

        } catch (NumberFormatException e) {
            TextIlmoitusAdd.setText("Tehtävän ID:n täytyy olla kokonaisluku!");
        }}
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
                //TaskManagerConfirmationController controller = fxmlLoader.getController();

                Stage primarystage = new Stage();
                primarystage.setTitle("Confirmation screen");
                primarystage.setScene(new Scene(parent));
                primarystage.setResizable(false);
                //primarystage.show();
                primarystage.initModality(Modality.APPLICATION_MODAL);

                //URL fxmlUrl = getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerConfirmation.fxml");
               // System.out.println("FXML path: " + fxmlUrl); // tämän pitäisi EI olla null

                // (valinnainen) jos haluat estää sulkemisen ruksista, lisää tämä:
                // stage.setOnCloseRequest(event -> event.consume());

                //Asetetaan valitun tehtävän nimi näkyväksi Confirmation screen -ikkunaan
                TaskManagerConfirmationKontrolli.asetaTehtavaNimi(TableViewCenter.getSelectionModel().getSelectedItem().getTehtavanimi());

                primarystage.showAndWait();

                //System.out.println(TableViewCenter.getSelectionModel().getSelectedIndices().toString());

                //Haetaan Confirmation screenin puolelta tieto käyttäjän valinnasta getUserChoice-metodilla, ja jos
                //Käyttäjä on painanut kyllä Confirmation screenillä, niin laitetaan TableView-sarakkeet listaan ja
                //poistetaan valittu sarake TableView:stä. Suljetaan lopuksi Confirmation screen.
                if (TaskManagerConfirmationKontrolli.getUserChoice()){
                    ObservableList<Integer> list = selectionModel.getSelectedIndices();
                    Integer[] selectedIndices = new Integer[list.size()];
                    selectedIndices = list.toArray(selectedIndices);
                    Arrays.sort(selectedIndices);

                    for (int i = selectedIndices.length-1; i>=0; i--){
                        selectionModel.clearSelection(selectedIndices[i]);
                        TableViewCenter.getItems().remove(selectedIndices[i].intValue());
                    }
                    TextIlmoitusDelete.setText("");
                    primarystage.close();
                }

                //Jos käyttäjä valitsee Ei-confirmation screenissä, niin ikkuna vain suljetaan eikä muuta tehdä
                else {
                    //TaskManagerConfirmationKontrolli.CloseWindow();
                    primarystage.close();
                }
            }

            catch (IOException ioError) {
                ioError.printStackTrace();
            }
        }
    }




}
