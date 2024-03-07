package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Random;

/**
 * This represents an enemy on the map. It represents a BattleEnemy, but it acts
 * much differently when on the map. The MapEnemy can fire Projectiles at
 * different speeds and at different frequencies. Their movement is somewhat
 * intelligent.
 * 
 * There are many enemies in a gym, and we'll figure out gym stuff later.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class MapEnemy {

	private final static int SPRITE_WIDTH = 50;
	private final static int SPRITE_HEIGHT = 50;
	private final static int DX = OrganelleMain.UNIVERSAL_DX;
	private final static int BOUNCE_DX = DX / 2;
	public final static int FULLSCREEN_WIDTH = 1920;
	public final static int FULLSCREEN_HEIGHT = 1080;
	private final static int BORDER_THICKNESS = 10;
	private final static int MAX_PROJECTILE_FREQUENCY = 10 * OrganelleMain.UNIVERSAL_DX;
	private final static int BOUNDARY_THICKNESS = OrganelleMain.UNIVERSAL_DX;
	private final static int ENEMY_ACCURACY = 20;
	private final static Color TYPE_0_COLOR = new Color(19, 96, 212);
	private final static Color TYPE_1_COLOR = new Color(255, 121, 21);
	private final static Color TYPE_2_COLOR = new Color(140, 46, 171);
	private final static Color TYPE_3_COLOR = new Color(219, 217, 66);
	
	private double centerX, centerY, vX, vY;
	private int projectileFrequency;
	private Rectangle2D.Double hitbox;
	private Rectangle2D.Double destinationBox;
	private Rectangle2D.Double topBox, bottomBox, leftBox, rightBox;
	private boolean hasReachedDestination;
	private ArrayList<Projectile> projectiles;
	private BattleEnemy battleEnemy;
	private int enemyType;
	private Random r;
	private int bounceCooldown;
	private int currentDx;
	private Color color;
	private int pokeCount;
	private double minVX, maxVX, minVY, maxVY;


	public MapEnemy(double x, double y, int projectileFrequency, String battleType, int battleLevelMin,
			int battleLevelMax, int pokeCount) {

		this.centerX = x;
		this.centerY = y;
		this.hasReachedDestination = false;
		this.pokeCount = pokeCount;
		this.findNewDestination();
		this.resetHitboxes();
		this.projectiles = new ArrayList<Projectile>();
		this.projectileFrequency = projectileFrequency;
		this.battleEnemy = new BattleEnemy(battleType, battleLevelMin, battleLevelMax, this.pokeCount);
		this.r = new Random();
		this.enemyType = r.nextInt(4);
		this.bounceCooldown = 0;
		this.currentDx = DX;
		if (this.enemyType == 0) {
			this.color = TYPE_0_COLOR;
		} else if (this.enemyType == 1) {
			this.color = TYPE_1_COLOR;
		} else if (this.enemyType == 2) {
			this.color = TYPE_2_COLOR;
		} else {
			this.color = TYPE_3_COLOR;
		}
		this.resetMinMaxVelocity();

	}

	public void shoot(double userX, double userY) {
		double distanceFromUser = this.distanceFromPoint(userX, userY);
		userX += this.r.nextInt((int) (distanceFromUser / 10));
		userX -= distanceFromUser / ENEMY_ACCURACY;
		userY += this.r.nextInt((int) (this.distanceFromPoint(userX, userY) / 10));
		userY -= distanceFromUser / ENEMY_ACCURACY;
		if (this.enemyType == 0) {
			Projectile p = new Projectile(this.centerX, this.centerY, userX, userY, true);
			this.projectiles.add(p);
		} else if (this.enemyType == 1){
			Projectile p1 = new Projectile(this.centerX, this.centerY, this.centerX + 1, this.centerY, false);
			Projectile p2 = new Projectile(this.centerX, this.centerY, this.centerX + 1, this.centerY + 1, false);
			Projectile p3 = new Projectile(this.centerX, this.centerY, this.centerX, this.centerY + 1, false);
			Projectile p4 = new Projectile(this.centerX, this.centerY, this.centerX - 1, this.centerY, false);
			Projectile p5 = new Projectile(this.centerX, this.centerY, this.centerX - 1, this.centerY + 1, false);
			Projectile p6 = new Projectile(this.centerX, this.centerY, this.centerX, this.centerY - 1, false);
			Projectile p7 = new Projectile(this.centerX, this.centerY, this.centerX - 1, this.centerY - 1, false);
			Projectile p8 = new Projectile(this.centerX, this.centerY, this.centerX + 1, this.centerY - 1, false);
			this.projectiles.add(p1);
			this.projectiles.add(p2);
			this.projectiles.add(p3);
			this.projectiles.add(p4);
			this.projectiles.add(p5);
			this.projectiles.add(p6);
			this.projectiles.add(p7);
			this.projectiles.add(p8);
		} else if (this.enemyType == 2) {
			Projectile p1 = new Projectile(this.centerX, this.centerY, userX, userY, false);
			Projectile p2 = new Projectile(this.centerX, this.centerY, userX, userY + 200, false);
			Projectile p3 = new Projectile(this.centerX, this.centerY, userX, userY - 200, false);
			this.projectiles.add(p1);
			this.projectiles.add(p2);
			this.projectiles.add(p3);
		} else {
			Projectile p1 = new Projectile(this.centerX, this.centerY, this.centerX + 1, this.centerY + 1, true);
			Projectile p2 = new Projectile(this.centerX, this.centerY, this.centerX - 1, this.centerY + 1, true);
			Projectile p3 = new Projectile(this.centerX, this.centerY, this.centerX - 1, this.centerY - 1, true);
			Projectile p4 = new Projectile(this.centerX, this.centerY, this.centerX + 1, this.centerY - 1, true);
			this.projectiles.add(p1);
			this.projectiles.add(p2);
			this.projectiles.add(p3);
			this.projectiles.add(p4);
		}
		
		

	}

	public void updatePosition(double userX, double userY) {

		if (this.isOutOfBounds()) {
			this.teleport();
		}
		
		if (this.bounceCooldown > 0) {
			this.bounceCooldown--;
		}
		for (Projectile p : this.projectiles) {
			p.updatePosition();
		}
		if (this.projectileFrequency >= MAX_PROJECTILE_FREQUENCY) {
			this.projectileFrequency = 0;
			this.shoot(userX, userY);
		}
		if (this.hasReachedDestination) {

			this.hasReachedDestination = false;
			this.findNewDestination();

		}
		if (this.vX > this.maxVX) {
			this.vX = this.maxVX;
		}
		if (this.vX < this.minVX) {
			this.vX = this.minVX;
		}
		if (this.vY > this.maxVY) {
			this.vY = this.maxVY;
		}
		if (this.vY < this.minVY) {
			this.vY = this.minVY;
		}
		this.resetMinMaxVelocity();

		this.centerX += this.vX * this.currentDx;
		this.centerY += this.vY * this.currentDx;
		this.resetHitboxes();
		if (this.hitbox.intersects(this.destinationBox)) {
			this.hasReachedDestination = true;
		}
		this.projectileFrequency++;

	}

	private boolean isOutOfBounds() {
		
		if (this.centerX - SPRITE_WIDTH / 2 > FULLSCREEN_WIDTH || this.centerX + SPRITE_WIDTH / 2 < 0) {
			return true;
		}
		if (this.centerY - SPRITE_WIDTH / 2 > FULLSCREEN_HEIGHT || this.centerY + SPRITE_WIDTH / 2 < 0) {
			return true;
		}
		
		return false;
	}

	public void findNewDestination() {

		this.currentDx = DX;
		Random r = new Random();
		int x = r.nextInt(FULLSCREEN_WIDTH - 2 * (BORDER_THICKNESS + SPRITE_WIDTH / 2)) + BORDER_THICKNESS
				+ SPRITE_WIDTH / 2;
		int y = r.nextInt(FULLSCREEN_HEIGHT - 2 * (BORDER_THICKNESS + SPRITE_HEIGHT / 2)) + BORDER_THICKNESS
				+ SPRITE_HEIGHT / 2;
		this.destinationBox = new Rectangle2D.Double(x - SPRITE_WIDTH / 2, y - SPRITE_HEIGHT / 2, SPRITE_WIDTH,
				SPRITE_HEIGHT);
		// this.vX = x - this.centerX;
		// this.vY = y - this.centerY;
		normalizeVelocity();

	}

	public void drawOn(Graphics2D g) {
		g.setColor(this.color);

		g.fillRect((int) (this.centerX - SPRITE_WIDTH / 2), (int) (this.centerY - SPRITE_HEIGHT / 2), SPRITE_WIDTH,
				SPRITE_HEIGHT);
		for (Projectile p : this.projectiles) {
			p.drawOn(g);
		}
//		g.setColor(Color.BLACK);
//		g.fill(this.destinationBox);

	}

	public void normalizeVelocity() {

		this.vX = this.destinationBox.getCenterX() - this.centerX;
		this.vY = this.destinationBox.getCenterY() - this.centerY;
		double distance = 0;
		distance += Math.sqrt(Math.pow(this.vX, 2) + Math.pow(this.vY, 2));

		if (distance != 0) {

			this.vX /= distance;
			this.vY /= distance;

		}

	}

	public void clearProjectiles() {
		this.projectiles = new ArrayList<Projectile>();
	}

	public ArrayList<Rectangle2D.Double> getAllHitboxes() {
		ArrayList<Rectangle2D.Double> hitboxes = new ArrayList<Rectangle2D.Double>();
		for (Projectile p : this.projectiles) {
			hitboxes.add(p.getHitbox());
		}
		hitboxes.add(this.hitbox);
		return hitboxes;
	}

	public BattleEnemy getBattleEnemy() {
		return this.battleEnemy;
	}

	public void testBuildingBox(Rectangle2D.Double hitbox) {
		for (int i = this.projectiles.size() - 1; i >= 0; i--) {
			if (this.projectiles.get(i).intersectsWith(hitbox)) {
				this.projectiles.remove(i);
			}
		}
	}

	public void bounce(String collisionDirection) {
		this.bounceCooldown = 5;
		this.currentDx = BOUNCE_DX;
		double newDestX = 0;
		double newDestY = 0;
		if (collisionDirection.equals("Up")) {
			//Go Down
			this.minVY = 0;
			newDestX = this.centerX + Math.random() * 2000 - 1000;
			newDestY = this.centerY + Math.random() * 700 + 300;
		} else if (collisionDirection.equals("Left")) {
			//Go Right
			this.minVX = 0;
			newDestX = this.centerX + Math.random() * 700 + 300;
			newDestY = this.centerY + Math.random() * 2000 - 1000;
		} else if (collisionDirection.equals("Right")) {
			//Go Left
			this.maxVX = 0;
			newDestX = this.centerX - Math.random() * 700 + 300;
			newDestY = this.centerY + Math.random() * 2000 - 1000;
		} else if (collisionDirection.equals("Down")) {
			//Go Up
			this.maxVY = 0;
			newDestX = this.centerX + Math.random() * 2000 - 1000;
			newDestY = this.centerY - Math.random() * 700 + 300;
		}
		this.destinationBox = new Rectangle2D.Double(newDestX - SPRITE_WIDTH / 2, newDestY - SPRITE_HEIGHT / 2,
				SPRITE_WIDTH, SPRITE_HEIGHT);
		this.normalizeDistance(150);
	}

	public void normalizeDistance(double desiredDistance) {
		double destX = this.destinationBox.getCenterX();
		double destY = this.destinationBox.getCenterY();
		double deltaX = destX - this.centerX;
		double deltaY = destY - this.centerY;
		double currentDistance = Math.sqrt(Math.pow(this.centerX - destX, 2) + Math.pow(this.centerY - destY, 2));
		double multiplier = desiredDistance / currentDistance;
		deltaX *= multiplier;
		deltaY *= multiplier;
		this.destinationBox = new Rectangle2D.Double(this.centerX + deltaX - SPRITE_WIDTH / 2,
				this.centerY + deltaY - SPRITE_HEIGHT / 2, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.normalizeVelocity();
	}

	public void resetHitboxes() {
		this.hitbox = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2, this.centerY - SPRITE_HEIGHT / 2,
				SPRITE_WIDTH, SPRITE_HEIGHT);
		this.leftBox = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2 - BOUNDARY_THICKNESS,
				this.centerY - SPRITE_HEIGHT / 2, BOUNDARY_THICKNESS, SPRITE_HEIGHT);
		this.rightBox = new Rectangle2D.Double(this.centerX + SPRITE_WIDTH / 2, this.centerY - SPRITE_HEIGHT / 2,
				BOUNDARY_THICKNESS, SPRITE_HEIGHT);
		this.topBox = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2,
				this.centerY - SPRITE_HEIGHT / 2 - BOUNDARY_THICKNESS, SPRITE_WIDTH, BOUNDARY_THICKNESS);
		this.bottomBox = new Rectangle2D.Double(this.centerX - SPRITE_WIDTH / 2, this.centerY + SPRITE_HEIGHT / 2,
				SPRITE_WIDTH, BOUNDARY_THICKNESS);
	}

	public void inBoundaryBox(Rectangle2D.Double hitbox) {
		if (this.bounceCooldown == 0) {
			int bounceCounter = 0;
			if (this.leftBox.intersects(hitbox)) {
				bounceCounter++;
				this.bounce("Left");
			}
			if (this.rightBox.intersects(hitbox)) {
				bounceCounter++;
				this.bounce("Right");
			}
			if (this.topBox.intersects(hitbox)) {
				bounceCounter++;
				this.bounce("Up");
			}
			if (this.bottomBox.intersects(hitbox)) {
				bounceCounter++;
				this.bounce("Down");
			}
			if (bounceCounter == 4) {
				this.teleport();
			}
		}
	}

	public double distanceFromPoint(double x, double y) {
		return Math.sqrt(Math.pow(x - this.centerX, 2) + Math.pow(y - this.centerY, 2));
	}
	
	public void resetMinMaxVelocity() {
		this.minVX = -1;
		this.minVY = -1;
		this.maxVX = 1;
		this.maxVY = 1;
	}

	public void teleport() {
		this.findNewDestination();
		this.centerX = this.destinationBox.getCenterX();
		this.centerY = this.destinationBox.getCenterY();
		this.resetMinMaxVelocity();
		this.findNewDestination();
		//this.hasReachedDestination = true;
	}
	
}
