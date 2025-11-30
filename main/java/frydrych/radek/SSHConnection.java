package frydrych.radek;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import frydrych.radek.Old.EnabledPasswordHandler;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;

public class SSHConnection {
    private Session session;
    private ChannelShell channel;
    private PrintWriter writer;
    private InputStream inStream;
    public TextArea outputArea;  // Reference to the TextArea for appending output
    private final EnabledPasswordHandler Handler = new EnabledPasswordHandler();

    public TextArea getOutputArea() {
        return outputArea;
    }

    private boolean AutoCompleat = false;

    // Constructor to pass in the TextArea reference
    public SSHConnection(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    private String user;

    public boolean connect(String host, String user, String password) {
        try {
            // Enable JSch logging
            JSch.setLogger(new MyLogger(outputArea));

            // Set up the JSch session
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);

            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");
            session.setServerAliveInterval(1000);
            // Establish the connection
            appendText("Connecting to the router...\n");
            session.connect();

            // Open a shell channel
            channel = (ChannelShell) session.openChannel("shell");

            // Setup input and output streams for the channel
            PipedInputStream pipedIn = new PipedInputStream();
            PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);

            channel.setInputStream(pipedIn);
            inStream = channel.getInputStream();
            writer = new PrintWriter(pipedOut, true);

            try {
                channel.connect();
                appendText("Connected to the router shell.\n");
            } catch (JSchException e) {
                appendText("Failed to open SSH channel: " + e.getMessage() + "\n");
                return false;
            }


            // Set terminal length to 0 to disable paging
            writer.println("terminal length 0");
            writer.flush();
            SSHConnectionApp.activeConnectin = this;
            // Start reading the output in a separate thread
            new Thread(this::readOutput).start();

            return true; // Connection established successfully
        } catch (JSchException e) {
            appendText("An error occurred during the SSH connection setup: " + e.getMessage() + "\n");
            return false; // Connection failed
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Connection failed
        }
    }

    public String executeCommand(String command) {
        if (writer != null && channel.isConnected()) {
            System.out.println("Executing command: " + command);
            writer.println(command);
            writer.flush();
            appendText("Command executed: " + command + "\n");
            return command;
        } else {
            appendText("Not connected or writer is null!\n");
            return null;
        }
    }

    // Read the output from the SSH session and append it to the TextArea
    private void readOutput() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while (session.isConnected() && (line = reader.readLine()) != null) {
                System.out.println("Received: " + line);  // Debugging
                if (!AutoCompleat) appendText(line + "\n");
                else appendText("AutoCompleat" + line + "\n");

                // Handling --More-- or confirmation prompts
                if (line.contains("--More--") || line.contains("[confirm]")) {
                    writer.print(" ");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            appendText("Error reading output: " + e.getMessage() + "\n");
        }
    }

    // Disconnect the SSH session and channel
    public void disconnect() {
        if (channel != null) {
            try {
                inStream.close();
                inStream=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
            channel=null;
        }
        if (session != null) {
            session.disconnect();
            session=null;
        }
        if (writer!= null){
            writer.close();
            writer=null;
        }

        appendText("Disconnected from the router.\n");
    }

    public String executeAutoComplete(String partialCommand) {
        if (writer != null) {
            AutoCompleat = true;

            // Send the command with '?' for auto-completion
            writer.print(partialCommand + "?");
            writer.flush();




        }
        return partialCommand;
    }



    // Helper method to append text to the TextArea on the JavaFX Application Thread
    private void appendText(String text) {
        Platform.runLater(() -> {
            System.out.println("Appending text to UI: " + text);
            outputArea.appendText(text);
        });
    }

    // Custom logger for JSch
    public static class MyLogger implements com.jcraft.jsch.Logger {
        static java.util.Hashtable<Integer, String> name = new java.util.Hashtable<>();
        private final TextArea outPutArea;
        static {
            name.put(DEBUG, "DEBUG: ");
            name.put(INFO, "INFO: ");
            name.put(WARN, "WARN: ");
            name.put(ERROR, "ERROR: ");
            name.put(FATAL, "FATAL: ");
        }

        public MyLogger(TextArea outputArea) {
            this.outPutArea=outputArea;
        }



        public boolean isEnabled(int level) {
            return true;
        }

        public void log(int level, String message) {
            System.out.print(name.get(level));
            System.out.println(message);
          //  outPutArea.appendText(name.get(level)+message+"\n");
        }
    }

    public boolean isConnected() {
        return session != null && session.isConnected();
    }
}
