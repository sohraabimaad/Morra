import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MorraTest {


	//Server constructor test
	@Test
	void serverConstructor()
	{
		Server s1 = new Server(null, 0);
		assertEquals("Server",s1.getClass().getName(),"Server constructor failed");
	}

	//testing the constructor of the player class
	@Test
	void playerConstructor()
	{
		player p1 = new player();
		assertEquals("player",p1.getClass().getName(),"Player constructor fails");
	}

	//Testing constructor for logic class
	@Test
	void logicConstructor()
	{
		Logic log = new Logic();
		assertEquals("Logic",log.getClass().getName(),"Logic constructor fails");
	}

	
	//Testing out some logic scenarios, if both are correct
	@Test
	void logicTestBothCorrect()
	{
		int evalResult;
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(2);
		log.p2.setGuess(2);

		evalResult = log.eval();

		assertEquals(0,evalResult,"should result in 0 in case of a tie");

	}

	//Testing out some logic scenarios, if p1 wins
	@Test
	void logicTestP1win()
	{
		int evalResult;
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(2);
		log.p2.setGuess(3);

		evalResult = log.eval();

		assertEquals(1,evalResult,"should result in 1 in case of p1 win");

	}

	//Testing out some logic scenarios, if p2 wins
	@Test
	void logicTestP2win()
	{
		int evalResult;
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(3);
		log.p2.setGuess(2);

		evalResult = log.eval();

		assertEquals(2,evalResult,"should result in 2 in case of p1 win");

	}

	//Testing out some logic scenarios, no one wins
	@Test
	void logicTestNoWinner()
	{
		int evalResult;
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(5);
		log.p2.setGuess(5);

		evalResult = log.eval();

		assertEquals(0,evalResult,"should result in 0 in case of no winner");

	}

	//Testing to see if the point counter increases if both players win
	@Test
	void logicP1P2Total()
	{
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(2);
		log.p2.setGuess(2);

		log.eval();
		log.eval();
		int totalwinsp1 = log.p1.getPoints();
		int totalwinsp2 = log.p2.getPoints();

		assertEquals(totalwinsp2,totalwinsp1,"P1 and P2 should have 2 points.Not incremented properly.");

	}

	//Testing to see if the point counter increases if a player wins
	@Test
	void logicP2Total()
	{
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(5);
		log.p2.setGuess(2);

		log.eval();
		log.eval();
		int totalwinsp2 = log.p2.getPoints();

		assertEquals(2,totalwinsp2,"P2 should have 2 points. Not incremented properly.");

	}

	//Testing to see if the point counter increases if a player wins
	@Test
	void logicP1Total()
	{
		Logic log = new Logic();
		log.p1.setTurn(1);
		log.p2.setTurn(1);
		log.p1.setGuess(2);
		log.p2.setGuess(5);

		log.eval();
		log.eval();
		int totalwinsp1 = log.p1.getPoints();

		assertEquals(2,totalwinsp1,"P1 should have 2 points. Not incremented properly.");

	}


}
