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
	
	public static Texture _1 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\null0.png", normal_block);
	public static Texture _2 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block17.png", normal_block);
	public static Texture _3 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block18.png", normal_block);
	public static Texture _4 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block19.png", normal_block);
	public static Texture _5 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block20.png", normal_block);
	public static Texture _6 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block21.png", normal_block);
	public static Texture _7 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block22.png", normal_block);
	public static Texture _8 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block23.png", normal_block);
	public static Texture _9 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block24.png", normal_block);
	public static Texture _10 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block25.png", normal_block);
	public static Texture _11 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block26.png", normal_block);
	public static Texture _12 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block27.png", normal_block);
	public static Texture _13 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block28.png", normal_block);
	public static Texture _14 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block29.png", normal_block);
	public static Texture _15 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block30.png", normal_block);
	public static Texture _16 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block31.png", normal_block);
	public static Texture _17 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block32.png", normal_block);
	public static Texture _18 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block33.png", normal_block);
	public static Texture _19 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block34.png", normal_block);
	public static Texture _20 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block35.png", normal_block);
	public static Texture _21 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block36.png", tall_block);
	public static Texture _22 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block37.png", normal_block);
	public static Texture _23 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block38.png", normal_block);
	public static Texture _24 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block39.png", normal_block);
	public static Texture _25 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block40.png", normal_block);
	public static Texture _26 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block41.png", normal_block);
	public static Texture _27 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block42.png", normal_block);
	public static Texture _28 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block43.png", normal_block);
	public static Texture _29 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block44.png", normal_block);
	public static Texture _30 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block45.png", normal_block);
	public static Texture _31 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block46.png", normal_block);
	public static Texture _32 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block47.png", normal_block);
	public static Texture _33 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block48.png", normal_block);
	public static Texture _34 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block49.png", normal_block);
	public static Texture _35 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block50.png", normal_block);
	public static Texture _36 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block51.png", normal_block);
	public static Texture _37 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block52.png", normal_block);
	public static Texture _38 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block53.png", normal_block);
	public static Texture _39 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block54.png", normal_block);
	public static Texture _40 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block55.png", normal_block);
	public static Texture _41 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block56.png", normal_block);
	public static Texture _42 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block57.png", normal_block);
	public static Texture _43 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block58.png", normal_block);
	public static Texture _44 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block59.png", normal_block);
	public static Texture _45 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block60.png", normal_block);
	public static Texture _46 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block61.png", normal_block);
	public static Texture _47 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block62.png", normal_block);
	public static Texture _48 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block63.png", normal_block);
	public static Texture _49 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\block64.png", normal_block);
	public static Texture _50 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite1.png", sprite);
	public static Texture _51 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite2.png", sprite);
	public static Texture _52 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite3.png", sprite);
	public static Texture _53 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite4.png", sprite);
	public static Texture _54 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite5.png", sprite);
	public static Texture _55 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite6.png", sprite);
	public static Texture _56 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite7.png", sprite);
	public static Texture _57 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite8.png", sprite);
	public static Texture _58 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite9.png", sprite);
	public static Texture _59 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite10.png", sprite);
	public static Texture _60 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite11.png", sprite);
	public static Texture _61 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite12.png", sprite);
	public static Texture _62 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite13.png", sprite);
	public static Texture _63 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite14.png", sprite);
	public static Texture _64 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite15.png", sprite);
	public static Texture _65 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\sprite16.png", sprite);
	public static Texture _66 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox1.png", sky_box);
	public static Texture _67 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox2.png", sky_box);
	public static Texture _68 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox3.png", sky_box);
	public static Texture _69 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox4.png", sky_box);
	public static Texture _70 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox5.png", sky_box);
	public static Texture _71 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox6.png", sky_box);
	public static Texture _72 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox7.png", sky_box);
	public static Texture _73 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox8.png", sky_box);
	public static Texture _74 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox9.png", sky_box);
	public static Texture _75 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox10.png", sky_box);
	public static Texture _76 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox11.png", sky_box);
	public static Texture _77 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox12.png", sky_box);
	public static Texture _78 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox13.png", sky_box);
	public static Texture _79 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox14.png", sky_box);
	public static Texture _80 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox15.png", sky_box);
	public static Texture _81 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\skybox16.png", sky_box);
	public static Texture _82 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling17.png", floor);
	public static Texture _83 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling18.png", floor);
	public static Texture _84 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling19.png", floor);
	public static Texture _85 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling20.png", floor);
	public static Texture _86 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling21.png", floor);
	public static Texture _87 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling22.png", floor);
	public static Texture _88 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling23.png", floor);
	public static Texture _89 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling24.png", floor);
	public static Texture _90 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling25.png", floor);
	public static Texture _91 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling26.png", floor);
	public static Texture _92 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling27.png", floor);
	public static Texture _93 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling28.png", floor);
	public static Texture _94 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling29.png", floor);
	public static Texture _95 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling30.png", floor);
	public static Texture _96 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling31.png", floor);
	public static Texture _97 = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\ceiling32.png", floor);
	
	// menu pictures. They should be the exact width and height of the screen
	public static Texture menu_background = new Texture("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\menu.png", menu);
	
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
