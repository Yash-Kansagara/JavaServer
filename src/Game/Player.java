package Game;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Player {

    public int           team;
    public String        name;
    public List<Integer> cards;
    public Socket        tcp;
    public Thread        receivingThread;
    public Game          game;
    public InetAddress   address;
    public int udpPort;
    public int tcpPort;

    // add tcp connection
    // some huge changes done with this comment

    public Player(String name, Socket socket) {
        this.name = name;
        this.tcp = socket;
    }

    public Player(String name) {
        this.name = name;

    }

    public boolean SendReliable(String message) throws Exception {
        tcp.getOutputStream().write(message.getBytes());
        tcp.getOutputStream().flush();
        return true;
    }

    public boolean SendUnreliable() {
        // implement unreliable udp send
        return false;
    }

    public void StartTCPReceiver() {
        receivingThread = new Thread(new Runnable() {

            public void run() {
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(tcp.getInputStream()));
                    while (true) {
                        String msg = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        receivingThread.start();
    }
}
