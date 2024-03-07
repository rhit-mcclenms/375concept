package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This class is used when the player must use menus to advance the plot or to
 * be used in battle.
 * 
 * This can handle menus where the user must press A (or something) to advance
 * or where the user must choose something from a list.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Menu {

	private ArrayList<Slot> slots;
	private int currentIndex;
	private final static int[] NO_PPS = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

	public Menu() {
		this(new String[0], 0, 0);
	}
	
	public Menu(ArrayList<Slot> slots) {
		this.slots = slots;
		this.currentIndex = 0;
	}
	
	public Menu(String[] options, double cornerX, double cornerY) {
		
		this(options, cornerX, cornerY, null);
		
	}
	
	public Menu(String[] options, double cornerX, double cornerY, String[][] data) {
		
		this.slots = new ArrayList<Slot>();
		
		for (String option: options) {
			this.slots.add(new Slot(cornerX, cornerY, false, option));
		}
		this.currentIndex = 0;
		this.handleDataInit(data);
		
	}
	
	public Menu(String[] names, Color[] unselectColors, Color[] selectColors, double cornerX, double cornerY) {
		this(names, unselectColors, selectColors, NO_PPS, NO_PPS, cornerX, cornerY, false);
	}
	
	public Menu(String[] names, Color[] unselectColors, Color[] selectColors, double cornerX, double cornerY, String[][] data) {
		this(names, unselectColors, selectColors, NO_PPS, NO_PPS, cornerX, cornerY, false, data);
	}
	
	public Menu(String[] names, Color[] unselectColors, Color[] selectColors, int[] pps, int[] maxPPs, double cornerX, double cornerY, boolean iWantSomeBack) {
		
		this(names, unselectColors, selectColors, pps, maxPPs, cornerX, cornerY, iWantSomeBack, null);
		
	}
	
	public Menu(String[] names, Color[] unselectColors, Color[] selectColors, int[] pps, int[] maxPPs, double cornerX, double cornerY, boolean iWantSomeBack, String[][] data) {
		
		this.slots = new ArrayList<Slot>();
		
		for (int i = 0; i < names.length; i++) {
			this.slots.add(new Slot(cornerX, cornerY, false, names[i], unselectColors[i], selectColors[i], pps[i], maxPPs[i]));
		}
		this.currentIndex = 0;
		
		if (iWantSomeBack) {			
			this.slots.add(new Slot(cornerX, cornerY, Slot.DEFAULT_WIDTH, Slot.DEFAULT_HEIGHT, false, "Back"));
		}

		this.handleDataInit(data);
	}
	
	private void handleDataInit(String[][] data) {
		
		if (data != null) {
			for (int i = 0; i < this.slots.size(); i++) {
				if (data[i].length == 1) {
					this.slots.get(i).addDescription(data[i][0]);
				} else {					
					this.slots.get(i).addDescription(data[i]);
				}
			}
		}
	}

	public void drawOn(Graphics2D g) {
		double currentOffset = 0;
		for (Slot slot : slots) {
			currentOffset += slot.drawOn(g, currentOffset) * 1.25;
		}
	}
	
	public void update() {
		for (Slot s: this.slots) {
			s.setActivated(false);
		}
		if (this.slots.size() != 0) {
			this.slots.get(this.currentIndex).setActivated(true);
		}
	}

	public void addSlot(Slot s) {
		this.slots.add(s);
	}

	public int takeUserInput(String s) {
		if (this.slots.size() == 0) {
			return -1;
		}
		if (s.equals("Up")) {
			if (this.currentIndex == 0) {
				this.currentIndex = this.slots.size() - 1;
			} else {
				this.currentIndex--;
			}
		}
		else if (s.equals("Down")) {
			if (this.currentIndex == this.slots.size() - 1) {
				this.currentIndex = 0;
			} else {
				this.currentIndex++;
			}
		}
		else if (s.equals("Select")) {
			return this.currentIndex;
		}
		return -1;
	}
	
	public void resetCurrentIndex() {
		
		this.currentIndex = 0;
		
	}
	
	public void updatePP(int index, int newPP) {
		
		this.slots.get(index).updatePP(newPP);
		
	}
	
	public int size() {
		return this.slots.size();
	}
	
	public void updateSlot(int slotIndex, String text, int pp, int maxPP, Color unselectColor, Color selectColor) {
		if (slotIndex < this.slots.size() && slotIndex >= 0) {
			this.slots.get(slotIndex).update(text, pp, maxPP, unselectColor, selectColor);
		}
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}
	
	public Menu clone() {
		return new Menu(this.slots);
	}
	
	public Menu cloneWithoutBack() {
		ArrayList<Slot> newSlots = new ArrayList<Slot>();
		for (Slot s: this.slots) {
			if (!s.getText().contains("Back")) {
				newSlots.add(s);
			}
		}
		return new Menu(newSlots);
	}
	
	public String toString() {
		String str = "New Menu: \n";
		for (Slot s: this.slots) {
			str += "\t" + s + "\n";
		}
		return str;
	}
}
