package myGamePack;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
  
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private static String file_path;
	
	public Sound(String music_path) {
		file_path = music_path;
		playSound();
	}
	
	public static synchronized void playSound() {
		// create AudioInputStream object
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(file_path).getAbsoluteFile());
			// create clip reference
	        Clip clip;
	        clip = AudioSystem.getClip();
	        
	     // open audioInputStream to the clip
	        clip.open(audioInputStream);
	          
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        clip.start();
		} catch (UnsupportedAudioFileException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        
	}
}
