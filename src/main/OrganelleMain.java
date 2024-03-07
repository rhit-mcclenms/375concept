package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.naming.NamingException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start
 * by running main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * This is the main class for the whole game. It handles save data, etc. It starts the game, and continues the game
 * until its bitter end.
 * 
 * @author bakerjl1 and williagm
 *
 */

public class OrganelleMain {
	
	public final static int UNIVERSAL_DX = 10;
	
	public final static int FULLSCREEN_WIDTH = 1920;
	public final static int FULLSCREEN_HEIGHT = 1080;
	
	private final static int DELAY = 17;
	
	private MapUserTrainer mapUser;
	
	private KeyboardListener heyListen;
	private JFrame frame;
	
	private Timer mapTimer;
	private OrganelleComponent orgComponent;
	private DJSmooth dj;
	
	public OrganelleMain(MapUserTrainer mapUser) {
		
		this.dj = new DJSmooth();
		this.mapUser = mapUser;
		this.heyListen = new KeyboardListener(this.mapUser, dj);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int userStartX = 1525;
		int userStartY = 625;
		OrganelleMain orgMain = new OrganelleMain(new MapUserTrainer(userStartX, userStartY));
		
		//DJSmooth dj = new DJSmooth();
		//dj.playSong(0);
		
		orgMain.viewAll();
		
	}
	
	public void viewAll() {
		this.frame = new JFrame();
		frame.setSize(FULLSCREEN_WIDTH, FULLSCREEN_HEIGHT);
		frame.setTitle("Did this work?");
		frame.setUndecorated(true);
		
		this.orgComponent = new OrganelleComponent(new Map(this.mapUser), this.heyListen);
		frame.add(orgComponent);
		
		frame.addKeyListener(this.heyListen);
		
		this.mapTimer = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				testForBattle();
				orgComponent.update();
				orgComponent.repaint();
				frame.repaint();
				
			}
			
		});
		this.mapTimer.start();
		
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void testForBattle() {
		
		BattleTrainer t = this.mapUser.battleAgainst();
		
		if (t != null) {
			
			BattleMain b = new BattleMain(this.mapUser.getBattleUser(), t, dj);
			
			this.mapUser.resetOpponent();
			this.orgComponent.clearProjectiles();
			
			this.heyListen.addBattleMain(b);
			
			this.orgComponent.addBatMain(b);
		}
		
	}

	
	
}