package ni.edu.uam.hibernateorm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("estudiante-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 580);
        stage.setTitle("Sistema de Gestión Académica - UAM");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}