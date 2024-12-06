package myGamePack;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// Sound Class
// Description: Here we can play music for playing our game
public class Sound {
    private String file_path; 
    private Clip clip; 

    public Sound(String music_path, boolean loop) {
        this.file_path = music_path;
        playSound(loop);
    }

    public synchronized void playSound(boolean loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file_path).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); 
            clip.close();
        }
    }
}
