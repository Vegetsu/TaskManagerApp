package org.example.TaskManagerApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.DefaultStringConverter;

import java.awt.event.MouseEvent;
import java.util.Arrays;

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
    private TableView<Tehtava> TableViewCenter;

    @FXML
    private Text TextTehtavalista;

    @FXML
    private TextField TextFieldNimi;

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

    @FXML
    private void initialize() {
        TableColumnNimi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavanimi()));
        TableColumnKuvaus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTehtavakuvaus()));

        TableColumnNimi.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.5));
        TableColumnKuvaus.prefWidthProperty().bind(TableViewCenter.widthProperty().multiply(0.5));


        TableColumnNimi.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()) {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(TableColumnNimi.widthProperty().subtract(10));
            }

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
        TableColumnNimi.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tehtava, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Tehtava, String> tehtavaStringCellEditEvent) {
                Tehtava tehtava = tehtavaStringCellEditEvent.getRowValue();
                tehtava.setTehtavanimi(tehtavaStringCellEditEvent.getNewValue());
            }
        });

        TableColumnKuvaus.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()) {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(TableColumnKuvaus.widthProperty().subtract(10));
            }

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
        TableColumnKuvaus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Tehtava, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Tehtava, String> tehtavaStringCellEditEvent) {
                Tehtava tehtava = tehtavaStringCellEditEvent.getRowValue();
                tehtava.setTehtavakuvaus(tehtavaStringCellEditEvent.getNewValue());
            }
        });



    }

    @FXML
    private void AddTehtava() {

        String nimi = TextFieldNimi.getText();
        String kuvaus = TextFieldKuvaus.getText();

        if (TextFieldNimi.getText().isEmpty() || TextFieldKuvaus.getText().isEmpty()){
            TextIlmoitusAdd.setText("Molemmissa tekstikentissä pitää olla tekstiä!");
        }
        else {
            TableViewCenter.getItems().add(new Tehtava(nimi, kuvaus));

            TextFieldNimi.clear();
            TextFieldKuvaus.clear();
            TextIlmoitusAdd.setText("");
        }
    }

    @FXML
    private void DeleteTehtava() {

        TableView.TableViewSelectionModel<Tehtava> selectionModel = TableViewCenter.getSelectionModel();

        if (selectionModel.isEmpty()){
            TextIlmoitusDelete.setText("Valitse poistettava kenttä!");
        }
        else {
            ObservableList<Integer> list = selectionModel.getSelectedIndices();
            Integer[] selectedIndices = new Integer[list.size()];
            selectedIndices = list.toArray(selectedIndices);
            Arrays.sort(selectedIndices);

            for (int i = selectedIndices.length-1; i>=0; i--){
                selectionModel.clearSelection(selectedIndices[i]);
                TableViewCenter.getItems().remove(selectedIndices[i].intValue());
            }
            TextIlmoitusDelete.setText("");
        }
    }


}
