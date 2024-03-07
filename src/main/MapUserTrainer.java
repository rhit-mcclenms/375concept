package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import javax.swing.JTextField;

/**
 * This class determines the user's motion while the user is traveling on the
 * map. It can encounter Pokemon, dodge projectiles and enemies, etc. While the
 * user is on the map, it does not need to know about Pokemon, as it really only
 * needs to know about them when in battle.
 * 
 * This class can initialize battles.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class MapUserTrainer {

	private final static int SPRITE_WIDTH = 50;
	private final static int SPRITE_HEIGHT = 50;
	private final static int DX = OrganelleMain.UNIVERSAL_DX;
	private final static int BORDER_THICKNESS = 10;
	private final static int TRANSITION_UP = 40 + BORDER_THICKNESS;
	private final static int TRANSITION_DOWN = 1040 - BORDER_THICKNESS;
	private final static int TRANSITION_LEFT = 40 + BORDER_THICKNESS;
	private final static int TRANSITION_RIGHT = 1880 - BORDER_THICKNESS;
	private final static int BOUNDARY_THICKNESS = OrganelleMain.UNIVERSAL_DX;
	private final static double GRASS_ENCOUNTER_CHANCE = 0.004;
	private final static Color ITEM_COLOR = new Color(255, 250, 112);
	private double centerX, centerY;
	private boolean hasItem;
	private Rectangle2D.Double hitbox;
	private Rectangle2D.Double leftBoundary;
	private Rectangle2D.Double rightBoundary;
	private Rectangle2D.Double topBoundary;
	private Rectangle2D.Double bottomBoundary;
	private Rectangle2D.Double northBorder;
	private Rectangle2D.Double southBorder;
	private Rectangle2D.Double eastBorder;
	private Rectangle2D.Double westBorder;
	private Rectangle2D.Double possibleItemHitbox;
	private int transitionId;
	private double vX;
	private double vY;
	private boolean goingUp;
	private boolean goingLeft;
	private boolean goingRight;
	private boolean goingDown;
	private BattleUserTrainer userBattleTrainer;
	private BattleTrainer opponent;
	private double storedPosX;
	private double storedPosY;

	private int minVX;
	private int minVY;
	private int maxVX;
	private int maxVY;

	public MapUserTrainer(int x, int y) {
		this.centerX = x;
		this.centerY = y;
		this.storedPosX = 0;
		this.storedPosY = 0;
		this.hasItem = false;
		this.vX = 0;
		this.vY = 0;
		this.transitionId = 0;
		this.resetMinMaxVelocity();
		this.userBattleTrainer = new BattleUserTrainer();
		this.opponent = null;
		this.resetHitboxes();
		this.westBorder = new Rectangle2D.Double(0, 0, 15, 1080);
		this.eastBorder = new Rectangle2D.Double(1905, 0, 15, 1080);
		this.northBorder = new Rectangle2D.Double(0, 0, 1920, 15);
		this.southBorder = new Rectangle2D.Double(0, 1065, 1920, 15);
	}

	public void updatePosition() {
		this.vX = 0;
		this.vY = 0;
		if (!this.inBorderBox(westBorder)) {
			if (!this.inBorderBox(eastBorder)) {
				if (!this.inBorderBox(northBorder)) {
					this.inBorderBox(southBorder);
				}
			}
		}
		if (this.goingUp) {
			if (this.vY - 1 >= this.minVY) {
				this.vY--;
			}
		}
		if (this.goingLeft) {
			if (this.vX - 1 >= this.minVX) {
				this.vX--;
			}
		}
		if (this.goingRight) {
			if (this.vX + 1 <= this.maxVX) {
				this.vX++;
			}
		}
		if (this.goingDown) {
			if (this.vY + 1 <= this.maxVY) {
				this.vY++;
			}
		}
		this.resetMinMaxVelocity();
		this.normalizeVelocity();
		this.centerX += this.vX * DX;
		this.centerY += this.vY * DX;
		this.resetHitboxes();
	}

	public void normalizeVelocity() {
		double distance = 0;
		distance += Math.sqrt(Math.pow(this.vX, 2) + Math.pow(this.vY, 2));
		if (distance != 0) {
			this.vX /= distance;
			this.vY /= distance;
		}
	}

	public void updatePosition(double x, double y) {
		this.centerX = x;
		this.centerY = y;
	}

	public void drawOn(Graphics2D g) {
		g.setColor(Color.RED);
		g.fill(this.hitbox);
		if (this.hasItem) {
			g.setColor(ITEM_COLOR);
			g.fillOval((int) this.possibleItemHitbox.getX(), (int) this.possibleItemHitbox.getY(),
					(int) this.possibleItemHitbox.getWidth(), (int) this.possibleItemHitbox.getHeight());
		}
	}

	public void takeUserInput(String keyId) {
		this.updatePosition();
	}

	public boolean isInGrass() {
		return false;
	}

	public void setInGrass() {
	}

	public void inBoundaryBox(Rectangle2D.Double hitbox) {
		if (this.leftBoundary.intersects(hitbox)) {
			this.minVX = 0;
		}
		if (this.rightBoundary.intersects(hitbox)) {
			this.maxVX = 0;
		}
		if (this.topBoundary.intersects(hitbox)) {
			this.minVY = 0;
		}
		if (this.bottomBoundary.intersects(hitbox)) {
			this.maxVY = 0;
		}
	}

	public void inGrassBox(Rectangle2D.Double hitbox, BattleWildPokemon wildPokemon) {
		if (this.isMoving() && this.hitbox.intersects(hitbox) && Math.random() < GRASS_ENCOUNTER_CHANCE) {
			this.opponent = wildPokemon;
		}

	}

	public boolean inTransitionBox(Rectangle2D.Double hitbox) {
		return this.hitbox.intersects(hitbox);
	}

	public boolean inEnemyBoxes(ArrayList<Rectangle2D.Double> hitboxes, BattleEnemy possibleOpponent) {
		for (int i = 0; i < hitboxes.size(); i++) {
			if (this.hitbox.intersects(hitboxes.get(i))) {
				this.opponent = possibleOpponent;
				return true;
			}
		}
		return false;
	}

	public void inNucleus(Rectangle2D.Double hitbox) {
		if (this.hitbox.intersects(hitbox)) {
			this.userBattleTrainer.maxHealAll();
			this.userBattleTrainer.replenishPP();
			this.userBattleTrainer.resetVesicles();
		}
	}

	public boolean tryGrab(Rectangle2D.Double itemHitbox) {
		if (!this.hasItem && this.hitbox.intersects(itemHitbox)) {
			this.hasItem = true;
			return true;
		}
		return false;
	}

	public boolean tryDeposit(Rectangle2D.Double dropBox) {
		if (this.hasItem && this.hitbox.intersects(dropBox)) {
			this.hasItem = false;
			return true;
		}
		return false;
	}

	public boolean inBorderBox(Rectangle2D.Double hitbox) {
		boolean replaced = false;
		this.transitionId = 0;
		if (this.leftBoundary.intersects(hitbox)) {
			this.minVX = 0;
			this.transitionId = 1;
			replaced = true;
		}
		if (this.rightBoundary.intersects(hitbox)) {
			this.maxVX = 0;
			this.transitionId = 2;
			replaced = true;
		}
		if (this.topBoundary.intersects(hitbox)) {
			this.minVY = 0;
			this.transitionId = 3;
			replaced = true;
		}
		if (this.bottomBoundary.intersects(hitbox)) {
			this.maxVY = 0;
			this.transitionId = 4;
			replaced = true;
		}
		return replaced;
	}

	public void acceptTransitionRequest(int transitionId) {
		if (transitionId == 1) {
			this.centerX = TRANSITION_RIGHT;
		}
		if (transitionId == 2) {
			this.centerX = TRANSITION_LEFT;
		}
		if (transitionId == 3) {
			this.centerY = TRANSITION_DOWN;
		}
		if (transitionId == 4) {
			this.centerY = TRANSITION_UP;
		}
		this.resetMinMaxVelocity();
		this.transitionId = 0;
	}

	public void setGoingUp(boolean b) {
		this.goingUp = b;
	}

	public void setGoingLeft(boolean b) {
		this.goingLeft = b;
	}

	public void setGoingRight(boolean b) {
		this.goingRight = b;
	}

	public void setGoingDown(boolean b) {
		this.goingDown = b;
	}

	public int getTransitionId() {
		return this.transitionId;
	}

	public void dontMove(int directionId) {
//Left
		if (directionId == 1) {
			this.minVX = 0;
			this.centerX = TRANSITION_LEFT;
		}
//Right
		else if (directionId == 2) {
			this.maxVX = 0;
			this.centerX = TRANSITION_RIGHT;
		}
//Up
		else if (directionId == 3) {
			this.minVY = 0;
			this.centerY = TRANSITION_UP;
		}
//Down
		else if (directionId == 4) {
			this.maxVY = 0;
			this.centerY = TRANSITION_DOWN;
		}
	}

	public void resetMinMaxVelocity() {
		this.minVX = -1;
		this.maxVX = 1;
		this.minVY = -1;
		this.maxVY = 1;
	}

	public BattleTrainer battleAgainst() {
		return this.opponent;
	}

	public BattleUserTrainer getBattleUser() {
		return this.userBattleTrainer;
	}

	public double getUserX() {
		return this.centerX;
	}

	public double getUserY() {
		return this.centerY;
	}

	public void resetOpponent() {
		this.opponent = null;
	}

	public void resetInput() {
		this.goingUp = false;
		this.goingRight = false;
		this.goingLeft = false;
		this.goingDown = false;
	}

	public void resetHitboxes() {
		this.hitbox = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2, this.centerY - SPRITE_HEIGHT / 2,
				SPRITE_WIDTH, SPRITE_HEIGHT);
		this.leftBoundary = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2 - BOUNDARY_THICKNESS,
				this.centerY - SPRITE_HEIGHT / 2, BOUNDARY_THICKNESS, SPRITE_HEIGHT);
		this.rightBoundary = new Rectangle2D.Double(this.centerX + SPRITE_WIDTH / 2, this.centerY - SPRITE_HEIGHT / 2,
				BOUNDARY_THICKNESS, SPRITE_HEIGHT);
		this.topBoundary = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2,
				this.centerY - SPRITE_HEIGHT / 2 - BOUNDARY_THICKNESS, SPRITE_WIDTH, BOUNDARY_THICKNESS);
		this.bottomBoundary = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2, this.centerY + SPRITE_HEIGHT / 2,
				SPRITE_WIDTH, BOUNDARY_THICKNESS);
		double newStartX = hitbox.getX() + (hitbox.getWidth() / 3);
		double newStartY = hitbox.getY() + (hitbox.getHeight() / 3);
		this.possibleItemHitbox = new Rectangle2D.Double(newStartX, newStartY, this.hitbox.getWidth() / 3,
				this.hitbox.getHeight() / 3);
	}

	public boolean isMoving() {
		if (this.vX == 0 && this.vY == 0) {
			return false;
		}
		return true;
	}
	
	public void giveOpponent(BattleEnemy opponent) {
		this.opponent = opponent;
	}
	
	public void storeCurrentPos() {
		this.storedPosX = this.centerX;
		this.storedPosY = this.centerY;
	}
	
	public void toStoredPos() {
		this.updatePosition(this.storedPosX, this.storedPosY);
	}

}
