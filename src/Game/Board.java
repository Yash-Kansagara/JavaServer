package Game;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class Board {

	public ArrayList<CardBox> cards;
	public HashMap<Integer, CardBox> idToCard;

	public static Board GetNew() {
		Board b = new Board();
		b.cards = new ArrayList<CardBox>();
		b.idToCard = new HashMap<Integer, CardBox>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				CardBox cb = new CardBox();
				b.cards.add(cb);
				cb.id = i * 10 + j;
				b.idToCard.put(cb.id, cb);
				cb.board = b;
				cb.isFilled = false;
				cb.isLocked = false;
				cb.owner = -1;
				cb.team = -1;
				cb.x = j;
				cb.y = i;

				// connect Left
				if (j > 0) {
					cb.left = b.cards.get(i * 10 + (j - 1));
					cb.left.right = cb;
				} else {
					cb.left = null;
				}
				if (j == 9) {
					cb.right = null;
				}

				if (i != 0) {
					cb.up = b.cards.get((i - 1) * 10 + j);
					cb.up.down = cb;
				} else {
					cb.up = null;
				}
				if (i == 9) {
					cb.down = null;
				}

				if (cb.left != null) {
					cb.leftup = cb.left.up;
					if (cb.leftup != null)
						cb.leftup.rightdown = cb;
				}

				if (cb.up != null) {
					cb.rightup = cb.up.right;
					if (cb.rightup != null) {
						cb.rightup.leftdown = cb;
					}
				}
			}
		}

		int row = 0;

		int column = 0;
		// first row
		b.cards.get(row * 10 + column++).name = "J";
		b.cards.get(row * 10 + column++).name = "D6";
		b.cards.get(row * 10 + column++).name = "D7";
		b.cards.get(row * 10 + column++).name = "D8";
		b.cards.get(row * 10 + column++).name = "D9";
		b.cards.get(row * 10 + column++).name = "D10";
		b.cards.get(row * 10 + column++).name = "D12";
		b.cards.get(row * 10 + column++).name = "D13";
		b.cards.get(row * 10 + column++).name = "D1";
		b.cards.get(row * 10 + column++).name = "J";

		row++;
		column = 0;
		b.cards.get(row * 10 + column++).name = "D5";
		b.cards.get(row * 10 + column++).name = "H3";
		b.cards.get(row * 10 + column++).name = "H2";
		b.cards.get(row * 10 + column++).name = "S2";
		b.cards.get(row * 10 + column++).name = "S3";
		b.cards.get(row * 10 + column++).name = "S4";
		b.cards.get(row * 10 + column++).name = "S5";
		b.cards.get(row * 10 + column++).name = "S6";
		b.cards.get(row * 10 + column++).name = "S7";
		b.cards.get(row * 10 + column++).name = "C1";

		row++;
		column = 0;
		b.cards.get(row * 10 + column++).name = "D4";
		b.cards.get(row * 10 + column++).name = "H4";
		b.cards.get(row * 10 + column++).name = "D13";
		b.cards.get(row * 10 + column++).name = "D1";
		b.cards.get(row * 10 + column++).name = "C1";
		b.cards.get(row * 10 + column++).name = "C13";
		b.cards.get(row * 10 + column++).name = "C12";
		b.cards.get(row * 10 + column++).name = "C10";
		b.cards.get(row * 10 + column++).name = "S8";
		b.cards.get(row * 10 + column++).name = "C13";

		row++;// ROW 4
		column = 0;
		b.cards.get(row * 10 + column++).name = "D3";
		b.cards.get(row * 10 + column++).name = "H5";
		b.cards.get(row * 10 + column++).name = "D12";
		b.cards.get(row * 10 + column++).name = "H12";
		b.cards.get(row * 10 + column++).name = "H10";
		b.cards.get(row * 10 + column++).name = "H9";
		b.cards.get(row * 10 + column++).name = "H8";
		b.cards.get(row * 10 + column++).name = "C9";
		b.cards.get(row * 10 + column++).name = "S9";
		b.cards.get(row * 10 + column++).name = "C12";

		row++;// ROW 5
		column = 0;
		b.cards.get(row * 10 + column++).name = "D2";
		b.cards.get(row * 10 + column++).name = "H6";
		b.cards.get(row * 10 + column++).name = "D10";
		b.cards.get(row * 10 + column++).name = "H13";
		b.cards.get(row * 10 + column++).name = "H3";
		b.cards.get(row * 10 + column++).name = "H2";
		b.cards.get(row * 10 + column++).name = "H7";
		b.cards.get(row * 10 + column++).name = "C8";
		b.cards.get(row * 10 + column++).name = "S10";
		b.cards.get(row * 10 + column++).name = "C10";

		row++;// ROW 6
		column = 0;
		b.cards.get(row * 10 + column++).name = "S1";
		b.cards.get(row * 10 + column++).name = "H7";
		b.cards.get(row * 10 + column++).name = "D9";
		b.cards.get(row * 10 + column++).name = "H1";
		b.cards.get(row * 10 + column++).name = "H4";
		b.cards.get(row * 10 + column++).name = "H5";
		b.cards.get(row * 10 + column++).name = "H6";
		b.cards.get(row * 10 + column++).name = "C7";
		b.cards.get(row * 10 + column++).name = "S12";
		b.cards.get(row * 10 + column++).name = "C9";

		row++;// ROW 7
		column = 0;
		b.cards.get(row * 10 + column++).name = "S13";
		b.cards.get(row * 10 + column++).name = "H8";
		b.cards.get(row * 10 + column++).name = "D8";
		b.cards.get(row * 10 + column++).name = "C2";
		b.cards.get(row * 10 + column++).name = "C3";
		b.cards.get(row * 10 + column++).name = "C4";
		b.cards.get(row * 10 + column++).name = "C5";
		b.cards.get(row * 10 + column++).name = "C6";
		b.cards.get(row * 10 + column++).name = "S13";
		b.cards.get(row * 10 + column++).name = "C8";

		row++;// ROW 8
		column = 0;
		b.cards.get(row * 10 + column++).name = "S12";
		b.cards.get(row * 10 + column++).name = "H9";
		b.cards.get(row * 10 + column++).name = "D7";
		b.cards.get(row * 10 + column++).name = "D6";
		b.cards.get(row * 10 + column++).name = "D5";
		b.cards.get(row * 10 + column++).name = "D4";
		b.cards.get(row * 10 + column++).name = "D3";
		b.cards.get(row * 10 + column++).name = "D2";
		b.cards.get(row * 10 + column++).name = "S1";
		b.cards.get(row * 10 + column++).name = "C7";

		row++;// ROW 9
		column = 0;
		b.cards.get(row * 10 + column++).name = "S10";
		b.cards.get(row * 10 + column++).name = "H10";
		b.cards.get(row * 10 + column++).name = "H12";
		b.cards.get(row * 10 + column++).name = "H13";
		b.cards.get(row * 10 + column++).name = "H1";
		b.cards.get(row * 10 + column++).name = "C2";
		b.cards.get(row * 10 + column++).name = "C3";
		b.cards.get(row * 10 + column++).name = "C4";
		b.cards.get(row * 10 + column++).name = "C5";
		b.cards.get(row * 10 + column++).name = "C6";

		row++;// ROW 10
		column = 0;
		b.cards.get(row * 10 + column++).name = "J";
		b.cards.get(row * 10 + column++).name = "S9";
		b.cards.get(row * 10 + column++).name = "S8";
		b.cards.get(row * 10 + column++).name = "S7";
		b.cards.get(row * 10 + column++).name = "S6";
		b.cards.get(row * 10 + column++).name = "S5";
		b.cards.get(row * 10 + column++).name = "S4";
		b.cards.get(row * 10 + column++).name = "S3";
		b.cards.get(row * 10 + column++).name = "S2";
		b.cards.get(row * 10 + column++).name = "J";

		b.cards.get(0).isJack = true;
		b.cards.get(9).isJack = true;
		b.cards.get(90).isJack = true;
		b.cards.get(99).isJack = true;

		// initialize search cache
		b.searchCache = new Hashtable<String, Integer[]>();

		return b;
	}

	public void setTokenToTeam(int x, int y, int teamID) {
		CardBox cb = this.cards.get(x + y * 10);
		if (cb.isJack)
			return;
		cb.isFilled = true;
		cb.team = teamID;
	}

	public void setTokenToTeamRange(int x1, int y1, int x2, int y2, int teamID) {

		int yincr = y1==y2?0:1;
		int xincr = x1==x2?0:1;
		if (x1 <= x2) {
			for (int i = x1, j = y1; i <= x2 && j <= y2; i+=xincr, j+=yincr) {
				
					CardBox cb = this.cards.get(i + j * 10);
					if (cb.isJack)
						continue;
					cb.isFilled = true;
					cb.team = teamID;
				
			}
		}else{
			for (int i = x1, j = y1; i >= x2 && j <= y2; i-=xincr, j+=yincr) {
				
					CardBox cb = this.cards.get(i + j * 10);
					if (cb.isJack)
						continue;
					cb.isFilled = true;
					cb.team = teamID;
				
			}
		}
	}
	
	
	public void printBoard(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				CardBox cb = cards.get(j + i*10);
				
				System.out.print(cb.isFilled?cb.team:"*");
				System.out.print(' ');
			}
			System.out.print('\n');
		}
	}

	public Integer[] nameToIds(String name) {
		Integer[] results = new Integer[2];

		if (searchCache.get(name) != null) {
			return searchCache.get(name);
		}

		int counter = 0;
		for (int i = 0; i < this.cards.size(); i++) {
			if (this.cards.get(i).name.equals(name)) {
				results[counter++] = i;
				if (counter == 2) {
					break;
				}
			}
		}
		searchCache.put(name, results);
		return results;
	}

	public Dictionary<String, Integer[]> searchCache;
}
