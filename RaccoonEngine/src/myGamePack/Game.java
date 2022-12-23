package myGamePack;

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
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import java.util.AbstractMap.SimpleEntry;

public class Game extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static String game_title = "Raccoon Engine";
	public static int WID = 800;
	public static int HEI = 600;
	public static boolean generate_new_world = false;
	public static double FPS = 60.0;
	public static int SEED = 7; // Between 0 and 2147483647
	public int fog_col = 0x898557; 
	public int animation_speed = 135;
	public boolean in_doors = true;
	public int default_tile_number = 1000;
	public int ceiling_structure_number = 1002;
	public int render_distance = 15;
	public boolean sky_self_movement = true;
	public int retro_feel = 2;
	public int numberSprites = 0;
	public Sprite[] spriteArray;
	public int animation_lengths = 19;
	public int[] pixels;
	public ArrayList<Texture> textures;
	public Camera camera;
	public Screen screen;
	public static int[][] map = new int[402][402];
	private int mapWidth = map[0].length;
	private int mapHeight = map.length;
	public SimpleEntry<Integer, Integer>[] floor_tile_position_array = new SimpleEntry[mapWidth*mapHeight];
	public Texture mapSkybox;
	private int TEX_MAX = 2000;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public Game() {
		thread = new Thread(this);
		image = new BufferedImage(WID, HEI, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		init_textures_for_render(); 
		System.out.println(SEED);
		camera = new Camera(mapWidth/2, mapHeight/2, 1, 0, 0, -.66, "C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\game.wav");
		screen = new Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col, mapSkybox, numberSprites, spriteArray, animation_lengths, sky_self_movement, in_doors, floor_tile_position_array, ceiling_structure_number, render_distance, default_tile_number);
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
	public int[][] generate_world(int[][] map_matrix, int seed, int[] world_vals, int[] sprite_vals) {
		
		Random r = new Random(seed);
		
		// before anything, let's populate the huge map with the border
				for (int row = 0; row < mapHeight; row++) {
					for (int col = 0; col < mapWidth; col++) {
						if (row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1) {
							map[row][col]=world_vals[r.nextInt(world_vals.length)];
						}
					}
				}
				
		// World gen. To generate the floors we simply add 1000 to our regular world blocks
		int biome = SEED%8; // since there are 32 biomes
		if (biome==0) { // UNDERWORLD;
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(100) == 0) {
							map[row][col]=sprite_vals[0];
						} else {
							map[row][col]= 0;
						}
						
					}
				}
			}
					default_tile_number = 1059-1; //you need to subs 1 for the offset. this is the default tile in the world
					fog_col = 0x000000;
					mapSkybox = Texture._66;
					sky_self_movement = false;
					in_doors = true;
					ceiling_structure_number = 1113;
					render_distance = 100;
		} else if (biome==1) { //HEAVENS
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
							map[row][col]= 0;
						
					}
				}
			}
					default_tile_number = 1027-1;
					fog_col = 0x76f3ff;
					mapSkybox = Texture._80;
					sky_self_movement = false;
					in_doors = false;
					ceiling_structure_number = 1111;
					render_distance = 20;
		}else if (biome==2) { //PLAINS
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(10) == 0) {
							map[row][col]=sprite_vals[14];
						} else {
							map[row][col]= 0;
						}
						
					}
				}
			}
			for (int i=0; i < r.nextInt(4000)+100; i++) {
				int spot_on_map_x = r.nextInt(mapHeight);
				if (spot_on_map_x<=5) spot_on_map_x=5;
				if (spot_on_map_x>=mapHeight-5) spot_on_map_x=mapHeight-5;
				int spot_on_map_y = r.nextInt(mapWidth);
				if (spot_on_map_y<=5) spot_on_map_y=5;
				if (spot_on_map_y>=mapWidth-5) spot_on_map_y=mapWidth-5;
				
				if (r.nextInt(2)==0) map[spot_on_map_y][spot_on_map_x] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+1] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+1] = sprite_vals[13];
				
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+2] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+2] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+2] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+1] = sprite_vals[13];
				
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+3] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+3] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+3] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+2] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+1] = sprite_vals[13];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+3] = sprite_vals[13];
			}
			
			
					default_tile_number = 1029-1;
					fog_col = 0xb0f1ff;
					mapSkybox = Texture._76;
					sky_self_movement = true;
					in_doors = false;
					ceiling_structure_number = 1111;
					render_distance = 25;
		}else if (biome==3) { //MARS
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(200) == 0) {
							map[row][col]=sprite_vals[15];
						} else {
							map[row][col]= 0;
						}

					}
				}
			}
					default_tile_number = 1031-1;
					fog_col = 0x000000;
					mapSkybox = Texture._81;
					sky_self_movement = true;
					in_doors = false;
					ceiling_structure_number = 1111;
					render_distance = 35;
		} else if (biome==4) { // THE CITY
			
			int apart = r.nextInt(3, 6);
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (row % apart == 0 && col % apart == 0) {
							map[row][col]=world_vals[0];
						} else {
							map[row][col]= 0;
						}

					}
				}
			}
			
		
			default_tile_number = 1057-1;
			fog_col = 0x122d65;
			mapSkybox = Texture._71;
			sky_self_movement = false;
			in_doors = false;
			ceiling_structure_number = 1111;
			render_distance = 55;
			
			
		}else if (biome==5) { //ELEVATOR WORLD
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(5) == 0) {
							map[row][col]=world_vals[47];
						} else {
							map[row][col]= 0;
						}

					}
				}
			}
					default_tile_number = 1083-1;
					fog_col = 0x4f90ac;
					mapSkybox = Texture._79;
					sky_self_movement = true;
					in_doors = false;
					ceiling_structure_number = 1101;
					render_distance = 10;
		}else if (biome==6) { //EYE WORLD
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(50) == 0) {
							map[row][col]=sprite_vals[12];
						} else {
							map[row][col]= 0;
						}

					}
				}
			}
					default_tile_number = 1059-1;
					fog_col = 0x5e0000;
					mapSkybox = Texture._81;
					sky_self_movement = false;
					in_doors = true;
					ceiling_structure_number = 1101;
					render_distance = 10;
		}else if (biome==7) { //DESERT
			for (int row = 0; row < mapHeight; row++) {
				for (int col = 0; col < mapWidth; col++) {
					if (!(row==0 || col==0 || row==mapHeight-1 || col==mapWidth-1)) {
						if (r.nextInt(200) == 0) {
							map[row][col]=world_vals[43];
						} else {
							map[row][col]= 0;
						}
						
					}
				}
			}
			for (int i=0; i < r.nextInt(2000)+100; i++) {
				int spot_on_map_x = r.nextInt(mapHeight);
				if (spot_on_map_x<=5) spot_on_map_x=5;
				if (spot_on_map_x>=mapHeight-5) spot_on_map_x=mapHeight-5;
				int spot_on_map_y = r.nextInt(mapWidth);
				if (spot_on_map_y<=5) spot_on_map_y=5;
				if (spot_on_map_y>=mapWidth-5) spot_on_map_y=mapWidth-5;
				
				if (r.nextInt(2)==0) map[spot_on_map_y][spot_on_map_x] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+1] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+1] = world_vals[43];
				
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+2] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+2] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+2] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+1] = world_vals[43];
				
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y][spot_on_map_x+3] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+3] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+2][spot_on_map_x+3] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+2] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+3][spot_on_map_x+1] = world_vals[43];
				if (r.nextInt(2)==0)map[spot_on_map_y+1][spot_on_map_x+3] = world_vals[43];
			}
			
			
					default_tile_number = 1055-1;
					fog_col = 0xd0ca7d;
					mapSkybox = Texture._78;
					sky_self_movement = false;
					in_doors = false;
					ceiling_structure_number = 1111;
					render_distance = 100;
		}
		
		map_matrix[(int)(mapHeight/2)][(int)(mapWidth/2)] = 0;
		
		return map_matrix;
	}
	
	// Here we can init all the textures to each cell AND we also call the algorithm for generating a new map
	public void init_textures_for_render() {
		
		
		ArrayList<SimpleEntry<Integer, Texture>> index_to_texture_array = new ArrayList<SimpleEntry<Integer, Texture>>();
		// For blocks, even numbers represent tall blocks. Values 1 to 700
		// for sprites is 700 to 800
		// and for animating sprites is 800 up to and including 998
		// draw your tiles in the map for values 0 or less. So negative values are the tiles
		// floor tiles the texture array at: 1000  -1 for the offeset of 0 in arrays. Keep adding after 1000 as you see fit. To get right texture is abs(
		// neg tile number) + 1000 -1
		// special values: 0 -> no wall at all, nothing
		// 999: invisible wall
		// keep in mind 1000 is the default floor tile to draw the map with, most common tile in game
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1, Texture._1));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(2, Texture._21)); //0 index in world_values array
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(3, Texture._2)); //1
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(5, Texture._3)); //2
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(7, Texture._4)); //3
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(9, Texture._5)); //4
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(11, Texture._6)); //5
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(13, Texture._7)); //6
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(15, Texture._8)); //7
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(17, Texture._9)); //8
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(19, Texture._10)); //9
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(21, Texture._11)); //10
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(23, Texture._12)); //11
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(25, Texture._13)); //12
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(27, Texture._14)); //13
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(29, Texture._15)); //14
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(31, Texture._16)); //15
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(33, Texture._17)); //16
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(35, Texture._18)); //17
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(37, Texture._19)); //18
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(39, Texture._20)); //19
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
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(91, Texture._47)); //45
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(93, Texture._48)); //46
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(95, Texture._49)); //47
		
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
		
		// here are the floor tiles (which are just duplicates of regular blocks) except for Texture_21
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1001, Texture._1));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1003, Texture._2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1005, Texture._3));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1007, Texture._4));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1009, Texture._5));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1011, Texture._6));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1013, Texture._7));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1015, Texture._8));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1017, Texture._9));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1019, Texture._10));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1021, Texture._11));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1023, Texture._12));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1025, Texture._13));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1027, Texture._14));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1029, Texture._15));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1031, Texture._16));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1033, Texture._17));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1035, Texture._18));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1037, Texture._19));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1039, Texture._20));
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
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1091, Texture._47));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1093, Texture._48));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1095, Texture._49));
		
		
		//this array will be used to randomly pick the world borders textures. Used in generation of world function
		int[] world_values = new int[48]; //48 is the number of block textures
		int[] sprite_values = new int[16]; // number codes for the sprites
		
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
		
		int seed = SEED; // WHAT SEED SHOULD THE WORLD USE TO GENERATE?
		map = generate_world(map, seed, world_values, sprite_values);

		Sprite[] tempSpriteArray = new Sprite[mapWidth*mapHeight];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				if (map[row][col] > 699 && map[row][col] < 998) {
					tempSpriteArray[numberSprites] = new Sprite(row, col);
					numberSprites++;// count how many sprites are on the map. 
				}
			} 
		}
		spriteArray = new Sprite[numberSprites];
		for (int k=0; k < numberSprites; k++) {
			spriteArray[k] = tempSpriteArray[k];
		}
		
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
		//Sound.playSound("C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\menu.wav");
		while(running) {
			
			if (generate_new_world) {
				Random r = new Random();
				SEED = Math.abs(r.nextInt()) % 2147483647; 
				System.out.println(SEED);
				numberSprites = 0;
				init_textures_for_render();
				camera = new Camera(mapWidth/2, mapHeight/2, 1, 0, 0, -.66, "C:\\Users\\rafas\\eclipse-workspace\\LURG\\res\\game.wav");
				camera.isMenu = false;
				screen = new Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col, mapSkybox, numberSprites, spriteArray, animation_lengths, sky_self_movement, in_doors, floor_tile_position_array, ceiling_structure_number, render_distance, default_tile_number);
				addKeyListener(camera);
				generate_new_world = false;
			}
			
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
				//handles all of the logic restricted time
				screen.update(camera, pixels, frame_num);
				camera.update(map);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	public static void main(String [] args) {
		
		Game game = new Game();
	}
}