package Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GameServer implements IGameServer {

    public List<Game>                         games;
    public Hashtable<String, Player>                      lobbyPlayers;
    public static Dictionary<String, Integer> cardDictionary;
    public ServerSocket                       gameServer;
    public DatagramSocket gameServerUDP;
    
    
    public void InitializeServer() throws Exception {
        games = new ArrayList<Game>();
        lobbyPlayers = new Hashtable<String, Player>();
        gameServer = new ServerSocket(4545);
        gameServerUDP = new DatagramSocket(4546);
        
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
        if(lobbyPlayers.containsKey(name)){
            Player p = lobbyPlayers.get(name);
            p.tcp = socket;
            System.out.println("Player Updated to Lobby : "+name);
            try {
                p.SendReliable("success");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Player p = new Player(name, socket);
            lobbyPlayers.put(name, p);
            System.out.println("Player Added to Lobby : "+name);
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
        while (true) {
            try {
                Socket s = gameServer.accept();
                Debug.Log("Incoming connection");
                s.setKeepAlive(true);
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
        while (true) {
            try {
                Socket s = gameServer.accept();
                s.setKeepAlive(true);
                BufferedReader sr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String playerName = sr.readLine();
                addPlayerToLobby(playerName, s);
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
