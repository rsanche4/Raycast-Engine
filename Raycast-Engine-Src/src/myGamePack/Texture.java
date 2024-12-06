package myGamePack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Texture Class
// Description: Here we can load the textures. Returns a 1D array of the textures
public class Texture {
	public int[] pixels;
	public String loc;
	public int SIZE = 64;
	public int IMG_WID;
	public int IMG_HEI;

	public Texture(String file_location) {
		loc = file_location;
		try {
			BufferedImage image = ImageIO.read(new File(loc));
			IMG_WID = image.getWidth();
			IMG_HEI = image.getHeight();
			pixels = new int[IMG_WID * IMG_HEI];
			image.getRGB(0, 0, IMG_WID, IMG_HEI, pixels, 0, IMG_WID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}