package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import Config.Config;
import Constants.Const;
import HomeServer.HomeServerOperationCode;
import Util.Container;

public class GameServer extends GameServerEventListener implements IGameServer {

    public Hashtable<String, Game>            games;
    public Hashtable<String, Player>          gameServerPlayers;
    public static Dictionary<String, Integer> cardDictionary;
    public ServerSocket                       gameServerTCP;
    public DatagramSocket                     gameServerUDP;
    InetAddress                               homeAddress;
    InetAddress                               myAddress;

    public void InitializeServer() throws Exception {

        games = new Hashtable<String, Game>();
        gameServerPlayers = new Hashtable<String, Player>();

        gameServerTCP = new ServerSocket(Config.GAMESERVER_TCP_PORT);
        gameServerUDP = new DatagramSocket(Config.GAMESERVER_UDP_PORT);
        homeAddress = InetAddress.getByName("127.0.0.1");
        myAddress = InetAddress.getByName("127.0.0.1");
        eventListeners = new ArrayList<>();
        AddGameServerEventListener(this);

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
    public boolean createGame(Hashtable<Byte, Object> param, Byte operation) throws Exception {

        String name = (String)param.get(ParameterCodes.gameName);
        
       
        boolean success = true;
        if (games.contains(name)){
            success = false;
        }
        
        Game newGame = new Game(name);
        games.put(name, newGame);

        String playerName = (String)param.get(ParameterCodes.name);
        Player p = new Player(playerName);
        p.game = newGame;
        p.address = InetAddress.getByAddress((byte[])param.get(ParameterCodes.address));
        
        p.tcpPort = (int)param.get(ParameterCodes.tcpPort);
        p.udpPort = (int)param.get(ParameterCodes.udpPort);
        
        newGame.players.add(p);

        Container c = new Container();
        c.put(ParameterCodes.operationCode, HomeServerOperationCode.ACK);
        c.put(ParameterCodes.operationCodeACK, operation);
        c.put(ParameterCodes.result, success ? Const.RESULT_OK : Const.RESULT_FAIL);
        c.put(ParameterCodes.name, "GameServer1");
        c.put(ParameterCodes.gameName, name);
        byte[] data = c.getBytes();

        
        DatagramPacket dp = new DatagramPacket(data, data.length, homeAddress, Config.HOMESERVER_UDP_PORT);
        gameServerUDP.send(dp);
        Debug.Log("GameServer: ACK CREATE GAME -> HOMESERVER "+data.length + " bytes");

        c.put(ParameterCodes.address, myAddress.getAddress());
        c.put(ParameterCodes.udpPort, Config.GAMESERVER_UDP_PORT);
        c.put(ParameterCodes.tcpPort, Config.GAMESERVER_TCP_PORT);
        
        
        data = c.getBytes();

        dp = new DatagramPacket(data, data.length, p.address, p.udpPort);
        gameServerUDP.send(dp);
Debug.Log("GameServer: ACK CREATE GAME -> PLAYER");

        return success;
    }


    public boolean JoinGame(Hashtable<Byte, Object> param){
         String name = (String)param.get(ParameterCodes.gameName);
        if (!games.contains(name)){
            return false;
        }
        
        //TODO join and respond
        
        return true;
    }


    @Override
    public boolean removeGame(String gameName) {

        if (games.containsKey(gameName)) {
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
        if (gameServerPlayers.containsKey(name)) {
            Player p = gameServerPlayers.get(name);
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
            gameServerPlayers.put(name, p);
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

        Debug.Log("TCP Server started...");
        while (true) {
            try {
                Socket s = gameServerTCP.accept();

                Debug.Log("Incoming connection");

                BufferedReader sr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String playerName = sr.readLine();
                addPlayerToLobby(playerName, s);
                Thread.sleep(100);
            } catch (Exception e) {
                //System.out.println(e);
                e.printStackTrace();
            }
        }
    }


    public void UDPServer() {
        // TODO Auto-generated method stub
        Debug.Log("UDP Server started...");
        while (true) {
            try {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, 1024);
                gameServerUDP.receive(packet);
                //data = packet.getData();
                Container requestContainer = Container.getFromBytes(data, packet.getLength());
                Hashtable<Byte, Object> request = requestContainer.table;

                byte operation = (byte)request.get(ParameterCodes.operationCode);
                HandleOperation(operation, request);

                Event e = new Event();
                e.code = operation;
                e.container = requestContainer;
                RaiseEvent(e);

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void HandleOperation(byte operation, Hashtable<Byte, Object> param) {

        switch (operation) {
            case GameServerOperationCode.CREATE_GAME:
                try {
              
                    createGame(param, operation);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GameServerOperationCode.JOIN_GAME:
                try {
                    //TODO join game

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                
                case GameServerOperationCode.GET_PLAYERS:
                 try {
                    SendPlayers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    public void RegisterOnHomeServer() {
        try {

            Container request = new Container();
            byte[] address = InetAddress.getByName("127.0.0.1").getAddress();
            request.put(ParameterCodes.operationCode, HomeServerOperationCode.REGISTER_GAMESERVER);
            request.put(ParameterCodes.address, address);
            request.put(ParameterCodes.udpPort, Config.GAMESERVER_UDP_PORT);
            request.put(ParameterCodes.tcpPort, Config.GAMESERVER_TCP_PORT);
            request.put(ParameterCodes.name, "GameServer1");
            byte[] data = request.getBytes();
            Debug.Log("Registering server takes " + data.length + " bytes...");
            DatagramPacket dp = new DatagramPacket(data, data.length, homeAddress, Config.HOMESERVER_UDP_PORT);
            gameServerUDP.send(dp);

        } catch (Exception e) {
            Debug.Log("Error registering on server...");
            e.printStackTrace();
        }
    }

    public ArrayList<GameServerEventListener> eventListeners;
    
    public void SendPlayers(Hashtable<byte, Object> req)
    {
        Container c = new Container();
        c.put(ParameterCodes.operationCode, GameServerOperationCode.ACK);
        c.put(ParameterCodes.operationCodeACK, GameServerOperationCode.GET_PLAYERS);
        Game game = games.get((String)req.get(ParameterCodes.gameName));
        int players = game.players.size();
        c.put(ParameterCodes.count, players);
        for(int i = 0; i < players ; i++){
            Player p = game.players.get(i);
            //TODO send LIsts with containers
        }
    }

    public void AddGameServerEventListener(GameServerEventListener listener) {
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    public void RaiseEvent(Event e) {
        int size = eventListeners.size();
        for (int i = 0; i < size; i++) {
            eventListeners.get(i).messageReceivedUnreliable(e);
        }
    }


    //-------------------------------------- EVENT ZONE ------------------------------------------------------------------//


    @Override
    public void RegisteredOnHomeServer(Event e) {
        // TODO Auto-generated method stub
        Debug.Log("Registered on homeServer Event");
    }
}
