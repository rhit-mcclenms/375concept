package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

/**
 * This represents a projectile fired by the enemy which can come into contact
 * with the userTrainer. It is fired at a direction, and it continues in said
 * direction until it hits something or until it leaves the room.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Projectile {

	private static final double PROJECTILE_RADIUS = 10;
	private final static int FAST_DX = 5 * OrganelleMain.UNIVERSAL_DX;
	private final static int SLOW_DX = (int)(3 * OrganelleMain.UNIVERSAL_DX);
	public final static int FULLSCREEN_WIDTH = 1920;
	public final static int FULLSCREEN_HEIGHT = 1080;
	private final static int BORDER_THICKNESS = 10;

	private double centerX, centerY, vX, vY;
	private Rectangle2D.Double hitbox;
	private Point destination;
	private int myDx;

	public Projectile(double startX, double startY, double targetX, double targetY, boolean isFast) {

		this.centerX = startX;
		this.centerY = startY;

		this.vX = targetX - startX;
		this.vY = targetY - startY;

		this.normalizeVelocity();

		this.hitbox = new Rectangle2D.Double(this.centerX, this.centerY, PROJECTILE_RADIUS, PROJECTILE_RADIUS);
		if (isFast) {
			this.myDx = FAST_DX;
		}
		else {
			this.myDx = SLOW_DX;
		}

	}

	public void drawOn(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillOval((int) this.centerX, (int) this.centerY, (int) PROJECTILE_RADIUS, (int) PROJECTILE_RADIUS);
	}

	public void updatePosition() {

		this.centerX += this.vX * this.myDx;
		this.centerY += this.vY * this.myDx;
		this.hitbox = new Rectangle2D.Double(this.centerX, this.centerY, PROJECTILE_RADIUS, PROJECTILE_RADIUS);

	}

	public void normalizeVelocity() {

		double distance = 0;
		distance += Math.sqrt(Math.pow(this.vX, 2) + Math.pow(this.vY, 2));

		if (distance != 0) {

			this.vX /= distance;
			this.vY /= distance;

		}

	}
	
	public Rectangle2D.Double getHitbox() {
		return this.hitbox;
	}
	
	public boolean intersectsWith(Rectangle2D.Double hitbox) {
		return this.hitbox.intersects(hitbox);
	}

}
