package myGamePack;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

// Screen Class
// Description: This is the core screen calculation class, here is where we update the screen with the pixels we need to draw, the math and core of the engine happens here.
public class Screen {
	private String[][] layer0;
	private String[][] layer1;
	private String[] event_data;
	private int mapWidth;
	private int mapHeight;
	private HashMap<String, Texture> textures;
	private int width;
	private int height;
	private int pixel_effect;
	private int fog_color;
	private String skyb;
	private boolean sky_wave;
	private int render_dist;
	private int sprite_render_dist;
	private double world_light_factor;
	private boolean in_doors;
	private Texture skybox_default;
	private ArrayList<Sprite> spriteArrTemp = new ArrayList<Sprite>();
	private Sprite[] spriteArr = new Sprite[0];
	private int[] pixels;
	private int frames;
	private Camera camera;

	public Screen(String[][] layer0, String[][] layer1, String[] event_data, int MAX_WORLD_LIMIT,
			HashMap<String, Texture> allTextures, int game_width, int game_height, int pixel_effect, int fog_col,
			String skyboxId, boolean skySelfMovement, int renderDistance, double world_light_factor, int[] pixels,
			Camera camera, int renderSpriteDistance) {
		this.layer0 = layer0;
		this.layer1 = layer1;
		this.event_data = event_data;
		mapWidth = MAX_WORLD_LIMIT;
		mapHeight = MAX_WORLD_LIMIT;
		textures = allTextures;
		width = game_width;
		height = game_height;
		this.pixel_effect = pixel_effect;
		fog_color = fog_col;
		skyb = skyboxId;
		sky_wave = skySelfMovement;
		render_dist = renderDistance;
		sprite_render_dist = renderSpriteDistance;
		this.world_light_factor = world_light_factor;
		in_doors = skyb.contains("block") ? true : skyb.contains("skybox") ? false : false;
		skybox_default = allTextures.get(skyb);
		for (int i = 0; i < layer1.length; i++) {
			for (int j = 0; j < layer1[i].length; j++) {
				if (!layer1[i][j].contains("sprite0") && !layer1[i][j].contains("block")) {
					spriteArrTemp.add(new Sprite(layer1[i][j], (double) i, (double) j));
				}
			}
		}
		spriteArr = new Sprite[spriteArrTemp.size()];
		int k = 0;
		for (Sprite sprite : spriteArrTemp) {
			spriteArr[k] = sprite;
			k++;
		}
		this.pixels = pixels;
		this.camera = camera;
	}

