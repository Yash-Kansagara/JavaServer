package Game;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import HomeServer.HomeServerOperationCode;

public class GameServer implements IGameServer {

    public List<Game>                         games;
    public Hashtable<String, Player>          lobbyPlayers;
    public static Dictionary<String, Integer> cardDictionary;
    public ServerSocket                       gameServer;
    public DatagramSocket                     gameServerUDP;


    public void InitializeServer() throws Exception {
        
        games = new ArrayList<Game>();
        lobbyPlayers = new Hashtable<String, Player>();
        
        gameServer = new ServerSocket(Config.Config.GAMESERVER_TCP_PORT);
        gameServerUDP = new DatagramSocket(Config.Config.GAMESERVER_UDP_PORT);

        
        Thread TCPthread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPServer();
            }
        });

        Thread UDPthread = new Thread(new Runnable() {
            @Override
            public void run() {
                UDPServer();
            }
        });
        
        // kick start both socket threads
        TCPthread.start();
        UDPthread.start();
        
        // register game server on homeServer
        RegisterOnHomeServer();
        
    }

    @Override
    public boolean createGame(String gameName) {
        if (games.contains(gameName))
            return false;
        return false;
    }

    @Override
    public boolean removeGame(String gameName) {

        if (games.contains(gameName)) {
            games.get(games.indexOf(gameName)).OnDestroyGame();
            games.remove(gameName);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveGame(String gameName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPlayerToLobby(String name, Socket socket) {
        if (lobbyPlayers.containsKey(name)) {
            Player p = lobbyPlayers.get(name);
            p.tcp = socket;
            System.out.println("Player Updated to Lobby : " + name);
            try {
                p.SendReliable("success");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Player p = new Player(name, socket);
            lobbyPlayers.put(name, p);
            System.out.println("Player Added to Lobby : " + name);
            try {
                p.SendReliable("success");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean removePlayerFromLobby(Socket s) {
        // TODO Auto-generated  method stub
        return false;
    }


    public void TCPServer() {
        // TODO Auto-generated method stub
        Debug.Log("TCP Server started...");
        while (true) {
            try {
                Socket s = gameServer.accept();
                Debug.Log("Incoming connection");
                //                s.setKeepAlive(true);
                BufferedReader sr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String playerName = sr.readLine();
                addPlayerToLobby(playerName, s);
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public void UDPServer() {
        // TODO Auto-generated method stub
        Debug.Log("UDP Server started...");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                gameServerUDP.receive(packet);
                byte[] data = packet.getData();
                int operation = data[0];
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void RegisterOnHomeServer() {
        try {
            InetAddress homeAddress = InetAddress.getByName("127.0.0.1");
           Hashtable<String, Object> table = new Hashtable<String, Object>();
          
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            // 1 byte operation
            byte opr = HomeServerOperationCode.REGISTER_GAMESERVER;
            //bos.write(new byte[]{opr});
            // x byte address
            byte[] address = InetAddress.getByName("127.0.0.1").getAddress();
             table.put("op", opr);
             table.put("address", address);
             table.put("uport", Config.Config.GAMESERVER_UDP_PORT);
             table.put("tport", Config.Config.GAMESERVER_TCP_PORT);
             table.put("name", "GameServer1");
            // bos.write(address);
            // rest is name
            //bos.write(new String("GameServerName").getBytes());
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(table);
            byte[] data = bos.toByteArray();
            DatagramPacket dp = new DatagramPacket(data, data.length, homeAddress, Config.Config.HOMESERVER_UDP_PORT);
            gameServerUDP.send(dp);
           
        } catch (Exception e) {
            Debug.Log("Error registering on server...");    
            e.printStackTrace();
        }
    }

}
