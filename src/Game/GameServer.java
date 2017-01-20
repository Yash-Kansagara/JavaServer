package Game;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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

import Constants.Const;
import HomeServer.HomeServerOperationCode;
import Util.Container;

public class GameServer implements IGameServer {

    public List<Game>                         games;
    public Hashtable<String, Player>          gameServerPlayers;
    public static Dictionary<String, Integer> cardDictionary;
    public ServerSocket                       gameServerTCP;
    public DatagramSocket                     gameServerUDP;


    public void InitializeServer() throws Exception {

        games = new ArrayList<Game>();
        gameServerPlayers = new Hashtable<String, Player>();

        gameServerTCP = new ServerSocket(Config.Config.GAMESERVER_TCP_PORT);
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
                Container requestContainer = Container.getFromBytes(data, packet.getLength());
                Hashtable<Byte, Object> request = requestContainer.table;
                byte operation = (byte)request.get(ParameterCodes.operationCode);
                HandleOperation(operation, request);
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public void HandleOperation(byte operation, Hashtable<Byte, Object> param) {

        switch (operation) {
            case GameServerOperationCode.CREATE_GAME:
                //TODO create game
                String name = (String)param.get(ParameterCodes.gameName);
                Game newGame = new Game(name);

                //TODO notify homeserver about it
                SendAck(operation, Const.RESULT_OK);
                break;
        }
    }

    public void SendAck(byte operation, byte result) {
        Hashtable<Byte, Object> data = new Hashtable<>();
        data.put(ParameterCodes.operationCodeACK, operation);
        data.put(ParameterCodes.result, result);
    }

    @Override
    public void RegisterOnHomeServer() {
        try {
            InetAddress homeAddress = InetAddress.getByName("127.0.0.1");
            Container request = new Container();
            byte[] address = InetAddress.getByName("127.0.0.1").getAddress();
            request.put(ParameterCodes.operationCode, HomeServerOperationCode.REGISTER_GAMESERVER);
            request.put(ParameterCodes.address, address);
            request.put(ParameterCodes.udpPort, Config.Config.GAMESERVER_UDP_PORT);
            request.put(ParameterCodes.tcpPort, Config.Config.GAMESERVER_TCP_PORT);
            request.put(ParameterCodes.name, "GameServer1");
            byte[] data = request.getBytes();
            Debug.Log("Registering server takes "+ data.length + " bytes...");
            DatagramPacket dp = new DatagramPacket(data, data.length, homeAddress, Config.Config.HOMESERVER_UDP_PORT);
            gameServerUDP.send(dp);

        } catch (Exception e) {
            Debug.Log("Error registering on server...");
            e.printStackTrace();
        }
    }

}