	public int darkenColor(int color, double factor) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		r = (int) (r * factor);
		g = (int) (g * factor);
		b = (int) (b * factor);
		r = Math.max(0, Math.min(255, r));
		g = Math.max(0, Math.min(255, g));
		b = Math.max(0, Math.min(255, b));
		return (r << 16) | (g << 8) | b;
	}

	public void update(int frames) {
		this.frames = frames;
		run_user_scripts();
		double darkened_factor = world_light_factor;
		if (!in_doors) {
			Texture sky = skybox_default;
			double row = -1.0;
			double max_row = height / 2;
			int new_fog_color_ceiling = 0;
			double fog_perc = 0;
			int skybox_x = 0;
			int skybox_y = -1;
			int rotate_sky = (int) (((360 - camera.player_degree) / 360.0) * width * 4);
			if (sky_wave) {
				rotate_sky = rotate_sky + (int) (30 * Math.cos(((double) frames % 50) / 8.0));
			}
			for (int n = 0; n < pixels.length / 2; n = n + pixel_effect) {
				if (n % width == 0 && row < max_row) {
					skybox_x = rotate_sky;
					skybox_y++;
					row++;
					fog_perc = row / max_row;
				} else if (row > max_row) {
					break;
				}
				int rc = (int) ((fog_perc) * ((fog_color & 0xFF0000) >> 16)
						+ (1 - fog_perc) * ((sky.pixels[skybox_x + width * 4 * skybox_y] & 0xFF0000) >> 16));
				int gc = (int) ((fog_perc) * ((fog_color & 0xFF00) >> 8)
						+ (1 - fog_perc) * ((sky.pixels[skybox_x + width * 4 * skybox_y] & 0xFF00) >> 8));
				int bc = (int) ((fog_perc) * ((fog_color & 0xFF))
						+ (1 - fog_perc) * ((sky.pixels[skybox_x + width * 4 * skybox_y] & 0xFF)));
				new_fog_color_ceiling = ((rc & 0x0ff) << 16) | ((gc & 0x0ff) << 8) | (bc & 0x0ff);
				for (int i = 0; i < pixel_effect; i++)
					pixels[n + i] = darkenColor(new_fog_color_ceiling, darkened_factor);
				skybox_x += pixel_effect;
			}
		}
		double[] perp_wall_dist_buffer = new double[width];
		for (int x = 0; x < width; x = x + pixel_effect) {
			double cameraX = 2 * x / (double) (width) - 1;
			double rayDirX = camera.xDir + camera.xPlane * cameraX;
			double rayDirY = camera.yDir + camera.yPlane * cameraX;
			int mapX = (int) camera.xPos;
			int mapY = (int) camera.yPos;
			double sideDistX;
			double sideDistY;
			double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
			double perpWallDist;
			int stepX, stepY;
			boolean hit = false;
			int side = 0;
			if (rayDirX < 0) {
				stepX = -1;
				sideDistX = (camera.xPos - mapX) * deltaDistX;
			} else {
				stepX = 1;
				sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
			}
			if (rayDirY < 0) {
				stepY = -1;
				sideDistY = (camera.yPos - mapY) * deltaDistY;
			} else {
				stepY = 1;
				sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
			}
			while (!hit) {
				if (sideDistX < sideDistY) {
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
				} else {
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
				}
				if (layer1[mapX][mapY].contains("block"))
					hit = true;
			}
			if (side == 0)
				perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
			else
				perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);
			for (int i = 0; i < pixel_effect; i++)
				perp_wall_dist_buffer[x + i] = perpWallDist;
			int lineHeight;
			if (perpWallDist > 0)
				lineHeight = Math.abs((int) (height / perpWallDist));
			else
				lineHeight = height;
			int drawStart = (-lineHeight / 2 + height / 2);
			if (drawStart < 0)
				drawStart = 0;
			int drawEnd = lineHeight / 2 + height / 2;
			if (drawEnd >= height)
				drawEnd = height - 1;
			if (perpWallDist > render_dist) {
				drawStart = 0;
				drawEnd = 0;
			}
			Texture texBlock = textures.get(layer1[mapX][mapY]);
			double wallX;
			if (side == 1) {
				wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
			} else {
				wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
			}
			wallX -= Math.floor(wallX);
			int texX = (int) (wallX * (texBlock.SIZE));
			if (side == 0 && rayDirX > 0)
				texX = texBlock.SIZE - texX - 1;
			if (side == 1 && rayDirY < 0)
				texX = texBlock.SIZE - texX - 1;
			for (int y = drawStart; y < drawEnd; y++) {
				int texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
				int color;
				if (side == 0)
					color = texBlock.pixels[texX + (texY * texBlock.SIZE)];
				else
					color = (texBlock.pixels[texX + (texY * texBlock.SIZE)] >> 1) & 0x7F7F7F;
				double percd = perpWallDist / render_dist;
				int rc = (int) ((percd) * ((fog_color & 0xFF0000) >> 16) + (1 - percd) * ((color & 0xFF0000) >> 16));
				int gc = (int) ((percd) * ((fog_color & 0xFF00) >> 8) + (1 - percd) * ((color & 0xFF00) >> 8));
				int bc = (int) ((percd) * ((fog_color & 0xFF)) + (1 - percd) * ((color & 0xFF)));
				color = ((rc & 0x0ff) << 16) | ((gc & 0x0ff) << 8) | (bc & 0x0ff);
				for (int i = 0; i < pixel_effect; i++)
					pixels[(x + i) + y * (width)] = darkenColor(color, darkened_factor);
			}
			double floorXWall, floorYWall;
			if (side == 0 && rayDirX > 0) {
				floorXWall = mapX;
				floorYWall = mapY + wallX;
			} else if (side == 0 && rayDirX < 0) {
				floorXWall = mapX + 1.0;
				floorYWall = mapY + wallX;
			} else if (side == 1 && rayDirY > 0) {
				floorXWall = mapX + wallX;
				floorYWall = mapY;
			} else {
				floorXWall = mapX + wallX;
				floorYWall = mapY + 1.0;
			}
			double distWall, distPlayer, currentDist;
			distWall = perpWallDist;
			distPlayer = 0.0;
			if (drawEnd < 0)
				drawEnd = height;
			for (int y = drawEnd + 1; y < height; y++) {
				currentDist = height / (2.0 * y - height);
				double weight = (currentDist - distPlayer) / (distWall - distPlayer);
				double currentFloorX = weight * floorXWall + (1.0 - weight) * camera.xPos;
				double currentFloorY = weight * floorYWall + (1.0 - weight) * camera.yPos;
				int floorTexX, floorTexY;
				floorTexX = ((int) (Math.abs(currentFloorX) * texBlock.SIZE)) % texBlock.SIZE;
				floorTexY = ((int) (Math.abs(currentFloorY) * texBlock.SIZE)) % texBlock.SIZE;
				int draw_tilex = Math.abs(((int) currentFloorX) % mapWidth);
				int draw_tiley = Math.abs(((int) currentFloorY) % mapHeight);
				Texture textureFloor = textures.get(layer0[draw_tilex][draw_tiley]);
				int new_color = textureFloor.pixels[floorTexX + (floorTexY * textureFloor.SIZE)];
				double percd_floor = currentDist / render_dist;
				if (percd_floor > 1) {
					percd_floor = 1;
				}
				int rcf = (int) ((percd_floor) * ((fog_color & 0xFF0000) >> 16)
						+ (1 - percd_floor) * ((new_color & 0xFF0000) >> 16));
				int gcf = (int) ((percd_floor) * ((fog_color & 0xFF00) >> 8)
						+ (1 - percd_floor) * ((new_color & 0xFF00) >> 8));
				int bcf = (int) ((percd_floor) * ((fog_color & 0xFF)) + (1 - percd_floor) * ((new_color & 0xFF)));
				new_color = ((rcf & 0x0ff) << 16) | ((gcf & 0x0ff) << 8) | (bcf & 0x0ff);
				if (!in_doors && y < height / 2) {
					continue;
				}
				for (int i = 0; i < pixel_effect; i++)
					pixels[(x + i) + y * (width)] = darkenColor(new_color, darkened_factor);
				Texture ceilingTexture = skybox_default;
				int new_color_c = ceilingTexture.pixels[floorTexX + (floorTexY * ceilingTexture.SIZE)];
				int rcc = (int) ((percd_floor) * ((fog_color & 0xFF0000) >> 16)
						+ (1 - percd_floor) * ((new_color_c & 0xFF0000) >> 16));
				int gcc = (int) ((percd_floor) * ((fog_color & 0xFF00) >> 8)
						+ (1 - percd_floor) * ((new_color_c & 0xFF00) >> 8));
				int bcc = (int) ((percd_floor) * ((fog_color & 0xFF)) + (1 - percd_floor) * ((new_color_c & 0xFF)));
				new_color_c = ((rcc & 0x0ff) << 16) | ((gcc & 0x0ff) << 8) | (bcc & 0x0ff);
				if (in_doors) {
					for (int i = 0; i < pixel_effect; i++)
						pixels[(x + i) + (height - y) * (width)] = darkenColor(new_color_c, darkened_factor);
				}
			}
		}
		int numSprites = spriteArr.length;
		for (int s = 0; s < numSprites; s++) {

			spriteArr[s].spriteDist = ((camera.xPos - spriteArr[s].spriteXPos) * (camera.xPos - spriteArr[s].spriteXPos)
					+ (camera.yPos - spriteArr[s].spriteYPos) * (camera.yPos - spriteArr[s].spriteYPos));
		}
		Arrays.sort(spriteArr);
		for (int i = 0; i < numSprites; i++) {
			double spriteX = (double) spriteArr[i].spriteXPos - camera.xPos;
			double spriteY = (double) spriteArr[i].spriteYPos - camera.yPos;
			double invDet = 1.0 / (camera.xPlane * camera.yDir - camera.xDir * camera.yPlane);
			double transformX = invDet * (camera.yDir * spriteX - camera.xDir * spriteY);
			double transformY = invDet * (-camera.yPlane * spriteX + camera.xPlane * spriteY);
			int spriteScreenX = (int) ((width / 2) * (1 + transformX / transformY));
			int spriteHeight = (int) (Math.abs((int) (height / (transformY))));
			int drawStartY = -spriteHeight / 2 + height / 2;
			if (drawStartY < 0)
				drawStartY = 0;
			int drawEndY = spriteHeight / 2 + height / 2;
			if (drawEndY >= height)
				drawEndY = height - 1;
			int spriteWidth = (int) (Math.abs((int) (height / (transformY))));
			int drawStartX = -spriteWidth / 2 + spriteScreenX;
			if (drawStartX < 0)
				drawStartX = 0;
			int drawEndX = spriteWidth / 2 + spriteScreenX;
			if (drawEndX >= width)
				drawEndX = width - 1;
			if (spriteArr[i].spriteDist > sprite_render_dist) {
				drawStartX = 0;
				drawEndX = 0;
			}
			for (int stripe = drawStartX; stripe < drawEndX; stripe = stripe + pixel_effect) {
				int texX = (int) (256 * (stripe - (-spriteWidth / 2 + spriteScreenX))
						* spriteArr[i].getTexture(textures).SIZE / spriteWidth) / 256;
				if (transformY > 0 && stripe > 0 && stripe < width && transformY < perp_wall_dist_buffer[stripe])
					for (int y = drawStartY; y < drawEndY; y++) {
						int d = (y) * 256 - height * 128 + spriteHeight * 128;
						int texY = ((d * spriteArr[i].getTexture(textures).SIZE) / spriteHeight) / 256;
						double angleInDegrees = Math.toDegrees(Math.atan2(spriteArr[i].spriteYPos - camera.yPos,
								spriteArr[i].spriteXPos - camera.xPos));
						if (angleInDegrees < 0) {
							angleInDegrees += 360;
						}
						int direction = (int) ((angleInDegrees + 225) % 360 / 90);
						int color = spriteArr[i].getTexture(textures).pixels[Math
								.abs(texX + ((texY + spriteArr[i].getTexture(textures).SIZE * direction)
										* spriteArr[i].getTexture(textures).SIZE))];
						if (color != 0x000000) {
							double percd = spriteArr[i].spriteDist / sprite_render_dist;
							int rc = (int) ((percd) * ((fog_color & 0xFF0000) >> 16)
									+ (1 - percd) * ((color & 0xFF0000) >> 16));
							int gc = (int) ((percd) * ((fog_color & 0xFF00) >> 8)
									+ (1 - percd) * ((color & 0xFF00) >> 8));
							int bc = (int) ((percd) * ((fog_color & 0xFF)) + (1 - percd) * ((color & 0xFF)));
							color = ((rc & 0x0ff) << 16) | ((gc & 0x0ff) << 8) | (bc & 0x0ff);
							for (int doingthepixeffect = 0; doingthepixeffect < pixel_effect; doingthepixeffect++) {
								pixels[((stripe) + y * (width)) + doingthepixeffect] = darkenColor(color,
										darkened_factor);
							}
						}
					}
			}
		}
	}

	public String getSpriteTextureId(int index) {
		return this.spriteArr[index].spritename;
	}

	public Double getSpriteX(int index) {
		return this.spriteArr[index].spriteXPos;
	}

	public Double getSpriteY(int index) {
		return this.spriteArr[index].spriteYPos;
	}

	public Double getSpriteDistance(int index) {
		return this.spriteArr[index].spriteDist;
	}

	public void setSpriteTextureId(int index, String spritename) {
		this.spriteArr[index].spritename = spritename;
	}

	public void setSpriteX(int index, double spriteXPos) {
		this.spriteArr[index].spriteXPos = spriteXPos;
	}

	public void setSpriteY(int index, double spriteYPos) {
		this.spriteArr[index].spriteYPos = spriteYPos;
	}

	public void setSpriteDistance(int index, double spriteDist) {
		this.spriteArr[index].spriteDist = spriteDist;
	}
	
	public void addSprite(String spritename, double spriteXPos, double spriteYPos) {
		Sprite spr = new Sprite(spritename, spriteXPos, spriteYPos);
		ArrayList<Sprite> spriteArrTemp = new ArrayList<>(Arrays.asList(this.spriteArr));
		spriteArrTemp.add(spr);
		this.spriteArr = spriteArrTemp.toArray(new Sprite[0]);
	}
	
	public void removeSprite(int index) {
		ArrayList<Sprite> spriteArrTemp = new ArrayList<>(Arrays.asList(this.spriteArr));
		spriteArrTemp.remove(index);
		this.spriteArr = spriteArrTemp.toArray(new Sprite[0]);
	}

	public int getFogColor() {
		return fog_color;
	}

	public void setFogColor(int fog_color) {
		this.fog_color = fog_color;
	}

	public String getLayerValue(int layer, int x, int y) {
		if (layer == 0) {
			return layer0[x][y];
		} else if (layer == 1) {
			return layer1[x][y];
		}
		return null;
	}

	public void setLayerValue(int layer, int x, int y, String value) {
		if (layer == 0) {
			layer0[x][y] = value;
		} else if (layer == 1) {
			layer1[x][y] = value;
		}
	}

	public double getLightFactor() {
		return world_light_factor;
	}

	public void setLightFactor(double new_light_factor) {
		world_light_factor = new_light_factor;
	}

	public int getMaxWorldLimit() {
		return mapWidth;
	}

	public int getGameWidth() {
		return width;
	}

	public int getGameHeight() {
		return height;
	}

	public String getScreenPixels() {
		return String.join(", ", Arrays.toString(pixels));
	}

	public void setScreenPixels(String comma_pixels) {
		pixels = Arrays.stream(comma_pixels.split(",")).mapToInt(Integer::parseInt).toArray();
	}

	public String getSkybox() {
		return skyb;
	}

	public void setSkybox(String newSkyb) {
		skyb = newSkyb;
		in_doors = skyb.contains("block") ? true : skyb.contains("skybox") ? false : false;
		skybox_default = textures.get(skyb);
	}

	public boolean getSkyboxWave() {
		return sky_wave;
	}

	public void setSkyboxWave(boolean wave) {
		sky_wave = wave;
	}

	public int getRenderDistance() {
		return render_dist;
	}

	public void setRenderDistance(int newRenderDist) {
		render_dist = newRenderDist;
	}

	public int getRenderSpriteDistance() {
		return this.sprite_render_dist;
	}

	public void setRenderSpriteDistance(int newRenderDist) {
		this.sprite_render_dist = newRenderDist;
	}

	public void endScript(int eventx, int eventy) {
		String eventXstr = Integer.toString(eventx);
		String eventYstr = Integer.toString(eventy);
		ArrayList<String> eventList = new ArrayList<>(Arrays.asList(event_data));
		for (int i = 0; i < event_data.length; i = i + 3) {
			if (event_data[i + 1].equals(eventXstr) && event_data[i + 2].equals(eventYstr)) {
				eventList.remove(i);
				eventList.remove(i);
				eventList.remove(i);
				break;
			}
		}
		event_data = eventList.toArray(new String[0]);
	}

	public void addScript(String script_name, int eventx, int eventy) {
		String eventXstr = Integer.toString(eventx);
		String eventYstr = Integer.toString(eventy);
		ArrayList<String> eventList = new ArrayList<>(Arrays.asList(event_data));
		eventList.add(script_name);
		eventList.add(eventXstr);
		eventList.add(eventYstr);
		event_data = eventList.toArray(new String[0]);
	}

	public String getEventList() {
		return String.join(", ", event_data);
	}

	public void setEventList(String comma_seperated_eventlist) {
		event_data = comma_seperated_eventlist.split(",");
	}

	public int getFrameNumber() {
		return frames;
	}

	public double getPlayerX() {
		return camera.xPos;
	}

	public void setPlayerX(double xLoc) {
		camera.xPos = xLoc;
	}

	public double getPlayerY() {
		return camera.yPos;
	}

	public void setPlayerY(double yLoc) {
		camera.yPos = yLoc;
	}

	public double getPlayerDegree() {
		return camera.player_degree;
	}

	public void setPlayerDegree(double playerdegree) {
		camera.player_degree = playerdegree;
	}

	public double getMoveSpeed() {
		return camera.MOVE_SPEED;
	}

	public void setMoveSpeed(double newMoveSpeed) {
		camera.MOVE_SPEED = newMoveSpeed;
	}

	public double getRotationSpeed() {
		return camera.ROTATION_SPEED;
	}

	public void setRotationSpeed(double newRotationSpeed) {
		camera.ROTATION_SPEED = newRotationSpeed;
	}

	public double getFPS() {
		return Game.FPS;
	}

	public void setFPS(double newFPS) {
		Game.FPS = newFPS;
	}

	private void run_user_scripts() {

		for (int i = 0; i < event_data.length; i += 3) {
			try {
				Globals globals = JsePlatform.standardGlobals();
				globals.set("REAPI", CoerceJavaToLua.coerce(this));
				globals.load(new FileReader("data/" + event_data[i]), "data/" + event_data[i]).call(
						LuaValue.valueOf(Integer.parseInt(event_data[i + 1])),
						LuaValue.valueOf(Integer.parseInt(event_data[i + 2])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
