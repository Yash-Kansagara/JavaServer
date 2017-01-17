package HomeServer;

import java.net.InetAddress;

public class GameServerInstance {
    
    public String      name;
    public int         udp_port;
    public int          tcp_port;
    public InetAddress address;
    
    public GameServerInstance(String name, int udpPort, int tcpPort, InetAddress address)
    {
        this.name = name;
        this.udp_port = udpPort;
        this.tcp_port = tcpPort;
        this.address = address;
    }
}
