package myGamePack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture { // keep all textures to .png, and here is where you have them. You have to change the setting from "normal" block or tall block, depending on the texture you want manually here
	public int[] pixels;
	private String loc;
	public int SIZE=64;
	public int H_SKYSIZE = Game.HEI;
	public int W_SKYSIZE = Game.WID*4;
	private static String tall_sprite = "tall sprite";
	private static String normal_block = "normal block";
	private static String sky_box = "sky box";
	private static String sprite = "sprite";
	private static String menu = "menu";
	private static String floor = "floor"; // ceiling textures are also used with floor textures so both fall under same category
	
	public static Texture _1 = new Texture("res/null0.png", normal_block);
	//public static Texture _2 = new Texture("res/block17.png", tall_block);
	public static Texture _3 = new Texture("res/block18.png", normal_block);
	public static Texture _4 = new Texture("res/block19.png", normal_block);
	public static Texture _5 = new Texture("res/block20.png", normal_block);
	//public static Texture _6 = new Texture("res/block21.png", tall_block);
	public static Texture _7 = new Texture("res/block22.png", normal_block);
	public static Texture _8 = new Texture("res/block23.png", normal_block);
	public static Texture _9 = new Texture("res/block24.png", normal_block);
	public static Texture _10 = new Texture("res/block25.png", normal_block);
	public static Texture _11 = new Texture("res/block26.png", normal_block);
	public static Texture _12 = new Texture("res/block27.png", normal_block);
	public static Texture _13 = new Texture("res/block28.png", normal_block);
	//public static Texture _14 = new Texture("res/block29.png", tall_block);
	public static Texture _15 = new Texture("res/block30.png", normal_block);
	public static Texture _16 = new Texture("res/block31.png", normal_block);
	//public static Texture _17 = new Texture("res/block32.png", tall_block);
	public static Texture _18 = new Texture("res/block33.png", normal_block);
	/*
	 * public static Texture _19 = new Texture("res/block34.png", tall_block);
	 * public static Texture _20 = new Texture("res/block35.png", tall_block);
	 * public static Texture _21 = new Texture("res/block36.png", tall_block);
	 */
	public static Texture _22 = new Texture("res/block37.png", normal_block);
	public static Texture _23 = new Texture("res/block38.png", normal_block);
	public static Texture _24 = new Texture("res/block39.png", normal_block);
	public static Texture _25 = new Texture("res/block40.png", normal_block);
	public static Texture _26 = new Texture("res/block41.png", normal_block);
	public static Texture _27 = new Texture("res/block42.png", normal_block);
	public static Texture _28 = new Texture("res/block43.png", normal_block);
	public static Texture _29 = new Texture("res/block44.png", normal_block);
	public static Texture _30 = new Texture("res/block45.png", normal_block);
	public static Texture _31 = new Texture("res/block46.png", normal_block);
	public static Texture _32 = new Texture("res/block47.png", normal_block);
	public static Texture _33 = new Texture("res/block48.png", normal_block);
	public static Texture _34 = new Texture("res/block49.png", normal_block);
	public static Texture _35 = new Texture("res/block50.png", normal_block);
	public static Texture _36 = new Texture("res/block51.png", normal_block);
	public static Texture _37 = new Texture("res/block52.png", normal_block);
	public static Texture _38 = new Texture("res/block53.png", normal_block);
	public static Texture _39 = new Texture("res/block54.png", normal_block);
	public static Texture _40 = new Texture("res/block55.png", normal_block);
	public static Texture _41 = new Texture("res/block56.png", normal_block);
	public static Texture _42 = new Texture("res/block57.png", normal_block);
	public static Texture _43 = new Texture("res/block58.png", normal_block);
	public static Texture _44 = new Texture("res/block59.png", normal_block);
	public static Texture _45 = new Texture("res/block60.png", normal_block);
	public static Texture _46 = new Texture("res/block61.png", normal_block);
	public static Texture _48 = new Texture("res/block63.png", normal_block);
	public static Texture _49 = new Texture("res/block64.png", normal_block);
	public static Texture _50 = new Texture("res/sprite1.png", sprite);
	public static Texture _51 = new Texture("res/sprite2.png", sprite);
	public static Texture _52 = new Texture("res/sprite3.png", tall_sprite);
	public static Texture _53 = new Texture("res/sprite4.png", tall_sprite);
	public static Texture _54 = new Texture("res/sprite5.png", sprite);
	public static Texture _55 = new Texture("res/sprite6.png", sprite);
	public static Texture _56 = new Texture("res/sprite7.png", sprite);
	public static Texture _57 = new Texture("res/sprite8.png", sprite);
	public static Texture _58 = new Texture("res/sprite9.png", sprite);
	public static Texture _59 = new Texture("res/sprite10.png", sprite);
	public static Texture _60 = new Texture("res/sprite11.png", tall_sprite);
	public static Texture _61 = new Texture("res/sprite12.png", tall_sprite);
	public static Texture _62 = new Texture("res/sprite13.png", sprite);
	public static Texture _63 = new Texture("res/sprite14.png", sprite);
	public static Texture _64 = new Texture("res/sprite15.png", sprite);
	public static Texture _65 = new Texture("res/sprite16.png", sprite);
	public static Texture _66 = new Texture("res/skybox1.png", sky_box);
	public static Texture _67 = new Texture("res/skybox2.png", sky_box);
	public static Texture _68 = new Texture("res/skybox3.png", sky_box);
	public static Texture _69 = new Texture("res/skybox4.png", sky_box);
	public static Texture _70 = new Texture("res/skybox5.png", sky_box);
	public static Texture _71 = new Texture("res/skybox6.png", sky_box);
	public static Texture _72 = new Texture("res/skybox7.png", sky_box);
	public static Texture _73 = new Texture("res/skybox8.png", sky_box);
	public static Texture _74 = new Texture("res/skybox9.png", sky_box);
	public static Texture _75 = new Texture("res/skybox10.png", sky_box);
	public static Texture _76 = new Texture("res/skybox11.png", sky_box);
	public static Texture _77 = new Texture("res/skybox12.png", sky_box);
	public static Texture _78 = new Texture("res/skybox13.png", sky_box);
	public static Texture _79 = new Texture("res/skybox14.png", sky_box);
	public static Texture _80 = new Texture("res/skybox15.png", sky_box);
	public static Texture _81 = new Texture("res/skybox16.png", sky_box);
	public static Texture _82 = new Texture("res/ceiling17.png", floor);
	public static Texture _83 = new Texture("res/ceiling18.png", floor);
	public static Texture _84 = new Texture("res/ceiling19.png", floor);
	public static Texture _85 = new Texture("res/ceiling20.png", floor);
	public static Texture _86 = new Texture("res/ceiling21.png", floor);
	public static Texture _87 = new Texture("res/ceiling22.png", floor);
	public static Texture _88 = new Texture("res/ceiling23.png", floor);
	public static Texture _89 = new Texture("res/ceiling24.png", floor);
	public static Texture _90 = new Texture("res/ceiling25.png", floor);
	public static Texture _91 = new Texture("res/ceiling26.png", floor);
	public static Texture _92 = new Texture("res/ceiling27.png", floor);
	public static Texture _93 = new Texture("res/ceiling28.png", floor);
	public static Texture _94 = new Texture("res/ceiling29.png", floor);
	public static Texture _95 = new Texture("res/ceiling30.png", floor);
	public static Texture _96 = new Texture("res/ceiling31.png", floor);
	public static Texture _97 = new Texture("res/ceiling32.png", floor);
	
	// menu pictures. They should be the exact width and height of the screen
	public static Texture menu_background = new Texture("res/menu.png", menu);
	
	public Texture(String location, String type) {
		loc = location;
		if (type.equals(tall_sprite)) {pixels = new int[256*256]; SIZE= 256;}
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
