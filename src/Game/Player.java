package Game;

import java.util.List;
import java.net.*;

public class Player {

	public int team;
	public String name;
	public List<Integer> cards;
    public Socket tcp;
    
    // add tcp connection
	// some huge changes done with this comment
	
	public Player(String name, Socket socket){
	    this.name = name;
	    tcp = socket;
	}
	
	public boolean SendReliable(String message) throws Exception{
	    
        tcp.getOutputStream().write(message.getBytes());
        
		return true;
	}

	public boolean SendUnreliable() {
        // implement unreliable udp send
		return false;
	}
}