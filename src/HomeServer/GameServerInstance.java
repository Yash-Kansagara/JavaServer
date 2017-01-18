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
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        sb.append("GameServer\n__________\nName:").append(name).append('\n').append("Address:").append(address).append('\n').append("UDP port:").append(udp_port).append('\n').append("TCP port:").append(tcp_port).append('\n');
        return sb.toString();
    }
}
