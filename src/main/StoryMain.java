package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

/**
 * This class gives the basic story of the game.
 * @author bakerjl1
 *
 */
public class StoryMain {

	private final static Color ANIMAL_COLOR = new Color(145, 207, 255);
	private final static Color ANIMAL_SELECTED = new Color(51, 159, 242);
	private final static Color PLANT_COLOR = new Color(148, 255, 176);
	private final static Color PLANT_SELECTED = new Color(27, 207, 74);
	private final static Color BACTERIA_COLOR = new Color(255, 255, 163);
	private final static Color BACTERIA_SELECTED = new Color(223, 237, 24);
	private final static Color ARCHAEA_COLOR = new Color(217, 163, 255);
	private final static Color ARCHAEA_SELECTED = new Color(176, 64, 255);
	private final static Color FUNGUS_COLOR = new Color(173, 148, 147);
	private final static Color FUNGUS_SELECTED = new Color(115, 78, 77);
	private final static Color PROF_YUCCA_COLOR = new Color(184, 184, 184);
	private final static String[] TYPE_NAMES = {"Animal", "Plant", "Bacteria", "Archaea", "Fungi"};
	private final static int STARTER_LEVEL = 5;
	
	private BattleUserTrainer battleUser;
	private ArrayList<String> stories;
	private int currentIndex;
	private Pokemon[] starterPokemon;
	private Pokemon[] archaeaPokemon;
	private Menu continueMenu;
	private Menu starterPokemonMenu;
	private Menu starterTypeMenu;
	private Menu starterArchaeaMenu;
	private Menu currentMenu;
	private boolean isOver;
	private Pokemon currentPokemon;
	private String currentDraw;
	private Rectangle2D.Double profYucca;
	private Rectangle2D.Double champMan;
	private int colorMultiplier;
	private int currentSubtract;
	
	public StoryMain(BattleUserTrainer battleUser) {
		
		this.battleUser = battleUser;
		this.currentIndex = 0;
		this.starterPokemon = new Pokemon[3];
		this.archaeaPokemon = new Pokemon[3];
		this.starterPokemon[0] = new Pokemon("Ribosome", STARTER_LEVEL);
		this.starterPokemon[1] = new Pokemon("Cytoplasm", STARTER_LEVEL);
		this.starterPokemon[2] = new Pokemon("Cell Membrane", STARTER_LEVEL);
		this.archaeaPokemon[0] = new Pokemon("R. Ribosome", STARTER_LEVEL);
		this.archaeaPokemon[1] = new Pokemon("R. Cytoplasm", STARTER_LEVEL);
		this.archaeaPokemon[2] = new Pokemon("R. Cell Membrane", STARTER_LEVEL);
		this.currentDraw = "Yucca";
		this.profYucca = new Rectangle2D.Double(400, 250, 500, 500);
		this.champMan = new Rectangle2D.Double(1000, 250, 500, 500);
		this.currentSubtract = 60;
		this.colorMultiplier = 2;
		this.isOver = false;
		
		String[] myOption = {"Continue"};
		this.continueMenu = new Menu(myOption, 1300, 50);
		Color[] unselectedOptions = {ANIMAL_COLOR, PLANT_COLOR, BACTERIA_COLOR, ARCHAEA_COLOR, FUNGUS_COLOR};
		Color[] selectedOptions = {ANIMAL_SELECTED, PLANT_SELECTED, BACTERIA_SELECTED, ARCHAEA_SELECTED, FUNGUS_SELECTED};
		this.starterTypeMenu = new Menu(TYPE_NAMES, unselectedOptions, selectedOptions, 1300, 50);
		this.setArchaeaMenu();
		this.setNormalMenu();
		this.currentMenu = this.continueMenu;
		this.resetCurrentPokemon();
		
		this.stories = new ArrayList<String>();
		this.stories.add("Hello there! My name is Professor Yucca. Our host organism has been attacked with the feared virus Explosive Influenza!");
		this.stories.add("However, you are a Cytotoxic T-Cell and we are VERY glad to see you. You may be our only hope.");
		this.stories.add("You must go about catching Organelles and building up your cell to defeat Explosive Influenza!");
		this.stories.add("You will find infected cells along the way, and you must find a way to defeat them.");
		this.stories.add("Just yesterday, our Champion was taken by the virus, and someone must stop him. He lives in the eighth gym.");
		this.stories.add("Explosive Influenza has set up barriers for us called Gyms, each having their own Master Cell.");
		this.stories.add("Defeating the Master Cell will open up the opportunity to challenge more gyms, but with this comes increased difficulty.");
		this.stories.add("Once you get through all seven gyms, you can challenge the Champion and his respected Mightychondria!");
		this.stories.add("(Champion) Yes, my Mightychondria is the Powerhouse of my cell! I wouldn't be this strong if not for my Mightychondria!");
		this.stories.add("Yes, he is correct in saying that the Mitochondria is the Powerhouse of any cell. It will be difficult to defeat him,");
		this.stories.add("especially with his Mightychondria by his side, which is the Powerhouse of his cell.");
		this.stories.add("(Champion) Ha ha ha! None can stop my Mightychondria: the Powerhouse of my cell!");
		this.stories.add("(Champion) Not even a Cytotoxic T-Cell can defeat the Powerhouse of my cell, which is of course my Mightychondria!");
		this.stories.add("Go young Cytotoxic T-Cell, you must prove him wrong and defeat the Powerhouse of his cell: the Mightychondria!");
		this.stories.add("However, I have forgotten... what type of cell are you again?");
		this.stories.add("Ahh, yes. I remember now. You must now choose your first organelle to begin your journey!");
		this.stories.add("Fantastic choice! Perhaps this little one can be the Powerhouse of your cell! Good luck, young padawan!");
		this.stories.add("Go on now. Don't be scared.");
		
	}
	
