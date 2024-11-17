package myGamePack;

import java.util.Properties;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.AbstractMap.SimpleEntry;

public class Game extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public String game_title = "Raccoon Engine";
	public static String username = "Rafa";
	public static int WID = 800;
	public static int HEI = 600;
	//public static boolean generate_new_world = false;
	public static double FPS = 60.0;
	public static int MAX_WORLD_LIMIT = 500;
	//public static int SEED = -1; // Between 0 and 2147483647, set to -1 for using your own world. Rn, we are only going to leave biome gen for legacy but in prod we dont use this.
	public int fog_col = 0x898557; 
	public int animation_speed = 135;
	public boolean in_doors = true;
	public int default_tile_number = 1000;
	public int ceiling_structure_number = 1102;
	public int render_distance = 15;
	public boolean sky_self_movement = true;
	public static boolean daynight_cycle = true;
	public static double daynight_cycle_startat = 0;
	public static int daynight_cycle_seconds = 0;
	
	public static int skybox_id_morning=76;
	public static int skybox_id_noon=77;
	public static int skybox_id_afternoon=78;
	public static int skybox_id_evening=79;
	public static int skybox_id_night=71;
	public static int skybox_id_dawn=70;
	public static int skybox_id_default=70;
	
	
	public int retro_feel = 2;
	public static int special_sprite_specialtile_number = -1999;
	public int numberSprites = 0;
	public Sprite[] spriteArray;
	public int animation_lengths = 19;
	public int[] pixels;
	public ArrayList<Texture> textures;
	public Camera camera;
	public static Screen screen;
	public static int[][] map = new int[MAX_WORLD_LIMIT][MAX_WORLD_LIMIT];
	private int mapWidth = map[0].length;
	private int mapHeight = map.length;
	public SimpleEntry<Integer, Integer>[] floor_tile_position_array = new SimpleEntry[mapWidth*mapHeight];
	public Texture mapSkybox_morning;
	public Texture mapSkybox_noon;
	public Texture mapSkybox_afternoon;
	public Texture mapSkybox_evening;
	public Texture mapSkybox_night;
	public Texture mapSkybox_dawn;
	public Texture mapSkybox_default;
	public static int number_of_daytimes = 6;
	private int TEX_MAX = 2000;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private ArrayList<SimpleEntry<Integer, Integer>> lookup_id = new ArrayList<SimpleEntry<Integer, Integer>>();
	public Game() {
		thread = new Thread(this);
		image = new BufferedImage(WID, HEI, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		init_textures_for_render(); 
		//System.out.println(SEED);
		camera = new Camera(mapWidth/2, mapHeight/2, 1, 0, 0, -.66, "res/game.wav");
		Texture skyboxes[] = new Texture[number_of_daytimes];
		skyboxes[0] = mapSkybox_night;
		skyboxes[1] = mapSkybox_dawn;
		skyboxes[2] = mapSkybox_evening;
		skyboxes[3] = mapSkybox_afternoon;
		skyboxes[4] = mapSkybox_noon;
		skyboxes[5] = mapSkybox_morning;
		
		screen = new Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col, skyboxes, mapSkybox_default, numberSprites, spriteArray, animation_lengths, sky_self_movement, in_doors, floor_tile_position_array, ceiling_structure_number, render_distance, default_tile_number);
		addKeyListener(camera);
		setSize(WID, HEI);
		setResizable(false);
		//setUndecorated(true);
		setTitle(game_title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		
		start();
	}
	
	// algorithm for random map generation
	public int[][] generate_world(int[][] map_matrix, int[] world_vals, int[] sprite_vals) {
		
	
		// Right here we want to load up the world
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("res/configs.cfg")) {
            properties.load(reader);
            
            // Accessing the properties
            game_title = properties.getProperty("game_title");
            retro_feel = Integer.parseInt(properties.getProperty("pixel_effect"));
            fog_col = Integer.parseInt(properties.getProperty("fog_col").substring(2), 16); 
            in_doors = Boolean.parseBoolean(properties.getProperty("in_doors"));
            render_distance = Integer.parseInt(properties.getProperty("render_distance"));
            sky_self_movement = Boolean.parseBoolean(properties.getProperty("sky_self_movement"));
            
            default_tile_number = Integer.parseInt(properties.getProperty("default_tile_id"));
            ceiling_structure_number = Integer.parseInt(properties.getProperty("ceiling_tile_id"));
            username = properties.getProperty("username");
            
            daynight_cycle = Boolean.parseBoolean(properties.getProperty("daynight_cycle"));
            daynight_cycle_startat = Double.parseDouble(properties.getProperty("daynight_cycle_brightness_start_perc"));
            if (daynight_cycle_startat<0.2) {
            	System.out.println("You must provide a daynight_cycle_brightness_start_perc bigger than 0.2!");
            	System.exit(0);       	
            }
            daynight_cycle_seconds = 10000-Integer.parseInt(properties.getProperty("daynight_speed"));
            if (daynight_cycle_seconds < 0) {
            	System.out.println("You must provide a daynight_speed less than 10000!");
            	System.exit(0);
            }

            
            
            // Quick lookup cuz im too lazy for something that actually more efficient so who cares
            ArrayList<SimpleEntry<Integer, Texture>> skybox_lookup_code = new ArrayList<SimpleEntry<Integer, Texture>>();
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(66, Texture._66));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(67, Texture._67));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(68, Texture._68));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(69, Texture._69));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(70, Texture._70));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(71, Texture._71));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(72, Texture._72));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(73, Texture._73));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(74, Texture._74));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(75, Texture._75));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(76, Texture._76));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(77, Texture._77));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(78, Texture._78));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(79, Texture._79));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(80, Texture._80));
            skybox_lookup_code.add(new SimpleEntry<Integer, Texture>(81, Texture._81));
            
            //int skybox_id = Integer.parseInt(properties.getProperty("skybox_id"));
            
            skybox_id_morning = Integer.parseInt(properties.getProperty("skybox_id_morning"));
            skybox_id_noon = Integer.parseInt(properties.getProperty("skybox_id_noon"));
            skybox_id_afternoon = Integer.parseInt(properties.getProperty("skybox_id_afternoon"));
            skybox_id_evening = Integer.parseInt(properties.getProperty("skybox_id_evening"));
            skybox_id_night = Integer.parseInt(properties.getProperty("skybox_id_night"));
            skybox_id_dawn = Integer.parseInt(properties.getProperty("skybox_id_dawn"));
            skybox_id_default = Integer.parseInt(properties.getProperty("skybox_default_id"));
            
            for (SimpleEntry<Integer, Texture> entry : skybox_lookup_code) {
                if (entry.getKey() == skybox_id_morning) {
                	mapSkybox_morning = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_noon) {
                	mapSkybox_noon = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_afternoon) {
                	mapSkybox_afternoon = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_evening) {
                	mapSkybox_evening = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_night) {
                	mapSkybox_night = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_dawn) {
                	mapSkybox_dawn = entry.getValue();  // Return the corresponding texture value
                     
                }
                if (entry.getKey() == skybox_id_default) {
                	mapSkybox_default = entry.getValue();  // Return the corresponding texture value
                     
                }
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
			
        
        
        
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0000, 1));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff1300, 2));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff2600, 4));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff3900, 5));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff4c00, 7));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff5f00, 9));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff7200, 12));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff8500, 13));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff9900, 15));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xffac00, 17));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xffbf00, 19));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xffd200, 21));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xffe500, 23));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xfff800, 25));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xf2ff00, 26));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xdfff00, 29));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xcbff00, 31));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xb8ff00, 32));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xa5ff00, 35));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x92ff00, 36));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x7fff00, 38));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x6cff00, 41));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x59ff00, 43));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x46ff00, 45));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x32ff00, 47));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x1fff00, 49));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0cff00, 51));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff06, 53));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff19, 55));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff2c, 57));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff3f, 59));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff52, 61));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff66, 63));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff79, 65));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff8c, 67));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ff9f, 69));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ffb2, 71));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ffc5, 73));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ffd8, 75));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ffeb, 77));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00feff, 79));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00ebff, 81));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00d8ff, 83));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00c5ff, 85));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x00b2ff, 87));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x009fff, 89));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x008cff, 92));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0079ff, 93));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0065ff, 95));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0052ff, -1));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x003fff, -5));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x002cff, -7));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0019ff, -9));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0006ff, -13));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0c00ff, -15));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x1f00ff, -17));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x3200ff, -19));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x4600ff, -21));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x5900ff, -23));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x6c00ff, -25));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x7f00ff, -29));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x9200ff, -31));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xa500ff, -35));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xb800ff, -41));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xcb00ff, -43));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xdf00ff, -45));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xf200ff, -47));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff00f8, -49));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff00e5, -51));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff00d2, -53));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff00bf, -55));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff00ac, -57));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0098, -59));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0085, -61));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0072, -63));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff005f, -65));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff004c, -67));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0039, -69));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0026, -71));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xff0013, -73));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x000001, -75));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x0a0a0a, -77));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x141414, -79));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x1e1e1e, -81));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x282828, -83));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x333333, -85));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x3d3d3d, -87));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x474747, -89));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x515151, -93));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x5b5b5b, -95));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x666666, 700));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x707070, 701));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x7a7a7a, 702));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x848484, 703));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x8e8e8e, 704));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0x999999, 705));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xa3a3a3, 706));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xadadad, 707));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xb7b7b7, 708));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xc1c1c1, 709));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xcccccc, 710));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xd6d6d6, 711));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xe0e0e0, 712));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xeaeaea, 713));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xf4f4f4, 714));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xfffff9, 715));
        lookup_id.add(new SimpleEntry<Integer, Integer>(0xffffff, 999));
        
        // Now read the pngs and get the hex values
        // Then look them up here
        
        
        
        // Path to the image file
        String imagePath = "res/layer0.png";
        
        
        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            //System.out.print(image.getHeight());
            
            // Loop through each pixel and get the RGB values
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    // Get the RGB value of the pixel
                    int rgb = image.getRGB(x, y);

                    // Convert RGB to hexadecimal
                    String hex = String.format("#%06X", (0xFFFFFF & rgb));
                    //System.out.println(hex);
                    // Store the hex value in the 2D array
                    for (SimpleEntry<Integer, Integer> entry : lookup_id) {
                        if (entry.getKey() == Integer.parseInt(hex.substring(1), 16)) {
                        	map_matrix[y][x] = entry.getValue();  // Return the corresponding texture value
                        	
                        	break;
                        }
                    }
                    
                }
            }
            
            

			 
            

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        
        
        
        
			//map_matrix[245][245]=-5; 
			
			// SO SPRITES MUST BE DRAWN AT THE BOTTOM
			
			
		 /*
			 * else {
			 * 
			 * 
			 * 
			 * 
			 * // World gen. To generate the floors we simply add 1000 to our regular world
			 * blocks int biome = SEED%9; // since there are 32 biomes if (biome==0) { //
			 * UNDERWORLD; for (int row = 0; row < mapHeight; row++) { for (int col = 0; col
			 * < mapWidth; col++) { if (!(row==0 || col==0 || row==mapHeight-1 ||
			 * col==mapWidth-1)) { if (r.nextInt(100) == 0) { map[row][col]=sprite_vals[0];
			 * } else { map[row][col]= 0; }
			 * 
			 * } } } default_tile_number = 1059-1; //you need to subs 1 for the offset. this
			 * is the default tile in the world fog_col = 0x000000; mapSkybox = Texture._66;
			 * sky_self_movement = false; in_doors = true; ceiling_structure_number = 1113;
			 * render_distance = 100; } else if (biome==1) { //HEAVENS for (int row = 0; row
			 * < mapHeight; row++) { for (int col = 0; col < mapWidth; col++) { if (!(row==0
			 * || col==0 || row==mapHeight-1 || col==mapWidth-1)) { map[row][col]= 0;
			 * 
			 * } } } default_tile_number = 1027-1; fog_col = 0x76f3ff; mapSkybox =
			 * Texture._80; sky_self_movement = false; in_doors = false;
			 * ceiling_structure_number = 1111; render_distance = 20; }else if (biome==2) {
			 * //PLAINS for (int row = 0; row < mapHeight; row++) { for (int col = 0; col <
			 * mapWidth; col++) { if (!(row==0 || col==0 || row==mapHeight-1 ||
			 * col==mapWidth-1)) { if (r.nextInt(10) == 0) { map[row][col]=sprite_vals[14];
			 * } else { map[row][col]= 0; }
			 * 
			 * } } } for (int i=0; i < r.nextInt(4000)+100; i++) { int spot_on_map_x =
			 * r.nextInt(mapHeight); if (spot_on_map_x<=5) spot_on_map_x=5; if
			 * (spot_on_map_x>=mapHeight-5) spot_on_map_x=mapHeight-5; int spot_on_map_y =
			 * r.nextInt(mapWidth); if (spot_on_map_y<=5) spot_on_map_y=5; if
			 * (spot_on_map_y>=mapWidth-5) spot_on_map_y=mapWidth-5;
			 * 
			 * if (r.nextInt(2)==0) map[spot_on_map_y][spot_on_map_x] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+1] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+1] = sprite_vals[13];
			 * 
			 * if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+2] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+2] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+2] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+1] = sprite_vals[13];
			 * 
			 * if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+3] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+3] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+3] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+2] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+1] = sprite_vals[13]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+3] = sprite_vals[13]; }
			 * 
			 * 
			 * default_tile_number = 1029-1; fog_col = 0xb0f1ff; mapSkybox = Texture._76;
			 * sky_self_movement = true; in_doors = false; ceiling_structure_number = 1111;
			 * render_distance = 25; }else if (biome==3) { //MARS for (int row = 0; row <
			 * mapHeight; row++) { for (int col = 0; col < mapWidth; col++) { if (!(row==0
			 * || col==0 || row==mapHeight-1 || col==mapWidth-1)) { if (r.nextInt(200) == 0)
			 * { map[row][col]=sprite_vals[15]; } else { map[row][col]= 0; }
			 * 
			 * } } } default_tile_number = 1031-1; fog_col = 0x000000; mapSkybox =
			 * Texture._81; sky_self_movement = true; in_doors = false;
			 * ceiling_structure_number = 1111; render_distance = 35; } else if (biome==4) {
			 * // THE CITY
			 * 
			 * int apart = r.nextInt(3, 6); for (int row = 0; row < mapHeight; row++) { for
			 * (int col = 0; col < mapWidth; col++) { if (!(row==0 || col==0 ||
			 * row==mapHeight-1 || col==mapWidth-1)) { if (row % apart == 0 && col % apart
			 * == 0) { map[row][col]=world_vals[0]; } else { map[row][col]= 0; }
			 * 
			 * } } }
			 * 
			 * 
			 * default_tile_number = 1057-1; fog_col = 0x122d65; mapSkybox = Texture._71;
			 * sky_self_movement = false; in_doors = false; ceiling_structure_number = 1111;
			 * render_distance = 55;
			 * 
			 * 
			 * }else if (biome==5) { //ELEVATOR WORLD for (int row = 0; row < mapHeight;
			 * row++) { for (int col = 0; col < mapWidth; col++) { if (!(row==0 || col==0 ||
			 * row==mapHeight-1 || col==mapWidth-1)) { if (r.nextInt(5) == 0) {
			 * map[row][col]=world_vals[47]; } else { map[row][col]= 0; }
			 * 
			 * } } } default_tile_number = 1083-1; fog_col = 0x4f90ac; mapSkybox =
			 * Texture._79; sky_self_movement = true; in_doors = false;
			 * ceiling_structure_number = 1101; render_distance = 10; }else if (biome==6) {
			 * //EYE WORLD for (int row = 0; row < mapHeight; row++) { for (int col = 0; col
			 * < mapWidth; col++) { if (!(row==0 || col==0 || row==mapHeight-1 ||
			 * col==mapWidth-1)) { if (r.nextInt(50) == 0) { map[row][col]=sprite_vals[12];
			 * } else { map[row][col]= 0; }
			 * 
			 * } } } default_tile_number = 1059-1; fog_col = 0x5e0000; mapSkybox =
			 * Texture._81; sky_self_movement = false; in_doors = true;
			 * ceiling_structure_number = 1101; render_distance = 10; }else if (biome==7) {
			 * //DESERT for (int row = 0; row < mapHeight; row++) { for (int col = 0; col <
			 * mapWidth; col++) { if (!(row==0 || col==0 || row==mapHeight-1 ||
			 * col==mapWidth-1)) { if (r.nextInt(200) == 0) { map[row][col]=world_vals[43];
			 * } else { map[row][col]= 0; }
			 * 
			 * } } } for (int i=0; i < r.nextInt(2000)+100; i++) { int spot_on_map_x =
			 * r.nextInt(mapHeight); if (spot_on_map_x<=5) spot_on_map_x=5; if
			 * (spot_on_map_x>=mapHeight-5) spot_on_map_x=mapHeight-5; int spot_on_map_y =
			 * r.nextInt(mapWidth); if (spot_on_map_y<=5) spot_on_map_y=5; if
			 * (spot_on_map_y>=mapWidth-5) spot_on_map_y=mapWidth-5;
			 * 
			 * if (r.nextInt(2)==0) map[spot_on_map_y][spot_on_map_x] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+1] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+1] = world_vals[43];
			 * 
			 * if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+2] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+2] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+2] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+1] = world_vals[43];
			 * 
			 * if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+3] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+3] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+3] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+2] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+1] = world_vals[43]; if
			 * (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+3] = world_vals[43]; }
			 * 
			 * 
			 * default_tile_number = 1055-1; fog_col = 0xd0ca7d; mapSkybox = Texture._78;
			 * sky_self_movement = false; in_doors = false; ceiling_structure_number = 1111;
			 * render_distance = 100; }else if (biome==8) { //BACKROOMS CLASSIC for (int row
			 * = 0; row < mapHeight; row++) { for (int col = 0; col < mapWidth; col++) { if
			 * (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
			 * map[row][col]=world_vals[7]; } } }
			 * 
			 * for (int row = 0; row < mapHeight; row++) { for (int col = 0; col < mapWidth;
			 * col++) { if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) { if
			 * (row%2==1 && col%2==1) { map[row][col]=0; if (row==1 && col!=1) {
			 * map[row][col-1]=0; } else if (col==1 && row!=1) { map[row-1][col]=0; } else {
			 * if (r.nextBoolean()) { map[row][col-1]=0; } else { map[row-1][col]=0; } }
			 * 
			 * }
			 * 
			 * } } }
			 * 
			 * for (int i=0; i < 4000; i++) { int spot_on_map_x = r.nextInt(mapHeight); if
			 * (spot_on_map_x<=5) spot_on_map_x=5; if (spot_on_map_x>=mapHeight-5)
			 * spot_on_map_x=mapHeight-5; int spot_on_map_y = r.nextInt(mapWidth); if
			 * (spot_on_map_y<=5) spot_on_map_y=5; if (spot_on_map_y>=mapWidth-5)
			 * spot_on_map_y=mapWidth-5;
			 * 
			 * if (r.nextInt(3)!=0) map[spot_on_map_y][spot_on_map_x] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y][spot_on_map_x+1] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+1][spot_on_map_x+1] = 0;
			 * 
			 * if (r.nextInt(3)!=0)map[spot_on_map_y+2][spot_on_map_x] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y][spot_on_map_x+2] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+2][spot_on_map_x+2] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+1][spot_on_map_x+2] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+2][spot_on_map_x+1] = 0;
			 * 
			 * if (r.nextInt(3)!=0)map[spot_on_map_y+3][spot_on_map_x] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y][spot_on_map_x+3] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+3][spot_on_map_x+3] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+2][spot_on_map_x+3] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+3][spot_on_map_x+2] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+3][spot_on_map_x+1] = 0; if
			 * (r.nextInt(3)!=0)map[spot_on_map_y+1][spot_on_map_x+3] = 0; }
			 * default_tile_number = 1063-1; fog_col = 0x636340; mapSkybox = Texture._81;
			 * sky_self_movement = false; in_doors = true; ceiling_structure_number = 1102;
			 * render_distance = 30; }
			 * 
			 * 
			 * 
			 * }
			 */
        
        Random r = new Random();
		
		// after everything, let's populate the huge map with the border
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				if (row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1) {
					map_matrix[row][col]=world_vals[r.nextInt(world_vals.length)];
				}
			}
		}
        
		//map_matrix[(int)(mapHeight/2)][(int)(mapWidth/2)] = 0;
		return map_matrix;
	}
	
	// Here we can init all the textures to each cell AND we also call the algorithm for generating a new map
	public void init_textures_for_render() {
		
		
		ArrayList<SimpleEntry<Integer, Texture>> index_to_texture_array = new ArrayList<SimpleEntry<Integer, Texture>>();
		// For blocks, just use odd numbers for now due to a legacy problem. Values 1 to 700
		// for sprites is 700 to 800
		// and for animating sprites is 800 up to and including 998
		// draw your tiles in the map for values 0 or less. So negative values are the tiles.
		// floor tiles the texture array at: 1000  -1 for the offeset of 0 in arrays. Keep adding after 1000 as you see fit. To get right texture is abs(
		// neg tile number) + 1000 -1
		// special values: 0 -> no wall at all, nothing
		// 999: invisible wall
		// keep in mind 1000 is the default floor tile to draw the map with, most common tile in game
		// DO NOT DRAW SPRITES YET. DO THEM AT THE BOTTOM
		 // So this number is only used here for the default, if you want draw a block use the 25 1 to 100 thing etc. If you want to draw a tile, do this fore ex say 1005 block: -5
		// So basically for example 1007-1 is just the value you use for default. and -7 is for drawing the tile.
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1, Texture._1));

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(5, Texture._3)); //2
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(7, Texture._4)); //3
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(9, Texture._5)); //4

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(13, Texture._7)); //6
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(15, Texture._8)); //7
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(17, Texture._9)); //8
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(19, Texture._10)); //9
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(21, Texture._11)); //10
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(23, Texture._12)); //11
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(25, Texture._13)); //12

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(29, Texture._15)); //14
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(31, Texture._16)); //15

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(35, Texture._18)); //17

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(41, Texture._22)); //20
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(43, Texture._23)); //21
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(45, Texture._24)); //22
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(47, Texture._25)); //23
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(49, Texture._26)); //24
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(51, Texture._27)); //25
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(53, Texture._28)); //26
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(55, Texture._29)); //27
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(57, Texture._30)); //28
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(59, Texture._31)); //29
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(61, Texture._32)); //30
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(63, Texture._33)); //31
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(65, Texture._34)); //32
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(67, Texture._35)); //33
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(69, Texture._36)); //34
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(71, Texture._37)); //35
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(73, Texture._38)); //36
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(75, Texture._39)); //37
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(77, Texture._40)); //38
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(79, Texture._41)); //39
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(81, Texture._42)); //40
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(83, Texture._43)); //41
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(85, Texture._44)); //42
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(87, Texture._45)); //43
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(89, Texture._46)); //44

		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(93, Texture._48)); //46
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(95, Texture._49)); //47
		
		int len_of_blocktexts = index_to_texture_array.size()-1; // dont count the null
		
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(700, Texture._50));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(701, Texture._51));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(702, Texture._52));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(703, Texture._53));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(704, Texture._54));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(705, Texture._55));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(706, Texture._56));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(707, Texture._57));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(708, Texture._58));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(709, Texture._59));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(710, Texture._60));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(711, Texture._61));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(712, Texture._62));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(713, Texture._63));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(714, Texture._64));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(715, Texture._65));
		
		int number_of_sprites = index_to_texture_array.size()-len_of_blocktexts;
		
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1100, Texture._82)); // ceilings. Don't use for drawing floor
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1101, Texture._83));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1102, Texture._84));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1103, Texture._85));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1104, Texture._86));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1105, Texture._87));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1106, Texture._88));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1107, Texture._89));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1108, Texture._90));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1109, Texture._91));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1110, Texture._92));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1111, Texture._93));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1112, Texture._94));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1113, Texture._95));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1114, Texture._96));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1115, Texture._97));
		
		// here are the floor tiles (which are just duplicates of regular blocks) except for the tall blocks
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1001, Texture._1));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1005, Texture._3));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1007, Texture._4));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1009, Texture._5));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1013, Texture._7));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1015, Texture._8));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1017, Texture._9));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1019, Texture._10));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1021, Texture._11));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1023, Texture._12));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1025, Texture._13));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1029, Texture._15));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1031, Texture._16));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1035, Texture._18));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1041, Texture._22));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1043, Texture._23));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1045, Texture._24));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1047, Texture._25));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1049, Texture._26));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1051, Texture._27));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1053, Texture._28));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1055, Texture._29));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1057, Texture._30));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1059, Texture._31));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1061, Texture._32));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1063, Texture._33));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1065, Texture._34));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1067, Texture._35));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1069, Texture._36));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1071, Texture._37));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1073, Texture._38));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1075, Texture._39));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1077, Texture._40));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1079, Texture._41));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1081, Texture._42));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1083, Texture._43));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1085, Texture._44));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1087, Texture._45));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1089, Texture._46));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1093, Texture._48));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1095, Texture._49));
		
		
		//this array will be used to randomly pick the world borders textures. Used in generation of world function
		int[] world_values = new int[len_of_blocktexts]; //40 is the number of block textures
		int[] sprite_values = new int[number_of_sprites]; // number codes for the sprites
		
		textures = new ArrayList<Texture>(TEX_MAX);
		while(textures.size() < TEX_MAX) textures.add(Texture._1);
		int world_index = 0;
		int sprite_index=0;
		for (SimpleEntry<Integer, Texture> index_to_text : index_to_texture_array) {
			textures.set(index_to_text.getKey()-1, index_to_text.getValue());
			// add each relevant getKey to an array
			if (index_to_text.getKey() < 700 && index_to_text.getKey() > 1) { // if it's a block (not including the null block)
				world_values[world_index]=index_to_text.getKey();
				world_index++;
			}
			if (index_to_text.getKey() >= 700 && index_to_text.getKey() < 800) { // if it's a sprite, then put it in this array
				sprite_values[sprite_index]=index_to_text.getKey();
				sprite_index++;
			}
		}
		
		//int seed = SEED; // WHAT SEED SHOULD THE WORLD USE TO GENERATE?
		map = generate_world(map, world_values, sprite_values);
		
		
		// this array contains all the floor tiles positions so they can be drawn in screen later
				// 0 is the default tile
				int f = 0;
					for (int rowf = 0; rowf < mapHeight; rowf++) {
						for (int colf = 0; colf < mapWidth; colf++) {
							if (map[rowf][colf] < 0) {
								floor_tile_position_array[f] = new SimpleEntry<Integer, Integer>(rowf, colf);
								f++;
							}
						}
				floor_tile_position_array[f] = new SimpleEntry<Integer, Integer>(-1, -1);
				}
		
		
		
		
		// now here create draw the sprites. These sprites just draw them on the map itself it's fine.
		// DRAW SPRITES HERE
				     // Path to the image file
			        String imagePath = "res/layer1.png";
			        
			        
			        try {
			            // Read the image
			            BufferedImage image = ImageIO.read(new File(imagePath));

			            //System.out.print(image.getHeight());
			            
			            // Loop through each pixel and get the RGB values
			            for (int y = 0; y < image.getHeight(); y++) {
			                for (int x = 0; x < image.getWidth(); x++) {
			                    // Get the RGB value of the pixel
			                    int rgb = image.getRGB(x, y);

			                    // Convert RGB to hexadecimal
			                    String hex = String.format("#%06X", (0xFFFFFF & rgb));
			                    //System.out.println(hex);
			                    // Store the hex value in the 2D array
			                    for (SimpleEntry<Integer, Integer> entry : lookup_id) {
			                        if (entry.getKey() == Integer.parseInt(hex.substring(1), 16)) {
			                        	
			                        	if (map[y][x] < 0) { // lazy coding. This is terrible you should never do this not pro at all. Just setting a specific number to save the sprite and the special tile. but idc
			                        		String resultString = Integer.toString(special_sprite_specialtile_number) + Integer.toString(entry.getValue()) + Integer.toString(Math.abs(map[y][x])); // So from 715 sprite and -23 as special tile let's say, it becomes 199971523
			                        		map[y][x] = Integer.parseInt(resultString);
			    
			                        	} else {
			                        		map[y][x] = entry.getValue();
			                        	}
			                        	
			                        	  // Return the corresponding texture value
			                        	
			                        	break;
			                        }
			                    }
			                    
			                }
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
			            

		Sprite[] tempSpriteArray = new Sprite[mapWidth*mapHeight];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				if ((map[row][col] > 699 && map[row][col] < 998) || (map[row][col] < special_sprite_specialtile_number)) {
					tempSpriteArray[numberSprites] = new Sprite(row, col);
					numberSprites++;// count how many sprites are on the map. 
				}
			} 
		}
		spriteArray = new Sprite[numberSprites];
		for (int k=0; k < numberSprites; k++) {
			spriteArray[k] = tempSpriteArray[k];
		}
		
	}
	
	private synchronized void start() {
		running = true;
		thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / FPS;//60 times per second
		double delta = 0;
		requestFocus();
		int frame_num = 0;
		int animation_delay = 0;
		//System.out.println("> Playing main menu music...");
		Sound.playSound("res/menu.wav");
		//System.out.println("> Main Menu finished loading.");
		while(running) {
			
			/*
			 * if (generate_new_world) { Random r = new Random(); SEED =
			 * Math.abs(r.nextInt()) % 2147483647; System.out.println(SEED); numberSprites =
			 * 0; init_textures_for_render(); camera = new Camera(mapWidth/2, mapHeight/2,
			 * 1, 0, 0, -.66, "res/game.wav"); camera.isMenu = false; screen = new
			 * Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col,
			 * mapSkybox, numberSprites, spriteArray, animation_lengths, sky_self_movement,
			 * in_doors, floor_tile_position_array, ceiling_structure_number,
			 * render_distance, default_tile_number); addKeyListener(camera);
			 * generate_new_world = false; }
			 */
			
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			if (animation_delay % animation_speed == 0) {
				frame_num = (frame_num % Integer.MAX_VALUE) + 1;
				animation_delay = (animation_delay % Integer.MAX_VALUE) + 1;
			} else {
				animation_delay = (animation_delay % Integer.MAX_VALUE) + 1;
			}
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//System.out.println(frame_num);
				//handles all of the logic restricted time
				screen.update(camera, pixels, frame_num);
				camera.update(map);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
		
	}
	public static void main(String [] args) {
		
		System.out.println("> Starting your world... Welcome!");
		
		Game game = new Game();
		
		// TESTING MULTIPLAYER
			Client c = new Client("localhost", 8080, username);
				Thread clientThread = new Thread(c);
		        clientThread.start();
	}
}