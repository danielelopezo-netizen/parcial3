package co.edu.uniquindio.smarthome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/smarthome/view/main-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 780);
        primaryStage.setTitle("SmartHomeFX - Sistema de Gestión y Automatización para Hogares Inteligentes");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
