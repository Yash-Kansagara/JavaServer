package Game;

import java.net.Socket;
import java.util.Hashtable;

public interface IGameServer {
	
	public boolean createGame(Hashtable<Byte, Object> gameName, Byte operation) throws Exception;
	
	public boolean removeGame(String gameName);
	
	public boolean saveGame(String gameName);
	
	public boolean addPlayerToLobby(String name, Socket s);
	
	public boolean removePlayerFromLobby(Socket s);
	
	public void RegisterOnHomeServer();
}
