package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * This is an individual sector of the map. One sector of the map is shown at
 * one time.
 * 
 * This Sector has access to trainers (including userTrainer), mapEnemies, etc.
 * This will pass in information to the MapUserTrainer, which may pass
 * information back.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Sector {
	public final static int FULLSCREEN_WIDTH = 1920;
	public final static int FULLSCREEN_HEIGHT = 1080;
	private final static int LOADZONE_THICKNESS = 15;
	private static final int SPACE_WIDTH = 105;
	private static final int SPACE_HEIGHT = 105;
	public final static String[] ANIMAL_ORGANELLES = { "Cytoskeleton", "Fightoskeleton", "Centrosome", "Doug Dimmadome",
			"Lysosome", "Lyster", "Peroxisome", "Smooth ER", "Groove ER", "Vacuole", "Cell Membrane", "Plasma Membrane",
			"Cytoplasm", "Cytoblast'em", "Golgi Body", "Golgi Apparatus", "Mitochondria", "Mightychondria", "Flagellum",
			"Snek", "Ribosome", "Vibeosome", "Rough ER", "Buff ER" };
	public final static String[] PLANT_ORGANELLES = { "Cytoskeleton", "Fightoskeleton", "Cell Wall", "Cell Barricade",
			"Big Vacuole", "Peroxisome", "Smooth ER", "Groove ER", "Vacuole", "Cell Membrane", "Plasma Membrane",
			"Cytoplasm", "Cytoblast'em", "Chloroplast", "Chloroblast", "Golgi Body", "Golgi Apparatus", "Mitochondria",
			"Mightychondria", "Ribosome", "Vibeosome", "Rough ER", "Buff ER" };
	public final static String[] BACTERIA_ORGANELLES = { "Cell Wall", "Cell Barricade", "Mesosome", "Mesostrong",
			"Cell Membrane", "Plasma Membrane", "Cytoplasm", "Cytoblast'em", "Pilus", "Cilia", "Flagellum", "Snek",
			"Ribosome", "Vibeosome", "Capsule", "Capsoul", "Plasmid", "Plassid", "Placid", "Plaswid", "Plastid",
			"Plasrid", "Plaspid", "Shuffle" };
	public final static String[] ARCHAEA_ORGANELLES = { "R. Cell Wall", "R. Cell Barricade", "R. Mesosome",
			"R. Mesostrong", "R. Cell Membrane", "R. Plasma Membrane", "R. Cytoplasm", "R. Cytoblast'em", "R. Pilus",
			"R. Cilia", "R. Flagellum", "R. Snek", "R. Ribosome", "R. Vibeosome" };
	public final static String[] FUNGI_ORGANELLES = { "Cytoskeleton", "Fightoskeleton", "Peroxisome", "Smooth ER",
			"Groove ER", "Vacuole", "Cell Membrane", "Plasma Membrane", "Cytoplasm", "Cytoblast'em", "Golgi Body",
			"Golgi Apparatus", "Mitochondria", "Mightychondria", "Ribosome", "Vibeosome", "Rough ER", "Buff ER" };

	private ArrayList<Rectangle2D.Double> grassBoxes;
	private MapUserTrainer mapUser;
	private Space[][] spaces;
	private ArrayList<BattleTrainer> trainers;
	private ArrayList<MapEnemy> enemies;
	private HashMap<Projectile, MapEnemy> projectileToEnemyMap;
	private int width;
	private int height;
	private HashMap<String, Rectangle2D.Double> directionToLoadzoneMap;
	private ArrayList<MapEnemy> enemigos;
	private int numEnemies;
	private ArrayList<String> grassOrganelles;
	private int possibleLevelMin;
	private int possibleLevelMax;
	private Random r;
	private int pokeCount;
	private boolean shouldTransition;
	private DropBox myDropBox;

	public Sector(MapUserTrainer guy, FileReader spaceTypes) {
		this.mapUser = guy;
		this.shouldTransition = false;
		this.spaces = new Space[18][10];
		this.numEnemies = 0;
		this.grassOrganelles = new ArrayList<String>();
		this.r = new Random();
		this.myDropBox = new DropBox();
		try {
			Scanner scan = new Scanner(spaceTypes);
			char nextType;
			String currentRowTypes;
			int nextX;
			int nextY;
			int nextWidth = SPACE_WIDTH;
			int nextHeight = SPACE_HEIGHT;
			for (int col = 0; col < spaces[0].length; col++) {
				currentRowTypes = scan.nextLine();
				for (int row = 0; row < spaces.length; row++) {
					nextType = currentRowTypes.charAt(row);
					nextX = LOADZONE_THICKNESS + row * SPACE_WIDTH;
					nextY = LOADZONE_THICKNESS + col * SPACE_HEIGHT;
					if (nextType == 'I') {
						this.spaces[row][col] = new ItemSpace(nextType,
								new Rectangle2D.Double(nextX, nextY, nextWidth, nextHeight));
					} else if (nextType == 'X') {
						this.spaces[row][col] = new DropBox(nextType,
								new Rectangle2D.Double(nextX, nextY, nextWidth, nextHeight));
						this.myDropBox = (DropBox)this.spaces[row][col];
					} else {
						this.spaces[row][col] = new Space(nextType,
								new Rectangle2D.Double(nextX, nextY, nextWidth, nextHeight));
					}
				}
			}
			if (scan.hasNextInt()) {
				this.numEnemies = scan.nextInt();
				scan.nextLine();
			}
			String myType = "";
			if (scan.hasNextLine()) {
				myType = scan.nextLine();
			}
			if (scan.hasNextInt()) {
				this.possibleLevelMin = scan.nextInt();
			} else {
				this.possibleLevelMin = 0;
			}
			if (scan.hasNextInt()) {
				this.possibleLevelMax = scan.nextInt();
				scan.nextLine();
			} else {
				this.possibleLevelMax = 1;
			}
			if (scan.hasNextInt()) {
				this.pokeCount = scan.nextInt();
				scan.nextLine();
			} else {
				this.pokeCount = -1;
			}
			while (scan.hasNextLine()) {
				this.grassOrganelles.add(scan.nextLine());
			}
			if (this.grassOrganelles.size() == 0) {
				this.grassOrganelles.add("Ribosome");
			}
			this.enemigos = new ArrayList<MapEnemy>();
			int currentX;
			int currentY;
			Random r = new Random();
			for (int i = 0; i < this.numEnemies; i++) {
				currentX = r.nextInt(1920);
				currentY = r.nextInt(1080);
				this.enemigos.add(new MapEnemy(currentX, currentY, (i * 20) % 50, myType, this.possibleLevelMin,
						this.possibleLevelMax, this.pokeCount));
			}
			scan.close();
		}

		finally {
			this.width = FULLSCREEN_WIDTH;
			this.height = FULLSCREEN_HEIGHT;
			this.directionToLoadzoneMap = new HashMap<String, Rectangle2D.Double>();
			this.directionToLoadzoneMap.put("NORTH", new Rectangle2D.Double(0, 0, this.width, LOADZONE_THICKNESS));
			this.directionToLoadzoneMap.put("EAST",
					new Rectangle2D.Double(this.width - LOADZONE_THICKNESS, 0, LOADZONE_THICKNESS, this.height));
			this.directionToLoadzoneMap.put("SOUTH",
					new Rectangle2D.Double(0, this.height - LOADZONE_THICKNESS, this.width, LOADZONE_THICKNESS));
			this.directionToLoadzoneMap.put("WEST", new Rectangle2D.Double(0, 0, LOADZONE_THICKNESS, this.height));
		}
	}

	public void addProjectileBox(Rectangle2D.Double hitbox) {
	}

	public void drawOn(Graphics2D g) {
		g.setColor(Color.BLACK);
		Rectangle2D rect1 = this.directionToLoadzoneMap.get("NORTH");
		Rectangle2D rect2 = this.directionToLoadzoneMap.get("EAST");
		Rectangle2D rect3 = this.directionToLoadzoneMap.get("SOUTH");
		Rectangle2D rect4 = this.directionToLoadzoneMap.get("WEST");
		g.fill(rect1);
		g.fill(rect2);
		g.fill(rect3);
		g.fill(rect4);
		for (Space[] row : spaces) {
			for (Space s : row) {
				s.drawOn(g);
			}
		}
		this.mapUser.drawOn(g);
		for (MapEnemy enemigo : enemigos) {
			enemigo.drawOn(g);
		}
	}

	public void testGrassBoxes() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].isGrass()) {
					int randomIndex = r.nextInt(this.grassOrganelles.size());
					int randomLevel = r.nextInt(this.possibleLevelMax - this.possibleLevelMin) + this.possibleLevelMin;
					this.mapUser.inGrassBox(this.spaces[row][col].getHitbox(),
							new BattleWildPokemon(this.grassOrganelles.get(randomIndex), randomLevel));
				}
			}
		}
	}

	public void testTransitionBoxes() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].isTransition()) {
					if (this.mapUser.inTransitionBox(this.spaces[row][col].getHitbox())) {
						this.shouldTransition = true;
					}
				}
			}
		}
	}

	public void testNucleusBoxes() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].isNucleusSpace()) {
					this.mapUser.inNucleus(this.spaces[row][col].getHitbox());
				}
			}
		}
	}

	public void tryGrabItems() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].hasObject()) {
					if (this.mapUser.tryGrab(((ItemSpace) this.spaces[row][col]).getItemHitbox())) {
						((ItemSpace) this.spaces[row][col]).grabItem();
					}
				}
			}
		}
	}

	public void tryDropItems() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].wantsItem()) {
					if (this.mapUser.tryDeposit((this.spaces[row][col]).getHitbox())) {
						((DropBox) this.spaces[row][col]).addItem();
					}
				}
			}
		}
	}

	public void testEnemyBoxes() {
		//do a funny dance
		for (int i = enemigos.size() - 1; i >= 0; i--) {
			if (this.mapUser.inEnemyBoxes(this.enemigos.get(i).getAllHitboxes(),
					this.enemigos.get(i).getBattleEnemy())) {
				this.enemigos.remove(i);
			}
		}
	}

	public void testBoundaryBoxes() {
		for (int row = 0; row < this.spaces.length; row++) {
			for (int col = 0; col < this.spaces[0].length; col++) {
				if (this.spaces[row][col].isBoundarySpace()) {
					this.mapUser.inBoundaryBox(this.spaces[row][col].getHitbox());
					if (this.spaces[row][col].isBuildingSpace()) {
						for (MapEnemy e : this.enemigos) {
							e.testBuildingBox(this.spaces[row][col].getHitbox());
							e.inBoundaryBox(this.spaces[row][col].getHitbox());
						}
					}
				}
			}
		}
	}

	public void update() {
		this.mapUser.updatePosition();
		for (MapEnemy enemigo : this.enemigos) {
			enemigo.updatePosition(this.mapUser.getUserX(), this.mapUser.getUserY());
		}
		this.testEnemyBoxes();
		this.testBoundaryBoxes();
		this.testGrassBoxes();
		this.testNucleusBoxes();
		this.tryGrabItems();
		this.tryDropItems();
		this.testTransitionBoxes();
	}

	public void clearProjectiles() {
		for (MapEnemy e : this.enemigos) {
			e.clearProjectiles();
		}
	}

	protected Space[][] getSpaces() {
		return this.spaces;
	}
	
	//resets shouldTransition after it transitions.
	public boolean getShouldTransition() {
		if (this.shouldTransition) {
			this.shouldTransition = false;
			return true;
		}
		return false;
	}
	
	protected void flagTransition() {
		this.shouldTransition = true;
	}
	
	protected boolean isDropBoxFull() {
		return this.myDropBox.isComplete();
	}
	
	protected MapUserTrainer getUserTrainer() {
		return this.mapUser;
	}
}
