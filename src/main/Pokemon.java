package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This represents a Pokemon which will be used in battle! The Pokemon has four
 * moves it can use against other Pokemon in BattleMain. The Pokemon knows when
 * it has fainted, it can take damage, etc.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Pokemon {


	private final static int YOUR_POKEMON_CENTER_X = 325;
	private final static int YOUR_POKEMON_CENTER_Y = 665;
	private final static int OTHER_POKEMON_CENTER_X = 1050;
	private final static int OTHER_POKEMON_CENTER_Y = 250;
	private final static int DISPLAY_POKEMON_CENTER_X = 400;
	private final static int DISPLAY_POKEMON_CENTER_Y = 250;
	private final static int MOVES_CAP = 4;

	// Organelle Types: Structure, Acid, Water, Heat, Resistant, Protein, Normal
	private final static String[] STRUCTURE_TYPES = { "Cytoskeleton", "Fightoskeleton", "Centrosome", "Doug Dimmadome",
			"Cell Wall", "Cell Barricade", "Big Vacuole", "Mesosome", "Mesostrong", "Plassid" };
	private final static String[] ACID_TYPES = { "Lysosome", "Lyster", "Peroxisome", "Smooth ER", "Groove ER",
			"Cell Membrane", "Plasma Membrane", "Placid" };
	private final static String[] WATER_TYPES = { "Vacuole", "Cytoplasm", "Cytoblast'em", "Chloroplast", "Chloroblast",
			"Plaswid" };
	private final static String[] HEAT_TYPES = { "Golgi Body", "Golgi Apparatus", "Mitochondria", "Mightychondria",
			"Pilus", "Cilia", "Flagellum", "Snek", "Plastid" };
	private final static String[] RESISTANT_TYPES = { "Capsule", "Capsoul", "Plasrid" };
	private final static String[] PROTEIN_TYPES = { "Ribosome", "Vibeosome", "Rough ER", "Buff ER", "Plaspid" };
	private final static String[] NORMAL_TYPES = { "Plasmid", "Shuffle" };

	// Organelle stats: {SPEED, ATTACK, DEFENSE, HP, SP. ATTACK, SP. DEFENSE}
	private final static int[] CYTOSKELETON_STATS = { 40, 90, 90, 83, 50, 70 };
	private final static int[] CENTROSOME_STATS = { 60, 87, 46, 67, 93, 50 };
	private final static int[] CELL_WALL_STATS = { 40, 74, 82, 80, 80, 91 };
	private final static int[] BIG_VACUOLE_STATS = { 50, 64, 80, 80, 77, 88 };
	private final static int[] MESOSOME_STATS = { 63, 70, 74, 70, 70, 77 };
	private final static int[] LYSOSOME_STATS = { 68, 85, 60, 75, 100, 61 };
	private final static int[] PEROXISOME_STATS = { 62, 73, 53, 60, 68, 48 };
	private final static int[] SMOOTH_ER_STATS = { 59, 70, 66, 77, 69, 71 };
	private final static int[] CELL_MEMBRANE_STATS = { 67, 71, 80, 79, 76, 83 };
	private final static int[] VACUOLE_STATS = { 62, 70, 57, 53, 68, 63 };
	private final static int[] CYTOPLASM_STATS = { 80, 86, 68, 84, 84, 73 };
	private final static int[] CHLOROPLAST_STATS = { 87, 64, 72, 80, 70, 69 };
	private final static int[] GOLGI_BODY_STATS = { 78, 70, 71, 75, 67, 66 };
	private final static int[] MITOCHONDRIA_STATS = { 105, 110, 100, 130, 115, 107 };
	private final static int[] PILUS_STATS = { 110, 50, 54, 55, 48, 53 };
	private final static int[] FLAGELLUM_STATS = { 120, 85, 78, 79, 87, 75 };
	private final static int[] CAPSULE_STATS = { 67, 63, 95, 88, 60, 100 };
	private final static int[] RIBOSOME_STATS = { 77, 85, 80, 90, 88, 73 };
	private final static int[] ROUGH_ER_STATS = { 73, 77, 80, 78, 74, 72 };
	private final static int[] PLASMID_STATS = { 25, 45, 37, 40, 32, 38 };
	private final static int[] PLASSID_STATS = { 60, 93, 82, 76, 74, 79 };
	private final static int[] PLACID_STATS = { 45, 85, 74, 80, 95, 74 };
	private final static int[] PLASWID_STATS = { 70, 84, 79, 93, 81, 86 };
	private final static int[] PLASTID_STATS = { 75, 97, 81, 78, 91, 92 };
	private final static int[] PLASRID_STATS = { 65, 89, 95, 74, 82, 83 };
	private final static int[] PLASPID_STATS = { 65, 81, 76, 77, 83, 75 };
	private final static int[] SHUFFLE_STATS = { 55, 70, 71, 67, 61, 65 };
	private static final int[] MISSING_NO_STATS = { 0, 0, 0, 0, 0, 0 };
	
	//Organelle moves
	private final static int[] CYTOSKELETON_POS_MOVES = { 1, 5, 7, 8, 309, 406, 407, 507, 604, 606, 615 };
	private final static int[] CENTROSOME_POS_MOVES = { 2, 4, 7, 9, 407, 504, 607, 611 };
	private final static int[] CELL_WALL_POS_MOVES = { 1, 3, 5, 6, 405, 505, 608, 605,   };
	private final static int[] BIG_VACUOLE_POS_MOVES = { 3, 4, 8, 201, 206, 407, 506, 602, 613,  };
	private final static int[] MESOSOME_POS_MOVES = { 2, 6, 8, 9, 405, 504, 602, 610,  };
	private final static int[] FIGHTOSKELETON_POS_MOVES = { 1, 5, 7, 9, 310, 407, 507, 603, 609, 614 };
	private final static int[] DOUG_DIMMADOME_POS_MOVES = { 2, 4, 5, 9, 202, 505, 613 };
	private final static int[] CELL_BARRICADE_POS_MOVES = { 1, 3, 6, 8, 405, 506, 604, 615 };
	private final static int[] MESOSTRONG_POS_MOVES = { 2, 4, 7, 9, 305, 407, 504, 606, 611 };
	private final static int[] PLASSID_POS_MOVES = { 1, 2, 3, 4, 7, 507, 610, 616 };
	private final static int[] PLACID_POS_MOVES = { 101, 104, 107, 108, 111, 201, 203, 504, 601, 604 };
	private final static int[] LYSOSOME_POS_MOVES = { 1, 2, 3, 4, 7, 507, 610, 616 };
	private final static int[] PEROXISOME_POS_MOVES = { 101, 102, 105, 108, 109, 110, 112, 203, 205, 308, 602, 605, 616 };
	private final static int[] SMOOTH_ER_POS_MOVES = { 102, 106, 107, 110, 111, 204, 208, 603, 609, 616 };
	private final static int[] CELL_MEMBRANE_POS_MOVES = { 2, 101, 102, 103, 105, 109, 112, 202, 206, 613 };
	private final static int[] LYSTER_POS_MOVES = { 103, 105, 108, 110, 111, 201, 203, 309, 607, 608 };
	private final static int[] GROOVE_ER_POS_MOVES = { 104, 106, 107, 109, 110, 205, 208, 610, 614 };
	private final static int[] PLASMA_MEMBRANE_POS_MOVES = { 104, 105, 108, 112, 204, 207, 611, 615 };
	private final static int[] PLASWID_POS_MOVES = { 104, 201, 202, 204, 205, 506, 607, 613, 616 };
	private final static int[] CYTOBLASTEM_POS_MOVES = { 106, 109, 202, 204, 205, 208, 403, 508, 610, 614 };
	private final static int[] CHLOROBLAST_POS_MOVES = { 101, 112, 201, 202, 205, 206, 207, 303, 305, 308, 605 };
	private final static int[] VACUOLE_POS_MOVES = { 110, 203, 204, 206, 208, 602, 605, 613 };
	private final static int[] CYTOPLASM_POS_MOVES = { 202, 203, 205, 208, 308, 604, 609, 613, 615 };
	private final static int[] CHLOROPLAST_POS_MOVES = { 105, 201, 204, 206, 207, 308, 310, 608, 611 };
	private final static int[] PLASTID_POS_MOVES = {4, 107, 301, 304, 305, 308, 601, 608};
	private final static int[] GOLGI_APPARATUS_POS_MOVES = {101, 302, 303, 307, 310, 507, 602, 607};
	private final static int[] MIGHTYCHONDRIA_POS_MOVES = {7, 303, 304, 306, 307, 309, 310, 401, 604, 609};
	private final static int[] CILIA_POS_MOVES = {101, 301, 303, 304, 307, 310, 608, 611};
	private final static int[] SNEK_POS_MOVES = {3, 6, 301, 302, 305, 308, 309, 510, 614, 616};
	private final static int[] GOLGI_BODY_POS_MOVES = {101, 303, 306, 307, 309, 507, 609, 615};
	private final static int[] MITOCHONDRIA_POS_MOVES = {303, 304, 305, 307, 310, 508, 603, 605};
	private final static int[] PILUS_POS_MOVES = {8, 302, 306, 307, 309, 606, 607, 610};
	private final static int[] FLAGELLUM_POS_MOVES = {6, 301, 303, 304, 308, 310, 610, 613};
	private final static int[] PLASRID_POS_MOVES = {201, 301, 401, 402, 404, 405, 407, 614, 615};
	private final static int[] CAPSULE_POS_MOVES = {1, 204, 303, 401, 403, 404, 407, 610, 611};
	private final static int[] CAPSOUL_POS_MOVES = {104, 204, 310, 401, 402, 403, 404, 406, 602, 605, 606};
	private final static int[] PLASPID_POS_MOVES = {2, 102, 501, 502, 504, 507, 508, 510, 601, 607};
	private final static int[] VIBEOSOME_POS_MOVES = {9, 106, 112, 207, 405, 501, 503, 506, 507, 509, 602, 608};
	private final static int[] BUFF_ER_POS_MOVES = {3, 102, 112, 407, 501, 505, 506, 508, 509, 510, 609, 616};
	private final static int[] RIBOSOME_POS_MOVES = {504, 8, 502, 606, 614, 106, 503, 505, 510};
	private final static int[] ROUGH_ER_POS_MOVES = {504, 611, 8, 112, 205, 502, 506, 507, 508, 613};
	private final static int[] PLASMID_POS_MOVES = {203, 504, 601, 603, 609, 606, 608, 615, 616};
	private final static int[] SHUFFLE_POS_MOVES = {203, 504, 601, 602, 603, 604, 607, 609, 611, 614};

	private final static int[] ARCHAEA_ADDITIONAL_MOVES = {401, 404, 405};

	private static final int[] MISSING_NO_POS_MOVES = { 612, 203, 614 };

	private String type, evolution, officialName, name;
	private int speed, attack, defense, spAttack, spDefense, currentHp, hpCap, level, xpCap, currentXp;
	private boolean shouldEvolve;
	private Color color;
	private HashMap<Integer, Integer> movesToPPMap;
	private ArrayList<Integer> possibleMoves;
	private Random r;

	// Attack, Defense, SP Attack, SP Defense, Speed
	private int[] modifiers;

	public Pokemon(String name) {

		this(name, 1);

	}

	public Pokemon(String name, int level) {
		this.officialName = name;
		this.level = 0;

		int[] myStats;
		int[] myPosMoves;
		this.possibleMoves = new ArrayList<Integer>();
		this.r = new Random();
		
		// initializing myStats
		if (this.officialName.equals("Cytoskeleton") || this.officialName.equals("Fightoskeleton")) {
			myStats = CYTOSKELETON_STATS;
			this.color = new Color(168, 168, 168);
			if (this.officialName.equals("Cytoskeleton")) {
				myPosMoves = CYTOSKELETON_POS_MOVES;
			} else {
				myPosMoves = FIGHTOSKELETON_POS_MOVES;
			}
		} else if (this.officialName.equals("Centrosome") || this.officialName.equals("Doug Dimmadome")) {
			myStats = CENTROSOME_STATS;
			this.color = new Color(199, 199, 0);
			if (this.officialName.equals("Centrosome")) {
				myPosMoves = CENTROSOME_POS_MOVES;
			} else {
				myPosMoves = DOUG_DIMMADOME_POS_MOVES;
			}
		} else if (this.officialName.equals("Cell Wall") || this.officialName.equals("Cell Barricade")
				|| this.officialName.equals("R. Cell Wall") || this.officialName.equals("R. Cell Barricade")) {
			myStats = CELL_WALL_STATS;
			this.color = new Color(0, 135, 2);
			if (this.officialName.contains("Cell Wall")) {
				myPosMoves = CELL_WALL_POS_MOVES;
			} else {
				myPosMoves = CELL_BARRICADE_POS_MOVES;
			}
		} else if (this.officialName.equals("Big Vacuole")) {
			myStats = BIG_VACUOLE_STATS;
			this.color = new Color(212, 242, 255);
			myPosMoves = BIG_VACUOLE_POS_MOVES;
		} else if (this.officialName.equals("Mesosome") || this.officialName.equals("Mesostrong")
				|| this.officialName.equals("R. Mesosome") || this.officialName.equals("R. Mesostrong")) {
			myStats = MESOSOME_STATS;
			this.color = new Color(134, 145, 150);
			if (this.officialName.contains("Mesosome")) {
				myPosMoves = MESOSOME_POS_MOVES;
			} else {
				myPosMoves = MESOSTRONG_POS_MOVES;
			}
		} else if (this.officialName.equals("Lysosome") || this.officialName.equals("Lyster")) {
			myStats = LYSOSOME_STATS;
			this.color = new Color(190, 255, 77);
			if (this.officialName.equals("Lysosome")) {
				myPosMoves = LYSOSOME_POS_MOVES;
			} else {
				myPosMoves = LYSTER_POS_MOVES;
			}
		} else if (this.officialName.equals("Peroxisome")) {
			myStats = PEROXISOME_STATS;
			this.color = new Color(43, 0, 2);
			myPosMoves = PEROXISOME_POS_MOVES;
		} else if (this.officialName.equals("Smooth ER") || this.officialName.equals("Groove ER")) {
			myStats = SMOOTH_ER_STATS;
			this.color = new Color(118, 69, 255);
			if (this.officialName.equals("Smooth ER")) {
				myPosMoves = SMOOTH_ER_POS_MOVES;
			} else {
				myPosMoves = GROOVE_ER_POS_MOVES;
			}
		} else if (this.officialName.equals("Cell Membrane") || this.officialName.equals("Plasma Membrane")
				|| this.officialName.equals("R. Cell Membrane") || this.officialName.equals("R. Plasma Membrane")) {
			myStats = CELL_MEMBRANE_STATS;
			this.color = new Color(255, 221, 153);
			if (this.officialName.contains("Cell Membrane")) {
				myPosMoves = CELL_MEMBRANE_POS_MOVES;
			} else {
				myPosMoves = PLASMA_MEMBRANE_POS_MOVES;
			}
		} else if (this.officialName.equals("Vacuole")) {
			myStats = VACUOLE_STATS;
			this.color = new Color(209, 255, 253);
			myPosMoves = VACUOLE_POS_MOVES;
		} else if (this.officialName.equals("Cytoplasm") || this.officialName.equals("Cytoblast'em")
				|| this.officialName.equals("R. Cytoplasm") || this.officialName.equals("R. Cytoblast'em")) {
			myStats = CYTOPLASM_STATS;
			this.color = new Color(196, 169, 88);
			if (this.officialName.contains("Cytoplasm")) {
				myPosMoves = CYTOPLASM_POS_MOVES;
			} else {
				myPosMoves = CYTOBLASTEM_POS_MOVES;
			}
		} else if (this.officialName.equals("Chloroplast") || this.officialName.equals("Chloroblast")) {
			myStats = CHLOROPLAST_STATS;
			this.color = new Color(0, 189, 22);
			if (this.officialName.equals("Chloroplast")) {
				myPosMoves = CHLOROPLAST_POS_MOVES;
			} else {
				myPosMoves = CHLOROBLAST_POS_MOVES;
			}
		} else if (this.officialName.equals("Golgi Body") || this.officialName.equals("Golgi Apparatus")) {
			myStats = GOLGI_BODY_STATS;
			this.color = new Color(235, 28, 118);
			if (this.officialName.equals("Golgi Body")) {
				myPosMoves = GOLGI_BODY_POS_MOVES;
			} else {
				myPosMoves = GOLGI_APPARATUS_POS_MOVES;
			}
		} else if (this.officialName.equals("Mitochondria") || this.officialName.equals("Mightychondria")) {
			myStats = MITOCHONDRIA_STATS;
			this.color = new Color(255, 119, 0);
			if (this.officialName.equals("Mitochondria")) {
				myPosMoves = MITOCHONDRIA_POS_MOVES;
			} else {
				myPosMoves = MIGHTYCHONDRIA_POS_MOVES;
			}
		} else if (this.officialName.equals("Pilus") || this.officialName.equals("Cilia")
				|| this.officialName.equals("R. Pilus") || this.officialName.equals("R. Cilia")) {
			myStats = PILUS_STATS;
			this.color = new Color(255, 179, 112);
			if (this.officialName.contains("Pilus")) {
				myPosMoves = PILUS_POS_MOVES;
			} else {
				myPosMoves = CILIA_POS_MOVES;
			}
		} else if (this.officialName.equals("Flagellum") || this.officialName.equals("Snek")
				|| this.officialName.equals("R. Flagellum") || this.officialName.equals("R. Snek")) {
			myStats = FLAGELLUM_STATS;
			this.color = new Color(230, 145, 0);
			if (this.officialName.contains("Flagellum")) {
				myPosMoves = FLAGELLUM_POS_MOVES;
			} else {
				myPosMoves = SNEK_POS_MOVES;
			}
		} else if (this.officialName.equals("Capsule") || this.officialName.equals("Capsoul")) {
			myStats = CAPSULE_STATS;
			this.color = new Color(61, 186, 66);
			if (this.officialName.equals("Capsule")) {
				myPosMoves = CAPSULE_POS_MOVES;
			} else {
				myPosMoves = CAPSOUL_POS_MOVES;
			}
		} else if (this.officialName.equals("Ribosome") || this.officialName.equals("Vibeosome")
				|| this.officialName.equals("R. Ribosome") || this.officialName.equals("R. Vibeosome")) {
			myStats = RIBOSOME_STATS;
			this.color = new Color(158, 0, 11);
			if (this.officialName.contains("Ribosome")) {
				myPosMoves = RIBOSOME_POS_MOVES;
			} else {
				myPosMoves = VIBEOSOME_POS_MOVES;
			}
		} else if (this.officialName.equals("Rough ER") || this.officialName.equals("Buff ER")) {
			myStats = ROUGH_ER_STATS;
			this.color = new Color(112, 0, 7);
			if (this.officialName.equals("Rough ER")) {
				myPosMoves = ROUGH_ER_POS_MOVES;
			} else {
				myPosMoves = BUFF_ER_POS_MOVES;
			}
		} else if (this.officialName.equals("Plasmid")) {
			myStats = PLASMID_STATS;
			this.color = new Color(224, 204, 206);
			myPosMoves = PLASMID_POS_MOVES;
		} else if (this.officialName.equals("Plassid")) {
			myStats = PLASSID_STATS;
			this.color = new Color(227, 182, 182);
			myPosMoves = PLASSID_POS_MOVES;
		} else if (this.officialName.equals("Placid")) {
			myStats = PLACID_STATS;
			this.color = new Color(182, 227, 187);
			myPosMoves = PLACID_POS_MOVES;
		} else if (this.officialName.equals("Plaswid")) {
			myStats = PLASWID_STATS;
			this.color = new Color(182, 182, 227);
			myPosMoves = PLASWID_POS_MOVES;
		} else if (this.officialName.equals("Plastid")) {
			myStats = PLASTID_STATS;
			this.color = new Color(227, 210, 182);
			myPosMoves = PLASTID_POS_MOVES;
		} else if (this.officialName.equals("Plasrid")) {
			myStats = PLASRID_STATS;
			this.color = new Color(210, 182, 227);
			myPosMoves = PLASRID_POS_MOVES;
		} else if (this.officialName.equals("Plaspid")) {
			myStats = PLASPID_STATS;
			this.color = new Color(191, 140, 140);
			myPosMoves = PLASPID_POS_MOVES;
		} else if (this.officialName.equals("Shuffle")) {
			myStats = SHUFFLE_STATS;
			this.color = new Color(235, 235, 235);
			myPosMoves = SHUFFLE_POS_MOVES;
		} else {
			myStats = MISSING_NO_STATS;
			this.officialName = "MissingNo";
			myPosMoves = MISSING_NO_POS_MOVES;
		}
		
		for (int move: myPosMoves) {
			this.possibleMoves.add(move);
		}

		// Setting up official name and regular name, which can be changed.
		if (this.officialName.substring(0, 2).equals("R.")) {
			this.name = this.officialName.substring(3);
			for (int move: ARCHAEA_ADDITIONAL_MOVES) {
				this.possibleMoves.add(move);
			}
		} else {
			this.name = this.officialName;
		}

		this.speed = myStats[0];
		this.attack = myStats[1];
		this.defense = myStats[2];
		this.hpCap = myStats[3];
		this.spAttack = myStats[4];
		this.spDefense = myStats[5];
		
		this.movesToPPMap = new HashMap<Integer, Integer>();
		this.tryLearnMove();
		this.tryLearnMove();
		if (this.getName() != "MissingNo") {
			this.tryLearnMove();
			this.tryLearnMove();
		}

		// Leveling up the Pokemon as it should be leveled up.
		for (int i = 0; i < level; i++) {
			this.levelUp(false);
		}

		this.heal();

		//this.addMove(305);
		//this.addMove(403);
		//this.addMove(202);
		//this.addMove(103);

		this.modifiers = new int[5];
		
		// Declaring type
		for (String kind : STRUCTURE_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Structure";
				return;
			}
		}

		for (String kind : ACID_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Acid";
				return;
			}
		}

		for (String kind : WATER_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Water";
				return;
			}
		}

		for (String kind : HEAT_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Heat";
				return;
			}
		}

		for (String kind : RESISTANT_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Resistant";
				return;
			}
		}

		for (String kind : PROTEIN_TYPES) {
			if (kind.equals(this.name)) {
				this.type = "Protein";
				return;
			}
		}

		this.type = "Normal";

	}

	public Pokemon(int[] stats, String name, int level) {
		this(name, level);

		this.speed = stats[0];
		this.attack = stats[1];
		this.defense = stats[2];
		this.hpCap = stats[3];
		this.spAttack = stats[4];
		this.spDefense = stats[5];

	}

	public boolean levelUp(boolean isControlled) {
		this.currentXp = 0;
		this.level++;
		this.attack++;
		this.defense++;
		this.speed++;
		this.hpCap++;
		this.spAttack++;
		this.spDefense++;
		this.setXpToLevelUp();

		double random;
		for (int i = 0; i < 4; i++) {
			random = Math.random();
			if (random < 1.0 / 6) {
				this.attack++;
			} else if (random < 2.0 / 6) {
				this.defense++;
			} else if (random < 3.0 / 6) {
				this.speed++;
			} else if (random < 4.0 / 6) {
				this.hpCap++;
			} else if (random < 4.0 / 6) {
				this.spAttack++;
			} else {
				this.spDefense++;
			}
		}
		if (!isControlled && this.level % 8 == 0 && this.tryLearnMove()) {
			this.forgetMove();
			this.heal();
			return false;
		}
		this.heal();

		return this.level % 8 == 0 && this.tryLearnMove();
		
	}

	public void takeDamage(int amount) {
		this.currentHp -= amount;
		if (this.currentHp <= 0) {
			this.currentHp = 0;
		}
	}

	public void drawYourPokemon(Graphics2D g) {
		g.setColor(this.color);
		g.fillOval(YOUR_POKEMON_CENTER_X - 150, YOUR_POKEMON_CENTER_Y - 160, 300, 300);
	}

	public void drawOtherPokemon(Graphics2D g) {
		g.setColor(this.color);
		g.fillOval(OTHER_POKEMON_CENTER_X - 100, OTHER_POKEMON_CENTER_Y - 100, 200, 200);
	}
	
	public void drawDisplayPokemon(Graphics2D g) {
		g.setColor(this.color);
		g.fillOval(DISPLAY_POKEMON_CENTER_X, DISPLAY_POKEMON_CENTER_Y, 500, 500);
	}

	public void heal(int amount) {
		if (this.currentHp + amount > this.hpCap) {
			this.heal();
		} else {
			this.currentHp += amount;
		}
	}

	public void heal() {
		this.currentHp = this.hpCap;
	}

	public boolean gainXP(int xpGained) {

		//Should return true when we need to discard a move.
		boolean willReturn = false;
		boolean temp;
		
		while (this.currentXp + xpGained >= this.xpCap) {
			xpGained -= (this.xpCap - currentXp);
			temp = this.levelUp(true);
			if (!willReturn) {
				willReturn = temp;
			}
		}

		this.currentXp += xpGained;
		return willReturn;

	}

	public void changeName(String newName) {

		this.name = newName;

	}

	public boolean isFainted() {

		if (this.currentHp > 0) {
			return false;
		}
		return true;

	}

	public String getType() {

		return this.type;

	}

	public int getLevel() {

		return this.level;

	}

	public String getName() {
		return this.name;
	}

	public int getHp() {
		return this.currentHp;
	}

	public int getMaxHp() {
		return this.hpCap;
	}

	public ArrayList<Integer> getMoves() {

		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int id : this.movesToPPMap.keySet()) {
			temp.add(id);
		}

		return temp;

	}

	public void addMove(int index) {
		this.movesToPPMap.put(index, BattleMain.ID_TO_PP_MAP.get(index));
	}

	public void setXpToLevelUp() {

		this.xpCap = (int) (4.0 * Math.pow(this.level, 3) / 5);

	}

	public double getAttack() {
		return this.attack * this.translateModifier(this.modifiers[0]);
	}

	public double getDefense() {
		return this.defense * this.translateModifier(this.modifiers[1]);
	}

	public double getSpAttack() {
		return this.spAttack * this.translateModifier(this.modifiers[2]);
	}

	public double getSpDefense() {
		return this.spAttack * this.translateModifier(this.modifiers[3]);
	}

	public double getSpeed() {
		return this.speed * this.translateModifier(this.modifiers[4]);
	}

	public double translateModifier(int modifier) {

		if (modifier >= 0) {
			return (2.0 + modifier) / 2.0;
		} else {
			return 2.0 / (2.0 - modifier);
		}

	}

	public void editMultiplier(int index, int inkBuy) {
		
		this.modifiers[index] += inkBuy;
		if (this.modifiers[index] > 6) {
			this.modifiers[index] = 6;
		} else if (this.modifiers[index] < -6) {
			this.modifiers[index] = -6;
		}

	}

	public void wipeMultipliers() {
		for (int i = 0; i < this.modifiers.length; i++) {
			this.modifiers[i] = 0;
		}
	}

	public boolean useMove(int moveId) {

		if (this.movesToPPMap.get(moveId) <= 0) {
			return false;
		} else {
			this.movesToPPMap.put(moveId, this.movesToPPMap.get(moveId) - 1);
			return true;
		}

	}

	public int getPP(int moveId) {
		return this.movesToPPMap.get(moveId);
	}
	
	public void replenishPP() {
		
		for (int moveId : this.movesToPPMap.keySet()) {
			
			if (this.movesToPPMap.get(moveId) + 2 > BattleMain.ID_TO_PP_MAP.get(moveId)) {
				this.movesToPPMap.put(moveId, BattleMain.ID_TO_PP_MAP.get(moveId));
			} else {
				this.movesToPPMap.put(moveId, this.movesToPPMap.get(moveId) + 2);
			}
			
		}
		
	}

	public boolean cannotMove() {
		for (int moveId: this.movesToPPMap.keySet()) {
			if (this.movesToPPMap.get(moveId) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public int getMoveAmount() {
		return this.movesToPPMap.keySet().size();
	}
	
	public boolean tryLearnMove() {
		
		int indexToSelect = this.r.nextInt(this.possibleMoves.size());
		int moveToAdd = this.possibleMoves.get(indexToSelect);
		while (this.movesToPPMap.containsKey(moveToAdd)) {
			indexToSelect = this.r.nextInt(this.possibleMoves.size());
			moveToAdd = this.possibleMoves.get(indexToSelect);
		}
		
		this.movesToPPMap.put(moveToAdd, BattleMain.ID_TO_PP_MAP.get(moveToAdd));
		
		return this.getMoveAmount() > MOVES_CAP;
	}
	
	public void forgetMove() {
		int indexToRemove = r.nextInt(MOVES_CAP);
		this.movesToPPMap.remove(this.movesToPPMap.keySet().toArray()[indexToRemove]);
	}
	
	public void forgetMoveByIndex(int indexToForget) {
		int forgetID = -1;
		int num = 0;
		for (int id: this.movesToPPMap.keySet()) {
			if (num == indexToForget) {
				forgetID = id;
			}
			num++;
		}
		this.forgetMoveByID(forgetID);
	}
	
	public void forgetMoveByID(int moveToForget) {
		this.movesToPPMap.remove(moveToForget);
	}
	
	public int getPPFromMoveId(int moveId) {
		return this.movesToPPMap.get(moveId);
	}
	
//	public boolean hasMove(int moveId) {
//		for (int move: this.movesToPPMap.keySet()) {
//			if (move == moveId) {
//				return true;
//			}
//		}
//		return false;
//	}

}
