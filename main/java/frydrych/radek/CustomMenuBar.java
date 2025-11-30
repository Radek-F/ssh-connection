package frydrych.radek;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CustomMenuBar {

    private Stage primaryStage;

    public CustomMenuBar(Stage primaryStage, SSHConnectionHandler sshConnectionHandler) {
        this.primaryStage = primaryStage;
    }

    public CustomMenuBar(Stage primaryStage) {
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem loadConfigItemSmart = new MenuItem("Load Configuration Smart");
        loadConfigItemSmart.setOnAction(e -> SmartloadConfigurationFromFile());

        MenuItem loadConfigItem = new MenuItem("Load Configuration Smart");
        loadConfigItem.setOnAction(e -> SmartloadConfigurationFromFile());

        MenuItem loadConfigItem2 = new MenuItem("Load Configuration");
        loadConfigItem2.setOnAction(e -> LoadConfig());

        MenuItem helpItem = new MenuItem("Nápověda");
        helpItem.setOnAction(e -> showHelp());


        fileMenu.getItems().addAll(loadConfigItem);
        fileMenu.getItems().addAll(loadConfigItem2);
        fileMenu.getItems().addAll(helpItem);

        menuBar.getMenus().addAll(fileMenu);

        return menuBar;
    }
    private void showHelp() {
        // Vytvoříme okno pro zobrazení nápovědy
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nápověda");
        alert.setHeaderText("Nápověda - SSH Konfigurátor");

        String content = "Tento program pracuje s SSH a dokáže načíst textové soubory (.txt).\n\n"
                + "Je určen k nahrávání konfigurací ze souborů na server.\n\n"
                + "Existují dva způsoby načítání konfigurací:\n"
                + "1. Normální načítání - Běžné nahrávání konfigurace.\n"
                + "2. Smart načítání - Používá speciální syntaxi pro interakci se serverem.\n\n"
                + "Smart načítání Syntaxe:\n"
                + " - Tato syntaxe umožňuje interaktivní výzvy, jako jsou <choice> a <write>.\n"
                + " - Příklad: Při připojení přes SSH můžete stisknout TAB pro zobrazení návrhů příkazů podobně jako u Cisco zařízení.\n"
                + " - Stisknutím ENTER odešlete příkaz na server.\n\n"
                + "Syntaxe programu:\n"
                + " - <choice> - Zobrazí seznam možností pro výběr.\n"
                + " - <write> - Požádá uživatele o zadání hodnoty.\n";

        alert.setContentText(content);

        // Zobrazí okno nápovědy
        alert.showAndWait();
    }

    // Load configuration from file
    private void SmartloadConfigurationFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Smart Open Configuration File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                List<String> commands = Files.readAllLines(file.toPath());
                executeParsedCommands(commands);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void LoadConfig(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Smart Open Configuration File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            LoadConfig loadConfig = new LoadConfig();
            loadConfig.loadCommandsFromFile(file);
        }
    }
    // Process commands with interactive input
    private void executeParsedCommands(List<String> commands) throws IOException {
        for (String command : commands) {
            // Parse command to handle any <choice> or <write> prompts
            String parsedCommand = CommandParser.parseAndPromptCommand(command);

            // Execute the parsed command
            System.out.printf(String.valueOf(SSHConnectionApp.activeConnectin));
            if (SSHConnectionApp.activeConnectin!=null) {
                SSHConnectionApp.activeConnectin.executeCommand(parsedCommand);
            }else
            {
                System.out.println(parsedCommand);
            }
        }
    }
}
