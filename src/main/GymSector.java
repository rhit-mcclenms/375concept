package main;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * Sector used for Gyms because Gyms are special.
 * 
 * @author bakerjl1
 *
 */
public class GymSector extends Sector {

	private final static Color GYM_BUILDING_COLOR = new Color(0, 166, 168);
	private final static Color GYM_DIRT_COLOR = new Color(122, 122, 122);
	private final static Color GYM_FOREST_COLOR = new Color(188, 201, 0);
	
	private BattleEnemy gymLeader;
	private boolean isComplete;

	public GymSector(MapUserTrainer guy, FileReader spaceTypes, int gymNum) {
		super(guy, spaceTypes);
		this.isComplete = false;
		Space[][] spaces = super.getSpaces();
		for (int row = 0; row < spaces.length; row++) {
			for (int col = 0; col < spaces[0].length; col++) {
				char currentType = spaces[row][col].getType();
				if (currentType == 'B') {
					spaces[row][col].setColor(GYM_BUILDING_COLOR);
				} else if (currentType == 'D') {
					spaces[row][col].setColor(GYM_DIRT_COLOR);
				} else if (currentType == 'F') {
					spaces[row][col].setColor(GYM_FOREST_COLOR);
				}
			}
		}
		String name = "sectorG" + gymNum + ".txt";
		try {
			spaceTypes = new FileReader(name);
		} catch (FileNotFoundException e) {
			System.out.println("Welp, that didn't work.");
		}
		Scanner scan = new Scanner(spaceTypes);
		for (int i = 0; i < 12; i++) {
			scan.nextLine();
		}
		int minLevel = scan.nextInt();
		int maxLevel = scan.nextInt();
		scan.nextInt();
		this.gymLeader = new BattleEnemy("Plant", minLevel, maxLevel);
		while (scan.hasNext()) {
			this.gymLeader.addPokemon(new Pokemon(scan.nextLine()));
		}
	}
	
	@Override
	public void testTransitionBoxes() {
		
		if (super.isDropBoxFull() && !this.isComplete) {
			this.isComplete = true;
			MapUserTrainer mapUser = super.getUserTrainer();
			mapUser.giveOpponent(this.gymLeader);
		}
		
	}
	
}
