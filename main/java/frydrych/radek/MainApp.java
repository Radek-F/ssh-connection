package frydrych.radek;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up the main GUI
        SSHConnectionApp gui = new SSHConnectionApp();

        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
