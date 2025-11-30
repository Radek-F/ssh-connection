package frydrych.radek;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandParser {
    // Parses a command line and detects interactive parts
    public static String parseAndPromptCommand(String command) {
        // Check for choice prompt syntax
        if (command.contains("<choice")) {
            command = handleChoicePrompt(command);
        }
        // Check for write prompt syntax
        if (command.contains("<write")) {
            command = handleWritePrompt(command);
        }

        return command;
    }

    // Handle <choice,(options...),Prompt> syntax
    private static String handleChoicePrompt(String command) {
        int start = command.indexOf("<choice");
        int end = command.indexOf(">", start);

        if (start == -1 || end == -1) return command; // Fail-safe for malformed commands

        String choiceSegment = command.substring(start, end + 1);

        // Extract options and prompt text accurately
        int optionsStart = choiceSegment.indexOf("(") + 1;
        int optionsEnd = choiceSegment.indexOf(")");
        if (optionsStart == 0 || optionsEnd == -1) return command; // Ensure options are well-formed

        String optionsPart = choiceSegment.substring(optionsStart, optionsEnd).trim();
        String prompt = choiceSegment.substring(optionsEnd + 2, choiceSegment.length() - 1).trim();

        // Split options by comma
        String[] choices = optionsPart.split(",");

        // Create and display ChoiceDialog
        List<String> options = Arrays.asList(choices);
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setHeaderText(prompt);
        dialog.setContentText("Choose an option:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            command = command.replace(choiceSegment, result.get());
        }
        return command;
    }

    // Handle <write,Prompt> syntax
    private static String handleWritePrompt(String command) {
        int start = command.indexOf("<write");
        int end = command.indexOf(">", start);
        String writeSegment = command.substring(start, end + 1);

        // Extract prompt text
        String[] parts = writeSegment.split(",");
        String prompt = parts[1].replace(">", "").trim();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(prompt);
        dialog.setContentText("Enter value:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            command = command.replace(writeSegment, result.get());
        }
        return command;
    }
}
