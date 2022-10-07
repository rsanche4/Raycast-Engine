package myGamePack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
	public int[] pixels;
	public int[] sky_pixels;
	private String loc;
	public final int SIZE=64;
	public int H_SKYSIZE;
	public int W_SKYSIZE;
	// Textures must be smaller than screen resolution
	public static Texture null_texture = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\null.png", false);
	public static Texture null_texture2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\null2.png", false);
	public static Texture lain = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\lain.png", false);
	public static Texture distant = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\distant.png", false);
	public static Texture skybox = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox.png", false);
	public static Texture skybox2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox2.png", false);
	public static Texture skybox3 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox3.png", false);
	public static Texture tall_wall = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\windmill.png", true);
	
	public Texture(String location, boolean isTall) {
		loc = location;
		if (isTall) pixels = new int[SIZE * 2 * SIZE];
		else pixels = new int[SIZE * SIZE];
		load();
	}

	private void load() {
		try {
			BufferedImage image = ImageIO.read(new File(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			if (w*h == SIZE*SIZE || w*h == SIZE*2*SIZE) {
				image.getRGB(0, 0, w, h, pixels, 0, w);
			} else {
				H_SKYSIZE = h;
				W_SKYSIZE = w;
				sky_pixels = new int[w * h];
				if (Game.WID != w || Game.HEI != h) {
					System.err.println("Warning: Skybox must be same size as current screen resolution: " + Game.WID + "x" + Game.HEI);
					return;
				}
				image.getRGB(0, 0, w, h, sky_pixels, 0, w);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
