package myGamePack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture { // keep all textures to .png
	public int[] pixels;
	private String loc;
	public final int SIZE=64;
	public int H_SKYSIZE = Game.HEI;
	public int W_SKYSIZE = Game.WID*4;
	private static String tall_block = "tall block";
	private static String normal_block = "normal block";
	private static String sky_box = "sky box";
	private static String sprite = "sprite";
	private static String menu = "menu";
	private static String floor = "floor"; // ceiling textures are also used with floor textures so both fall under same category
	
	// Blocks should be SIZE pixels height and SIZE pixels width
	public static Texture null_texture = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\null.png", normal_block);
	public static Texture wood_block = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\wood.png", normal_block);
	public static Texture wood_door = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\wood_door.png", normal_block);
	public static Texture tall_wood = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\tall_wood.png", tall_block);
	public static Texture grass_floor = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\grass_texture.png", floor);
	public static Texture water = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\water.png", floor);
	public static Texture ceiling = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\ceil.png", floor);
	public static Texture c1 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors0.png", floor);
	public static Texture c2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors1.png", floor);
	public static Texture c3 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors2.png", floor);
	public static Texture c4 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors3.png", floor);
	public static Texture c5 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors4.png", floor);
	public static Texture c6 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors5.png", floor);
	public static Texture c7 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\colors6.png", floor);
	
	// skybox. Should be 4 times width of screen resolution and same height
	public static Texture skybox = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\skybox.png", sky_box);
	
	// Keep in mind, black in sprites mean invisible color so purely black pixels of sprites will not be drawn on screen
	// animation sprites should individually be uploaded here. sprites should have same dimension as block textures
	public static Texture tree = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\tree.png", sprite);
	public static Texture grass = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\grass.png", sprite);
	public static Texture star0 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_00.png", sprite);
	public static Texture star1 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_01.png", sprite);
	public static Texture star2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_02.png", sprite);
	public static Texture star3 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_03.png", sprite);
	public static Texture star4 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_04.png", sprite);
	public static Texture star5 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_05.png", sprite);
	public static Texture star6 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_06.png", sprite);
	public static Texture star7 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_07.png", sprite);
	public static Texture star8 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_08.png", sprite);
	public static Texture star9 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_09.png", sprite);
	public static Texture star10 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_10.png", sprite);
	public static Texture star11 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_11.png", sprite);
	public static Texture star12 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_12.png", sprite);
	public static Texture star13 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_13.png", sprite);
	public static Texture star14 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_14.png", sprite);
	public static Texture star15 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_15.png", sprite);
	public static Texture star16 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_16.png", sprite);
	public static Texture star17 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_17.png", sprite);
	public static Texture star18 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\sprite_star_18.png", sprite);
	
	
	// menu pictures. They should be the exact width and height of the screen
	public static Texture menu_background = new Texture("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\menu.png", menu);
	
	public Texture(String location, String type) {
		loc = location;
		if (type.equals(tall_block)) pixels = new int[SIZE * 2 * SIZE];
		else if (type.equals(normal_block) || type.equals(floor) || type.equals(sprite)) pixels = new int[SIZE * SIZE];
		else if (type.equals(sky_box)) pixels = new int[(Game.WID*4) * Game.HEI];
		else if (type.equals(menu)) pixels = new int[Game.WID * Game.HEI];  
		load();
	}

	private void load() {
		try {
			BufferedImage image = ImageIO.read(new File(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
