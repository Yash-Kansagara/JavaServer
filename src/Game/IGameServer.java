package Game;

import java.net.Socket;

public interface IGameServer {
	
	public boolean createGame(String gameName);
	
	public boolean removeGame(String gameName);
	
	public boolean saveGame(String gameName);
	
	public boolean addPlayerToLobby(String name, Socket s);
	
	public boolean removePlayerFromLobby(Socket s);
}
