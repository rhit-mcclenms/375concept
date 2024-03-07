package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This is where the spaces are droppped.
 * 
 * @author bakerjl1
 *
 */
public class DropBox extends Space {

	private final static Color ITEM_COLOR = new Color(255, 250, 112);
	private final static Color DROPBOX_COLOR = new Color(0, 205, 212);
	private int maxItems;
	private int numItems;

	public DropBox() {
		this('n', new Rectangle2D.Double(-500, -500, 1, 1));
	}
	
	public DropBox(char type, Rectangle2D.Double hitbox) {
		this(type, hitbox, 5);
	}

	public DropBox(char type, Rectangle2D.Double hitbox, int maxItems) {
		super(type, hitbox);
		this.numItems = 0;
		this.maxItems = maxItems;
	}

	public void addItem() {
		if (this.numItems + 1 <= this.maxItems) {
			this.numItems++;
		}
	}

	@Override
	public void drawOn(Graphics2D g) {
		g.setColor(DROPBOX_COLOR);
		Rectangle2D.Double hitbox = super.getHitbox();
		g.fill(hitbox);
		if (this.numItems > 0) {
			Rectangle2D.Double filledBox = new Rectangle2D.Double(hitbox.getX(), hitbox.getY(),
					hitbox.getWidth() * (this.numItems * 1.0 / this.maxItems), hitbox.getHeight());
			g.setColor(ITEM_COLOR);
			g.fill(filledBox);
		}
	}

	public boolean wantsItem() {
		return this.numItems < this.maxItems;
	}
	
	public boolean isComplete() {
		return this.numItems == this.maxItems;
	}
	
}
