package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * There are many types of trainers who will battle each other. There are wild pokemon, enemy trainers, the user
 * trainer, and your rival, which all act differently. 
 * 
 * All trainers should be able to send out Pokemon, choose a move for their pokemon to use, attack the other pokemon,
 * etc. 
 * 
 * @author bakerjl1 and williagm
 *
 */
public abstract class BattleTrainer {
	
	private ArrayList<Pokemon> party;
	private Pokemon currentPokemon;
	
	private final static int VESICLE_DIAMETER = 25;
	
	public BattleTrainer() {
		
		this.party = new ArrayList<Pokemon>();
		//this.party.add(new Pokemon("Chloroplast"));
		this.currentPokemon = this.getHealthyPokemon();
		
	}
	
	public BattleTrainer(Pokemon p) {
		this.party = new ArrayList<Pokemon>();
		this.party.add(p);
		this.currentPokemon = p;
	}
	
	public abstract BattleMove chooseMove();
	public abstract Pokemon sendPokemon();	
	public abstract boolean isWild();
	
	public void addPokemon(Pokemon p) {
		
		for (int i = 0; i < this.party.size(); i++) {
			if (this.party.get(i).getName().equals("MissingNo")) {
				this.party.remove(i);
			}
		}
		this.party.add(p);
		
	}
	
	public void drawPokemon(Graphics2D g, boolean inFront) {
		int healthyPokemon = this.getNumHealthyPokemon();
		int faintedPokemon = this.party.size() - healthyPokemon;
		int notUsed = 6 - this.party.size();
		int startX;
		int startY;
		int deltaX = 30;
		if (inFront) {
			this.currentPokemon.drawYourPokemon(g);
			startX = 25;
			startY = 805;
		}
		else {
			this.currentPokemon.drawOtherPokemon(g);
			startX = 1075;
			startY = 20;
		}
		if (this.isWild()) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			if (notUsed > 0) {
				notUsed--;
				g.setColor(Color.BLACK);
				g.setStroke( new BasicStroke(3)  );
				g.drawOval(startX + i * deltaX, startY, VESICLE_DIAMETER, VESICLE_DIAMETER);
			} else if (faintedPokemon > 0) {
				faintedPokemon--;
				g.setColor(Color.GRAY);
				g.fillOval(startX + i * deltaX, startY, VESICLE_DIAMETER, VESICLE_DIAMETER);
				g.setColor(Color.BLACK);
				g.setStroke( new BasicStroke(3)  );
				g.drawOval(startX + i * deltaX, startY, VESICLE_DIAMETER, VESICLE_DIAMETER);
			} else {
				g.setColor(new Color(210, 50, 50));
				g.fillOval(startX + i * deltaX, startY, VESICLE_DIAMETER, VESICLE_DIAMETER);
				g.setColor(Color.BLACK);
				g.setStroke( new BasicStroke(3)  );
				g.drawOval(startX + i * deltaX, startY, VESICLE_DIAMETER, VESICLE_DIAMETER);
				g.setStroke( new BasicStroke(1)  );
			}
		}
		
	}
	
	public Pokemon getHealthyPokemon() {
		
		for (Pokemon p: this.party) {
			if (!p.isFainted()) {
				this.currentPokemon = p;
				return p;
			}
		}
		
		return null;
		
	}
	
	protected ArrayList<Pokemon> getParty() {
		
		return this.party;
		
	}
	
	protected Pokemon getCurrentPokemon() {
		
		return this.currentPokemon;
		
	}
	
	public boolean setCurrentPokemon(int index) {
		
		if (index < this.party.size() && index > -1) {
			this.currentPokemon = this.party.get(index);
			return true;
		}
		return false;
		
	}
	
	protected void setCurrentPokemon(Pokemon p) {
		this.currentPokemon = p;
	}
	
	public int getPokemonAmount() {
		return this.party.size();
	}
	
	public Pokemon getNotFainedPokemon(int index) {
		return this.party.get(index);
	}
	
	public int getNumHealthyPokemon() {
		int healthy = 0;
		for (Pokemon p: this.party) {
			if (!p.isFainted()) {
				healthy++;
			}
		}
		return healthy;
	}
	
	public int currentPokemonIndex() {
		for (int i = 0; i < this.party.size(); i++) {
			if (this.currentPokemon == this.party.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
}
