package myGamePack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import org.json.JSONArray;
import org.json.JSONObject;

// Game  Class
// Description: This is the main class, and it starts the whole game, it initializes everything that is needed to run, etc.
public class Game extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	public static double FPS = 30.0;
	public static int MAX_WORLD_LIMIT = 500;
	public static double world_light_factor = 1.0;
	private static String game_title;
	private static String game_version;
	private static String current_world;
	private static int game_width=256;
	private static int game_height=256;
	private static int SCREEN_W;
	private static int SCREEN_H;
	private static String[][] layer0;
	private static String[][] layer1;
	private static String[] event_data;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private Camera camera;
	private Screen screen;
	private boolean running;
	private int[] gamepixels;

	public Game(String worldName, int renderDistance, boolean skySelfMovement, double walkingSpeed, double turningSpeed,
			String skyboxId, int fog_col, int renderSpriteDist) {
		thread = new Thread(this);
		image = new BufferedImage(SCREEN_W, SCREEN_H, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		Path dataFolder = Paths.get("data");
		HashMap<String, Texture> allTextures = new HashMap<>();
		try {
			Files.walkFileTree(dataFolder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".png")) {
						allTextures.put(file.getFileName().toString(), new Texture(file.toString()));
					}
					return FileVisitResult.CONTINUE;
				}
			});
			camera = new Camera(MAX_WORLD_LIMIT / 2, MAX_WORLD_LIMIT / 2, 1, 0, 0, -.66, walkingSpeed, turningSpeed);
			for (int j = 0; j < layer1[0].length; j++) {
				layer1[0][j] = "block0.png";
				layer1[layer1.length - 1][j] = "block0.png";
			}
			for (int i = 0; i < layer1.length; i++) {
				layer1[i][0] = "block0.png";
				layer1[i][layer1[i].length - 1] = "block0.png";
			}
			gamepixels = new int[game_width*game_width];
			screen = new Screen(layer0, layer1, event_data, MAX_WORLD_LIMIT, allTextures, game_width, game_height,
					fog_col, skyboxId, skySelfMovement, renderDistance, world_light_factor, pixels,
					camera, renderSpriteDist, SCREEN_W, SCREEN_H, gamepixels);
			addKeyListener(camera);
			setSize(SCREEN_W, SCREEN_H);
			setResizable(false);
			setUndecorated(true);
			setLocation(0, 0);
			setTitle(game_title + " " + game_version.toString());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBackground(Color.black);
			setLocationRelativeTo(null);
			setVisible(true);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			start();
		} catch (IOException e) {
			e.printStackTrace();
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / FPS;
		double delta = 0;
		requestFocus();
		int frame_num = 0;
		while (running) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;
			while (delta >= 1) {
				screen.update(frame_num);
				camera.update(layer0, layer1, event_data);
				frame_num++;
				delta--;
			}
			render();
		}
	}

	public static void main(String[] args) {
		try {
			String filePath = "data/global_configs.cfg";
			Map<String, String> config = new HashMap<>();
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty() || line.startsWith("#")) {
						continue;
					}
					String[] parts = line.split("=", 2);
					if (parts.length == 2) {
						String key = parts[0].trim();
						String value = parts[1].trim();
						config.put(key, value);
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading the configuration file: " + e.getMessage());
			}
			game_title = config.get("game_title");
			game_version = config.get("game_version");
			current_world = config.get("world_init");
			SCREEN_W = Integer.parseInt(config.get("game_width"));
			SCREEN_H = Integer.parseInt(config.get("game_height"));
			String content = new String(Files.readAllBytes(Paths.get("data/worlds_data.json")));
			JSONObject jsonObject = new JSONObject(content);
			JSONArray worlds_data = jsonObject.getJSONArray("world_data");
			String world_name = "";
			int render_distance = 0;
			boolean sky_self_movement = false;
			double walking_speed = 0.0;
			double turning_speed = 0.0;
			String skybox_id = "";
			int fog_col = 0;
			int renderSpriteDist = 0;
			layer0 = new String[MAX_WORLD_LIMIT][MAX_WORLD_LIMIT];
			layer1 = new String[MAX_WORLD_LIMIT][MAX_WORLD_LIMIT];
			event_data = null;
			for (int i = 0; i < worlds_data.length(); i++) {
				JSONObject world_data = worlds_data.getJSONObject(i);
				world_name = world_data.getString("VAR0").split(":")[1];
				if (world_name.contentEquals(current_world)) {
					render_distance = Integer.parseInt(world_data.getString("VAR1").split(":")[1]);
					sky_self_movement = Boolean.parseBoolean(world_data.getString("VAR2").split(":")[1]);
					walking_speed = Double.parseDouble(world_data.getString("VAR3").split(":")[1]);
					turning_speed = Double.parseDouble(world_data.getString("VAR4").split(":")[1]);
					fog_col = Integer.parseInt(world_data.getString("VAR5").split(":")[1].substring(1), 16);
					skybox_id = world_data.getString("VAR6").split(":")[1];
					renderSpriteDist = Integer.parseInt(world_data.getString("VAR10").split(":")[1]);
					String[] layer0_flat = world_data.getString("VAR7").split(":")[1].split(",");
					for (int row = 0; row < MAX_WORLD_LIMIT; row++) {
						for (int col = 0; col < MAX_WORLD_LIMIT; col++) {
							layer0[row][col] = layer0_flat[row * MAX_WORLD_LIMIT + col];
						}
					}
					String[] layer1_flat = world_data.getString("VAR8").split(":")[1].split(",");
					for (int row = 0; row < MAX_WORLD_LIMIT; row++) {
						for (int col = 0; col < MAX_WORLD_LIMIT; col++) {
							layer1[row][col] = layer1_flat[row * MAX_WORLD_LIMIT + col];
						}
					}
					event_data = world_data.getString("VAR9").split(":")[1].split(",");
					break;
				}
			}
			new Game(world_name, render_distance, sky_self_movement, walking_speed, turning_speed, skybox_id, fog_col,
					renderSpriteDist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}