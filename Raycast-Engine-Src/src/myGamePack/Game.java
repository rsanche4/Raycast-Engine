package myGamePack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
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

// GPU acceleration imports
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.common.nio.Buffers;

// Game Class with GPU Acceleration
// Description: This is the main class with GPU acceleration using JOGL while keeping the same pixel array logic
public class Game extends JFrame implements Runnable, GLEventListener {
	private static final long serialVersionUID = 1L;
	public static double FPS = 30.0;
	public static int MAX_WORLD_LIMIT;
	public static double world_light_factor = 1.0;
	private static String game_title;
	private static String game_version;
	public static String current_world;
	private static String fullscreen;
	private static int game_width=320;
	private static int game_height=240;
	private static int SCREEN_W;
	private static int SCREEN_H;
	public static String[][] layer0;
	public static String[][] layer1;
	public static String[] event_data;
	public static String[] event_data_names;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private Camera camera;
	private Screen screen;
	private boolean running;
	private int[] gamepixels;
	
	// GPU related variables
	private GLCanvas canvas;
	private int textureId;
	private ByteBuffer pixelBuffer;
	private boolean textureInitialized = false;

	public Game(String worldName, int renderDistance, boolean skySelfMovement, double walkingSpeed, double turningSpeed,
			String skyboxId, int fog_col, int renderSpriteDist) {
		thread = new Thread(this);
		image = new BufferedImage(SCREEN_W, SCREEN_H, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		// Initialize OpenGL
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		canvas = new GLCanvas(glCapabilities);
		canvas.addGLEventListener(this);
		
		// Create pixel buffer for GPU transfer
		pixelBuffer = Buffers.newDirectByteBuffer(SCREEN_W * SCREEN_H * 4); // RGBA
		
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
			gamepixels = new int[game_width*game_width];
			screen = new Screen(current_world, layer0, layer1, event_data, MAX_WORLD_LIMIT, allTextures, game_width, game_height,
					fog_col, skyboxId, skySelfMovement, renderDistance, world_light_factor, pixels,
					camera, renderSpriteDist, SCREEN_W, SCREEN_H, gamepixels, event_data_names);
			addKeyListener(camera);
			
			// Setup JFrame with OpenGL canvas
			add(canvas);
			setSize(SCREEN_W, SCREEN_H);
			setResizable(false);
			setUndecorated(fullscreen.contentEquals("on"));
			setLocation(0, 0);
			setTitle(game_title + " " + game_version.toString());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBackground(Color.black);
			setLocationRelativeTo(null);
			setVisible(true);
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

	// OpenGL initialization
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		// Enable 2D textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// Generate texture ID
		IntBuffer textureBuffer = Buffers.newDirectIntBuffer(1);
		gl.glGenTextures(1, textureBuffer);
		textureId = textureBuffer.get(0);
		
		// Bind and configure texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		
		// Set up orthographic projection
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, 1, 0, 1, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// Disable depth testing for 2D rendering
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		textureInitialized = true;
	}

	// OpenGL display method - this replaces the old render() method
	@Override
	public void display(GLAutoDrawable drawable) {
		if (!textureInitialized) return;
		
		GL2 gl = drawable.getGL().getGL2();
		
		// Clear the screen
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// Convert pixel array to ByteBuffer for GPU transfer
		pixelBuffer.clear();
		for (int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];
			// Convert from ARGB to RGBA
			pixelBuffer.put((byte) ((pixel >> 16) & 0xFF)); // R
			pixelBuffer.put((byte) ((pixel >> 8) & 0xFF));  // G
			pixelBuffer.put((byte) (pixel & 0xFF));         // B
			pixelBuffer.put((byte) 255);                    // A (full opacity)
		}
		pixelBuffer.flip();
		
		// Upload pixel data to GPU texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, SCREEN_W, SCREEN_H, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, pixelBuffer);
		
		// Render fullscreen quad with the texture
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
		gl.glTexCoord2f(1, 1); gl.glVertex2f(1, 0);
		gl.glTexCoord2f(1, 0); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(0, 0); gl.glVertex2f(0, 1);
		gl.glEnd();
		
		// Force rendering
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// Clean up OpenGL resources
		GL2 gl = drawable.getGL().getGL2();
		if (textureId != 0) {
			gl.glDeleteTextures(1, new int[]{textureId}, 0);
		}
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
				camera.update(layer0, layer1);
				frame_num = (frame_num + 1) % 1000;
				delta--;
			}
			// Trigger OpenGL rendering
			canvas.display();
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
			fullscreen = config.get("fullscreen");
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        Dimension screenSize = toolkit.getScreenSize();
	        SCREEN_W = 640;
	        SCREEN_H = 480;
	        if (fullscreen.contentEquals("on")) {
				SCREEN_W = screenSize.width;
				SCREEN_H = screenSize.height;
			}
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
			event_data = null;
			event_data_names = null;
			for (int i = 0; i < worlds_data.length(); i++) {
				JSONObject world_data = worlds_data.getJSONObject(i);
				world_name = world_data.getString("VAR0").split(":")[1];
				if (world_name.contentEquals(current_world)) {
					MAX_WORLD_LIMIT = Integer.parseInt(world_data.getString("VAR12").split(":")[1]);
					layer0 = new String[MAX_WORLD_LIMIT][MAX_WORLD_LIMIT];
					layer1 = new String[MAX_WORLD_LIMIT][MAX_WORLD_LIMIT];
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
					event_data_names = world_data.getString("VAR11").split(":")[1].split(",");
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