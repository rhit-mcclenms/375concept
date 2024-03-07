package main;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Contrary to the name of the class, this is a trainer, which holds one Pokemon. This Pokemon can be captured in
 * battle by a vesicle, ending the battle. It implements BattleTrainer, so it can do normal BattleTrainer things.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class BattleWildPokemon extends BattleTrainer {
	
	
	
	public BattleWildPokemon(String myPokemon, int myLevel) {
		
		super(new Pokemon(myPokemon, myLevel));
		
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
	public Pokemon getHealthyPokemon() {
		
		if (!this.getCurrentPokemon().isFainted()) {
			return this.getCurrentPokemon(); 
		}
		
		return null;
		
	}

	@Override
	public void addPokemon(Pokemon p) {
		
		super.setCurrentPokemon(p);
		
	}

	@Override
	public boolean isWild() {
		return true;
	}
	
	
}
