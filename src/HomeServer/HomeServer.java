package HomeServer;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

import Game.Board;
import Game.CardBox;
import Game.Debug;
import Game.Game;
import Game.GameServer;
import Game.Player;
import Test.Tester;

public class HomeServer {

    public DatagramSocket                        udpSocket;
    public Hashtable<String, Player>             lobbyPlayers;
    public Thread                                UdpThread;
    public Hashtable<String, GameServerInstance> gameServers;

    private void Initialize() {

        try {
            udpSocket = new DatagramSocket(Config.Config.HOMESERVER_UDP_PORT);
            lobbyPlayers = new Hashtable<String, Player>();
            gameServers = new Hashtable<String, GameServerInstance>();

            UdpThread = new Thread(new Runnable() {
                public void run() {
                    UDPServer();
                }
            });


            UdpThread.start();

        } catch (Exception e) {
            Debug.Log("Error Starting Home Server...");
            e.printStackTrace();
            udpSocket.close();
        }

    }

    public static void PrintIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String
            System.out.println(ip);
        } catch (Exception e) {
            Debug.Log(e);
        }
    }


    public boolean addPlayerToLobby(String name, Socket socket) {
        if (lobbyPlayers.containsKey(name)) {
            Player p = lobbyPlayers.get(name);
            p.tcp = socket;
            System.out.println("Player Updated to Home Lobby : " + name);
            try {
                p.SendReliable("success");
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            Player p = new Player(name, socket);
            lobbyPlayers.put(name, p);
            System.out.println("Player Added to Home Lobby : " + name);
            try {
                p.SendReliable("success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public void removePlayerFromLobby() {
        //TODO
    }

    public String getGameServerToConnect() {
        String address = null;
        return address;
    }

    // listenes UDP radio channel, bajate raho
    public void UDPServer() {
        // TODO Auto-generated method stub
        Debug.Log("UDP HomeServer started...");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                udpSocket.receive(packet);
                byte[] data = packet.getData();

                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Hashtable<String, Object> table = (Hashtable<String, Object>)ois.readObject();

                byte operation = (byte)table.get("op");

                HandleOperation(operation, table);

                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void HandleOperation(byte operation, Hashtable<String, Object> table) {
        switch (operation) {
            case HomeServerOperationCode.REGISTER_GAMESERVER:
                RegisterGameServer(table);
                break;
        }
    }


    public void RegisterGameServer(Hashtable<String, Object> table) {
        try {

            byte[] adderss = (byte[])table.get("address");
            int uport = (int)table.get("uport");
            int tport = (int)table.get("tport");
            String name = (String)table.get("name");
            InetAddress gameServerAddress = InetAddress.getByAddress(adderss);

            if (gameServers.containsKey(name)) {
                GameServerInstance gameServer = new GameServerInstance(name, uport, tport, gameServerAddress);
                gameServers.put(name, gameServer);
                Debug.Log("Updated Game Server : " + name);
            } else {
                GameServerInstance gameServer = new GameServerInstance(name, uport, tport, gameServerAddress);
                gameServers.put(name, gameServer);
                Debug.Log("Registered Game Server : " + name);
            }
            
        } catch (IOException e) {
            Debug.Log("Error registering game server...");
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------------

    public static void main(String args[]) {

        HomeServer hs = new HomeServer();
        hs.Initialize();

        Scanner scanner = new Scanner(System.in);
        Board b = null;
        Game g = null;

        try {

             Thread.sleep(2000);

            GameServer gs = new GameServer();
            gs.InitializeServer();
            Thread.sleep(2000);

            new Tester().JoinPlayerTest();
        } catch (Exception e) {
            Debug.Log(e);
        }

        while (true) {

            String command = scanner.nextLine();
            System.out.println("command= " + command);
            if (command.equals("exit")) {
                scanner.close();

                break;
            } else if (command.equals("test")) {
                g = new Game();
                b = g.board;
                //				int s = 0;
                //				int c = 0;
                //				int h = 0;
                //				int d = 0;
                //				
                //				System.out.println("total cards = "+b.cards.size());
                //				for (Iterator<CardBox> iterator = b.cards.iterator(); iterator.hasNext();) {
                //					CardBox cb = iterator.next();
                //					switch(cb.name.charAt(0)){
                //					case 'S':
                //						s++;
                //						break;
                //					case 'H':
                //						h++;
                //						break;
                //					case 'C':
                //						c++;
                //						break;
                //					case 'D':
                //						d++;
                //						break;
                //					}
                //					
                //				}
                //				System.out.println("Spades = "+s);
                //				System.out.println("Diamonds = "+d);
                //				System.out.println("Hearts = "+h);
                //				System.out.println("Clubs = "+c);

            } else if (command.startsWith("print")) {
                b.printBoard();
            } else if (command.startsWith("seq")) {
                String[] chunks = command.split(" ");
                int x1 = Integer.parseInt(chunks[1]);
                int y1 = Integer.parseInt(chunks[2]);
                int x2 = Integer.parseInt(chunks[3]);
                int y2 = Integer.parseInt(chunks[4]);
                int teamID = Integer.parseInt(chunks[5]);
                b.setTokenToTeamRange(x1, y1, x2, y2, teamID);

            } else if (command.startsWith("scan")) {
                String[] chunks = command.split(" ");
                int team = Integer.parseInt(chunks[1]);
                g.ScanBoardForSequence(team);
            } else if (command.startsWith("card")) {
                b = Board.GetNew();
                String[] chunks = command.split(" ");
                int x = Integer.parseInt(chunks[1]);
                int y = Integer.parseInt(chunks[2]);
                CardBox cb = b.cards.get(y * 10 + x);
                System.out.println("Card = " + cb.name);
                if (cb.left != null)
                    System.out.println("	Left = " + cb.left.name);
                if (cb.right != null)
                    System.out.println("	Right = " + cb.right.name);
                if (cb.up != null)
                    System.out.println("	Up = " + cb.up.name);
                if (cb.down != null)
                    System.out.println("	Down = " + cb.down.name);
                if (cb.leftup != null)
                    System.out.println("	LeftUp = " + cb.leftup.name);
                if (cb.leftdown != null)
                    System.out.println("	LeftDown = " + cb.leftdown.name);
                if (cb.rightup != null)
                    System.out.println("	RightUp = " + cb.rightup.name);
                if (cb.rightdown != null)
                    System.out.println("	RightDown = " + cb.rightdown.name);
            } else {
                System.out.println("no command found = " + command);
            }
        }
    }

}
