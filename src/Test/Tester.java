package Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Game.Debug;

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
}
