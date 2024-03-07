package main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class OrganelleComponent extends JComponent{

	
	private static final BattleMain PLACEHOLDER = new BattleMain(new BattleUserTrainer(), new BattleEnemy("Animal", 1, 2), new DJSmooth());
	
	private Map bigMap;
	private BattleMain batMain;
	private StoryMain storyMain;
	private KeyboardListener heyListen;
	
	
	public OrganelleComponent(Map myMap, KeyboardListener heyListen) {
		this.bigMap = myMap;
		this.batMain = PLACEHOLDER;
		this.heyListen = heyListen;
		this.storyMain = this.heyListen.getStoryMain();
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if (this.heyListen.getState() == 0) {
			this.bigMap.drawOn(g2d);
		} else if (this.heyListen.getState() == 1) {
			this.batMain.drawOn(g2d);
		} else {
			this.storyMain.drawOn(g2d);
		}
		
	}
	
	
	public void update() {
		if (this.heyListen.getState() == 0) {
			this.bigMap.update();
		} else if (this.heyListen.getState() == 1) {
			this.batMain.update();
			if (this.batMain.isOver()) {
				this.endBatMain();
			}
		} else {
			if (this.storyMain.isOver()) {
				
			}
		}
	}
	
	public void addBatMain(BattleMain b) {
		this.heyListen.addBattleMain(b);
		this.batMain = b;
	}
	
	public void endBatMain() {
		//bing bong
		this.heyListen.endBatMain(PLACEHOLDER);
		this.batMain = PLACEHOLDER;
		if (this.bigMap.finishedGym()) {
			this.bigMap.backToOverworld();
		}
	}
	
	public int getState() {
		return this.heyListen.getState();
	}
	
	public void setState(int newState) {
		this.heyListen.setState(newState);
	}
	
	public void clearProjectiles() {
		this.bigMap.clearProjectiles();
	}
	
	
}

