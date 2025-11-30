//package frydrych.radek;
//
//import javafx.geometry.Insets;
//import javafx.scene.control.Label;
//import javafx.scene.control.Tooltip;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PortInfoDisplayFromSSHApp {
//
//    private SSHConnectionApp sshConnectionApp;
//
//    public PortInfoDisplayFromSSHApp(SSHConnectionApp sshConnectionApp) {
//        this.sshConnectionApp = sshConnectionApp;
//    }
//
//    // This method will return a GridPane instead of creating a new window
//    public GridPane createPortInfoGrid() {
//        GridPane gridPane = new GridPane();
//        gridPane.setPadding(new Insets(10));
//        gridPane.setHgap(15);
//        gridPane.setVgap(15);
//
//        // Use the existing SSH connection
//        if (sshConnectionApp.isFakeConnection()) {
//            // Simulate a fake command response for testing
//            String fakeOutput = "FastEthernet0/1 unassigned YES unset administratively down down\n" +
//                    "FastEthernet0/2 unassigned YES unset up down\n" +
//                    "FastEthernet0/3 192.168.1.1 YES manual up up\n";
//
//            List<PortInfo> portInfoList = parseShowIpInterfaceBriefOutput(fakeOutput);
//            createPortBlocks(gridPane, portInfoList);
//
//        } else if (sshConnectionApp.getSshConnection() != null && sshConnectionApp.getSshConnection().isConnected()) {
//            // Execute real SSH command
//            String command = "show ip interface brief";
//            String sshOutput = sshConnectionApp.getSshConnection().executeCommand(command);
//
//            // Parse the output and generate port blocks
//            List<PortInfo> portInfoList = parseShowIpInterfaceBriefOutput(sshOutput);
//            createPortBlocks(gridPane, portInfoList);
//        } else {
//            System.out.println("No active SSH connection!");
//        }
//
//        return gridPane; // Return the populated GridPane
//    }
//
//    // Example of parsing output from "show ip interface brief"
//    private List<PortInfo> parseShowIpInterfaceBriefOutput(String output) {
//        List<PortInfo> portInfoList = new ArrayList<>();
//        String[] lines = output.split("\n");
//
//        for (String line : lines) {
//            String[] columns = line.trim().split("\\s+");
//            if (columns.length >= 6) {
//                String interfaceName = columns[0];
//                String ipAddress = columns[1];
//                String status = columns[4] + " " + columns[5]; // Status and Protocol
//                PortInfo portInfo = new PortInfo(interfaceName, ipAddress, status);
//                portInfoList.add(portInfo);
//            }
//        }
//        return portInfoList;
//    }
//
//    // Method to create visual blocks for each port
//    private void createPortBlocks(GridPane gridPane, List<PortInfo> portInfoList) {
//        int numCols = 4;  // Number of columns in the grid
//        int row = 0;
//        int col = 0;
//
//        for (PortInfo portInfo : portInfoList) {
//            StackPane portBlock = createPortBlock(portInfo);
//            gridPane.add(portBlock, col, row);
//            col++;
//
//            if (col >= numCols) {
//                col = 0;
//                row++;
//            }
//        }
//    }
//
//    // Method to create an individual port block
//    private StackPane createPortBlock(PortInfo portInfo) {
//        Rectangle portSquare = new Rectangle(100, 100);
//        portSquare.setFill(Color.LIGHTBLUE);
//        portSquare.setStroke(Color.DARKBLUE);
//
//        Label portLabel = new Label(portInfo.interfaceName);
//        portLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
//
//        Tooltip portTooltip = new Tooltip("IP: " + portInfo.ipAddress + "\nStatus: " + portInfo.status);
//        Tooltip.install(portSquare, portTooltip);
//
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().addAll(portSquare, portLabel);
//
//        stackPane.setOnMouseEntered(e -> portSquare.setFill(Color.DODGERBLUE));
//        stackPane.setOnMouseExited(e -> portSquare.setFill(Color.LIGHTBLUE));
//
//        return stackPane;
//    }
//
//    // Class to store information about each port
//    private static class PortInfo {
//        String interfaceName;
//        String ipAddress;
//        String status;
//
//        PortInfo(String interfaceName, String ipAddress, String status) {
//            this.interfaceName = interfaceName;
//            this.ipAddress = ipAddress;
//            this.status = status;
//        }
//    }
//}
