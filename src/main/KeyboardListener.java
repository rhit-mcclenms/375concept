package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class listens to the keyboard and gives useful information to the
 * MapUserTrainer.
 * 
 * @author bakerjl1
 *
 */
public class KeyboardListener implements KeyListener {

	private MapUserTrainer mapUser;
	private BattleMain battle;
	private StoryMain story;
	private DJSmooth dj;
	
	private final static int UP_KEYCODE = 38;
	private final static int DOWN_KEYCODE = 40;
	private final static int LEFT_KEYCODE = 37;
	private final static int RIGHT_KEYCODE = 39;
	private final static int W_KEYCODE = 87;
	private final static int S_KEYCODE = 83;
	private final static int A_KEYCODE = 65;
	private final static int D_KEYCODE = 68;
	
	
	//1: Normal Gameplay, 2: Battle Gameplay, 3: Menu
	private int state;

	public KeyboardListener(MapUserTrainer mapUser, DJSmooth dj) {
		this.mapUser = mapUser;
		this.state = 2;
		this.battle = null;
		this.story = new StoryMain(this.mapUser.getBattleUser());
		this.dj = dj;
		this.dj.playSong(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {

		//Moving through the game
		if (this.state == 0) {
			// UP
			if (e.getKeyCode() == UP_KEYCODE || e.getKeyCode() == W_KEYCODE) {
				this.mapUser.setGoingUp(true);
			}
			// LEFT
			else if (e.getKeyCode() == LEFT_KEYCODE || e.getKeyCode() == A_KEYCODE) {
				this.mapUser.setGoingLeft(true);		
			}
			// RIGHT
			else if (e.getKeyCode() == RIGHT_KEYCODE || e.getKeyCode() == D_KEYCODE) {
				this.mapUser.setGoingRight(true);		
			}
			// DOWN
			else if (e.getKeyCode() == DOWN_KEYCODE || e.getKeyCode() == S_KEYCODE) {
				this.mapUser.setGoingDown(true);		
			}
		}
		//Going through a battle
		else if (this.state == 1) {
			this.battle.pressInput(e);
		}
		//Menu
		else {
			this.story.pressInput(e);
			if (this.story.isOver()) {
				this.state = 0;
			}
		}
		

	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		
		//Going through map
		if (this.state == 0) {
			// UP
			if (e.getKeyCode() == UP_KEYCODE || e.getKeyCode() == W_KEYCODE) {
				this.mapUser.setGoingUp(false);
			}
			// LEFT
			else if (e.getKeyCode() == LEFT_KEYCODE || e.getKeyCode() == A_KEYCODE) {
				this.mapUser.setGoingLeft(false);
			}
			// RIGHT
			else if (e.getKeyCode() == RIGHT_KEYCODE || e.getKeyCode() == D_KEYCODE) {
				this.mapUser.setGoingRight(false);		
			}
			// DOWN
			else if (e.getKeyCode() == DOWN_KEYCODE || e.getKeyCode() == S_KEYCODE) {
				this.mapUser.setGoingDown(false);		
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	public void addBattleMain(BattleMain battle) {
		this.battle = battle;
		this.state = 1;
		//this.dj.playSong(1);
	}
	
	public void endBatMain(BattleMain placeHolder) {
		this.battle = placeHolder;
		this.state = 0;
		this.mapUser.resetInput();
		//this.dj.playSong(0);
	}
	
	public void endStory() {
		this.state = 0;
		this.mapUser.resetInput();
	}
	
	public int getState() {
		return this.state;
	}
	
	public void setState(int newState) {
		
		if (newState >= 0 && newState <= 2) {
			this.state = newState;
		}
		
	}
	
	public StoryMain getStoryMain() {
		return this.story;
	}

}
