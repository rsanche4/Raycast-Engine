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
	public Texture(String file_location) {
		loc = file_location;
		try {
			BufferedImage image = ImageIO.read(new File(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			pixels = new int[w*h];
			image.getRGB(0, 0, w, h, pixels, 0, w); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}