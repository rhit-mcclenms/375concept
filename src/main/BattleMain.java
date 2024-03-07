package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This represents the main battling class. Whenever a battle is made, this
 * class is called, and the battle sequence continues.
 * 
 * Takes in two Trainers and makes them fight.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class BattleMain {

	// Structure, Acid, Water, Heat, Normal, Resistant, Protein
	private final static double[] STRUCTURE_EFFECTIVENESS = { 0.5, 0.25, 4, 0.5, 4, 2, 0.25 };
	private final static double[] ACID_EFFECTIVENESS = { 4, 0.25, 0.25, 2, 1, 0.25, 4 };
	private final static double[] WATER_EFFECTIVENESS = { 0.25, 4, 0.5, 4, 0.5, 0.5, 1 };
	private final static double[] HEAT_EFFECTIVENESS = { 2, 0.5, 0.25, 1, 4, 0.5, 4 };
	private final static double[] NORMAL_EFFECTIVENESS = { 0.25, 1, 2, 0.25, 1, 1, 2 };
	private final static double[] RESISTANT_EFFECTIVENESS = { 0.5, 4, 2, 2, 1, 0.25, 0.25 };
	private final static double[] PROTEIN_EFFECTIVENESS = { 4, 0.25, 1, 0.25, 0.5, 4, 2 };
	
	private final static Color STRUCTURE_COLOR_UNSELECTED = new Color(235, 143, 136);
	private final static Color ACID_COLOR_UNSELECTED = new Color(164, 235, 165);
	private final static Color HEAT_COLOR_UNSELECTED = new Color(255, 171, 107);
	private final static Color PROTEIN_COLOR_UNSELECTED = new Color(166, 131, 99);	
	private final static Color RESISTANT_COLOR_UNSELECTED = new Color(209, 135, 255);
	private final static Color WATER_COLOR_UNSELECTED = new Color(183, 215, 237);
	private final static Color NORMAL_COLOR_UNSELECTED = new Color(220, 220, 220);
	
	private final static Color STRUCTURE_COLOR_SELECTED = new Color(224, 74, 63);
	private final static Color ACID_COLOR_SELECTED = new Color(76, 230, 78);
	private final static Color HEAT_COLOR_SELECTED = new Color(250, 129, 37);
	private final static Color PROTEIN_COLOR_SELECTED = new Color(115, 61, 13);	
	private final static Color RESISTANT_COLOR_SELECTED = new Color(178, 54, 255);
	private final static Color WATER_COLOR_SELECTED = new Color(77, 169, 232);
	private final static Color NORMAL_COLOR_SELECTED = new Color(110, 110, 110);
	
	private final static Color HEALTHY_COLOR = new Color(39, 186, 54);
	private final static Color DAMAGED_COLOR = new Color(214, 214, 11);
	private final static Color CRITICAL_COLOR = new Color(199, 6, 6);
	
	private final static int MENU_X_POS = 1300;
	private final static int MENU_Y_POS = 50;
	
	private final static int HEAL_BY = 15;
	
	private HashMap<Integer, String> idToTypeMap;
	private HashMap<Integer, Color> idToUnselectColorMap;
	private HashMap<Integer, Color> idToSelectColorMap;
	private HashMap<Integer, String> idToNameMap;
	private HashMap<Integer, String> idToDescriptionMap;
	private HashMap<Integer, Integer> idToStatusIncreaseMap;
	public static final HashMap<Integer, Integer> ID_TO_PP_MAP = new HashMap<Integer, Integer>() {{
			put(1,10); put(2,20); put(3,15); put(4,15); put(5,20); put(6,35); put(7,10); put(8,30); put(9,10);
			put(101,25); put(102,15); put(103, 10); put(104,20); put(105,30); put(106,20); put(107,30); put(108,35);
			put(109,30); put(110,15); put(111,25); put(112,15); put(201,25); put(202,20); put(203,35); put(204,20);
			put(205,15); put(206,25); put(207,25); put(208,15); put(301,20); put(302,20); put(303,10); put(304,5);
			put(305,20); put(306,20); put(307,15); put(308,35); put(309,25); put(310,5); put(401,20); put(402,1);
			put(403,15); put(404,15); put(405,20); put(406,10); put(407,20); put(501,15); put(502,15); put(503,25);
			put(504,15); put(505,20); put(506,20); put(507,20); put(508,25); put(509,5); put(510,10); put(601,35);
			put(602,25); put(603,35); put(604,20); put(605,15); put(606,25); put(607,30); put(608,20); put(609,20);
			put(610,15); put(611,15); put(612,1); put(613,35); put(614,25); put(615,25); put(616,20);
	}};
	private HashMap<Integer, Integer> idToPowerMap;
	private HashMap<Integer, Integer> idToStatusMap;
	private HashMap<Integer, Double> idToAccuracyMap;
	
	private BattleUserTrainer firstTrainer;
	private BattleTrainer secondTrainer;
	private Pokemon currentFirst;
	private Pokemon currentSecond;
	private Color firstColor;
	private Color secondColor;
	private boolean isOver;
	private int frameCounter;
	private Menu currentMenu, attackMenu, mainMenu, changeMenu, blankMenu, discardMenu, continueMenu, learnMoveMenu, afterLearnMenu;
	private HashMap<Menu, Integer> deckMenusToIndexMap;
	private int indexForLearn;
	private String userCommentary;
	private String enemyCommentary;
	private ArrayList<Integer> userMoves;
	private ArrayList<Integer> opponentMoves;
	private int damageToFirst;
	private int damageToSecond;
	private boolean battleToEnd;
	private boolean donkeyKong; //Forgot what this does, not gonna lie.
	private DJSmooth dj;

	public BattleMain() {
		
	}
	
	public BattleMain(BattleUserTrainer trainer1, BattleTrainer trainer2, DJSmooth dj) {
		this.dj = dj;
		this.firstTrainer = trainer1;
		this.secondTrainer = trainer2;
		this.currentFirst = trainer1.getHealthyPokemon();
		this.currentSecond = trainer2.getHealthyPokemon();
		this.frameCounter = 0;
		this.isOver = false;
		this.deckMenusToIndexMap = new HashMap<Menu, Integer>();
		this.indexForLearn = -1;
//		String[] attackOptions = new String[this.currentFirst.getMoveAmount()];
//		Color[] someColors = new Color[this.currentFirst.getMoveAmount()];
//		Color[] moreColors = new Color[this.currentFirst.getMoveAmount()];
		this.damageToFirst = 0;
		this.damageToSecond = 0;
		this.battleToEnd = false;
		this.donkeyKong = false;
		this.afterLearnMenu = null;
		
		int[] pps = {20, 14, 17, 12};
		int[] maxPPs = {25, 20, 20, 15};
		
		
		this.idToTypeMap = new HashMap<Integer, String>();
		this.idToNameMap = new HashMap<Integer, String>();
		this.idToUnselectColorMap = new HashMap<Integer, Color>();
		this.idToSelectColorMap = new HashMap<Integer, Color>();
		this.idToDescriptionMap = new HashMap<Integer, String>();
		this.idToPowerMap = new HashMap<Integer, Integer>();
		this.idToStatusMap = new HashMap<Integer, Integer>();
		this.idToStatusIncreaseMap = new HashMap<Integer, Integer>();
		this.idToAccuracyMap = new HashMap<Integer, Double>();
		
		String firstType = this.currentFirst.getType();
		String secondType = this.currentSecond.getType();
		if (trainer2.isWild()) {
			this.userCommentary = "You encountered a Wild Organelle!";
		} else {
			this.userCommentary = "You encountered an Infected Cell!";
		}
		this.enemyCommentary = "";
		
		this.firstColor = getUnselectedColor(firstType);
		this.secondColor = getUnselectedColor(secondType);
		
		this.makeMove(1, "Structure", "Mitosis", "Increase Defense", 3, 0, 3, 0.8);
		this.makeMove(2, "Structure", "Filament Defense", "Increase Defense", 3, 0, 1, 0.95);
		this.makeMove(3, "Structure", "Strong Hit", "Hits opponent with a lot of force", 1, 90, 0, 0.9);
		this.makeMove(4, "Structure", "Bunker",	"Heavily increases defense", 3, 0,	2, 0.9);
		this.makeMove(5, "Structure", "Roll Over", "Damages opponent with sheer weight", 1, 85, 0, 0.85);
		this.makeMove(6, "Structure", "Slam", "Slams down onto the opponent", 1, 75, 0, 0.75);
		this.makeMove(7, "Structure", "Bicicleta", "The opponent gets Bicicleta'd by the power of structure", 1, 100, 0, 0.75);
		this.makeMove(8, "Structure", "Shake", "Uses strong hold on the cell to shake things up", 1, 60, 0, 0.8);
		this.makeMove(9, "Structure", "Self-Destruct", "Faints on use, but deals a lot of damage", 1, 150, 0, 0.6);
		this.makeMove(101, "Acid", "Acid Burn", "Burns the player with acid", 1, 80, 0, 0.85);
		this.makeMove(102, "Acid", "Break Down", "Organelle starts to dance furiously (special attack buff)", 5, 0, 2, 1);
		this.makeMove(103, "Acid", "Lipid Nergy", "Unleashes the energy and power inside lipids", 1, 105, 0, 0.95);
		this.makeMove(104, "Acid", "Ooze", "Decrease opponent speed", -11, 0, 1, 0.75);
		this.makeMove(105, "Acid", "Sizzle", "Increase attack", 2, 0, 1, 0.8);
		this.makeMove(106, "Acid", "Big pH", "Increases the pH of the opponent's surroundings, damaging them greatly", 1, 80, 0, 0.8);
		this.makeMove(107, "Acid", "Citric Acid", "Slight speed increase", 11, 0, 1, 1);
		this.makeMove(108, "Acid", "Basic Hit", "Just a basic hit", 1, 45, 0, 0.95);
		this.makeMove(109, "Acid", "Hydrohydroxic Acid", "Is a water type move. Trust me", 1, 65, 0, 0.7);
		this.makeMove(110, "Acid", "Sulfuric Acid", "H2SO4", 1, 90, 0, 0.8);
		this.makeMove(111, "Acid", "Proton Punch", "Gives free protons to whoever is near", 1, 75, 0, 0.7);
		this.makeMove(112, "Acid", "Digestion", "Attempts to digest its opponent", 1, 95, 0, 0.7);
		this.makeMove(201, "Water", "Flood", "Floods the opponent and tries to make it burst", 1, 75, 0, 0.8);
		this.makeMove(202, "Water", "H20 Jet", "Sprays strong jet of water", 1, 90, 0, 0.85);
		this.makeMove(203, "Water", "Splash", "No effect. Literally no effect.", 1, 0, 0, 1);
		this.makeMove(204, "Water", "Slippery", "Increase speed", 11, 0, 1, 0.9);
		this.makeMove(205, "Water", "Rain Dance", "Increase special attack", 5, 0, 2, 0.9);
		this.makeMove(206, "Water", "Submerge", "Decrease opponent attack", -2, 0, 1, 0.75);
		this.makeMove(207, "Water", "Soak", "Decrease opponent defense", -3, 0, 1, 0.8);
		this.makeMove(208, "Water", "Drain", "Drains water and health from opponent and restores half hP.", 1, 55, 0, 0.75);
		this.makeMove(301, "Heat", "Quick Attack", "Always attacks first. Does what needs to be done.", 1, 70, 0, 0.85);
		this.makeMove(302, "Heat", "Outspeed", "Works like dig.", 1, 70, 0, 0.8);
		this.makeMove(303, "Heat", "Calvin Cycle", "Uses energy to boost attack and defense.", 6, 0, 2, 0.7);
		this.makeMove(304, "Heat", "Powerhouse", "The mitochondria is the powerhouse of the cell", 10, 120, 3, 0.8);
		this.makeMove(305, "Heat", "Energize", "Increase attack", 2, 0, 1, 0.7);
		this.makeMove(306, "Heat", "Scorch", "Powerful attack", 1, 85, 0, 0.75);
		this.makeMove(307, "Heat", "Microondas Type Beat", "Uses strong microwave powers to damage opponent", 1, 95, 0, 0.65);
		this.makeMove(308, "Heat", "Boil", "Pokemon boils water around opponent.", 1, 50, 0, 0.85);
		this.makeMove(309, "Heat", "Fireball", "Ba da da dadada", 1, 65, 0, 0.9);
		this.makeMove(310, "Heat", "Hobbes Cycle", "Makes energy to boost ALL STATS!!!", 2310, 0, 1, 0.75);
		this.makeMove(401, "Resistant", "Counter", "Takes the move used by the other organelle and throws it right back at it!", 17, 0, 0, 0.75);
		this.makeMove(402, "Resistant", "Copy", "This move permanently changes to the move used by the opponent.", 13, 0, 0, 0.5);
		this.makeMove(403, "Resistant", "Adapt", "Strongly increases defense.", 3, 0, 2, 0.85);
		this.makeMove(404, "Resistant", "Tentative Copy", "Works like copy would, but it doesn't last long.", 13, 0, 0, 0.6);
		this.makeMove(405, "Resistant", "Fortify", "Increase defense", 3, 0, 1, 0.9);
		this.makeMove(406, "Resistant", "Extremophile", "Sharply increases special attack and defense", 35, 0, 2, 0.8);
		this.makeMove(407, "Resistant", "Stubbornness", "Increases defense", 3, 0, 1, 0.9);
		this.makeMove(501, "Protein", "Enzyme", "Powerful attack that tries to break down its opponent", 1, 95, 0, 0.85);
		this.makeMove(502, "Protein", "Chain Attack", "Attack deals bonus damage for every other protien in player's group", 1, 60, 0, 0.9);
		this.makeMove(503, "Protein", "Antibody", "Increase defense", 3, 0, 1, 0.8);
		this.makeMove(504, "Protein", "Repair", "Drain", 1, 70, 0, 0.75);
		this.makeMove(505, "Protein", "Muscle Move", "Powerful attack", 1,	85, 0, 0.85);
		this.makeMove(506, "Protein", "Strongarm", "Damage opponent, weaken opponent's defense", 1, 70, 1, 0.8);
		this.makeMove(507, "Protein", "Big Flex", "Increase attack", 2, 0,	2, 0.75);
		this.makeMove(508, "Protein", "Protein Shake", "Makes TONS of protein rapidly to increase attack and defense", 6, 0, 1, 0.8);
		this.makeMove(509, "Protein", "Montar Bicicleta", "The opponent gets montar bicicleta'd by the power of protein", 1, 110, 0, 0.6);
		this.makeMove(510, "Protein", "Sneak Attack", "1-hit KO if lands", 1, 1000, 0, 0.23);
		this.makeMove(601, "Normal", "Bland Attack", "Doesn't really do much.", 1, 50, 0, 0.8);
		this.makeMove(602, "Normal", "Vague Energy", "Does some stuff and increases defense I think.", 3, 0, 1, 0.75);
		this.makeMove(603, "Normal", "Normal Hit", "Hits the other organelle where it doesn't hurt too much.", 1, 55, 0, 0.85);
		this.makeMove(604, "Normal", "Whack", "Whacks the other oppponent", 1, 65, 0, 0.8);
		this.makeMove(605, "Normal", "Big Yeet", "Best move a normal organelle can have. But really it's not all that great.", 1, 75, 0, 0.85);
		this.makeMove(606, "Normal", "Ram", "Hits the opponent.", 1, 60, 0, 0.75);
		this.makeMove(607, "Normal", "Self-Confidence", "Increases attack.", 2, 0, 1, 0.85);
		this.makeMove(608, "Normal", "Confident Yell", "Decreases opponent defense", -3, 0, 1, 0.9);
		this.makeMove(609, "Normal", "Big DNA", "Increase special attack and special defense.", 35, 0, 1, 0.9);
		this.makeMove(610, "Normal", "Absorb", "Leach opponent's HP", 1, 0, 0, 0.75);
		this.makeMove(611, "Normal", "Angry Carbon", "Hits the opponent with slightly random damage.", 1, 10, 0, 0);
		this.makeMove(612, "Normal", "Struggle", "Only used when the Organelle doesn't have any other moves", 1, 35, 0, 0.5);
		this.makeMove(613, "Normal", "Generic Blast", "Attacks with some force.", 1, 70, 0, 0.8);
		this.makeMove(614, "Normal", "Mundane Footwork", "Speed buff (maybe)", 11, 0, 1, 0.8);
		this.makeMove(615, "Normal", "Tarea Type Beat", "Pokemon studies up and finds weak spots.  (Decrease opponent defense)", -3, 0, 1, 0.9);
		this.makeMove(616, "Normal", "Naptime", "Pokemon takes some rest to increase their health a bit", 1, 40, 0, 0.8);

//		for (int i = 0; i < this.currentFirst.getMoveAmount(); i++) {
//			attackOptions[i] = this.idToNameMap.get(userMoves.get(i));
//			someColors[i] = this.idToUnselectColorMap.get(userMoves.get(i));
//			moreColors[i] = this.idToSelectColorMap.get(userMoves.get(i));
//			
//			//This will need changing! Only temporary.
//			pps[i] = ID_TO_PP_MAP.get(userMoves.get(i));
//			//This will need changing! Only temporary.
//			
//			maxPPs[i] = ID_TO_PP_MAP.get(userMoves.get(i));
//		}
//		
//		this.attackMenu = new Menu(attackOptions, someColors, moreColors, pps, maxPPs, MENU_X_POS, MENU_Y_POS, true);
		this.makeLearnMenu();
		String[] menuOptions = { "Attack", "Change", "Catch", "Run" };
		this.mainMenu = new Menu(menuOptions, MENU_X_POS, MENU_Y_POS);
		this.currentMenu = this.mainMenu;
		this.blankMenu = new Menu();
		
		String[] myPokemon = this.firstTrainer.getPokemonNames();
		pps = new int[this.firstTrainer.getPokemonAmount()];
		maxPPs = new int[this.firstTrainer.getPokemonAmount()];
		String[] pokemonTypes = this.firstTrainer.getPokemonTypes();
		Color[] selectedPokemonColors = new Color[this.firstTrainer.getPokemonAmount()];
		Color[] unselectedPokemonColors = new Color[this.firstTrainer.getPokemonAmount()];
		for (int i = 0; i < selectedPokemonColors.length; i++) {
			pps[i] = this.firstTrainer.getNotFainedPokemon(i).getHp();
			maxPPs[i] = this.firstTrainer.getNotFainedPokemon(i).getMaxHp();
			selectedPokemonColors[i] = getSelectedColor(pokemonTypes[i]);
			unselectedPokemonColors[i] = getUnselectedColor(pokemonTypes[i]);
		}
		
		this.changeMenu = new Menu(myPokemon, unselectedPokemonColors, selectedPokemonColors, pps, maxPPs, MENU_X_POS, MENU_Y_POS, true);
		this.updateAttackMenu();
		this.discardMenu = new Menu(myPokemon, unselectedPokemonColors, selectedPokemonColors, pps, maxPPs, MENU_X_POS, MENU_Y_POS, false);
		String[] myOneOption = {"End"};
		this.continueMenu = new Menu(myOneOption, MENU_X_POS, MENU_Y_POS);

	}

	/**
	 * attackTrainer: 1 if firstTrainer, 2 if secondTrainer.
	 * 
	 * @return
	 */
	public int calculateDamage(int attackTrainerID, int moveId) {

		Pokemon attackPokemon;
		Pokemon defendPokemon;

		if (attackTrainerID == 1) {
			attackPokemon = this.currentFirst;
			defendPokemon = this.currentSecond;
		} else {
			attackPokemon = this.currentSecond;
			defendPokemon = this.currentFirst;
		}

		double effectiveness = this.calculateEffectiveness(attackPokemon, defendPokemon, idToTypeMap.get(moveId));

		double firstPart = (2 * attackPokemon.getLevel() + 2) * idToPowerMap.get(moveId)
				* (attackPokemon.getAttack() * 1.0 / defendPokemon.getDefense());

		boolean crit;
		if (Math.random() > 0.0625) {
			crit = false;
		} else {
			crit = true;
		}

		double critModifier = 1;
		if (crit) {
			critModifier = 1.5;
		}

		double randomModifier = 0.15 * Math.random() + 0.85;

		double stabbyStab = 1;
		if (idToTypeMap.get(moveId).equals(attackPokemon.getType())) {
			stabbyStab = 1.5;
		}

		double modifier = critModifier * randomModifier * stabbyStab * effectiveness;

		int totalDamage = (int) (((firstPart + 10) / 250) * modifier);
		if (totalDamage < 0) {
			totalDamage = 1;
		}
		if (moveId == 612) {
			totalDamage = 5;
		}
		
		return totalDamage;

	}
	
	public String doStatusThings(int moveId, int moveUser) {
		
		int numToTranslate = this.idToStatusIncreaseMap.get(moveId);
		
		if (numToTranslate == 1 || numToTranslate % 17 == 0) {
			return "";
		}
		
		HashMap<Integer, String> idToStatNameMap = new HashMap<Integer, String>();
		idToStatNameMap.put(0, "attack");
		idToStatNameMap.put(1, "defense");
		idToStatNameMap.put(2, "special attack");
		idToStatNameMap.put(3, "special defense");
		idToStatNameMap.put(4, "speed");
		
		String newCommentary;
		
		Pokemon attackPokemon;
		Pokemon defendPokemon;
		
		if (moveUser == 0) {
			attackPokemon = this.currentFirst;
			defendPokemon = this.currentSecond;
		} else {
			attackPokemon = this.currentSecond;
			defendPokemon = this.currentFirst;
		}
		String highLow;
		Pokemon affectedPokemon;
		int multiplier;
		if (numToTranslate < 0) {
			affectedPokemon = defendPokemon;
			multiplier = -1;
			highLow = " which lowered ";
		} else {
			affectedPokemon = attackPokemon;
			multiplier = 1;
			highLow = " which heightened ";
		}
		newCommentary = highLow + affectedPokemon.getName() + "'s ";
		int[] primeNumbers = {2, 3, 5, 7, 11};
		for (int i = 0; i < primeNumbers.length; i++) {
			if (numToTranslate % primeNumbers[i] == 0) {
				affectedPokemon.editMultiplier(i, this.idToStatusMap.get(moveId) * multiplier);
				newCommentary += idToStatNameMap.get(i) + "";
			}
		}
		newCommentary += "!!!";
		return newCommentary;
		
	}

	/**
	 * 
	 * @param attackTrainer: the trainer that is attacking.
	 * @param defendTrainer: the trainer that is defending.
	 * @return
	 */
	public double calculateEffectiveness(Pokemon attackPokemon, Pokemon defendPokemon, String moveType) {

		String attackType = attackPokemon.getType();
		String defendType = defendPokemon.getType();

		double baseEffectiveness;
		double[] effectivenessCategory;
		int defendEffectID;

		if (attackType.equals("Structure")) {
			effectivenessCategory = STRUCTURE_EFFECTIVENESS;
		} else if (attackType.equals("Acid")) {
			effectivenessCategory = ACID_EFFECTIVENESS;
		} else if (attackType.equals("Water")) {
			effectivenessCategory = WATER_EFFECTIVENESS;
		} else if (attackType.equals("Heat")) {
			effectivenessCategory = HEAT_EFFECTIVENESS;
		} else if (attackType.equals("Normal")) {
			effectivenessCategory = NORMAL_EFFECTIVENESS;
		} else if (attackType.equals("Resistant")) {
			effectivenessCategory = RESISTANT_EFFECTIVENESS;
		} else {
			effectivenessCategory = PROTEIN_EFFECTIVENESS;
		}

		if (defendType.equals("Structure")) {
			defendEffectID = 0;
		} else if (defendType.equals("Acid")) {
			defendEffectID = 1;
		} else if (defendType.equals("Water")) {
			defendEffectID = 2;
		} else if (defendType.equals("Heat")) {
			defendEffectID = 3;
		} else if (defendType.equals("Normal")) {
			defendEffectID = 4;
		} else if (defendType.equals("Resistant")) {
			defendEffectID = 5;
		} else {
			defendEffectID = 6;
		}

		baseEffectiveness = effectivenessCategory[defendEffectID];
		if (baseEffectiveness < 1) {
			return Math.pow(baseEffectiveness, defendPokemon.getSpDefense() / attackPokemon.getSpAttack());
		}

		return baseEffectiveness;

	}

	public void drawOn(Graphics2D g) {
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, OrganelleMain.FULLSCREEN_WIDTH, OrganelleMain.FULLSCREEN_HEIGHT);
		g.setColor(Color.BLACK);

		this.firstTrainer.drawPokemon(g, true);
		this.secondTrainer.drawPokemon(g, false);

		if (this.currentMenu != null) {
			this.currentMenu.drawOn(g);
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 850, 1275, 10);
		g.fillRect(1275, 0, 10, 1280);
		
		g.setColor(this.firstColor);
		g.fillRect(775, 575, 500, 275);
		g.setColor(this.secondColor);
		g.fillRect(0, 0, 500, 275);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("yuh", 7, 60));
		g.drawString(this.currentFirst.getName(), 800, 650);
		g.drawString(this.currentSecond.getName(), 25, 75);
		g.setFont(new Font("yuh", 7, 35));
		g.drawString("lv. " + this.currentFirst.getLevel(), 1155, 815);
		g.drawString("lv. " + this.currentSecond.getLevel(), 380, 240);
		g.drawString("HP: " + this.currentFirst.getHp() + "/" + this.currentFirst.getMaxHp(), 815, 815);
		g.drawString("HP: " + this.currentSecond.getHp() + "/" + this.currentSecond.getMaxHp(), 40, 240);
		
		//Draw first Pokemon's health bar
		
		g.setColor(Color.WHITE);
		g.fillRect(815, 690, 420, 70);
		if (this.currentFirst.getHp() > 0.5 * this.currentFirst.getMaxHp()) {
			g.setColor(HEALTHY_COLOR);
		} else if (this.currentFirst.getHp() < 0.2 * this.currentFirst.getMaxHp()) {
			g.setColor(CRITICAL_COLOR);
		} else {
			g.setColor(DAMAGED_COLOR);
		}
		g.fillRect(815, 690, 420 * this.currentFirst.getHp() / this.currentFirst.getMaxHp() , 70);
		g.setColor(Color.BLACK);
		g.setStroke( new BasicStroke(5)  );
		g.drawRect(815, 690, 420, 70);
		g.setStroke( new BasicStroke(1)  );

		
		
		//Draw second Pokemon's heath bar
		g.setColor(Color.WHITE);
		g.fillRect(40, 115, 420, 70);
		if (this.currentSecond.getHp() > 0.5 * this.currentSecond.getMaxHp()) {
			g.setColor(HEALTHY_COLOR);
		} else if (this.currentSecond.getHp() < 0.25 * this.currentSecond.getMaxHp()) {
			g.setColor(CRITICAL_COLOR);
		} else {
			g.setColor(DAMAGED_COLOR);
		}
		g.fillRect(40, 115, 420 * this.currentSecond.getHp() / this.currentSecond.getMaxHp() , 70);
		g.setColor(Color.BLACK);
		g.setStroke( new BasicStroke(5)  );
		g.drawRect(40, 115, 420, 70);

		g.setStroke( new BasicStroke(10) );
		g.drawString(this.userCommentary, 60, 925);
		g.drawString(this.enemyCommentary, 60, 1000);

	}

	public void update() {
		if (this.frameCounter > 0) {
			this.frameCounter--;
			if (this.frameCounter == 0) {
				if (this.battleToEnd) {
					this.isOver = true;
					return;
				}
			}
			return;
		}
		if (this.currentMenu == this.blankMenu) {
			if (this.damageToFirst != 0 && this.damageToSecond != 0) {
				if (this.currentFirst.getSpeed() > this.currentSecond.getSpeed()) {
					this.currentSecond.takeDamage(1);
					this.damageToSecond--;
				} else {
					this.currentFirst.takeDamage(1);
					this.damageToFirst--;
				}
				if (this.currentFirst.isFainted()) {
					this.handleFaintFirst();
				}
				if (this.currentSecond.isFainted()) {
					this.handleFaintSecond();
				}
			}
			else if (this.damageToFirst != 0 && !this.battleToEnd) {
				this.currentFirst.takeDamage(1);
				this.damageToFirst--;
				if (damageToFirst == 0) {
					this.currentMenu = this.mainMenu;
				}
				if (this.currentFirst.isFainted()) {
					this.handleFaintFirst();
				}
			}
			else if (this.damageToSecond != 0 && !this.battleToEnd) {
				this.currentSecond.takeDamage(1);
				this.damageToSecond--;
				if (damageToSecond == 0) {
					this.currentMenu = this.mainMenu;
				}
				if (this.currentSecond.isFainted()) {
					this.handleFaintSecond();
				}
			}
			else {
				this.handleNextMenu(this.mainMenu);
			}
		}

		this.currentMenu.update();

	}

	public void endBattle(boolean store) {
		
		this.currentMenu = this.continueMenu;
		this.firstTrainer.replenishPP();
		this.firstTrainer.healAll(HEAL_BY);
		this.firstTrainer.wipeAllMultipliers();
		if (this.currentFirst.isFainted()) {
			this.userCommentary = "You have lost the battle";
			this.enemyCommentary = "You fled fearfully to save your fainted organelles.";
		}
		if (this.currentSecond.isFainted()) {
			this.userCommentary = "You won the battle!";
			this.enemyCommentary = "";
		}
		this.battleToEnd = true;
	}

	public boolean isOver() {
		return this.isOver;
	}

	public void pressInput(KeyEvent e) {
		// UP
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
			this.currentMenu.takeUserInput("Up");
		}
		
		// DOWN
		else if (e.getKeyCode() == 40 || e.getKeyCode() == 83) {
			this.currentMenu.takeUserInput("Down");
		}
		//ENTER
		if (e.getKeyCode() == 10 || e.getKeyCode() == 32) {
			
			handleSelect(this.currentMenu.takeUserInput("Select"));
			//Do some things.
		}
		
	}
	
	//Returns true if successful catch
	public boolean attemptCatch() {
		
		if (this.secondTrainer.isWild()) {
			if (!this.firstTrainer.canCatch(this.currentSecond.getName())) {
				if (this.firstTrainer.getType().charAt(0) == 'A') {
					this.userCommentary = "An ";
				} else {					
					this.userCommentary = "A ";
				}
				this.userCommentary += this.firstTrainer.getType() + " cell cannot catch a ";
				this.userCommentary += this.currentSecond.getName() + "!!";
				this.enemyCommentary = "";
				return false;
			}
			double probability = (0.8 - 0.6 * ( this.currentSecond.getHp() * 1.0 / this.currentSecond.getMaxHp()));
			double val = Math.random();
			if (!this.firstTrainer.useVesicle()) {
				this.userCommentary = "You ran out of vesicles! You cannot catch.";
				this.enemyCommentary = "";
				return false;
			}
			if (val < probability) {
				this.firstTrainer.addPokemon(this.secondTrainer.getNotFainedPokemon(0));
				this.userCommentary = "You caught a " + this.secondTrainer.getParty().get(0).getName() + "!";
				this.enemyCommentary = "";
				if (this.firstTrainer.getPokemonAmount() > 6) {
					this.makeDiscardMenu();
					this.currentMenu = this.discardMenu;
					return false;
				}
				return true;
			}
			else {
				this.userCommentary = "You missed. You have " + this.firstTrainer.getVesicleAmount() + " vesicles left.";
				this.enemyCommentary = "";
				this.takeOpponentTurn(401);
			}
		}
		else {
			this.userCommentary = "You cannot catch an infected Organelle!";
			this.enemyCommentary = "";
		}
		return false;
	}
	
	public void handleSelect(int indexSelected) {
		
		for (Menu m: this.deckMenusToIndexMap.keySet()) {
			System.out.println(m);
		}
		
		if (!this.battleToEnd) {			
			this.updateChangeMenu();
		}

		String canCauseLearn = "false";
		boolean continued = false;
		
		if (this.currentMenu == this.mainMenu) {
			continued = this.handleMainMenu(indexSelected);
		} else if (this.currentMenu == attackMenu) {
			canCauseLearn = "attack";
			continued = this.handleAttackMenu(indexSelected);
		}
		else if (this.currentMenu == this.changeMenu) {
			continued = this.handleChangeMenu(indexSelected);
		}
		else if (this.currentMenu == this.discardMenu) {
			continued = this.handleDiscardMenu(indexSelected);
		} else if (this.currentMenu == this.continueMenu) {
			if (this.currentFirst.isFainted()) {
				System.exit(0);
			}
			continued = false;
			this.isOver = true;
		} else if (this.currentMenu == this.blankMenu) {
			continued = false;
			//Do nothing.
		}
		//Occurs when we are in a learnMenu.
		else {
			canCauseLearn = "learn";
			continued = this.handleLearnMenu(indexSelected);
		}
		
		if (this.battleToEnd && !this.currentIsLearn()) {
			this.endBattle(false);
		}

		if (continued && this.deckMenusToIndexMap.keySet().size() > 0) {
			//We have continued, and there are learnMenus stored.
			//Shenanigans have occurred.
			
		}
			
		
	}
	
	private boolean currentIsLearn() {
		return menuIsLearn(this.currentMenu);
	}
	
	private boolean menuIsLearn(Menu test) {
		if (test == this.attackMenu) {
			return false;
		} else if (test == this.changeMenu) {
			return false;
		} else if (test == this.blankMenu) {
			return false;
		} else if (test == this.continueMenu) {
			return false;
		} else if (test == this.discardMenu) {
			return false;
		} else if (test == this.mainMenu) {
			return false;
		}
		return true;
	}

	public boolean handleMainMenu(int indexSelected) {
		
		if (indexSelected == 0) { //Attack is selected
			this.currentMenu = this.attackMenu;		

		} else if (indexSelected == 1) { //Change is selected
			this.currentMenu = this.changeMenu;
			
		} else if (indexSelected == 2) { //Catch is selected
			if (this.attemptCatch()) {
				this.endBattle(false);
			}

		} else if (indexSelected == 3) { //Run is selected
			if (this.secondTrainer.isWild()) {
				this.userCommentary = "You got away safely!";
				this.enemyCommentary = "";
				this.endBattle(false);
			}
			else {
				this.userCommentary = "You couldn't get away!";
				this.enemyCommentary = "";
			}
		}
		return false;
	}
	
	public boolean handleAttackMenu(int indexSelected) {
		boolean changedMenus = false;
		if (indexSelected == attackMenu.size() - 1) {
			this.currentMenu = mainMenu;
			return false;
		} else {
			int moveUsed = this.userMoves.get(indexSelected);
			if (this.currentFirst.cannotMove()) {
				moveUsed = 612;
			}
			else if (!this.currentFirst.useMove(moveUsed)) {
				this.userCommentary = "No more power! Select different move.";
				this.enemyCommentary = "";
				return false;
			}
			this.updateAttackMenu();
			int oppMoveUsed = this.takeOpponentTurn(moveUsed);
			int myMoveStatus = this.idToStatusIncreaseMap.get(moveUsed);
			String actualMoveUsed = this.idToNameMap.get(moveUsed);
			if (myMoveStatus % 17 == 0) {
				moveUsed = oppMoveUsed;
				System.out.println("I have used Counter!");
				System.out.println("New damage should be: " + this.calculateDamage(1, oppMoveUsed));
				System.out.println("They used" + this.idToNameMap.get(oppMoveUsed));
			}
			int damageToSecond = this.calculateDamage(1, moveUsed);
			if (damageToSecond > this.currentSecond.getHp()) {
				damageToSecond = this.currentSecond.getHp();
			}
			this.damageToSecond = damageToSecond;
			this.updateChangeMenu();
			//*** Not sure about this ***
			if (currentSecond.isFainted()) {
				//Maybe true here?
				this.endBattle(false);
				this.currentFirst.heal(HEAL_BY);
			}
			if (currentFirst.isFainted()) {
				System.exit(0);
			}
			//*** Not sure about this ***
			this.currentMenu = this.blankMenu;
			changedMenus = true;
			String always = "You just used " + actualMoveUsed;
			String statusCommentary = this.doStatusThings(moveUsed, 0);
			if (damageToSecond != 0) {
				this.userCommentary = always + " which did " + damageToSecond + " damage!";		
			} else  if (statusCommentary != ""){
				this.userCommentary = always + statusCommentary;
			} else {
				this.userCommentary = always + " which had no effect!";
			}
			boolean userOutspeeds = this.currentFirst.getSpeed() > this.currentSecond.getSpeed();
			if (this.damageToSecond >= this.currentSecond.getHp() && userOutspeeds) {
				this.enemyCommentary = "";
			}
			else if (this.damageToFirst >= this.currentFirst.getHp() && !userOutspeeds){
				this.userCommentary = "";
			}
			
		}
		return changedMenus;
	}
	
	public boolean handleChangeMenu(int indexSelected) {
		boolean canUseBack = !(this.currentFirst.isFainted() || this.donkeyKong);
		boolean changedMenus;
		if (indexSelected == changeMenu.size() - 1) {
			//Back is selected
			if (canUseBack) {
				this.currentMenu = mainMenu;
				changedMenus = true;
			} else {
				this.userCommentary = "You have to choose a Pokemon!";
				this.enemyCommentary = "";
				changedMenus = false;
			}

		}
		else if (firstTrainer.getNotFainedPokemon(indexSelected).isFainted()) {
			this.userCommentary = "You cannot select a fainted Pokemon!";
			this.enemyCommentary = "";
			changedMenus = false;
		}
		else if (firstTrainer.getNotFainedPokemon(indexSelected) == this.currentFirst && !this.donkeyKong) {
			this.userCommentary = this.currentFirst.getName() + " is already selected!";
			this.enemyCommentary = "";
			changedMenus = false;
		}
		else {
			this.firstTrainer.setCurrentPokemon(indexSelected);
			this.updateCurrentPokemon();
			this.currentMenu = mainMenu;
			this.userCommentary = "Go, " + this.currentFirst.getName() + "!!!";
			this.enemyCommentary = "";
			if (canUseBack) {	
				this.takeOpponentTurn(401);
			} else {
				this.donkeyKong = false;
			}
			this.updateAttackMenu();
			changedMenus = true;

		}
		return changedMenus;
	}
	
	public boolean handleDiscardMenu(int indexSelected) {
		String discardedName = this.firstTrainer.removePokemon(indexSelected);
		if (!discardedName.equals("No!")) {
			this.userCommentary = "You have discarded " + discardedName;
			this.enemyCommentary = "";
			this.endBattle(false);
			return true;
		} else {
			this.userCommentary = "Hey buddy choose a different one. That didn't work";
			this.enemyCommentary = "I really don't know what just happened. Oh well";
			return false;
		}
	}
	
	public boolean handleLearnMenu(int indexSelected) {
		this.firstTrainer.getNotFainedPokemon(this.indexForLearn).forgetMoveByIndex(indexSelected);
		this.handleNextMenu(this.afterLearnMenu);
		return false;
	}
		
	
	public void makeMove(int id, String type, String name, String description, int statusType, int power, int status, double accuracy) {
		
		this.idToTypeMap.put(id, type);
		this.idToNameMap.put(id, name);
		this.idToDescriptionMap.put(id, description);
		this.idToPowerMap.put(id, power);
		this.idToStatusMap.put(id, status);
		this.idToAccuracyMap.put(id, accuracy);
		this.idToStatusIncreaseMap.put(id, statusType);
		
		Color currentColor = getUnselectedColor(type);
		this.idToUnselectColorMap.put(id, currentColor);
		currentColor = getSelectedColor(type);
		this.idToSelectColorMap.put(id, currentColor);
		
	}
	
	public static Color getUnselectedColor(String type) {
		if (type.equals("Structure")) {
			return STRUCTURE_COLOR_UNSELECTED;
		}
		else if (type.equals("Acid")) {
			return ACID_COLOR_UNSELECTED;
		}
		else if (type.equals("Heat")) {
			return HEAT_COLOR_UNSELECTED;
		}
		else if (type.equals("Water")) {
			return WATER_COLOR_UNSELECTED;
		}
		else if (type.equals("Protein")) {
			return PROTEIN_COLOR_UNSELECTED;
		}
		else if (type.equals("Resistant")) {
			return RESISTANT_COLOR_UNSELECTED;
		}
		else {
			return NORMAL_COLOR_UNSELECTED;
		}
	}
	
	public static Color getSelectedColor(String type) {
		if (type.equals("Structure")) {
			return STRUCTURE_COLOR_SELECTED;
		}
		else if (type.equals("Acid")) {
			return ACID_COLOR_SELECTED;
		}
		else if (type.equals("Heat")) {
			return HEAT_COLOR_SELECTED;
		}
		else if (type.equals("Water")) {
			return WATER_COLOR_SELECTED;
		}
		else if (type.equals("Protein")) {
			return PROTEIN_COLOR_SELECTED;
		}
		else if (type.equals("Resistant")) {
			return RESISTANT_COLOR_SELECTED;
		}
		else {
			return NORMAL_COLOR_SELECTED;
		}
	}
	
	public void updateChangeMenu() {
		for(int i = 0; i < this.changeMenu.size() - 1; i++) {
			this.changeMenu.updatePP(i, this.firstTrainer.getPokemon(i).getHp());
		}
	}
	
	public void updateAttackMenu() {
		this.updateCurrentPokemon();
		String[] attackOptions = new String[this.currentFirst.getMoveAmount()];
		Color[] someColors = new Color[this.currentFirst.getMoveAmount()];
		Color[] moreColors = new Color[this.currentFirst.getMoveAmount()];
		int[] pps = new int[this.currentFirst.getMoveAmount()];
		int[] maxPPs = new int[this.currentFirst.getMoveAmount()];
		for (int i = 0; i < this.currentFirst.getMoveAmount(); i++) {
			attackOptions[i] = this.idToNameMap.get(userMoves.get(i));
			someColors[i] = this.idToUnselectColorMap.get(userMoves.get(i));
			moreColors[i] = this.idToSelectColorMap.get(userMoves.get(i));
			
			//This will need changing! Only temporary.
			pps[i] = this.currentFirst.getPPFromMoveId(userMoves.get(i));
			//This will need changing! Only temporary.
			
			maxPPs[i] = ID_TO_PP_MAP.get(userMoves.get(i));
		}
		
		this.attackMenu = new Menu(attackOptions, someColors, moreColors, pps, maxPPs, MENU_X_POS, MENU_Y_POS, true);
	}
	
	public void makeDiscardMenu() {
		this.enemyCommentary = "Choose an Organelle to Discard.";
		String[] myPokemon = this.firstTrainer.getPokemonNames();
		int[] pps = new int[this.firstTrainer.getPokemonAmount()];
		int [] maxPPs = new int[this.firstTrainer.getPokemonAmount()];
		String[] pokemonTypes = this.firstTrainer.getPokemonTypes();
		Color[] selectedPokemonColors = new Color[this.firstTrainer.getPokemonAmount()];
		Color[] unselectedPokemonColors = new Color[this.firstTrainer.getPokemonAmount()];
		for (int i = 0; i < selectedPokemonColors.length; i++) {
			pps[i] = this.firstTrainer.getNotFainedPokemon(i).getHp();
			maxPPs[i] = this.firstTrainer.getNotFainedPokemon(i).getMaxHp();
			selectedPokemonColors[i] = getSelectedColor(pokemonTypes[i]);
			unselectedPokemonColors[i] = getUnselectedColor(pokemonTypes[i]);
		}
		this.discardMenu = new Menu(myPokemon, unselectedPokemonColors, selectedPokemonColors, pps, maxPPs, 1300, 50, false);
	}
	
	public void makeLearnMenu() {
		this.updateAttackMenu();
		this.learnMoveMenu = this.attackMenu.cloneWithoutBack();
	}
	
	public int takeOpponentTurn(int moveAgainst) {
		this.currentMenu = this.blankMenu;
		int opponentSelected = (int)(Math.random() * this.currentSecond.getMoveAmount());
		int oppMoveUsed = this.opponentMoves.get(opponentSelected);
		int oppStatusNum = this.idToStatusIncreaseMap.get(oppMoveUsed);
		System.out.println("oppStatusNum: " + oppStatusNum);
		if (oppStatusNum % 17 == 0) {
			oppMoveUsed = moveAgainst;
		}
		int damageToFirst = this.calculateDamage(2, oppMoveUsed);
		String actualMoveUsed = this.idToNameMap.get(oppMoveUsed);
		if (damageToFirst > this.currentFirst.getHp()) {
			this.damageToFirst = this.currentFirst.getHp();
		}
		String always = "Opponent just used " + actualMoveUsed;
		String statusCommentary = this.doStatusThings(oppMoveUsed, 1);
		if (damageToFirst != 0) {
			this.damageToFirst = damageToFirst;
			this.enemyCommentary = always + " which did " + damageToFirst + " damage!";		
		} else  if (statusCommentary != ""){
			this.enemyCommentary = always + statusCommentary;
		} else {
			this.enemyCommentary = always + " which had no effect!";
		}
		return oppMoveUsed;
//		if (this.userCommentary.contains("missed")) {
//			return;
//		}
//		if (!this.userCommentary.substring(0, 2).equals("Go") && this.justChanged) {
//			this.userCommentary = this.enemyCommentary;
//			this.enemyCommentary = "";
//			this.justChanged = false;
//		}
	}
	
	public void handleFaintFirst() {
		this.currentFirst.wipeMultipliers();
		this.damageToSecond = 0;
		if (this.firstTrainer.getHealthyPokemon() == null) {
			this.endBattle(false);
		} else {
			this.updateChangeMenu();
			this.currentMenu = this.changeMenu;
			this.update();			
		}
	}
	
	public void handleFaintSecond() {
		double a = 1;
		if (!this.secondTrainer.isWild()) {
			a = 1.5;
		}
		boolean[] shouldLevelUp = this.firstTrainer.handleXPGain(this.currentSecond.getName(), this.currentSecond.getLevel(), a);
		for (int i = 0; i < shouldLevelUp.length; i++) {
			if (shouldLevelUp[i]) {
				System.out.println("Pokemon Number " + i + " should level up!");
				Menu m = this.getLearnMenu(i);
				if (m != null) {
					this.deckMenusToIndexMap.put(m, i);
					System.out.println("I have stored a menu!");
				}
			}
		}
		this.damageToFirst = 0;
		this.updateChangeMenu();
		if (this.secondTrainer.getHealthyPokemon() != null) {
			this.donkeyKong = true;
			this.handleNextMenu(this.changeMenu);
			this.updateCurrentPokemon();
			this.userCommentary = "Your well-matched adversary sent out " + this.secondTrainer.getHealthyPokemon().getName() + "!";
			this.enemyCommentary = "Would you like to change your Organelle?";
		}
		else {
			this.endBattle(true);
			this.handleNextMenu(this.continueMenu);
		}
		return;
		
	}
	
	public void updateCurrentPokemon() {
		this.currentFirst = this.firstTrainer.getCurrentPokemon();
		this.currentSecond = this.secondTrainer.getCurrentPokemon();
		this.firstColor = getUnselectedColor(this.firstTrainer.getCurrentPokemon().getType());
		this.secondColor = getUnselectedColor(this.secondTrainer.getCurrentPokemon().getType());
		this.userMoves = this.currentFirst.getMoves();
		this.opponentMoves = this.currentSecond.getMoves();
	}
	
	public Menu getLearnMenu(int index) {
		
		Pokemon learning = this.firstTrainer.getNotFainedPokemon(index);
		System.out.println("In getLearnMenu");
		if (learning != null) {
			System.out.println("Learning does exist!");
			int currentIndex = this.firstTrainer.currentPokemonIndex();
			this.firstTrainer.setCurrentPokemon(index);
			this.updateAttackMenu();
			Menu toReturn = this.attackMenu.cloneWithoutBack();
			this.firstTrainer.setCurrentPokemon(currentIndex);
			this.updateAttackMenu();
			return toReturn;
		}
		System.out.println("getLearnMenu returning null.");
		return null;
	}
	
	public void backToStart() {
		this.currentMenu = this.mainMenu;
	}
	
	public void handleNextMenu(Menu upNext) {
		if (this.deckMenusToIndexMap.keySet().size() > 0) {
			this.afterLearnMenu = upNext;
			for (Menu m: this.deckMenusToIndexMap.keySet()) {
				this.currentMenu = m;
				Pokemon pokemonOfMenu = this.firstTrainer.getPokemon(this.deckMenusToIndexMap.get(m));
				String moveToLearn = this.idToNameMap.get(pokemonOfMenu.getMoves().get(pokemonOfMenu.getMoves().size() - 1));
				this.userCommentary = pokemonOfMenu.getName() + " wants to learn " + moveToLearn;
				this.enemyCommentary = "Which move would you like to discard?";
				this.indexForLearn = this.deckMenusToIndexMap.get(m);
				System.out.println("I am removing a menu from the deck!");
				this.deckMenusToIndexMap.remove(m);
				break;
			}
		} else {
			this.currentMenu = upNext;
		}
	}
	
	
}
