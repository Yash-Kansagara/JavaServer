package Game;

public class CardBox {

	public boolean isFilled;
	public boolean isLocked;
	public int owner;
	public int team;
	public boolean isJack;
	
	public int x;
	public int y;
	
	public String name;
	
	public Board board;
	public int id;
	
	public CardBox left;
	public CardBox right;
	public CardBox up;
	public CardBox down;
	
	public CardBox leftup;
	public CardBox leftdown;
	public CardBox rightup;
	public CardBox rightdown;
}