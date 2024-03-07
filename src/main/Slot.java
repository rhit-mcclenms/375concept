package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Scanner;

public class Slot {
	
	protected static final double DEFAULT_WIDTH = 600;
	protected static final double DEFAULT_HEIGHT = 70;
	private final static int DESCRIPTION_CHAR_PER_LINE = 20;
	private final static int MAX_DESCRIPTION_LINES = 10;
	
	protected final Color ACTIVATED_COLOR = new Color(100, 100, 100);
	protected final Color DEACTIVATED_COLOR = new Color(190, 190, 190);
	protected final Color ACTIVATED_TEXT_COLOR = Color.WHITE;
	protected final Color DEACTIVATED_TEXT_COLOR = Color.BLACK;
	private final Color FAINTED_ACTIVATED_COLOR = new Color(110, 110, 110);
	private final Color FAINTED_DEACTIVATED_COLOR = new Color(173, 173, 173);
	
	private double x, y, width, height;
	private int pp, maxPP;
	private boolean isHighlighted;
	private Color myColor;
	private Color unselectColor;
	private Color selectColor;
	private Color myTextColor;
	private String text;
	private String battleName;
	private String[] description;

	public Slot(double x, double y, boolean isHighlighted, String text) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, isHighlighted, text);
	}
	
	public Slot(double x, double y, boolean isHighlighted, String name, Color unselectColor, Color selectColor, int pp, int maxPP) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, isHighlighted, name, unselectColor, selectColor, pp, maxPP);
	}
	
	public Slot(double x, double y, double width, double height, boolean isHighlighted, String text) {
		this(x, y, width, height, isHighlighted, text, null, null, -1, -1);
	}
	
	public Slot(double x, double y, double width, double height, boolean isHighlighted, String name, Color unselectColor, Color selectColor, int pp, int maxPP) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.setActivated(isHighlighted);
		this.text = name;
		this.unselectColor = unselectColor;
		this.selectColor = selectColor;
		this.pp = pp;
		this.maxPP = maxPP;
		this.description = new String[MAX_DESCRIPTION_LINES];
	}
	
	

	public double drawOn(Graphics2D g, double offset) {
		if (this.pp == 0) {
			if (this.isHighlighted) {				
				g.setColor(FAINTED_ACTIVATED_COLOR);
			} else {
				g.setColor(FAINTED_DEACTIVATED_COLOR);
			}
		}
		else if (this.unselectColor != null && !this.isHighlighted) {
			g.setColor(this.unselectColor);
		} 
		else if (this.isHighlighted && this.selectColor != null) {
			g.setColor(this.selectColor);
		}
		else {
			g.setColor(this.myColor);
		}
		g.fillOval((int) this.x, (int) (this.y + offset), (int) this.height, (int) this.height);
		g.fillOval((int) (this.x + this.width - this.height), (int) (this.y + offset), (int) this.height, (int) this.height);
		g.fillRect((int) (this.x + this.height / 2), (int) (this.y + offset), (int) (this.width - this.height), (int) this.height);
		g.setColor(this.myTextColor);
		g.setFont(new Font("Fake", 5, (int) (this.height * 2.0 / 3)));
		g.drawString(this.text, (int) (this.x + this.height/2), (int) (this.y + offset + this.height * 5.0 / 7));
		g.setFont(new Font("Fake", 5, (int) (this.height / 2.0)));
		if (this.pp >= 0) {
			g.drawString(this.pp + "/" + this.maxPP, (int)(this.x + this.width * 3.0/4), (int) (this.y + offset + this.height * 5.5 / 7));
		}
		return this.height;
	}

	public void setActivated(boolean isActivated) {
		this.isHighlighted = isActivated;
		if (this.isHighlighted) {
			this.myColor = ACTIVATED_COLOR;
			this.myTextColor = ACTIVATED_TEXT_COLOR;
		} else {
			this.myColor = DEACTIVATED_COLOR;
			this.myTextColor = DEACTIVATED_TEXT_COLOR;
		}
	}
	
	public void updatePP(int newPP) {
		this.pp = newPP;
	}
	
	public void update(String text, int pp, int maxPP, Color unselectColor, Color selectColor) {
		this.text = text;
		this.pp = pp;
		this.maxPP = maxPP;
		this.selectColor = selectColor;
		this.unselectColor = unselectColor;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String toString() {
		return this.text;
	}
	
	public void drawDescription(Graphics2D g, int moveID) {
		g.setStroke(new BasicStroke(10));
		int descriptPerLine = DESCRIPTION_CHAR_PER_LINE;
		String[] substrings = new String[MAX_DESCRIPTION_LINES];
//		Scanner scan = new Scanner(this.description);
//		scan.useDelimiter(" ");
//		ArrayList<String> words = new ArrayList<String>();
//		while (scan.hasNext()) {
//			words.add(scan.next());
//		}	
	}
	
	public void addDescription(String[] data) {
		this.description = data;
	}
	
	public void addDescription(String data) {
		this.description = new String[1];
		this.description[0] = data;
	}
}
