package BattleShip;

import java.util.ArrayList;

/*
 * 
 *	#	Class of ship	Size
 *	1	Carrier			5
 *	1	Battleship		4
 *	1	Cruiser			3
 *	1	Submarine		3
 *	1	Destroyer		2
 * 
 */

abstract public class Ship
{
	protected ArrayList< Cell > position = null;
	protected String name = "Unnamed";
	
	public Ship( String name )
	{
		this.name = name;
	}
	
	public void setPosition( ArrayList< Cell > position )
	{
		this.position = position;
	}
	
	public abstract char drawShipStatusAtCell( boolean isDamaged );
	
	public abstract int getLength();
	
	public boolean isAlive()
	{
		for( Cell c : this.position )
			if( ! c.hasBeenStruckByMissile() )
				return true;
		return false;
	}
	
	public int getMaxDamage()
	{
		return this.getLength();
	}
	
	
	public String getName() { return this.name; }

}
