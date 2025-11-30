package frydrych.radek;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SSHLoginPane {

    private final TabPane tabPane;
    private TextArea outputArea;
    private final Tab connectionTab;
    public SSHConnection connection;
    public SSHLoginPane(TabPane tabPane, Tab connectionTab) {
        this.tabPane = tabPane;
        this.connectionTab=connectionTab;
    }

    public GridPane getLoginPane() {
        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10));
        loginPane.setVgap(8);
        loginPane.setHgap(10);

        TextField ipField = new TextField("192.168.1.1");
        TextField userField = new TextField("admin");
        PasswordField passField = new PasswordField();
        passField.setText("test");

        loginPane.add(new Label("IP Address:"), 0, 0);
        loginPane.add(ipField, 1, 0);
        loginPane.add(new Label("Username:"), 0, 1);
        loginPane.add(userField, 1, 1);
        loginPane.add(new Label("Password:"), 0, 2);
        loginPane.add(passField, 1, 2);

        Button connectButton = new Button("Connect");
        loginPane.add(connectButton, 1, 3);

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-family: 'Monospace';");

        outputArea.setPrefHeight(800); // Set to any height in pixels

        setupConnectButtonAction(connectButton,ipField,userField,passField);

        return loginPane;
    }
    public EventHandler<ActionEvent> setupConnectButtonAction(Button connectButton, TextField ipField, TextField userField, TextField passField) {
        connectButton.setOnAction(e -> {
            connectButton.setDisable(true);  // Disable button during connection attempt

            String host = ipField.getText();
            String user = userField.getText();
            String password = passField.getText();
            Task<Void> connectionTask = new Task<>() {
                @Override
                protected Void call() {
                    SSHConnectionHandler sshConnectionHandler = new SSHConnectionHandler(outputArea);

                    // Perform the connection in the background
                    boolean connected = sshConnectionHandler.connect(host, user, password);

                    // Update UI on the JavaFX Application Thread after the connection attempt
                    Platform.runLater(() -> {
                        if (connected) {
                            System.out.print("Připojeno");
                            if (connectionTab.isSelected()){
                                connection=sshConnectionHandler.getSshConnection();
                            }
                            outputArea.clear();
                            outputArea.appendText("Connected to " + host + "\n");
                            setupSSHSessionPane(sshConnectionHandler);  // Pass handler to session pane
                        } else {
                            outputArea.appendText("Failed to connect to " + host + "\n");
                        }
                        connectButton.setDisable(false);  // Re-enable button
                    });

                    return null;
                }
            };

            // Start the connection task in a new thread
            Thread connectionThread = new Thread(connectionTask);
            connectionThread.setDaemon(true);  // Ensure thread exits when app closes
            connectionThread.start();
        });
        return null;
    }
    // Set up the SSH session pane
    private void setupSSHSessionPane(SSHConnectionHandler sshConnectionHandler) {
        BorderPane root = new BorderPane();
        SSHSessionPane sessionPane = new SSHSessionPane(outputArea, sshConnectionHandler); // Pass handler to session pane
        root.setCenter(sessionPane.getSSHSessionPane());
        connectionTab.setContent(root);
        tabPane.getSelectionModel().select(connectionTab);
        connectionTab.setOnSelectionChanged(event -> {
            if (connectionTab.isSelected()) {
                SSHConnectionApp.activeConnectin=this.connection;
            }
        });    }
}
