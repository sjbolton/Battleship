package BattleShip;

public class Cruiser extends Ship{

	
	public Cruiser(String name) {
		super(name);	}

	@Override
	public char drawShipStatusAtCell(boolean isDamaged) {
		if (isDamaged == true)
			return 'c';
		return 'C';
	}

	@Override
	public int getLength() {
		return 4;
	}


}