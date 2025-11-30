package frydrych.radek.Old;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MenuBarManager {

    private final Stage primaryStage;

    public MenuBarManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem loadConfigItem = new MenuItem("Load Configuration");
        loadConfigItem.setOnAction(e -> loadConfigurationFromFile());

        fileMenu.getItems().addAll(loadConfigItem);
        menuBar.getMenus().addAll(fileMenu);

        return menuBar;
    }

    // Load configuration from file
    private void loadConfigurationFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                List<String> commands = Files.readAllLines(file.toPath());
                System.out.println("Loaded commands: " + commands); // Handle commands as necessary
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
