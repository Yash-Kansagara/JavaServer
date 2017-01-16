package HomeServer;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import Game.Board;
import Game.CardBox;
import Game.Debug;
import Game.Game;
import Game.GameServer;

public class HomeServer {

    public static void main(String args[]) {

        PrintIP();
        Scanner scanner = new Scanner(System.in);
        Board b = null;
        Game g = null;
        
        try{
            
        GameServer gs = new GameServer();
        gs.InitializeServer();
        
    }catch(Exception e){
        Debug.Log(e);
    }

        while (true) {

            String command = scanner.nextLine();
            System.out.println("command= " + command);
            if (command.equals("exit")) {
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

    public static void PrintIP() {
        try{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        }catch(Exception e){
            Debug.Log(e);
        }
    }
}
