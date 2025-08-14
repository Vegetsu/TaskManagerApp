package org.example.TaskManagerApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;




// Metodi joka avaa TaskManagerConfirmation -ruudun.
public class TaskManagerConfirmation extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerConfirmation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Confirmation screen");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }


}