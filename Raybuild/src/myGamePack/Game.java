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
	public String bgm_path = "C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\test.wav";
	public static int WID = 800;
	public static int HEI = 600;
	public int fog_col = 0xF2CDCD; 
	public int ground_color = Color.DARK_GRAY.getRGB();
	private int retro_feel = 5;
	private int TEX_MAX = 800;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public ArrayList<Texture> textures;
	public Camera camera;
	public Screen screen;
	public static int[][] map = 
		{ // special values: 0 -> no wall at all, nothing
				// 999: invisible wall
				{  1,   1,   1,   1,   1,   1,   1,   1,   1},
				{  1, 999,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   446,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   9,   0,   3,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0,   0,   0,   0,   0,   0,   0,   1},
				{  1,   0, 529,   0,   0,   0,   0,   0,   1},
				{  1,   1,   1,   1,   1,   1,   1,   1,   1}
		};
		public int mapWidth = map[0].length;
		public int mapHeight = map.length;
	public Game() {
		thread = new Thread(this);
		image = new BufferedImage(WID, HEI, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		init_textures_for_render(); 
		camera = new Camera(5.5, 6.5, 1, 0, 0, -.66);
		screen = new Screen(map, mapWidth, mapHeight, textures, WID, HEI, retro_feel, fog_col, ground_color, Texture.skybox);
		new Sound(bgm_path);
		addKeyListener(camera);
		setSize(WID, HEI);
		setResizable(false);
		setTitle("3D Engine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	
	// Here we can init all the textures to each cell
	public void init_textures_for_render() {
		ArrayList<SimpleEntry<Integer, Texture>> index_to_texture_array = new ArrayList<SimpleEntry<Integer, Texture>>();
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(1, Texture.null_texture));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(3, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(5, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(7, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(129, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(227, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(329, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(427, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(529, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(627, Texture.null_texture2));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(446, Texture.tall_wall));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(9, Texture.lain));
		index_to_texture_array.add(new SimpleEntry<Integer, Texture>(41, Texture.distant));
		textures = new ArrayList<Texture>(TEX_MAX);
		while(textures.size() < TEX_MAX) textures.add(Texture.null_texture);
		for (SimpleEntry<Integer, Texture> index_to_text : index_to_texture_array) {
			textures.set(index_to_text.getKey()-1, index_to_text.getValue());
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
		final double ns = 1000000000.0 / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				screen.update(camera, pixels);
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