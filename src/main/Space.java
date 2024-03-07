package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This class represents a space on each sector. It can draw itself, and it
 * knows whether or not it is grass. Grass spaces act differently than regular
 * spaces.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Space {

	private static final Color GRASS_COLOR = new Color(0, 135, 1);
	private static final Color WATER_COLOR = new Color(62, 95, 255);
	private static final Color DIRT_COLOR = new Color(143, 93, 66);
	private static final Color BUILDING_COLOR = new Color(158, 0, 0);
	private static final Color FOREST_COLOR = new Color(10, 92, 36);
	private static final Color NUCLEUS_COLOR = new Color(255, 250, 112);
	private static final Color TRANSITION_COLOR = new Color(37, 229, 232);

	private Rectangle2D.Double hitbox;
	private boolean isGrass;
	private boolean isTransition;
	private char type;
	private boolean isBoundarySpace;
	private boolean isBuildingSpace;
	private boolean isNucleus;
	private Color myColor;

	public Space(char type, Rectangle2D.Double hitbox) {
		this.type = type;
		this.hitbox = hitbox;
		this.isNucleus = false;
		this.isGrass = false;
		this.isTransition = false;
		this.isBoundarySpace = false;
		this.isBuildingSpace = false;
		if (this.type == 'G') {
			this.myColor = GRASS_COLOR;
			this.isGrass = true;
		} else if (this.type == 'D') {
			this.myColor = DIRT_COLOR;
		} else if (this.type == 'T') {
			this.myColor = TRANSITION_COLOR;
			this.isTransition = true;
		} else if (this.type == 'W') {
			this.myColor = WATER_COLOR;
			this.isBoundarySpace = true;
		} else if (this.type == 'B') {
			this.myColor = BUILDING_COLOR;
			this.isBoundarySpace = true;
			this.isBuildingSpace = true;
		} else if (this.type == 'F') {
			this.myColor = FOREST_COLOR;
			this.isBoundarySpace = true;
			this.isBuildingSpace = true;
		} else if (this.type == 'N') {
			this.myColor = NUCLEUS_COLOR;
			this.isNucleus = true;
		} else {
			this.myColor = new Color(0, 255, 0);
		}
	}

	public void drawOn(Graphics2D g) {

		g.setColor(myColor);
		g.fill(this.hitbox);
	}

	public boolean isGrass() {
		return this.isGrass;
	}

	public boolean isTransition() {
		return this.isTransition;
	}

	public Rectangle2D.Double getHitbox() {
		return this.hitbox;
	}

	public boolean isBoundarySpace() {
		return this.isBoundarySpace;
	}

	public boolean isBuildingSpace() {
		return this.isBuildingSpace;
	}

	public boolean isNucleusSpace() {
		return this.isNucleus;
	}

	public boolean hasObject() {
		return false;
	}

	public boolean wantsItem() {
		return false;
	}

	public char getType() {
		return this.type;
	}

	public void setColor(Color newColor) {
		this.myColor = newColor;
	}
}
