package frydrych.radek.Old;

import javafx.scene.control.TextInputDialog;

public class ProtocolHandlers {

    public String[] handleDhcpProtocol() {
        TextInputDialog poolDialog = new TextInputDialog("MY_POOL");
        poolDialog.setHeaderText("Enter DHCP Pool Name");
        String poolName = poolDialog.showAndWait().orElse(null);
        if (poolName == null) return null;  // Exit if the user cancels

        TextInputDialog networkDialog = new TextInputDialog("192.168.10.0");
        networkDialog.setHeaderText("Enter Network");
        String network = networkDialog.showAndWait().orElse(null);
        if (network == null) return null;

        TextInputDialog subnetDialog = new TextInputDialog("255.255.255.0");
        subnetDialog.setHeaderText("Enter Subnet Mask");
        String subnetMask = subnetDialog.showAndWait().orElse(null);
        if (subnetMask == null) return null;

        TextInputDialog routerDialog = new TextInputDialog("192.168.10.1");
        routerDialog.setHeaderText("Enter Default Router");
        String defaultRouter = routerDialog.showAndWait().orElse(null);
        if (defaultRouter == null) return null;

        TextInputDialog dnsDialog = new TextInputDialog("8.8.8.8");
        dnsDialog.setHeaderText("Enter DNS Server");
        String dnsServer = dnsDialog.showAndWait().orElse(null);
        if (dnsServer == null) return null;

        return new Commands().Dhcp(poolName, network, subnetMask, defaultRouter, dnsServer);
    }

    public String[] handleNatProtocol() {
        TextInputDialog publicInterfaceDialog = new TextInputDialog("GigabitEthernet0/0");
        publicInterfaceDialog.setHeaderText("Enter Public Interface");
        String publicInterface = publicInterfaceDialog.showAndWait().orElse(null);
        if (publicInterface == null) return null;  // Exit if the user cancels

        TextInputDialog privateInterfaceDialog = new TextInputDialog("GigabitEthernet0/1");
        privateInterfaceDialog.setHeaderText("Enter Private Interface");
        String privateInterface = privateInterfaceDialog.showAndWait().orElse(null);
        if (privateInterface == null) return null;

        TextInputDialog accessListRangeDialog = new TextInputDialog("192.168.1.0");
        accessListRangeDialog.setHeaderText("Enter Access List Range");
        String accessListRange = accessListRangeDialog.showAndWait().orElse(null);
        if (accessListRange == null) return null;

        return new Commands().Nat(publicInterface, privateInterface, accessListRange);
    }

    public String[] handleSshProtocol() {
        TextInputDialog usernameDialog = new TextInputDialog("admin");
        usernameDialog.setHeaderText("Enter Username");
        String username = usernameDialog.showAndWait().orElse(null);
        if (username == null) return null;  // Exit if the user cancels

        TextInputDialog passwordDialog = new TextInputDialog("password");
        passwordDialog.setHeaderText("Enter Password");
        String password = passwordDialog.showAndWait().orElse(null);
        if (password == null) return null;

        TextInputDialog domainDialog = new TextInputDialog("example.com");
        domainDialog.setHeaderText("Enter Domain Name");
        String domainName = domainDialog.showAndWait().orElse(null);
        if (domainName == null) return null;

        TextInputDialog keySizeDialog = new TextInputDialog("2048");
        keySizeDialog.setHeaderText("Enter RSA Key Size");
        String rsaKeySizeStr = keySizeDialog.showAndWait().orElse(null);
        if (rsaKeySizeStr == null) return null;
        int rsaKeySize = Integer.parseInt(rsaKeySizeStr);

        return new Commands().SSHConfig(username, password, domainName, rsaKeySize);
    }
}
