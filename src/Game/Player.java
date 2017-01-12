package Game;

import java.util.List;

public class Player {

	public int team;
	public String name;
	public List<Integer> cards;

    // add tcp connection
	// some huge changes done with this comment
	
	public boolean SendReliable() {

		return false;
	}

	public boolean SendUnreliable() {

		return false;
	}
}
