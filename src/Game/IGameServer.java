package Game;

public interface IGameServer {
	
	public boolean createGame(String gameName);
	
	public boolean removeGame(String gameName);
	
	public boolean saveGame(String gameName);
	
	
}
