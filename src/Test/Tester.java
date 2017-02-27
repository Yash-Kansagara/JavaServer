package Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

import Constants.Const;
import Game.Debug;
import Game.GameServerOperationCode;
import Game.ParameterCodes;
import HomeServer.HomeServerOperationCode;
import Util.Container;

public class Tester {

    DatagramSocket     udpSocket;

    public InetAddress current_gameserver_address;
    public int         current_gameserver_udp_port;
    public int         current_gameserver_tcp_port;
    public String      current_game_name;

    public void initialize() {

        try {
            udpSocket = new DatagramSocket(5546);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void CreateGame() {
        try {

            Container data = new Container();
            data.put(ParameterCodes.operationCode, HomeServerOperationCode.CREATE_GAME);
            data.put(ParameterCodes.gameName, "Game1");
            data.put(ParameterCodes.name, "yash");
            data.put(ParameterCodes.tcpPort, 5545);
            data.put(ParameterCodes.udpPort, 5546);
            data.put(ParameterCodes.address, InetAddress.getByName("localhost").getAddress());

            // DatagramSocket udpSocket = new DatagramSocket();

            SendUDP(data, udpSocket, InetAddress.getLocalHost(), Config.Config.HOMESERVER_UDP_PORT);

            //listen for reply from game server or homeserver
            Debug.Log("Tester: waiting for gameServer...");

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            udpSocket.receive(packet);

            byte[] d = packet.getData();
            Container requestContainer = Container.getFromBytes(d, packet.getLength());
            Hashtable<Byte, Object> request = requestContainer.table;


            byte operation = (byte)request.get(ParameterCodes.operationCode);

            if (operation == HomeServerOperationCode.ACK) {
                byte ackOperationCode = (byte)request.get(ParameterCodes.operationCodeACK);
                
                if(ackOperationCode == GameServerOperationCode.CREATE_GAME){
                    PlayerRoomCreatedEvent(operation, request);
                }else if(ackOperationCode == GameServerOperationCode.GET_PLAYERS){
                    
                }
                
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void PlayerRoomCreatedEvent(byte operation, Hashtable<Byte, Object> request) {

        try {


            byte result = (byte)request.get(ParameterCodes.result);
            String serverName = (String)request.get(ParameterCodes.name);

            current_gameserver_udp_port = (int)request.get(ParameterCodes.udpPort);
            current_gameserver_tcp_port = (int)request.get(ParameterCodes.tcpPort);
            current_gameserver_address = InetAddress.getByAddress((byte[])request.get(ParameterCodes.address));

            if (result == Const.RESULT_OK) {
                Debug.Log("Tester: Room created on..." + serverName);
                //TODO join the room on game server,
                joinGame((String)request.get(ParameterCodes.gameName));
                //TODO disconnect from home server if TCP,
            } else {
                PlayersReceivedEvent(request);
            }

        } catch (Exception e) {
            Debug.Log(e.getMessage());
            e.printStackTrace();
        }

    }
    
    public void PlayersReceivedEvent(Hashtable<Byte, Object> data){
        
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

    public void getRoomPlayers()
    {
        Container c = new Container();
        c.put(ParameterCodes.gameName, current_game_name);
        c.put(ParameterCodes.operationCode, GameServerOperationCode.GET_PLAYERS);
        
        SendUDP(c, udpSocket, current_gameserver_address, current_gameserver_udp_port);
    }

    public void joinGame(String name) {
        try {
            Container data = new Container();
            data.put(ParameterCodes.operationCode, GameServerOperationCode.JOIN_GAME);
            data.put(ParameterCodes.gameName, name);
            data.put(ParameterCodes.name, "yash");
            data.put(ParameterCodes.tcpPort, 5545);
            data.put(ParameterCodes.udpPort, 5546);
            data.put(ParameterCodes.address, InetAddress.getByName("localhost").getAddress());
            DatagramSocket dgs = new DatagramSocket();
            SendUDP(data, dgs, InetAddress.getByAddress(Config.Config.HOMESERVER_ADDRESS), Config.Config.HOMESERVER_UDP_PORT);

            // listen for success event from Game Server

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
