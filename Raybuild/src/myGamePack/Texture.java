package myGamePack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
	public int[] pixels;
	public int[] sky_pixels;
	public int[] extras;
	private String loc;
	public final int SIZE=64;
	public int H_SKYSIZE;
	public int W_SKYSIZE;
	public int H_EXTRA;
	public int W_EXTRA;
	// Textures must be smaller than screen resolution. Blocks
	public static Texture city = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\city.png", true);
	public static Texture null_texture = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\null.png", false);
	public static Texture distant = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\distant.png", false);
	
	// skybox
	public static Texture skybox23 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox.png", false);
	public static Texture skybox56 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox_sd.png", false);
	
	// Keep in mind, black in sprites mean invisible color so purely black pixels of sprites will not be drawn on screen
	// animation, sprites
	public static Texture sprite0 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_0.png", false);
	public static Texture sprite1 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_1.png", false);
	public static Texture sprite2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_2.png", false);
	public static Texture sprite3 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_3.png", false);
	public static Texture peach = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\peach.png", false);
	public static Texture sprite = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite.png", false);
	
	// menu pictures
	public static Texture menu = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\menu.png", false);
	public static Texture menu_sd = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\menu_sd.png", false);
	
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
			if (w*h == SIZE*SIZE || (w==SIZE && h==SIZE*2)) {
				image.getRGB(0, 0, w, h, pixels, 0, w); // sprites and blocks are stored here
			} else if (Game.WID == w/4 && Game.HEI == h) {
				H_SKYSIZE = h;
				W_SKYSIZE = w;
				sky_pixels = new int[w * h];
				image.getRGB(0, 0, w, h, sky_pixels, 0, w); // sky box is stored here
			}
				H_EXTRA = h;
				W_EXTRA = w;
				extras = new int[w * h];
				image.getRGB(0, 0, w, h, extras, 0, w);
			 // extra is used for any interface or effect. But in case we also want to display certain blocks as extras
			// we can as well, that's why everything is also stored in extra too
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
