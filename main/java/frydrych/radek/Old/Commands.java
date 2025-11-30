package frydrych.radek.Old;

public class Commands {

    // Generate DHCP commands with dynamic network and default router arguments
    public String[] Dhcp(String poolName, String network, String subnetMask, String defaultRouter, String dnsServer) {
        return new String[]{
                "ip dhcp pool " + poolName,
                "network " + network + " " + subnetMask,
                "default-router " + defaultRouter,
                "dns-server " + dnsServer,
                "lease 7"
        };
    }

    // Generate NAT commands with public and private interface arguments and dynamic IP range
    public String[] Nat(String publicInterface, String privateInterface, String accessListRange) {
        return new String[]{
                "interface " + publicInterface,
                "ip nat outside",
                "interface " + privateInterface,
                "ip nat inside",
                "ip nat inside source list 1 interface " + publicInterface + " overload",
                "access-list 1 permit " + accessListRange + " 0.0.0.255"
        };
    }

    // Generate SSH configuration commands with dynamic username, password, and domain name
    public String[] SSHConfig(String username, String password, String domainName, int rsaKeySize) {
        return new String[]{
                "username " + username + " privilege 15 secret " + password,
                "ip domain-name " + domainName,
                "crypto key generate rsa modulus " + rsaKeySize,
                "ip ssh version 2",
                "line vty 0 4",
                "login local",
                "transport input ssh",
                "exit"
        };
    }
}
