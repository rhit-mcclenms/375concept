package main;

/**
 * This class represents a move which can be used by one Pokemon against another Pokemon. It can be used and is
 * used differently if a move is an attack move or a non-attack move.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class BattleMove {
	

	private String type;
	private String name;
	private String description;
	private int pp;
	private int power;
	private int accuracy;
	
	public BattleMove(int pp, int power, int accuracy, String name, String description, String type) {
		
		this.type = type;
		this.name = name;
		this.description = description;
		this.pp = pp;
		this.power = power;
		this.accuracy = accuracy;
		
	}
	
	public String getType() {
		
		return this.type;
		
	}
	
	public int getPower() {
		
		return power;
		
	}
	
	public boolean failed() {
		
		double random = Math.random() * 100;
		if (random > accuracy) {
			return true;
		}		
		return false;
		
		
	}
	
	
	public String getName() {
		
		return this.name;
		
	}
	
}
