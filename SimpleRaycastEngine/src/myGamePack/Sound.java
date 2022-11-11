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

	// the sound class. Keep files to .wav
    private static Clip clip;
	
	public static synchronized void playSound(String file_path) {
		// create AudioInputStream object
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(file_path).getAbsoluteFile());
			
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
			
			e.printStackTrace();
		}
          
        
	}
	public static synchronized void stopSound() {
		try 
		{
			clip.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
