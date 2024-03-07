package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a user trainer when in battle, which acts differently
 * than a user on the map. This implements BattleTrainer, allowing it to do
 * normal BattleTrainer things.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class BattleUserTrainer extends BattleTrainer {
	
	private static final HashMap<String, Integer> TYPE_TO_BASE_YIELD_MAP = new HashMap<String, Integer>() {{ 
		put("Cytoskeleton", 150); put("Fightoskeleton", 195); put("Centrosome", 98); put("Doug Dimmadome", 200); put("Cell Wall", 160);
		put("Cell Barricade", 210); put("Big Vacuole", 147); put("Mesosome", 120); put("Mesostrong", 160); put("Lysosome", 180);
		put("Lyster", 230); put("Peroxisome", 160); put("Smooth ER", 190); put("Groove ER", 205); put("Cell Membrane", 185);
		put("Plasma Membrane", 235); put("Vacuole", 75); put("Cytoplasm", 155); put("Cytoblast'em", 215); put("Chloroplast", 197);
		put("Chloroblast", 240); put("Golgi Body", 146); put("Golgi Apparatus", 178); put("Mitochondria", 230); put("Mightychondria", 280);
		put("Pilus", 130); put("Cilia", 163); put("Flagellum", 136); put("Snek", 221); put("Capsule", 179); put("Capsoul", 208);
		put("Ribosome", 168); put("Vibeosome", 202); put("Rough ER", 180); put("Buff ER", 230); put("Plasmid", 60); put("Plassid", 164);
		put("Placid", 183); put("Plaswid", 137); put("Plastid", 122); put("Plasrid", 203); put("Plaspid", 174); put("Shuffle", 70);
		put("Missing-No", 0);
	}};

	private int numVesicles;
	private String type;
	private String[] thingsICanCatch;

	public BattleUserTrainer() {

		super();
		//this.addPokemon(new Pokemon("Mightychondria", 100));
		//this.addPokemon(new Pokemon("Golgi Body", 93));
		//this.addPokemon(new Pokemon("Groove ER", 70));
		//this.addPokemon(new Pokemon("Cytoskeleton", 50));
		this.addPokemon(new Pokemon("MissingNo"));
		this.resetVesicles();
		this.setType("Animal");

	}

	@Override
	public BattleMove chooseMove() {

		return null;

	}

	@Override
	public Pokemon sendPokemon() {

		return null;

	}

	@Override
	public void addPokemon(Pokemon p) {

		super.addPokemon(p);

		if (super.getParty().size() > 6) {
			// Now we discard a Pokemon.
		}

	}

	public String[] getPokemonNames() {

		String[] names = new String[super.getParty().size()];

		for (int i = 0; i < names.length; i++) {
			names[i] = super.getParty().get(i).getName();
		}

		return names;

	}

	public String[] getPokemonTypes() {

		String[] types = new String[super.getParty().size()];

		for (int i = 0; i < types.length; i++) {
			types[i] = super.getParty().get(i).getType();
		}

		return types;

	}

	@Override
	public boolean isWild() {
		return false;
	}

	public void maxHealAll() {
		
		for (Pokemon p: super.getParty()) {
			p.heal();
		}
		
	}
	
	public void healAll(int hp) {

		for (Pokemon p: super.getParty()) {
			if (!p.isFainted()) {
				p.heal(hp);
			}
		}

	}

	/**
	 * 
	 * @param xp
	 * @return true for each Pokemon which needs to level up.
	 */
	public boolean[] xpToAll(int xp) {

		boolean[] pokemonToLevelUp = new boolean[super.getParty().size()];
		
		for (int i = 0; i < super.getParty().size(); i++) {
			Pokemon p = super.getParty().get(i);
			pokemonToLevelUp[i] = false;
			if (!p.isFainted()) {				
				pokemonToLevelUp[i] = p.gainXP(xp);
			}
			if (p == this.getCurrentPokemon()) {
				pokemonToLevelUp[i] = pokemonToLevelUp[i] || this.getCurrentPokemon().gainXP(xp);
			}
		}

		return pokemonToLevelUp;
		
	}

	public String removePokemon(int index) {

		if (index >= 0 && index < this.getPokemonAmount()) {
			return super.getParty().remove(index).getName();
		}
		return "No!";

	}

	public boolean[] handleXPGain(String name, int level, double a) {
		double xpScale = 7.0;
		double moreXP = (a * TYPE_TO_BASE_YIELD_MAP.get(name) * level) / xpScale;
		return this.xpToAll((int) moreXP);
	}
	
	public void replenishPP() {
		
		for (Pokemon p : super.getParty()) {
			p.replenishPP();
		}
		
	}
	
	public void resetVesicles() {
		this.numVesicles = 20;
	}
	
	public boolean useVesicle() {
		if (this.numVesicles <= 0) {
			return false;
		}
		this.numVesicles--;
		return true;
	}

	public int getVesicleAmount() {
		return this.numVesicles;
	}
	
	public void setType(String type) {
		this.type = type;
		this.resetThingsICanCatch();
	}
	
	//Set this up dangit!
	public void resetThingsICanCatch() {
		if (type.equals("Animal")) {
			this.thingsICanCatch = Sector.ANIMAL_ORGANELLES;
		}
		else if (type.equals("Plant")) {
			this.thingsICanCatch = Sector.PLANT_ORGANELLES;
		}
		else if (type.equals("Bacteria")) {
			this.thingsICanCatch = Sector.BACTERIA_ORGANELLES;
		}
		else if (type.equals("Archaea")) {
			this.thingsICanCatch = Sector.ARCHAEA_ORGANELLES;
		}
		else {
			this.thingsICanCatch = Sector.FUNGI_ORGANELLES;
		}
	}
	
	public boolean canCatch(String opponentName) {
		for (String name: this.thingsICanCatch) {
			if (name.equals(opponentName)) {
				return true;
			}
		}
		return false;
	}

	public String getType() {
		return this.type;
	}
	
	public Pokemon getPokemon(int index) {
		if (index < super.getParty().size() && index > -1) {
			return super.getParty().get(index);
		}
		return null;
	}
	
	public Pokemon getNotFainedPokemon(int index) {
		if (index < super.getParty().size() && index > -1 && !super.getParty().get(index).isFainted()) {
			return super.getParty().get(index);
		}
		return null;
	}
	
	public void wipeAllMultipliers() {
		for (Pokemon p: super.getParty()) {
			p.wipeMultipliers();
		}
	}
	
}
