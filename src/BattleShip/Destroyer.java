package BattleShip;

public class Destroyer extends Ship{

	
	public Destroyer(String name) {
		super(name);
	}


	@Override
	public char drawShipStatusAtCell(boolean isDamaged) {
		if (isDamaged == true)
			return 'd';
		return 'D';
	}

	@Override
	public int getLength() {
		return 3;
	}
}
