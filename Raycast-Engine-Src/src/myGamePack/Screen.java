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
	private int[] outputPixels;
	private int frames;
	private Camera camera;
	private Sound current_bgm;
	private Sound current_sfe;
	private HashMap<String, Object> user_temp_variables = new HashMap<>();
	private int SCREEN_W;
	private int SCREEN_H;
	private int entity_limit = 30;
	private int entity_count = 0;
	private double BUFFER_DIST;
	
	public Screen(String[][] layer0, String[][] layer1, String[] event_data, int MAX_WORLD_LIMIT,
			HashMap<String, Texture> allTextures, int game_width, int game_height, int fog_col,
			String skyboxId, boolean skySelfMovement, int renderDistance, double world_light_factor, int[] pixels,
			Camera camera, int renderSpriteDistance, int SCREEN_W, int SCREEN_H, int[] gamepixels) {
		this.layer0 = layer0;
		this.layer1 = layer1;
		this.event_data = event_data;
		BUFFER_DIST = camera.BUFFER_DISTANCE;
		mapWidth = MAX_WORLD_LIMIT;
		mapHeight = MAX_WORLD_LIMIT;
		textures = allTextures;
		width = game_width;
		height = game_height;
		fog_color = fog_col;
		skyb = skyboxId;
		sky_wave = skySelfMovement;
		render_dist = renderDistance;
		sprite_render_dist = renderSpriteDistance;
		this.world_light_factor = world_light_factor;
		in_doors = skyb.contains("block") ? true : skyb.contains("skybox") ? false : false;
		skybox_default = allTextures.get(skyb);
		int spriteIdTemp = 0;
		for (int i = 0; i < layer1.length; i++) {
			for (int j = 0; j < layer1[i].length; j++) {
				if (!layer1[i][j].contains("sprite0") && !layer1[i][j].contains("block")) {
					spriteArrTemp.add(new Sprite(layer1[i][j], (double) i+0.5, (double) j+0.5, String.valueOf(spriteIdTemp), "null.lua"));
					spriteIdTemp++;
				}
			}
		}
		spriteArr = new Sprite[spriteArrTemp.size()];
		int k = 0;
		for (Sprite sprite : spriteArrTemp) {
			spriteArr[k] = sprite;
			k++;
		}
		this.pixels = gamepixels;
		this.camera = camera;
		this.SCREEN_W = SCREEN_W;
		this.SCREEN_H = SCREEN_H;
		this.outputPixels = pixels;
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
			for (int n = 0; n < pixels.length / 2; n++) {
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
				pixels[n] = darkenColor(new_fog_color_ceiling, darkened_factor);
				skybox_x++;
			}
		}
		double[] perp_wall_dist_buffer = new double[width];
		for (int x = 0; x < width; x++) {
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
			perp_wall_dist_buffer[x] = perpWallDist;
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
				pixels[x + y * (width)] = darkenColor(color, darkened_factor);
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
			for (int y = drawEnd; y < height; y++) {
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
				int new_color = textureFloor.pixels[floorTexY + (floorTexX * textureFloor.SIZE)];
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
				pixels[x + y * (width)] = darkenColor(new_color, darkened_factor);
				Texture ceilingTexture = skybox_default;
				int new_color_c = ceilingTexture.pixels[floorTexX + (floorTexY * ceilingTexture.SIZE)];
				int rcc = (int) ((percd_floor) * ((fog_color & 0xFF0000) >> 16)
						+ (1 - percd_floor) * ((new_color_c & 0xFF0000) >> 16));
				int gcc = (int) ((percd_floor) * ((fog_color & 0xFF00) >> 8)
						+ (1 - percd_floor) * ((new_color_c & 0xFF00) >> 8));
				int bcc = (int) ((percd_floor) * ((fog_color & 0xFF)) + (1 - percd_floor) * ((new_color_c & 0xFF)));
				new_color_c = ((rcc & 0x0ff) << 16) | ((gcc & 0x0ff) << 8) | (bcc & 0x0ff);
				if (in_doors) {
					pixels[x + (height - y) * (width)] = darkenColor(new_color_c, darkened_factor);
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
			for (int stripe = drawStartX; stripe < drawEndX; stripe++) {
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
						int direction = (int) ((angleInDegrees + 225) % 360 / 45);
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
							pixels[((stripe) + y * (width))] = darkenColor(color, darkened_factor);
						}
					}
			}
		}
		run_user_scripts();
		final float scale = Math.min(SCREEN_W / (float)width, SCREEN_H / (float)height);
		final int renderW = (int)(width * scale);
		final int renderH = (int)(height * scale);
		final int startX = (SCREEN_W - renderW) >> 1;
		final int startY = (SCREEN_H - renderH) >> 1;
		final int endX = startX + renderW;
		final int endY = startY + renderH;
		final float invScale = 1.0f / scale;
		for (int y = 0; y < SCREEN_H; y++) {
		    final int screenY = y * SCREEN_W;
		    final boolean inY = (y >= startY && y < endY);
		    for (int x = 0; x < SCREEN_W; x++) {
		        if (inY && x >= startX && x < endX) {
		            int srcX = (int)((x - startX) * invScale);
		            int srcY = (int)((y - startY) * invScale);		            
		            outputPixels[screenY + x] = pixels[srcY * width + srcX];
		        } else {
		            outputPixels[screenY + x] = 0xFF000000;
		        }
		    }
		}
	}
	
	public Boolean getKeyPressed(String keyname) {
		if (keyname.contains("left_arrow") && camera.left) {
			return true;
		} else if (keyname.contains("right_arrow") && camera.right) {
			return true;
		} else if (keyname.contains("down_arrow") && camera.back) {
			return true;
		} else if (keyname.contains("up_arrow") && camera.forward) {
			return true;
		} else if (keyname.contains("enter") && camera.enter) {
			return true;
		} else if (keyname.contains("space") && camera.space) {
			return true;
		} else if (keyname.contains("shift") && camera.shift) {
			return true;
		}
		return false;
	}
	
	public Boolean getKeyReleased(String keyname) {
		if (keyname.contains("left_arrow") && !camera.left) {
			return true;
		} else if (keyname.contains("right_arrow") && !camera.right) {
			return true;
		} else if (keyname.contains("down_arrow") && !camera.back) {
			return true;
		} else if (keyname.contains("up_arrow") && !camera.forward) {
			return true;
		} else if (keyname.contains("enter") && !camera.enter) {
			return true;
		} else if (keyname.contains("space") && !camera.space) {
			return true;
		} else if (keyname.contains("shift") && !camera.shift) {
			return true;
		}
		return false;
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
	
	public void addSprite(String spritename, double spriteXPos, double spriteYPos, String spriteId) {
		Sprite spr = new Sprite(spritename, spriteXPos, spriteYPos, spriteId, "null.lua");
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

	public void addUIToScreen(String textureName, int pos_x, int pos_y) {
        for (int y = 0; y < textures.get(textureName).IMG_HEI; y++) {
            for (int x = 0; x < textures.get(textureName).IMG_WID; x++) {
                int screenX = pos_x + x;
                int screenY = pos_y + y;
                int screenIndex = screenY * width + screenX;  
                if (screenX >= 0 && screenX < width && screenY >= 0 && screenY < height && textures.get(textureName).pixels[y * textures.get(textureName).IMG_WID + x]!=0x000000) {
                    pixels[screenIndex] = textures.get(textureName).pixels[y * textures.get(textureName).IMG_WID + x]; 
                }
            }
        }
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

	public void endScript(String script_name) {
		ArrayList<String> eventList = new ArrayList<>(Arrays.asList(event_data));
		for (int i = 0; i < event_data.length; i = i + 3) {
			if (event_data[i].equals(script_name)) {
				eventList.remove(i);
				eventList.remove(i);
				eventList.remove(i);
				break;
			}
		}
		event_data = eventList.toArray(new String[0]);
	}
	
	public void endScriptByPosition(String script_name, double posx, double posy) {
		ArrayList<String> eventList = new ArrayList<>(Arrays.asList(event_data));
		for (int i = 0; i < event_data.length; i = i + 3) {
			if (event_data[i].equals(script_name) && event_data[i+1].equals(Double.toString(posx)) && event_data[i+2].equals(Double.toString(posy))) {
				eventList.remove(i);
				eventList.remove(i);
				eventList.remove(i);
				break;
			}
		}
		event_data = eventList.toArray(new String[0]);
	}

	public void addScript(String script_name, double pos_x, double pos_y) {
		String eventXstr = Double.toString(pos_x);
		String eventYstr = Double.toString(pos_y);
		ArrayList<String> eventList = new ArrayList<>(Arrays.asList(event_data));
		eventList.add(script_name);
		eventList.add(eventXstr);
		eventList.add(eventYstr);
		event_data = eventList.toArray(new String[0]);
	}
	
	public void updateScriptPositionInMap(String script_name, double originalposx, double originalposy, double newposx, double newposy) {
		for (int i = 0; i < event_data.length; i = i + 3) {
			if (event_data[i].equals(script_name) && event_data[i+1].equals(Double.toString(originalposx)) && event_data[i+2].equals(Double.toString(originalposy))) {
				event_data[i+1] = Double.toString(newposx);
				event_data[i+2] = Double.toString(newposy);
				break;
			}
		}
	}

	public String getEventList() {
		return String.join(",", event_data);
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

	public void setPlayerDirection(double xdir, double ydir, double xplane, double yplane) {
		camera.xDir = xdir;
		camera.yDir = ydir;
		camera.xPlane = xplane;
		camera.yPlane = yplane;
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
	
	public void playBGM(String bgm_path, boolean loop) {
		current_bgm = new Sound("data/"+bgm_path, loop);
	}
	
	public void stopBGM() {
		current_bgm.stopSound();
	}
	
	public void playSE(String bgm_path, boolean loop) {
		current_sfe = new Sound("data/"+bgm_path, loop);
	}
	
	public void stopSE() {
		current_sfe.stopSound();
	}
	
	public void systemExit() {
		System.exit(0);
	}
	
	public void writeTempVar(String key, Object val) {
		user_temp_variables.put(key, val);
	}
	
	public Object readTempVar(String key) {
		return user_temp_variables.get(key);
	}
	
	public void add_entity(String entityid, String spritename, double pos_x, double pos_y, String behavior_script) {
		ArrayList<Sprite> spriteListTemp = new ArrayList<>(Arrays.asList(spriteArr));
		Sprite newSprite = new Sprite(spritename, pos_x, pos_y, entityid, behavior_script);
		spriteListTemp.add(newSprite);
		spriteArr = spriteListTemp.toArray(new Sprite[0]);
		addScript(behavior_script, pos_x, pos_y);
		entity_count++;
	}
	
	public int edit_entity(String entityid, String spritename, double pos_x, double pos_y) {
	    for (int i = spriteArr.length - 1; i >= 0; i--) {
	        if (spriteArr[i].getSpriteId().equals(entityid)) {
	        	updateScriptPositionInMap(get_entity_behavior_script(entityid), spriteArr[i].spriteXPos, spriteArr[i].spriteYPos, pos_x, pos_y);
	        	spriteArr[i].spritename = spritename;
	            spriteArr[i].spriteXPos = pos_x;
	            spriteArr[i].spriteYPos = pos_y;
	            return 1;
	        }
	    }
		return -1;
	}
	
	public String get_entity_id_by_position(double posx, double posy) {
		for (int i = spriteArr.length - 1; i >= 0; i--) {
			if (spriteArr[i].spriteXPos==posx && spriteArr[i].spriteYPos==posy) {
				return spriteArr[i].getSpriteId();
			}
		}
		return null;
	}
	
	public double get_entity_pos_x(String entityid) {
	    for (int i = spriteArr.length - 1; i >= 0; i--) {
	        if (spriteArr[i].getSpriteId().equals(entityid)) {
	            return spriteArr[i].spriteXPos;
	        }
	    }
	    return -1;
	}

	public double get_entity_pos_y(String entityid) {
	    for (int i = spriteArr.length - 1; i >= 0; i--) {
	        if (spriteArr[i].getSpriteId().equals(entityid)) {
	            return spriteArr[i].spriteYPos;
	        }
	    }
	    return -1;
	}

	public String get_entity_spritename(String entityid) {
	    for (int i = spriteArr.length - 1; i >= 0; i--) {
	        if (spriteArr[i].getSpriteId().equals(entityid)) {
	            return spriteArr[i].spritename;
	        }
	    }
	    return null;
	}

	public String get_entity_behavior_script(String entityid) {
	    for (int i = spriteArr.length - 1; i >= 0; i--) {
	        if (spriteArr[i].getSpriteId().equals(entityid)) {
	            return spriteArr[i].behaviorScript;
	        }
	    }
	    return null;
	}
	
	public void remove_entity(String entityid, String script_name) {
		ArrayList<Sprite> spriteListTemp = new ArrayList<>(Arrays.asList(spriteArr));
	    for (int i = spriteListTemp.size() - 1; i >= 0; i--) {
	        if (spriteListTemp.get(i).getSpriteId().equals(entityid)) {
	            spriteListTemp.remove(i);
	            break;
	        }
	    }
	    spriteArr = spriteListTemp.toArray(new Sprite[0]);
	    endScript(script_name);
	    entity_count--;
	}
	
	public void set_entity_limit(int limit) {
		entity_limit = limit;
	}
	
	public int read_entity_limit() {
		return entity_limit;
	}
	
	public int read_entity_count() {
		return entity_count;
	}
	
	public double euclidean_distance(double startx, double targetx, double starty, double targety) {
	    double dx = targetx - startx;
	    double dy = targety - starty;
	    return Math.sqrt(dx * dx + dy * dy);
	}
	
	public double manhattan_distance(double startx, double targetx, double starty, double targety) {
	    double dx = Math.abs(targetx - startx);
	    double dy = Math.abs(targety - starty);
	    return dx + dy;
	}
	
	public double decodeXfromPathString(String path_comma_sep) {
	    String[] parts = path_comma_sep.split(",");
	    return Double.parseDouble(parts[0]);
	}

	public double decodeYfromPathString(String path_comma_sep) {
	    String[] parts = path_comma_sep.split(",");
	    return Double.parseDouble(parts[1]);
	}
	
	public boolean[] check_wall(double xPos, double yPos, double BUFFER_DISTANCE, int xDir, int yDir, double MOVE_SPEED) {
			boolean[] good_to_move_dirs = new boolean[] {false, false};
			if (layer1[(int) (xPos + xDir * (MOVE_SPEED + BUFFER_DISTANCE))][(int) yPos].contains("sprite"))
				good_to_move_dirs[0] = true;
			if (layer1[(int) xPos][(int) (yPos + yDir * (MOVE_SPEED + BUFFER_DISTANCE))].contains("sprite"))
				good_to_move_dirs[1] = true;
		return good_to_move_dirs;
	}
	
	public double get_buffer_dist() {
		return BUFFER_DIST;
	}
	
	public String pathfindToward(String entityid, double source_x, double source_y, double targetx, double targety, double speed) {
		if (Math.abs(targety-source_y)<BUFFER_DIST && Math.abs(targetx-source_x)<BUFFER_DIST) {
			return source_x + "," + source_y;
		}
		int y_dir = 0;
		if (Math.abs(targety-source_y)<BUFFER_DIST) {
			y_dir = 0;
		} else if (targety > source_y) {
			y_dir = 1;
		} else if (targety < source_y) {
			y_dir = -1;
		}
		int x_dir = 0;
		if (Math.abs(targetx-source_x)<BUFFER_DIST) {
			x_dir = 0;
		} else if (targetx > source_x) {
			x_dir = 1;
		} else if (targetx < source_x) {
			x_dir = -1;
		}
		double newx = source_x + (x_dir*speed);
        double newy = source_y + (y_dir*speed);
        boolean[] gooddirs = check_wall(newx, newy, BUFFER_DIST, x_dir, y_dir, speed);
        if (gooddirs[0] && gooddirs[1]) {
            return newx + "," + newy;
        } else if (gooddirs[0]) {
        	return newx + "," + source_y;
        } else if (gooddirs[1]) {
        	return source_x + "," + newy;
        }
        return source_x + "," + source_y;
	}
	
	public double abs(double value) {
		return Math.abs(value);
	}
	
	public double cos(double degree) {
		return Math.cos(degree);
	}
	
	public double sin(double degree) {
		return Math.sin(degree);
	}
	
	public String padWithLeadingZeros(int number, int totalLength) {
	    return String.format("%0" + totalLength + "d", number);
	}
	
	public double getAngleBetweenVectors(double zvectorx, double zvectory, double pvectorx, double pvectory) {
		double dotProduct = zvectorx * pvectorx + zvectory * pvectory;
	    double zMagnitude = Math.sqrt(zvectorx * zvectorx + zvectory * zvectory);
	    double pMagnitude = Math.sqrt(pvectorx * pvectorx + pvectory * pvectory);
	    if (zMagnitude == 0 || pMagnitude == 0) {
	        return 0.0;
	    }
	    double cosTheta = dotProduct / (zMagnitude * pMagnitude);
	    cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));
	    double angleInDegrees = Math.toDegrees(Math.acos(cosTheta));
	    return angleInDegrees;
	}
	
	public void consoleDoubleLog(double value) {
		System.out.println(Double.toString(value));
	}
	
	public void displayText(String text, int pos_x, int pos_y, String fontfile) {
	    text = text.toLowerCase(); 
	    int[] font_pixels = textures.get(fontfile).pixels; 
	    int cursor = pos_x;  
	    
	    int font_original_pixel_size = textures.get(fontfile).IMG_HEI / 43; 
	    
	    for (int i = 0; i < text.length(); i++) {
	        int letter_location_in_fontpng = -1;  
	        switch (text.charAt(i)) {
	            case 'a': letter_location_in_fontpng = 0; break;
	            case 'b': letter_location_in_fontpng = 1; break;
	            case 'c': letter_location_in_fontpng = 2; break;
	            case 'd': letter_location_in_fontpng = 3; break;
	            case 'e': letter_location_in_fontpng = 4; break;
	            case 'f': letter_location_in_fontpng = 5; break;
	            case 'g': letter_location_in_fontpng = 6; break;
	            case 'h': letter_location_in_fontpng = 7; break;
	            case 'i': letter_location_in_fontpng = 8; break;
	            case 'j': letter_location_in_fontpng = 9; break;
	            case 'k': letter_location_in_fontpng = 10; break;
	            case 'l': letter_location_in_fontpng = 11; break;
	            case 'm': letter_location_in_fontpng = 12; break;
	            case 'n': letter_location_in_fontpng = 13; break;
	            case 'o': letter_location_in_fontpng = 14; break;
	            case 'p': letter_location_in_fontpng = 15; break;
	            case 'q': letter_location_in_fontpng = 16; break;
	            case 'r': letter_location_in_fontpng = 17; break;
	            case 's': letter_location_in_fontpng = 18; break;
	            case 't': letter_location_in_fontpng = 19; break;
	            case 'u': letter_location_in_fontpng = 20; break;
	            case 'v': letter_location_in_fontpng = 21; break;
	            case 'w': letter_location_in_fontpng = 22; break;
	            case 'x': letter_location_in_fontpng = 23; break;
	            case 'y': letter_location_in_fontpng = 24; break;
	            case 'z': letter_location_in_fontpng = 25; break;
	            case '0': letter_location_in_fontpng = 26; break;
	            case '1': letter_location_in_fontpng = 27; break;
	            case '2': letter_location_in_fontpng = 28; break;
	            case '3': letter_location_in_fontpng = 29; break;
	            case '4': letter_location_in_fontpng = 30; break;
	            case '5': letter_location_in_fontpng = 31; break;
	            case '6': letter_location_in_fontpng = 32; break;
	            case '7': letter_location_in_fontpng = 33; break;
	            case '8': letter_location_in_fontpng = 34; break;
	            case '9': letter_location_in_fontpng = 35; break;
	            case ',': letter_location_in_fontpng = 42; break;
	            case '.': letter_location_in_fontpng = 36; break;
	            case '\'': letter_location_in_fontpng = 37; break;
	            case '"': letter_location_in_fontpng = 37; break;
	            case '?': letter_location_in_fontpng = 39; break;
	            case '!': letter_location_in_fontpng = 38; break;
	            case '-': letter_location_in_fontpng = 41; break;
	            case ':': letter_location_in_fontpng = 40; break;
	            default: letter_location_in_fontpng = -1; break;
	        }

	        if (letter_location_in_fontpng > -1) {
	            for (int j = 0; j < font_original_pixel_size; j++) {
	                for (int k = 0; k < font_original_pixel_size; k++) {
	                    int font_index = (letter_location_in_fontpng * font_original_pixel_size + j) * font_original_pixel_size + k;
	                    int ind = (pos_y + j) * width + (cursor + k); 

	                    if (ind < width * height && font_pixels[font_index]!=0x000000) {
	                        pixels[ind] = font_pixels[font_index];
	                    }
	                }
	            }
	        }

	        cursor += font_original_pixel_size;
	    }
	}

	private void run_user_scripts() {
		for (int i = 0; i < event_data.length; i += 3) {
			try {
				Globals globals = JsePlatform.standardGlobals();
				globals.set("REAPI", CoerceJavaToLua.coerce(this));
				globals.load(new FileReader("data/" + event_data[i]), "data/" + event_data[i]).call(
						LuaValue.valueOf(Double.parseDouble(event_data[i + 1])),
						LuaValue.valueOf(Double.parseDouble(event_data[i + 2])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
