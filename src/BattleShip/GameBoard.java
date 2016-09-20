package BattleShip;
import java.util.ArrayList;

public class GameBoard
{
	int rowCount = 10;
	int colCount = 10;
	
	final String LINE_END = System.getProperty("line.separator"); 
	
	ArrayList< ArrayList< Cell > > cells;
	ArrayList< Ship > myShips = new ArrayList<Ship>();
	
	public GameBoard( int rowCount, int colCount )
	{
		this.rowCount = rowCount;
		this.colCount = colCount;
		
		//create the 2D array of cells
		this.cells = new ArrayList< ArrayList< Cell > >(colCount);
		
		for (int i=0; i < colCount; i++){
			cells.add(new ArrayList<Cell> (rowCount));		//add a newly instantiated array to the ith element
			for (int j=0; j < rowCount; j++){
				cells.get(i).add(new Cell());
			}
		}	
		

	}
	
	public String draw()
	{

		//draw the entire board... I'd use a StringBuilder object to improve speed
		//remember - you must draw one entire row at a time, and don't forget the
		//pretty border...
				
		StringBuilder str = new StringBuilder();
		
		//drawing top border
		str.append("+");
		for (int i=0; i < rowCount; i++){
			str.append("-");
		}
		str.append("+\n");
		
		//Drawing inside
		for (int i=0; i < colCount; i++){
			str.append("|");
			for (int j=0; j < rowCount; j++){
				str.append(cells.get(i).get(j).draw());
			}
			str.append("|\n");
		}
		
		
		//drawing bottom border
		str.append("+");
				for (int i=0; i < rowCount; i++){
			str.append("-");
		}
		str.append("+\n");
				return str.toString();
	}
	
	
	//add in a ship if it fully 1) fits on the board and 2) doesn't collide w/
	//an existing ship.
	//Returns true on successful addition; false, otherwise

	public boolean addShip( Ship s , Position sternLocation, HEADING bowDirection )
	{		
		
		int ypos = sternLocation.y;
		int xpos = sternLocation.x;
		
		ArrayList<Cell> shipPos = new ArrayList<Cell>(s.getLength());
		
		//check to see if it fits on the board
		if((bowDirection == HEADING.NORTH) && (ypos - s.getLength()+1 >= 0)){
			//if it enters the inside of the if statement, it fits on the board. Now check for collisions
			for(int i = ypos; i > ypos - s.getLength(); i--){
				if (cells.get(i).get(xpos).getShip() != null){
					System.out.println("Cannot add " + s.getName() + " to (" + xpos + "," + ypos + "). "
							+ "Overlaps with " + cells.get(i).get(xpos).getShip().getName() + ".");
					return false;
				}
			}
			//there are no collisions. Add it and return true
			for(int i = ypos; i > ypos - s.getLength(); i--){
				cells.get(i).get(xpos).setShip(s);
				shipPos.add(cells.get(i).get(xpos));
			}
			s.position = shipPos;
			myShips.add(s);
			return true;
		}
		else if ((bowDirection == HEADING.SOUTH) && (ypos + s.getLength() <= rowCount)){
			//if it enters the inside of the if statement, it fits on the board. Now check for collisions
			for(int i = ypos; i < ypos + s.getLength(); i++){
				if (cells.get(i).get(xpos).getShip() != null){
					System.out.println("Cannot add " + s.getName() + " to (" + xpos + "," + ypos + "). "
							+ "Overlaps with " + cells.get(i).get(xpos).getShip().getName() + ".");
					return false;
				}

			}
			//there are no collisions. Add it and return true
			for(int i = ypos; i < ypos + s.getLength(); i++){
				cells.get(i).get(xpos).setShip(s);
				shipPos.add(cells.get(i).get(xpos));
			}
			s.position = shipPos;
			myShips.add(s);
			return true;
		}
		else if ((bowDirection == HEADING.WEST) && (xpos - s.getLength()+1 >= 0)){
			//if it enters the inside of the if statement, it fits on the board. Now check for collisions
			for(int i = xpos; i > xpos - s.getLength(); i--){
				if (cells.get(ypos).get(i).getShip() != null){
					System.out.println("Cannot add " + s.getName() + " to (" + xpos + "," + ypos + "). "
							+ "Overlaps with " + cells.get(ypos).get(i).getShip().getName() + ".");
					return false;
				}
			}
			//there are no collisions. Add it and return true
			for(int i = xpos; i > xpos - s.getLength(); i--){
				cells.get(ypos).get(i).setShip(s);
				shipPos.add(cells.get(ypos).get(i));
			}
			s.position = shipPos;
			myShips.add(s);
			return true;
		}
		else if ((bowDirection == HEADING.EAST) && (xpos + s.getLength() <= colCount)) {
			//if it enters the inside of the if statement, it fits on the board. Now check for collisions
			for(int i = xpos; i < xpos + s.getLength(); i++){
				if (cells.get(ypos).get(i).getShip() != null){
					System.out.println("Cannot add " + s.getName() + " to (" + xpos + "," + ypos + "). "
							+ "Overlaps with " + cells.get(ypos).get(i).getShip().getName() + ".");
					return false;
				}
			}
			//there are no collisions. Add it and return true
			for(int i = xpos; i < xpos + s.getLength(); i++){
				cells.get(ypos).get(i).setShip(s);
				shipPos.add(cells.get(ypos).get(i));
			}
			s.position = shipPos;
			myShips.add(s);
			return true;
		}
		else{
			//Ends up going off the board
			System.out.println("The position of " + s.getName() + " at (" + xpos + "," + ypos + ") puts it off the end of the board. Try again.");
			return false;
		}
	}
	
	//Returns A reference to a ship, if that ship was struck by a missile.
	//The returned ship can then be used to print the name of the ship which
	//was hit to the player who hit it.
	//Ensure you handle missiles that may fly off the grid
	public Ship fireMissle( Position coordinate )
	{
		//create pointers for simplicity
		int xpos = coordinate.x;
		int ypos = coordinate.y;
		Cell hitCell;
		
		//check to see if x and y are on board
		if (xpos < 0 || ypos < 0 || xpos >= rowCount || ypos >= colCount){
			return null;
		}
		
		hitCell = cells.get(ypos).get(xpos);
		hitCell.hasBeenStruckByMissile(true);
		return cells.get(ypos).get(xpos).getShip();
	}
	
	//Here's a simple driver that should work without touching any of the code below this point
	public static void main( String [] args )
	{
		System.out.println( "Hello World" );
		GameBoard b = new GameBoard( 10, 10 );	
		System.out.println( b.draw() );
		
		Ship s = new Cruiser( "Cruiser" );
		if( b.addShip(s, new Position(3,6), HEADING.WEST) )
			System.out.println( "Added " + s.getName() + " Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		//System.out.println( b.draw() );
		
		s = new Destroyer( "Vader" );
		if( b.addShip(s, new Position(3,5), HEADING.NORTH ) )
			System.out.println( "Added " + s.getName() + " Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		//System.out.println( b.draw() );
		
		b.fireMissle( new Position(3,5) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,4) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,3));
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(0,6));
		System.out.println( b.draw() );
		b.fireMissle( new Position(1,6));
		System.out.println( b.draw() );
		b.fireMissle( new Position(2,6));
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,6));
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(6,6));
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(10,15));
		System.out.println( b.draw() );
	}

}
