package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class DJSmooth {

	private ArrayList<Clip> clips;
	private ArrayList<AudioInputStream> jukeBox;
	private int currentIndex;
	
	public DJSmooth() {
		this.clips = new ArrayList<Clip>();
		this.jukeBox = new ArrayList<AudioInputStream>();
		this.currentIndex = 0;
		AudioInputStream shopStream = null;
		AudioInputStream battleStream = null;
		try {
			shopStream = AudioSystem.getAudioInputStream(new File("wiiShop.wav"));
			battleStream = AudioSystem.getAudioInputStream(new File("battle.wav"));
			this.jukeBox.add(shopStream);
			this.jukeBox.add(battleStream);
			for (int i = 0; i < this.jukeBox.size(); i++) {
				this.clips.add((Clip) AudioSystem.getLine(new Line.Info(Clip.class)));
			}
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println("We had troubles making the files, sir.");
			e.printStackTrace();
		}
	}
	
	public void playSong(int index) {
		System.out.println("I am playing song!");
		if (index < jukeBox.size() && index > -1) {
			try {
				this.clips.get(currentIndex).stop();
				this.clips.get(index).open(this.jukeBox.get(index));
				this.clips.get(index).loop(Clip.LOOP_CONTINUOUSLY);
				this.currentIndex = index;
			} catch (LineUnavailableException | IOException e) {
				System.out.println("Couldn't play what you wanted.");
				e.printStackTrace();
			}
		} else {
			playSong(0);
		}
		this.clips.get(this.currentIndex).start();
	}
	
}
