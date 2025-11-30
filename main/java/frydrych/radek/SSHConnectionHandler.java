package frydrych.radek;

import javafx.scene.control.TextArea;
import java.io.IOException;

public class SSHConnectionHandler {

    private SSHConnection sshConnection;
    private final TextArea outputArea;

    public SSHConnection getSshConnection() {
        return sshConnection;
    }

    public SSHConnectionHandler(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    // Connect to the SSH server or establish a fake connection
    public boolean connect(String host, String user, String password) {

            sshConnection = new SSHConnection(outputArea);
            return sshConnection.connect(host, user, password);
    }

    // Execute commands
    public String executeCommand(String command) throws IOException {
        if (sshConnection != null && sshConnection.isConnected()) {
            return sshConnection.executeCommand(command);
        } else {
            return "No active connection.";
        }
    }

    // Disconnect
    public void disconnect() {
        if (sshConnection != null) {
            sshConnection.disconnect();
        }
        outputArea.appendText("Disconnected.\n");
    }

}
