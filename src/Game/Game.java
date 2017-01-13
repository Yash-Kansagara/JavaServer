package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.net.*;
import java.io.*;

public class Game implements Runnable {

    public Game() {
        try {
            InitializeGame();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Board        board;
    public boolean gameRunning;
    public ServerSocket gameConnection;
    ArrayList<Player>   players;
    public LinkedList<Event> EventQueue;

    public void InitializeGame() throws Exception {
        board = Board.GetNew();
        EventQueue = new LinkedList<Event>();
        gameConnection = new ServerSocket(4545);
        
        while(gameRunning){
            Event e = EventQueue.poll();
            if(e != null){
                e.Execute(null);
            }
            Thread.sleep(100);
        }
    }
    
    public void OnDestroyGame(){
        //TODO 
    }

    public void AddPlayer() throws Exception {
        throw new Exception("Not Implemented");
    }

    public void RemovePlayer() throws Exception {
        throw new Exception("Not Implemented");
    }

    public int ScanBoardForSequence(int teamID) {

        int sequences = 0;

        // check hor
        for (int i = 0; i < 10; i++) {
            int counter = 0;
            int i_10 = i * 10;
            for (int j = 0; j < 10; j++) {
                CardBox cb = board.cards.get(j + i_10);
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        sequences++;
                        counter = 0;
                        if (j >= 5) {
                            break;
                        }
                    }
                } else {
                    counter = 0;
                }
            }
        }

        // check vert
        for (int i = 0; i < 10; i++) {
            int counter = 0;
            for (int j = 0; j < 10; j++) {
                CardBox cb = board.cards.get(i + j * 10);
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        sequences++;
                        if (j >= 5) {
                            break;
                        }
                        counter = 0;
                    }
                } else {
                    counter = 0;
                }
            }
        }

        // ul -> rd
        int row = 0;
        int column = 0;
        int counter = 0;
        for (column = 0; column < 6; column++) {
            CardBox cb = board.cards.get(column + row * 10);
            int end = 10 - column;

            for (int i = 0; i < end; i++) {
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        counter = 0;
                        sequences++;
                        System.out.println("Sequence found at " + i);
                    }
                } else {
                    counter = 0;
                }
                cb = cb.rightdown;
            }
        }

        column = 0;
        row = 1;
        counter = 0;
        for (row = 1; row < 6; row++) {
            CardBox cb = board.cards.get(row * 10); // column being zero
            int end = 10 - row;
            for (int i = 0; i < end; i++) {
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        counter = 0;
                        sequences++;
                    }
                } else {
                    counter = 0;
                }
                cb = cb.rightdown;
            }
        }


        // ru -> ld
        column = 4;
        row = 0;
        counter = 0;
        for (column = 4; column < 10; column++) {
            CardBox cb = board.cards.get(column); // row being zero
            int end = column + 1;
            for (int i = 0; i < end; i++) {
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        counter = 0;
                        sequences++;
                    }
                } else {
                    counter = 0;
                }
                cb = cb.leftdown;
            }
        }

        column = 9;
        row = 1;
        counter = 0;
        for (row = 1; row < 6; row++) {
            CardBox cb = board.cards.get(row * 10 + column); // row being zero
            int end = 10 - row;
            for (int i = 0; i < end; i++) {
                if (cb != null && (cb.isFilled && cb.team == teamID || cb.isJack)) {
                    counter++;
                    if (counter == 5) {
                        counter = 0;
                        sequences++;
                    }
                } else {
                    counter = 0;
                }
                cb = cb.leftdown;
            }
        }


        System.out.println("Sequences found : " + sequences);
        return sequences;

    }

    public boolean isGameOver(int teamID) {
        return ScanBoardForSequence(teamID) >= 3;
    }

    public void SuggestOffensiveMove(Player p) throws Exception {
        Integer[] cards = (Integer[])p.cards.toArray();
        HashMap<CardBox, Integer> scores = new HashMap<CardBox, Integer>();
        for (int i = 0; i < cards.length; i++) {
            CardBox cb = board.idToCard.get(cards[i]);
            if (cb.isFilled) {
                scores.put(cb, 0);
                continue;
            }

            int hscore = 0;
            CardBox pointer = cb.left;
            // scan left
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    hscore++;
                pointer = pointer.left;
            }

            // scan right
            pointer = cb.right;
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    hscore++;
                pointer = pointer.right;
            }

            int vscore = 0;
            pointer = cb.up;
            // scan UP
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    vscore++;
                pointer = pointer.up;

            }

            // scan down
            pointer = cb.down;
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    vscore++;
                pointer = pointer.down;
            }

            int luscore = 0;
            pointer = cb.leftup;
            // scan lUP
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    luscore++;
                pointer = pointer.leftup;
            }

            // scan rdown
            pointer = cb.rightdown;
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    luscore++;
                pointer = pointer.rightdown;
            }

            int ruscore = 0;
            pointer = cb.rightup;
            // scan rUP
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    ruscore++;
                pointer = pointer.rightup;
            }

            // scan ldown
            pointer = cb.leftdown;
            while (pointer != null && ((pointer.isFilled && pointer.team == p.team) || !pointer.isFilled) || pointer.name.charAt(0) == 'J') {
                if (pointer.isFilled)
                    ruscore++;
                pointer = pointer.leftdown;
            }

            int finalScore = hscore + vscore + luscore + ruscore;
            scores.put(cb, finalScore);

            System.out.println("Score for " + cb.name + " = " + finalScore);
        }

        // search best out of all cards
        int max = 0;
        int topCard = -1;
        for (int i = 0; i < p.cards.size(); i++) {
            if (max < scores.get(p.cards.get(i))) {
                max = scores.get(p.cards.get(i));
                topCard = p.cards.get(i);
            }
        }
        System.out.println("Best card to play: " + board.idToCard.get(topCard).name);
    }
    
    @Override
    public void run() {
        
        while(gameRunning){
           
        }
    }

}
