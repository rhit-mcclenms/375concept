package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

/**
 * This is a space that has an item.
 * 
 * @author bakerjl1
 *
 */
public class ItemSpace extends Space {

	private final static Color ITEM_COLOR = new Color(255, 250, 112);
	private final static Color ITEM_SPACE_COLOR = new Color(214, 214, 214);
	private boolean hasItem;
	private Rectangle2D.Double itemHitbox;

	public ItemSpace(char type, Rectangle2D.Double hitbox) {
		super(type, hitbox);
		this.hasItem = true;
		double newStartX = hitbox.getX() + (hitbox.getWidth() / 3);
		double newStartY = hitbox.getY() + (hitbox.getHeight() / 3);
		this.itemHitbox = new Rectangle2D.Double(newStartX, newStartY, hitbox.getWidth() / 3, hitbox.getHeight() / 3);
	}

	@Override
	public void drawOn(Graphics2D g) {
		g.setColor(ITEM_SPACE_COLOR);
		g.fill(super.getHitbox());
		if (this.hasItem) {
			g.setColor(ITEM_COLOR);
			g.fillOval((int) this.itemHitbox.getX(), (int) this.itemHitbox.getY(), (int) this.itemHitbox.getWidth(),
					(int) this.itemHitbox.getHeight());
		}
	}

	public void grabItem() {
		this.hasItem = false;
	}

	@Override
	public boolean hasObject() {
		return this.hasItem;
	}

	public Rectangle2D.Double getItemHitbox() {
		return this.itemHitbox;
	}
}
