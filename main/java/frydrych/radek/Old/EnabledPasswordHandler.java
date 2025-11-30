package frydrych.radek.Old;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class EnabledPasswordHandler {
   private String password;

    public EnabledPasswordHandler() {
    }

    public EnabledPasswordHandler(String password) {
        this.password = password;
    }

    public String getPassword() {
        if (password!=null) return password;
        else {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enabled Password");
            dialog.setContentText("Enter Password:");
            Optional<String> result = dialog.showAndWait();
            if (result!= null) return null;
            return result.get();
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
