package Game;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class GameServer implements IGameServer{
	
	public List<Game> games;
	public static Dictionary<String, Integer> cardDictionary;
	
	public void InitializeServer()
	{
		games = new ArrayList<Game>();
	}

	@Override
	public boolean createGame(String gameName) {
		if(games.contains(gameName)) return false;
		
		return false;
	}

	@Override
	public boolean removeGame(String gameName) {
		// TODO Auto-generated method stub
		if(games.contains(gameName)){
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
	
	
}
