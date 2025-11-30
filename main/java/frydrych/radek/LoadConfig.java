package frydrych.radek;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadConfig {

    // Load configuration (list of commands) from a file
    public List<String> loadCommandsFromFile(File configFile) {
        List<String> commands = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    commands.add(line.trim()); // Add the command to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commands;
    }
}
