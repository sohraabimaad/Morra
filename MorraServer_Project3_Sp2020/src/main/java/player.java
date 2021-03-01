import java.io.Serializable;
public class player implements Serializable{
	
	// Total Points
	private int points;
	
	// The Guess
	private int guess;
	
	
	private int pNum;
	
	// The play
	private int turn;
	
	private int nextPlay;
	
	player()
	{
		points = 0;
		guess = 0;
		pNum = 0;
		turn = 0;
		nextPlay = 1;
	}
	
	int getPoints()
	{
		return points;
	}
	
	int getPNum()
	{
		return pNum;
	}
	
	
	int getGuess()
	{
		return guess;
	}
	
	int getTurn()
	{
		return turn;
	}
	
	int getNextPlay()
	{
		return nextPlay;
	}
	
	void setPoints(int p)
	{
		points = p;
	}
	
	void setGuess(int g)
	{
		guess = g;
	}
	
	void setTurn(int t)
	{
		turn = t;
	}
	
	void setNextPlay(int np)
	{
		nextPlay = np;
	}
	
	void setPNum(int n)
	{
		pNum = n;
	}

}
