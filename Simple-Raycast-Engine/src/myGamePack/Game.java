package myGamePack;

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
	public static String game_title = "Simple Raycaster Engine";
	public static int WID = 800;
	public static int HEI = 600;
	public static double FPS = 60.0;
	public int fog_col = 0x8df0ff; 
	public int ground_color = 0x4ed9a2;
	public int animation_speed = 135;
	public boolean sky_self_movement = true;
	public int retro_feel = 5;
	public int numberSprites = 0;
	public Sprite[] spriteArray;
	public int animation_lengths = 19;
	public int[] pixels;
	public ArrayList<Texture> textures;
	public Camera camera;
	public Screen screen;
	public static int[][] map = 
		{ // special values: 0 -> no wall at all, nothing
				// 999: invisible wall
				// 1000: game menu picture
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,700,0,700,0,0,1},
				{1,0,0,0,0,700,0,0,0,0,800,0,0,0,1},
				{1,0,700,0,0,0,0,0,0,0,0,700,0,0,1},
				{1,1,1,0,0,0,0,700,0,0,0,0,0,0,1},
				{1,0,1,0,0,1,1,1,0,0,0,1,1,3,1},
				{1,0,3,0,0,3,0,1,0,0,0,1,0,0,1},
				{1,1,1,0,0,1,1,1,0,0,0,3,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
				{1,0,701,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,701,0,0,700,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,701,0,0,0,0,1},
				{1,0,0,0,0,701,0,0,0,0,0,0,0,0,1},
				{1,2,0,0,0,2,2,0,2,2,0,0,0,2,1},
				{1,0,0,2,0,2,0,2,0,0,0,2,0,0,1},
				{1,0,2,0,0,0,0,0,0,2,0,0,2,0,1},
				{1,0,0,0,0,2,0,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				
		};
		private int mapWidth = map[0].length;
		private int mapHeight = map.length;
		private int TEX_MAX = 1000;
		private Thread thread;
		private boolean running;
		private BufferedImage image;
	public Game() {
		thread = new Thread(this);
		image = new BufferedImage(WID, HEI, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		init_textures_for_render(); 
		camera = new Camera(2.0, 2.0, 1, 0, 0, -.66);
		screen = new Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col, ground_color, Texture.skybox, numberSprites, spriteArray, animation_lengths, sky_self_movement);
		addKeyListener(camera);
		setSize(WID, HEI);
		setResizable(false);
		setTitle(game_title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	
	// Here we can init all the textures to each cell
	public void init_textures_for_render() {
		ArrayList<SimpleEntry<Integer, Texture>> index_to_texture_array = new ArrayList<SimpleEntry<Integer, Texture>>();
		// you need to add key 1000 because that is the Menu picture. For blocks, even numbers represent tall blocks. Values 1 to 700
		// for sprites is 700 to 800
		// and for animating sprites is 800 up to and including 998
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1000, Texture.menu_background));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1, Texture.wood_block));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(2, Texture.tall_wood));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(3, Texture.wood_door));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(700, Texture.grass));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(701, Texture.tree));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(800, Texture.star0));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(801, Texture.star1));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(802, Texture.star2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(803, Texture.star3));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(804, Texture.star4));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(805, Texture.star5));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(806, Texture.star6));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(807, Texture.star7));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(808, Texture.star8));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(809, Texture.star9));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(810, Texture.star10));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(811, Texture.star11));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(812, Texture.star12));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(813, Texture.star13));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(814, Texture.star14));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(815, Texture.star15));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(816, Texture.star16));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(817, Texture.star17));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(818, Texture.star18));
		textures = new ArrayList<Texture>(TEX_MAX);
		while(textures.size() < TEX_MAX) textures.add(Texture.null_texture);
		for (SimpleEntry<Integer, Texture> index_to_text : index_to_texture_array) {
			textures.set(index_to_text.getKey()-1, index_to_text.getValue());
		}
		Sprite[] tempSpriteArray = new Sprite[100];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				if (map[row][col] > 699 && map[row][col] < 998) {
					tempSpriteArray[numberSprites] = new Sprite(row, col);
					numberSprites++;// count how many sprites are on the map. Limit to 100
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
		Sound.playSound("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\menu.wav");
		while(running) {
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