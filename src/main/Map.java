package main;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles all of the individual Sectors in the map. Whenever a
 * Sector needs to be changed, it is handled here.
 * 
 * Has the ability to transition between sectors and transfer data between them.
 * Notified by the sector when a loading zone is entered.
 * 
 * @author bakerjl1 and williagm
 *
 */
public class Map {

	private boolean[] gymKeys;
	private Sector[][] allSectors;
	private ArrayList<GymSector> gymSectors;
	private Sector currentSector;
	private MapUserTrainer mapUser;
	private FileReader sectorFile;
	private int currentSectorX;
	private int currentSectorY;
	private boolean transitionLock;
	private HashMap<Integer, Integer> sectorToGymMap;
	private int xBeforeTransition;
	private int yBeforeTransition;

	public Map(MapUserTrainer mapUser) {
		this.mapUser = mapUser;
		this.allSectors = new Sector[10][5];
		this.gymSectors = new ArrayList<GymSector>();
		this.currentSectorX = 4;
		this.currentSectorY = 9;
		this.xBeforeTransition = 4;
		this.yBeforeTransition = 9;
		this.transitionLock = true;
		this.gymKeys = new boolean[8];
		this.sectorToGymMap = new HashMap<Integer, Integer>();
		this.sectorToGymMap.put(80, 0);
		this.sectorToGymMap.put(64, 1);
		this.sectorToGymMap.put(61, 2);
		this.sectorToGymMap.put(21, 3);
		this.sectorToGymMap.put(04, 4);
		this.sectorToGymMap.put(00, 5);
		this.sectorToGymMap.put(32, 6);
		this.sectorToGymMap.put(52, 7);
		for (int i = 0; i < this.gymKeys.length; i++) {
			
			this.gymKeys[i] = false;
			
		}

		String name;
		for (int row = 0; row < this.allSectors.length; row++) {
			for (int col = 0; col < this.allSectors[0].length; col++) {
				name = "sector" + row + col + ".txt";
				try {
					this.sectorFile = new FileReader(name);
					this.allSectors[row][col] = new Sector(this.mapUser, this.sectorFile);
				} catch (FileNotFoundException e) {
					this.allSectors[row][col] = null;
				}
			}
		}
		for (int i = 0; i < 8; i++) {
			name = "sectorG" + i + ".txt";
			try {
				this.sectorFile = new FileReader(name);
				this.gymSectors.add(new GymSector(this.mapUser, this.sectorFile, i));
			} catch (FileNotFoundException e) {
				System.out.println("Welp, that didn't work.");
			}
		}
		this.resetCurrentSector();
	}

	public void drawOn(Graphics2D g) {
		this.currentSector.drawOn(g);
	}

	public void update() {
		this.currentSector.update();
		int transId = this.mapUser.getTransitionId();
		if (transId != 0) {
			this.tryChangeSector(transId);
		}
		if (currentSector.getShouldTransition()) {
			this.getSwole();
		}
	}

	private void getSwole() {
		
		int sectorCode = this.currentSectorY * 10 + this.currentSectorX;
		int gymToGoTo = this.sectorToGymMap.get(sectorCode);
		if (!this.gymKeys[gymToGoTo]) {	
			this.mapUser.storeCurrentPos();
			this.xBeforeTransition = this.currentSectorX;
			this.yBeforeTransition = this.currentSectorY;
			this.currentSectorX = this.sectorToGymMap.get(sectorCode);
			this.currentSectorY = 10;
			this.resetCurrentSector();
		}
	}

	/**
	 * 
	 * @param transId: 1 is left, 2 is right, 3 is up, 4 is down
	 */
	private void tryChangeSector(int transId) {
		int newX = this.currentSectorX;
		int newY = this.currentSectorY;
		//Left
		if (transId == 1) {
			newX--;
		}
		//Right
		if (transId == 2) {
			newX++;
		}
		//Up
		if (transId == 3) {
			newY--;
		}
		//Down
		if (transId == 4) {
			newY++;
		}
		if (newX > -1 && newX < this.allSectors[0].length && newY > -1 && newY < this.allSectors.length
				&& this.transitionLock) {
			if (this.allSectors[newY][newX] != null) {
				this.currentSector = this.allSectors[newY][newX];
				this.currentSectorX = newX;
				this.currentSectorY = newY;
				this.mapUser.acceptTransitionRequest(transId);
			}
		}
		this.transitionLock = !this.transitionLock;
	}

	public void clearProjectiles() {
		this.currentSector.clearProjectiles();
	}
	
	public void resetCurrentSector() {
		if (this.isInGym()) {
			this.currentSector = this.gymSectors.get(currentSectorX);
		} else {
			this.currentSector = this.allSectors[this.currentSectorY][this.currentSectorX];
		}
	}

	public boolean isInGym() {
		
		return this.currentSectorY == 10;
		
	}
	
	public boolean finishedGym() {
		return this.currentSector.isDropBoxFull();
	}

	public void backToOverworld() {

		this.gymKeys[currentSectorX] = true;
		this.currentSectorX = this.xBeforeTransition;
		this.currentSectorY = this.yBeforeTransition;
		this.mapUser.toStoredPos();
		this.resetCurrentSector();
		
	}
}
