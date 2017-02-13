package HomeServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

import Util.Container;
import Game.Debug;
import Game.Event;
import Game.GameServerEventListener;

public class GameServerInstance{

	public String name;
	public int udp_port;
	public int tcp_port;
	public InetAddress address;

	public GameServerInstance(String name, int udpPort, int tcpPort, InetAddress address) {
		this.name = name;
		this.udp_port = udpPort;
		this.tcp_port = tcpPort;
		this.address = address;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("GameServer\n__________\nName:").append(name).append('\n').append("Address:").append(address)
				.append('\n').append("UDP port:").append(udp_port).append('\n').append("TCP port:").append(tcp_port)
				.append('\n');
		return sb.toString();
	}

	public void SendUDP(Container data, DatagramSocket dgs) {
		
		try {
			byte[] bytes = data.getBytes();
			dgs.send(new DatagramPacket(bytes, bytes.length, address, udp_port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

   
}
