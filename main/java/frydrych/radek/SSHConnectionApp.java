package frydrych.radek;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SSHConnectionApp {

    private TabPane tabPane;
    public static SSHConnection activeConnectin;
    public static TextArea outputArea;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("SSH Connection");

        BorderPane root = new BorderPane();

        // Integrate the CustomMenuBar
        CustomMenuBar customMenuBar = new CustomMenuBar(primaryStage);
        root.setTop(customMenuBar.createMenuBar()); // Add the custom menu bar to the top

        tabPane = new TabPane();
        Tab addTab = createAddTab();
        tabPane.getTabs().add(addTab);

        root.setCenter(tabPane); // Add the tab pane to the center

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Automatically open the first connection
        openNewConnection();
    }

    // Create the "+" tab for adding new connections
    private Tab createAddTab() {
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        addTab.setOnSelectionChanged(e -> {
            if (addTab.isSelected()) {
                openNewConnection();
            }
        });
        return addTab;
    }

    // Open a new tab for an SSH connection
    private void openNewConnection() {
        Tab connectionTab = new Tab("Connection");
        SSHLoginPane loginPane = new SSHLoginPane(tabPane,connectionTab); // Handle login
        connectionTab.setContent(loginPane.getLoginPane());
        tabPane.getTabs().add(tabPane.getTabs().size() - 1, connectionTab);
        tabPane.getSelectionModel().select(connectionTab);
        activeConnectin= loginPane.connection;
    }
}
