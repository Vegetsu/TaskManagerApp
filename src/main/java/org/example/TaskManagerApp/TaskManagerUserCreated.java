package org.example.TaskManagerApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



// Metodi joka avaa uuden käyttäjän luomisen varmistusikkunan
public class TaskManagerUserCreated extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/TaskManagerApp/fxml/TaskManagerUserCreated.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Created");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }


}