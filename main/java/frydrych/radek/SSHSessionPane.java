package frydrych.radek;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SSHSessionPane {

    private final TextArea outputArea;
    private final SSHConnectionHandler sshConnectionHandler;

    public SSHSessionPane(TextArea outputArea, SSHConnectionHandler sshConnectionHandler) {
        this.outputArea = outputArea;
        this.sshConnectionHandler = sshConnectionHandler;
    }
    private void removeEventHandler(TextField commandInput, EventHandler<KeyEvent> keyPressedHandler) {
        commandInput.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
    }
    public VBox getSSHSessionPane() {
        VBox sshPane = new VBox();
        sshPane.setPadding(new Insets(10));
        sshPane.setSpacing(10);

        // Top Bar with Disconnect Button
        HBox topBar = new HBox();
        topBar.setSpacing(10);

        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

        // Output Area (TextArea)
        outputArea.setWrapText(true);
        outputArea.setPrefSize(580, 1000); // Preferred size for the output area
        outputArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-family: 'Monospace';");
        outputArea.setEditable(false);

        // Command Input Field
        TextField commandInput = new TextField();
        commandInput.setId("commandInput");
        commandInput.setPromptText("Enter command here...");
        commandInput.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-family: 'Monospace';");
        EventHandler<KeyEvent> keyPressedHandler = null;


// Initialize the event handler
        EventHandler<KeyEvent> finalKeyPressedHandler = keyPressedHandler;
        keyPressedHandler = event -> {
            // Check if SSH connection is not connected
            switch (event.getCode()) {
                case ENTER -> {
                    try {
                        executeCommand(commandInput);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                case TAB -> {
                    String partialCommand = commandInput.getText();
                    String suggestions = this.sshConnectionHandler.getSshConnection().executeAutoComplete(partialCommand);

                    // Parse the suggestions to check if there's only one viable option
                    String[] suggestionLines = suggestions.split("\n");
                    if (suggestionLines.length == 1) {
                        // Auto-complete if only one suggestion is available
                        for (String s : suggestionLines) {
                            System.out.printf(s);
                        }
                        commandInput.setText(suggestionLines[0].trim());
                    } else {
                        // Display multiple suggestions in the output area
                        outputArea.appendText("Suggestions for '" + partialCommand + "':\n" + suggestions + "\n");
                    }

                    // Prevent default Tab behavior
                    event.consume();
                }
                default -> {
                    // Handle other keys if necessary or leave empty for no action
                }
            }

        };

// Set the event handler on the TextField
        commandInput.setOnKeyPressed(keyPressedHandler);

// Method to remove the event handler


        // Add elements to the pane
        sshPane.getChildren().addAll(topBar, outputArea, commandInput);

        // Disconnect Button Action
        disconnectButton.setOnAction(e -> sshConnectionHandler.disconnect());
        topBar.getChildren().add(disconnectButton);

        return sshPane;
    }

    // Execute command and display result
    private void executeCommand(TextField commandInput) throws IOException {
        String command = commandInput.getText();
        String result = sshConnectionHandler.executeCommand(command);
        outputArea.appendText(result + "\n");
        commandInput.clear();
    }
}
