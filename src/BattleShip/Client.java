package BattleShip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Client
{
	final String NEWL = System.getProperty("line.separator");
	
	private String name = "Player";
	PrintWriter out = null;
	BufferedReader in = null;
	GameManager man = null;
	GameBoard board = new GameBoard(10,10);
	GameBoard targets = new GameBoard(10,10);
	
	Client( BufferedReader in, PrintWriter out, GameManager manager )
	{
		this.in = in;
		this.out = out;
		this.man = manager;
	}
	
	public void playGame() throws IOException
	{
		this.out.println( NEWL + NEWL + "   Missiles Away! Game has begun" );
		this.out.println( "   To Launch a missle at your enemy:" );
		this.out.println( "F 2 4" );
		this.out.println( "Fires a missile at coordinate x=2, y=4." );
		
		while(true) // put Code Here to process in game commands, after each command, print the target board and game board w/ updated state 
		{
			processCommand();			
			
			out.println( "------------------------" );
			out.println( "Target Board:");
			out.println(this.targets.draw());
			out.println( "Your Ships:" );
			out.println(this.board.draw());
			out.println( "   Waiting for Next Command...\n\n" );
			out.flush();
			
			//Perform test here to see if we have run or lost
			
			if(allMyShipsAreDestroyed()){
				out.println("You have lost!");
				man.getOpponent(this).out.println("You have won!");
				return;
			}
		}
	}
	
	//Returns a bool, true iff all of this client's ships are destroyed
	boolean allMyShipsAreDestroyed()
	{
		for (Ship s : board.myShips){
			if (s.isAlive() == true)
				return false;
		}
		return true;
	}

	//Returns a bool, true iff all of the opponent's ships are destroyed
	boolean allEnemyShipsAreDestroyed()
	{
		for (Ship s : targets.myShips){
			if (s.isAlive() == true)
				return false;
		}
		return true;
	}

	//"F 2 4" = Fire command
	//"C Hello world, i am a chat message"
	//"D" - Redraw the latest game and target boards
	boolean processCommand() throws IOException
	{
		String strIn = in.readLine();
		String[] strParse = strIn.split(" ");
		
		switch (strParse[0]){
		case "F":
			return processFireCmd(strParse);
		case "C":
			return processChatCmd(strIn);
		case "D":
			out.println( "------------------------" );
			out.println( "Target Board:");
			out.println(this.targets.draw());
			out.println( "Your Ships:" );
			out.println(this.board.draw());
			out.flush();
			return true;
		default:
			return false;
		}
		
		
	}
	
	//When a fire command is typed, this method parses the coordinates and launches a missile at the enemy
	boolean processFireCmd( String [] s )
	{
		int x;
		int y;
		Ship tempShip;
		
		try{
			x = Integer.parseInt(s[1]);
			y = Integer.parseInt(s[2]);
		}catch (NumberFormatException e){
			return false;
		}
		
		Position pos = new Position(x, y);
		
		out.println("Firing missile at " + pos.x + "," + pos.y + "!");
		out.flush();
		
		targets.fireMissle(pos);
		
		tempShip = man.getOpponent(this).board.fireMissle(pos);
		if (tempShip != null){
			out.println(tempShip.getName() + " was struck!");
			out.flush();
		}
	
		
		return true;
	}
	
	//Send a message to the opponent
	boolean processChatCmd( String s )
	{
		man.getOpponent(this).out.println(">> " + this.getName() + ": " + s.substring(2));
		out.flush();
		return true;
	}
	
	GameBoard getGameBoard() { return this.board; }
	
	public void initPlayer() throws IOException
	{
		//1.Get player name
		out.println("Enter your name:");
		out.flush();
		name = in.readLine();
		
		//2.Print out instructions
		
//Here's some nice instructions to show a client		
		out.println("   You will now place 2 ships. You may choose between either a Cruiser (C) " );
		out.println("   and Destroyer (D)...");
		out.println("   Enter Ship info. An example input looks like:");
		out.println("\nD 2 4 S USS MyBoat\n");
		out.println("   The above line creates a Destroyer with the stern located at x=2 (col)," );
		out.println("   y=4 (row) and the front of the ship will point to the SOUTH (valid" );
		out.println("   headings are N, E, S, and W.\n\n" );
		out.println("   the name of the ship will be \"USS MyBoat\"");
		out.println("Enter Ship 1 information:" );
		out.flush();
		
		//Get ship locations from the player for all 2 ships (or more than 2 if you're using more ships)
		boolean shipAdded = false;
		String str;
		
		while (!shipAdded){ //add ship 1
			str = in.readLine();
			shipAdded = processAddShip(str);
			if (!shipAdded){
				out.println("Invalid Entry. Try again.");
				out.flush();
			}
		}
		
		out.println("Enter Ship 2 Information:");
		out.flush();
		str = "";
		shipAdded = false;
		while (!shipAdded){ //add ship 1
			str = in.readLine();
			shipAdded = processAddShip(str);
			if (!shipAdded){
				out.println("Invalid Entry. Try again.");
				out.flush();
			}
		}
		
		//After all game state is input, draw the game board to the client
		out.println( "------------------------" );
		out.println( "Target Board:");
		out.println(this.targets.draw());
		out.println( "Your Ships:" );
		out.println(this.board.draw());
		out.flush();
		
		
		out.println( "Waiting for other player to finish their setup, then war will ensue!" );
	}
	
	private boolean processAddShip(String str) {
		String[] strParse = str.split(" ");
		Ship ship;
		String name;
		if (str.length() < 8)
				name = "";
		else
			name = str.substring(8, str.length());
		
		switch (strParse[0]){
		case "D":
			ship = new Destroyer(name);
			break;
		case "C":
			ship = new Cruiser(name);
			break;
		default:
			return false;
		}
		
		int x;
		int y;
		try{
			x = Integer.parseInt(strParse[1]);
			y = Integer.parseInt(strParse[2]);
		}catch (NumberFormatException e){
			return false;
		}
				
		Position pos = new Position(x,y);
		
		HEADING dir;
		
		switch (strParse[3]){
		case "N":
			dir = HEADING.NORTH;
			break;
		case "S":
			dir = HEADING.SOUTH;
			break;
		case "E":
			dir = HEADING.EAST;
			break;
		case "W":
			dir = HEADING.WEST;
			break;
		default:
			return false;
		}
		
		board.addShip(ship, pos, dir);
		
		return true;
	}

	String getName() { return this.name; }
	
	public static void main( String [] args )
	{
		
	}
}
