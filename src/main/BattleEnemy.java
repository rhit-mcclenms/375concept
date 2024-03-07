package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * This represents a MapEnemy, but in battle form. This implements BattleTrainer, so it can be battled with. The
 * Enemies act very differently when on the map and in battle, so this will differentiate the two.
 * 
 * It implements BattleTrainer, allowing it to battle.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class BattleEnemy extends BattleTrainer {
	
	private int minLevel;
	private int maxLevel;
	private String[] possibleTypes;
	private Random r;
	private int pokemonCount;

	public BattleEnemy(String battleType, int minLevel, int maxLevel, int pokeCount) {
		
		super();
		
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		
		if (battleType.equals("Animal")) {
			this.possibleTypes = Sector.ANIMAL_ORGANELLES;
		}
		else if (battleType.equals("Plant")) {
			this.possibleTypes = Sector.PLANT_ORGANELLES;
		}
		else if (battleType.equals("Bacteria")) {
			this.possibleTypes = Sector.BACTERIA_ORGANELLES;
		}
		else if (battleType.equals("Archaea")) {
			this.possibleTypes = Sector.ARCHAEA_ORGANELLES;
		}
		else {
			this.possibleTypes = Sector.FUNGI_ORGANELLES;
		}
		
		this.pokemonCount = pokeCount;
		this.r = new Random();
		
		if (this.pokemonCount == -1) {			
			int random1 = (int)(this.r.nextInt(6)) + 1;
			int random2 = (int)(this.r.nextInt(6)) + 1;
			this.pokemonCount = (random1 + random2)/2;
		}
		
		for (int i = 0; i < this.pokemonCount; i++) {
			this.addPokemon();
		}
		
		this.getHealthyPokemon();
		
	}
	
	public BattleEnemy(String battleType, int minLevel, int maxLevel) {
		this(battleType, minLevel, maxLevel, -1);
	}
	
	
	@Override
	public BattleMove chooseMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pokemon sendPokemon() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addPokemon(Pokemon p) {
		
		super.addPokemon(p);
		if (super.getParty().size() > 6) {
			super.getParty().remove(0);
		}
		
	}
	
	public void addPokemon() {
		
		int myLevel = r.nextInt(this.maxLevel - this.minLevel) + this.minLevel;
		int randIndex = r.nextInt(this.possibleTypes.length - 1);
		Pokemon newGuy = new Pokemon(this.possibleTypes[randIndex], myLevel);
		super.addPokemon(newGuy);
		
	}
	
	@Override
	public boolean isWild() {
		return false;
	}
	
}
