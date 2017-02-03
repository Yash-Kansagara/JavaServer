package Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

import Game.Debug;
import Game.GameServerOperationCode;
import Game.ParameterCodes;
import Util.Container;

public class Tester {
    
    public void JoinPlayerTest(){
        byte[] data = "yash\n".getBytes();
//        
//        DatagramPacket dp = new DatagramPacket(data,data.length);
//        dp.setSocketAddress(new InetSocketAddress("127.0.0.1", 4546));
//        DatagramSocket ds;
//        try {
//            ds = new DatagramSocket();
//            ds.send(dp);
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        Socket s;
        try {
            
            s = new Socket("localhost",4545);
            s.getOutputStream().write(data);
            s.getOutputStream().flush();
            
            InputStream is = s.getInputStream();
            byte[] reply = new byte[1024];
            s.getInputStream().read(reply);
            Debug.Log(new String(reply));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void CreateGame(){
    	try {
			
    		Container data = new Container();
			data.put(ParameterCodes.operationCode, GameServerOperationCode.CREATE_GAME);
			data.put(ParameterCodes.gameName, "Game1");
			data.put(ParameterCodes.name, "yash");
			data.put(ParameterCodes.tcpPort, 5545);
			data.put(ParameterCodes.udpPort, 5546);
			data.put(ParameterCodes.address, InetAddress.getByName("localhost").getAddress());
			
			DatagramSocket dgs = new DatagramSocket();
			
			SendUDP(data, dgs, InetAddress.getLocalHost(), Config.Config.HOMESERVER_UDP_PORT);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void SendUDP(Container data, DatagramSocket dgs, InetAddress address, int udp_port) {
		
		try {
			byte[] bytes = data.getBytes();
			dgs.send(new DatagramPacket(bytes, bytes.length, address, udp_port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