	private void setNormalMenu() {
		String[] names = new String[3];
		Color[] unselecteds = new Color[3];
		Color[] selecteds = new Color[3];
		for (int i = 0; i < this.starterPokemon.length; i++) {
			names[i] = this.starterPokemon[i].getName();
			unselecteds[i] = BattleMain.getUnselectedColor(this.starterPokemon[i].getType());
			selecteds[i] = BattleMain.getSelectedColor(this.starterPokemon[i].getType());
		}
		this.starterPokemonMenu = new Menu(names, unselecteds, selecteds, 1300, 50);
	}
	
	private void setArchaeaMenu() {
		String[] names = new String[3];
		Color[] unselecteds = new Color[3];
		Color[] selecteds = new Color[3];
		for (int i = 0; i < this.starterPokemon.length; i++) {
			names[i] = this.archaeaPokemon[i].getName();
			unselecteds[i] = BattleMain.getUnselectedColor(this.archaeaPokemon[i].getType());
			selecteds[i] = BattleMain.getSelectedColor(this.archaeaPokemon[i].getType());
		}
		this.starterArchaeaMenu = new Menu(names, unselecteds, selecteds, 1300, 50);
	}
	
	public boolean isOver() {
		return this.isOver;
	}
	
	public void drawOn(Graphics2D g) {
		if (this.currentSubtract <= 50) {
			this.currentSubtract = 50;
			this.colorMultiplier = 2;
		}
		else if (this.currentSubtract >= 160) {
			this.currentSubtract = 160;
			this.colorMultiplier = -2;
		}
		this.currentSubtract += this.colorMultiplier;
		this.currentMenu.update();
		g.setStroke(new BasicStroke(50));
		g.setFont(new Font("Sup", 50, 35));
		if (this.stories.get(currentIndex).contains("(Champion)")) {
			g.setColor(Color.RED);
			this.currentDraw = "Champ";
		} else {
			g.setColor(Color.BLACK);
			this.currentDraw = "Yucca";
		}
		g.drawString(this.stories.get(this.currentIndex), 50, 1000);
		this.currentMenu.drawOn(g);
		if (this.currentIndex == 15) {
			this.currentPokemon.drawDisplayPokemon(g);
		} else if (this.currentDraw.equals("Yucca")){
			g.setColor(PROF_YUCCA_COLOR);
			g.fill(this.profYucca);
			g.setStroke(new BasicStroke(8));
			g.setColor(Color.BLACK);
			g.drawOval((int)this.profYucca.getX() + 50, (int)this.profYucca.getY() + 100, 150, 150);
			g.drawOval((int)this.profYucca.getX() + 300, (int)this.profYucca.getY() + 100, 150, 150);
			g.drawLine((int)(this.profYucca.getX() + 200), (int)(this.profYucca.getY() + 150), (int)(this.profYucca.getX() + 300), (int)(this.profYucca.getY() + 150));
		} else {
			g.setColor(new Color(this.currentSubtract, 0, 0));
			g.fill(this.champMan);
		}
	}
	
	public void handleSelect(int indexSelected) {
		this.currentIndex++;
		if (this.currentMenu == this.continueMenu) {
			if (this.currentIndex == 14) {
				this.currentMenu = this.starterTypeMenu;
			} else if (this.currentIndex >= 17) {
				this.isOver = true;
			}
		} 
		else if (this.currentMenu == this.starterTypeMenu) {
			this.handleStarterTypeMenu(indexSelected);
		}
		else if (this.currentMenu == this.starterPokemonMenu) {
			this.handleStarterPokemonMenu(indexSelected);
		}
		else if (this.currentMenu == this.starterArchaeaMenu) {
			this.handleStarterArchaeaMenu(indexSelected);
		}
			
		
	}
	
	private void handleStarterArchaeaMenu(int indexSelected) {
		if (indexSelected < this.archaeaPokemon.length) {
			this.battleUser.addPokemon(this.archaeaPokemon[indexSelected]);
		}
		this.currentMenu = this.continueMenu;
	}

	private void handleStarterPokemonMenu(int indexSelected) {
		if (indexSelected < this.starterPokemon.length) {
			this.battleUser.addPokemon(this.starterPokemon[indexSelected]);
		}
		this.currentMenu = this.continueMenu;
	}

	public void handleStarterTypeMenu(int indexSelected) {
		this.battleUser.setType(TYPE_NAMES[indexSelected]);
		this.currentMenu = this.starterPokemonMenu;
		if (indexSelected == 3) {
			this.currentMenu = this.starterArchaeaMenu;
		}
	}
	
	public void pressInput(KeyEvent e) {
		// UP
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
			this.currentMenu.takeUserInput("Up");
		}
		
		// DOWN
		else if (e.getKeyCode() == 40 || e.getKeyCode() == 83) {
			this.currentMenu.takeUserInput("Down");
		}
		//ENTER
		if (e.getKeyCode() == 10 || e.getKeyCode() == 32) {
			
			handleSelect(this.currentMenu.takeUserInput("Select"));
			//Do some things.
		}
		this.resetCurrentPokemon();
		
	}

	public void resetCurrentPokemon() {
		
		if (this.currentMenu == this.starterPokemonMenu) {
			this.currentPokemon = this.starterPokemon[this.starterPokemonMenu.getCurrentIndex()];
		}
		else if (this.currentMenu == this.starterArchaeaMenu) {
			this.currentPokemon = this.archaeaPokemon[this.starterArchaeaMenu.getCurrentIndex()];
		}
		else {
			this.currentPokemon = this.starterPokemon[0];
		}
		
	}
	
}
