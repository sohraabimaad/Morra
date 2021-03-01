
public class Logic {
	// Need to players
	player p1;
	player p2;
	
	boolean twoPlayers;
	
	Logic()
	{
		p1 = new player();
		p2 = new player();
		
		twoPlayers = false;
	}
	
	int eval()
	{
		// Get the total of the turns
		int total = (p1.getTurn()) + (p2.getTurn());
		
		// Check if both guessed correctly
		if(p1.getGuess() == total && p2.getGuess() == total)
			return 0;
		
		// Check if player1 won
		if(p1.getGuess() == total)
		{
			// Give player 1 a point
			p1.setPoints(p1.getPoints() + 1);
			return 1;
		}
		
		// Check if player2 won
		if(p2.getGuess() == total)
		{
			// Give player 1 a point
			p2.setPoints(p2.getPoints() + 1);
			return 2;
		}
		
		else // Nobody won
			return 0;
		
	}
	
	
	
	

}
