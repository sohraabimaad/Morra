import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

	//initializing
	private int playerNumber = 1;
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private TheServer server;
	private Consumer<Serializable> callback;
	private int port = 0;
	private Logic game;

	
	// For skipping if the game is over
	int skip = 0;

	Server(Consumer<Serializable> call, int portNum){
	    port = portNum;
		callback = call;
		server = new TheServer();
		server.start();
	}


	//Class thread
	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(port);){
				callback.accept("Server is waiting for a client!\n");
				while(true) {
					// Client has been found
					ClientThread c = new ClientThread(mysocket.accept(), playerNumber);
					
					// Add the client
					callback.accept("A client has connected to server: " + "client #" + playerNumber + "\n");
					clients.add(c);

					// 2 Players have connected
					if(clients.size() == 2)
                    {
						// Start the game
						game = new Logic();
						
						// Make the boolean true
						game.twoPlayers = true;
						

                    	
                    	// Game is starting
                        callback.accept("Both players are ready...Player 1 will go first...");
                        
                        // Notify the clients
                        clients.get(0).out.writeObject("You are Player 1...Type in a guess, then select your #");
                        clients.get(1).out.writeObject("You are Player 2...Please wait");
						clients.get(0).out.writeObject("It's your turn...");
						clients.get(1).out.writeObject("Please wait...");
						clients.get(0).start();
						clients.get(1).start();
                    }
					playerNumber = playerNumber + 1;
				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}




	
	class ClientThread extends Thread{


		Socket connection;
		int playerNumber;
		ObjectInputStream in;
		ObjectOutputStream out;


		ClientThread(Socket s, int playerNumber) throws IOException {
			System.out.println("------1");
			this.connection = s;
			this.playerNumber = playerNumber;
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());
		}

		public void run(){

			try {
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}


			while(true) {
				try {
					//reading the clients moves
					player data = (player)in.readObject();
					skip = 0;

					
					
					if(playerNumber == 1)
					{
						if(clients.get(0).playerNumber == 1 && data.getNextPlay() == 999)
						{
							// Notify the player that they are ready for the next round
							callback.accept("Player " + playerNumber + " will play again.");
							clients.get(0).out.writeObject("You will play again.");
							clients.get(1).out.writeObject("Player " + playerNumber + " will play again");
							
							// Set points to 0
							game.p1.setPoints(0);
							
							// Ready for next play
							game.p1.setNextPlay(2);
							
							
						}
						if(clients.get(0).playerNumber == 1 && data.getNextPlay() == 0)
						{
							// player has left, set next play to 0
							game.p1.setNextPlay(0);
							
							// Notify the player that they are NOT ready for the next round
							callback.accept("Player " + playerNumber + " has left the game");
							clients.get(0).out.writeObject("You left the game");
							clients.get(1).out.writeObject("Player " + playerNumber + " left the game");
							clients.get(1).out.writeObject("Final: You: " + game.p2.getPoints() + "\t Player 1: " + game.p1.getPoints());
							
						}

					}
					
					if(playerNumber == 1 && game.p1.getNextPlay() == 1 && skip != -1){
						
						// Update player 1
						game.p1.setTurn(data.getTurn());
						game.p1.setGuess(data.getGuess());
						
						// Update Client
						if(playerNumber == 1)
						{
							if(clients.get(0).playerNumber == 1)
							{
								
								// Tell the clients & server the game information
								callback.accept("Player " + playerNumber + " chose " + game.p1.getTurn());
								clients.get(0).out.writeObject("You chose: " + game.p1.getTurn());
								clients.get(0).out.writeObject("You guessed: " + game.p1.getGuess());

							}
							
							// Update clients
							clients.get(0).out.writeObject("Please wait...");
							clients.get(1).out.writeObject("It's your turn...");
						}
					}
					
					
					// Turn for Player 2
					if(playerNumber == 2)
					{
						// Check if Player 2 wants to continue
						if(data.getNextPlay() == 999 && clients.get(1).playerNumber == 2)
						{
							// Notify that the player wants to continue
							callback.accept("Player " + playerNumber + " will continue");
							clients.get(1).out.writeObject("You will play again");
							clients.get(0).out.writeObject("Player " + playerNumber + " will continue");
							game.p2.setNextPlay(2);
							game.p2.setPoints(0);
						}
						if(clients.get(1).playerNumber == 2 && data.getNextPlay() == 0)
						{
							// Player does not want to continue
							callback.accept("Player " + playerNumber + " left the game");
							
							// Notify the users
							clients.get(1).out.writeObject("You left the game.");
							clients.get(0).out.writeObject("Player " + playerNumber + " left the game.");
							clients.get(1).out.writeObject("Player " + playerNumber + " left the game");
							clients.get(1).out.writeObject("Final: You: " + game.p1.getPoints() + "\t Player 2: " + game.p2.getPoints());
							
						}
					}

					// One player has left
					if(game.p1.getNextPlay() == 0 || game.p2.getNextPlay() == 0)
					{
						// Break off
						break;
					}

					// Start next round
					if(game.p1.getNextPlay() == 2 && game.p1.getNextPlay() == 2)
					{
						// Change skip
						skip = -1;
						
						clients.get(0).out.writeObject("New Game...");
						clients.get(1).out.writeObject("New Game...");
						callback.accept("New Game...");
						
						
						// Update next play
						game.p1.setNextPlay(1);
						game.p2.setNextPlay(1);
					}
					
					
					if(playerNumber == 2 && skip != -1)
					{
						
						// Update player 2 Information
						game.p2.setTurn(data.getTurn());
						game.p2.setGuess(data.getGuess());
						
						// Check if player number was edited
						if(playerNumber == 2)
						{
							// Update the Server and Clients
							callback.accept("Player " + playerNumber + " chose " + game.p2.getTurn());
							clients.get(1).out.writeObject("You chose: " + game.p2.getTurn());
							clients.get(1).out.writeObject("You guessed: " + game.p2.getGuess());
							clients.get(0).out.writeObject("Player " + playerNumber + " chose " + game.p1.getTurn());
							clients.get(1).out.writeObject("Player 1 chose: " + game.p1.getTurn());


						}

						// Evaluate
						
						// They Tied
						if(game.eval() == 0)
						{
							// Update the Server & Clients
							callback.accept("Players Tied...");
							clients.get(0).out.writeObject("You Tied...");
							clients.get(1).out.writeObject("You Tied...");
						}
						
						// Player 1 Won
						if(game.eval() == 1)
						{
							// Increment Points
							game.p1.setPoints(game.p1.getPoints() + 1);
							
							// Update the Server & Clients
							callback.accept("Winner: Player 1...");
							clients.get(0).out.writeObject("Winner: You...");
							clients.get(1).out.writeObject("Winner: Player 1...");
						}
						
						// Player 2 won
						if(game.eval() == 2)
						{
							// Increment Points
							game.p2.setPoints(game.p2.getPoints() + 1);
							
							// Update the Server & Clients
							callback.accept("Winner: Player 2...");
							clients.get(0).out.writeObject("Winner: Player 2...");
							clients.get(1).out.writeObject("Winner: You...");
						}



						// Update the Server & Clients
						callback.accept("Score: Player 1: " + game.p1.getPoints() + "\t Player 2: " + game.p2.getPoints());
						clients.get(1).out.writeObject("Score: You: " + game.p2.getPoints() + "\t Player 1: " + game.p1.getPoints());
						clients.get(0).out.writeObject("Score: You: " + game.p1.getPoints() + "\t Player 2: " + game.p2.getPoints());
						clients.get(1).out.writeObject("Please wait...");
						clients.get(0).out.writeObject("It's your turn...");

						// Someone has hit 2 Points
						
						// Player 1 won
						if(game.p1.getPoints() == 2)
						{
							// Update the Server & the Client
							callback.accept("Winner of the game: Player 1...");
							clients.get(0).out.writeObject("Winner of the game: You...");
							clients.get(0).out.writeObject("Play again? Hit the button...");
							clients.get(1).out.writeObject("Winner of the game: Player 1...");
							clients.get(1).out.writeObject("Play again? Hit the button...");
						}

						// Player 2 won
						if(game.p2.getPoints() == 2)
						{
							// Update the Server & the Client
							callback.accept("Player 2 has won the game.");
							clients.get(1).out.writeObject("Winner of the game: You...");
							clients.get(0).out.writeObject("Winner of the game: Player 2...");
							clients.get(1).out.writeObject("Play again? Hit the button...");
							clients.get(0).out.writeObject("Play again? Hit the button...");
						}
					}
				}
				catch(Exception e) {
					callback.accept("OOOOPPs...Something wrong with the socket from client: " + playerNumber + "....closing down!");
					break;
				}
			}
		}//end of run
	}//end of client thread


}


	
	

	
// server at 5 10