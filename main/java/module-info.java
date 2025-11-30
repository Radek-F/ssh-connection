module frydrych.radek.sshfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jsch;
    requires flexdock.core;
    requires java.desktop;

    opens frydrych.radek.sshfx to javafx.fxml;
    exports frydrych.radek;
    opens frydrych.radek to javafx.fxml;
    exports frydrych.radek.Old;
    opens frydrych.radek.Old to javafx.fxml;
}