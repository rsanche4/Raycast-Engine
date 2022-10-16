package myGamePack;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public class Screen {
	public int[][] map;
	public int mapWidth, mapHeight, width, height;
	public ArrayList<Texture> textures;
	public int pixel_effect;
	public int render_dist = 20;
	public int sprite_render_dist = 15*render_dist;
	public int fog_color;
	public int ground_color;
	public int numSprites;
	public boolean sky_wave;
	public int animationLengths;
	public Sprite[] spriteArr;
	public Texture sky;
	
	public Screen(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h, int pixEf, int fogc, int ground_c, Texture skybox, int ns, Sprite[] sa, int aL, boolean sw) {
		map = m;
		mapWidth = mapW;
		mapHeight = mapH;
		textures = tex;
		width = w;
		height = h;
		pixel_effect = pixEf;
		fog_color = fogc;
		ground_color = ground_c;
		sky = skybox;
		numSprites = ns;
		spriteArr = sa;
		animationLengths = aL;
		sky_wave = sw;
		
	}
		
	
	
	public int[] update(Camera camera, int[] pixels, int frames) {
		if (camera.isMenu) {
			int menu_texture = 1000;
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = textures.get(menu_texture-1).pixels[i];
			}
		} else {
		
		
		// draws skybox
		double row=-1.0;
		double max_row = height/2;
		int new_fog_color_ceiling=0;
		double fog_perc = 0;
		int skybox_x = -1;
		int skybox_y = -1;
		int rotate_sky = (int)(((360-camera.player_degree) / 360.0)*sky.W_SKYSIZE);
		if (sky_wave) {
			rotate_sky = rotate_sky + (int)(30*Math.cos(((double)frames % 50)/8.0));
		}
		for (int n=0; n < pixels.length/2; n=n+pixel_effect) {
			// adding fog 
			if (n % width == 0 && row < max_row) {
				skybox_x = rotate_sky;
				if (skybox_x < 0) {
					skybox_x = 0;
				}
				skybox_y++;
				row++;
				fog_perc = row/max_row;
			} else if (row > max_row) {
				break;
			}
				int rc = (int)((fog_perc)*((fog_color & 0xFF0000) >> 16) + (1-fog_perc)*((sky.pixels[skybox_x + sky.W_SKYSIZE*skybox_y] & 0xFF0000) >> 16));
	            int gc = (int)((fog_perc)*((fog_color & 0xFF00) >> 8) + (1-fog_perc)*((sky.pixels[skybox_x + sky.W_SKYSIZE*skybox_y] & 0xFF00) >> 8));
	            int bc = (int)((fog_perc)*((fog_color & 0xFF)) + (1-fog_perc)*((sky.pixels[skybox_x + sky.W_SKYSIZE*skybox_y] & 0xFF)));
	            new_fog_color_ceiling = ((rc&0x0ff)<<16)|((gc&0x0ff)<<8)|(bc&0x0ff);
	            for (int i=0; i<pixel_effect; i++) pixels[n+i] = new_fog_color_ceiling;
	            skybox_x+=pixel_effect;
			
		}
		
		// drawing floor
		row = height/2;
		max_row = height/2;
		int new_fog_color_floor=fog_color;
		double not_fog_perc = 0.0;
		// draws floor
	    for(int i=pixels.length/2; i<pixels.length; i++) {
	    	//adding fog
	    	if (i % width == 0) {
				row--;
				not_fog_perc = row/max_row;
			}
	    	int rc = (int)((not_fog_perc)*((fog_color & 0xFF0000) >> 16) + (1-not_fog_perc)*((ground_color & 0xFF0000) >> 16));
            int gc = (int)((not_fog_perc)*((fog_color & 0xFF00) >> 8) + (1-not_fog_perc)*((ground_color & 0xFF00) >> 8));
            int bc = (int)((not_fog_perc)*((fog_color & 0xFF)) + (1-not_fog_perc)*((ground_color & 0xFF)));
            new_fog_color_floor = ((rc&0x0ff)<<16)|((gc&0x0ff)<<8)|(bc&0x0ff);
            pixels[i] = new_fog_color_floor;
	        
	    }
	    
	    double[] perp_wall_dist_buffer = new double[width];
	    for (int x=0; x<width; x=x+pixel_effect) {
	    	double cameraX = 2 * x / (double)(width) -1;
	        
	        double rayDirX = camera.xDir + camera.xPlane * cameraX; 
	        
	        double rayDirY = camera.yDir + camera.yPlane * cameraX;
	        //Map position
	        int mapX = (int)camera.xPos;
	        int mapY = (int)camera.yPos;
	        int playerX = (int)camera.xPos;
	        int playerY = (int)camera.yPos;
	        //length of ray from current position to next x or y-side
	        double sideDistX;
	        double sideDistY;
	        //Length of ray from one side to next in map
	        double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
	        double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
	        double perpWallDist;
	        //Direction to go in x and y
	        int stepX, stepY;
	        boolean hit = false;//was a wall hit
	        int side=0;//was the wall vertical or horizontal
	        
	      //Figure out the step direction and initial distance to a side
	        if (rayDirX < 0)
	        {
	            stepX = -1;
	            sideDistX = (camera.xPos - mapX) * deltaDistX;
	        }
	        else
	        {
	            stepX = 1;
	            sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
	        }
	        if (rayDirY < 0)
	        {
	            stepY = -1;
	            sideDistY = (camera.yPos - mapY) * deltaDistY;
	        }
	        else
	        {
	            stepY = 1;
	            sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
	        }
	        
	        while(!hit) {
	            //Jump to next square
	            if (sideDistX < sideDistY)
	            {
	                sideDistX += deltaDistX;
	                mapX += stepX;
	                side = 0;
	            }
	            else
	            {
	                sideDistY += deltaDistY;
	                mapY += stepY;
	                side = 1;
	            }
	            //Check if ray has hit a wall (if ray hits 999, that is an invisible wall, not a hit)
	         // if this ray hit a sprite, then ignore it because that is not a hit (sprites are 700 to 998, 999 is reserved for invisible wall, 800 to 998 is for animating sprites)
	            // anything below that is texture for a block
	            if(map[mapX][mapY] > 0 && map[mapX][mapY] < 700) hit = true;
	        }
	        
	        
	        
	      //this tries to find the taller wall structures
	        boolean tall_wall = false; // if texture is EVEN on map, then it's tall, odd is short
	        if (map[mapX][mapY] % 2 == 0) tall_wall = true;
	        
	        
	        
	      //Calculate distance to the point of impact
	        if(side==0)
	            perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
	        else
	            perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);
	        
	        // add it to distance buffer
	        for (int i=0; i<pixel_effect; i++) perp_wall_dist_buffer[x+i] = perpWallDist;
	        
	        
	        //Now calculate the height of the wall based on the distance from the camera
	        int lineHeight;
	        
	        if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist));
	        else lineHeight = height;
	        //calculate lowest and highest pixel to fill in current stripe
	        int drawStart = (-lineHeight/2+ height/2); 
	        if (tall_wall)
		        drawStart = (-lineHeight+ height/2);// wall is taller now
	        if(drawStart < 0)
	            drawStart = 0;
	        int drawEnd = lineHeight/2 + height/2;
	        if(drawEnd >= height) 
	            drawEnd = height - 1;
	        
	        if (perpWallDist > render_dist) {
	        	drawStart = 0; // sets a limmited view rendering distance
	        	drawEnd = 0;
	        }
	        
	      //add a texture to all sides (up to 99)
	        int texNum = map[mapX][mapY] - 1;
	        double wallX;//Exact position of where wall was hit
	        // textures to 2 sides, or 1 side if within these ranges
	        if ((map[mapX][mapY] > 99 && map[mapX][mapY] < 200 && side==0) 
	        		|| (map[mapX][mapY] > 499 && map[mapX][mapY] < 700 && side==0)
	        		|| (map[mapX][mapY] > 199 && map[mapX][mapY] < 500 && side==1) 
	        		|| (map[mapX][mapY] > 299 && map[mapX][mapY] < 400 && playerX > mapX)       	       
	        		|| (map[mapX][mapY] > 399 && map[mapX][mapY] < 500 && playerX < mapX)       
	        		|| (map[mapX][mapY] > 499 && map[mapX][mapY] < 600 && playerY > mapY)        
	        		|| (map[mapX][mapY] > 599 && map[mapX][mapY] < 700 && playerY < mapY)) wallX =0.0;
	        else if(side==1) {//If its a y-axis wall
	            wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
	        } 
	        else {//X-axis wall
	            wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
	        }
	        wallX-=Math.floor(wallX);
	        
	       
	       
	      //x coordinate on the texture
	        int texX = (int)(wallX * (textures.get(texNum).SIZE));
	        if(side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZE - texX - 1;
	        if(side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZE - texX - 1;
	        
	      //calculate y coordinate on texture
	        for(int y=drawStart; y<drawEnd; y++) {
	        	int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;	
	        	if (tall_wall) {
	        		texY+=64 ;//Note that the top of your 64x128 texture will get cut off
	        
	            }
	            	
		            int color;
		            if(side==0) color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
		            else color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]>>1) & 0x7F7F7F;//Make y sides darker
		            // adding fog 
		            double percd = perpWallDist/render_dist;
		            int rc = (int)((percd)*((fog_color & 0xFF0000) >> 16) + (1-percd)*((color & 0xFF0000) >> 16));
		            int gc = (int)((percd)*((fog_color & 0xFF00) >> 8) + (1-percd)*((color & 0xFF00) >> 8));
		            int bc = (int)((percd)*((fog_color & 0xFF)) + (1-percd)*((color & 0xFF)));
		            color = ((rc&0x0ff)<<16)|((gc&0x0ff)<<8)|(bc&0x0ff);
		            for (int i=0; i<pixel_effect; i++) pixels[(x+i) + y*(width)] = color;	
	            
	        }
	        
	    }
	    
	    
	    // sorting sprites and also check which is an animating sprite
	    for (int s=0; s < numSprites; s++) {
	    	
	    	spriteArr[s].spriteDist = ((camera.xPos - spriteArr[s].spriteXPos) * (camera.xPos - spriteArr[s].spriteXPos) + (camera.yPos - spriteArr[s].spriteYPos) * (camera.yPos - spriteArr[s].spriteYPos)); //sqrt not taken, unneeded
	    	if (map[(int)spriteArr[s].spriteXPos][(int)spriteArr[s].spriteYPos] > 799 && map[(int)spriteArr[s].spriteXPos][(int)spriteArr[s].spriteYPos] < 999) {
	    		map[(int)spriteArr[s].spriteXPos][(int)spriteArr[s].spriteYPos] = 800 + frames % animationLengths;
	    	}
	    }
	    Arrays.sort(spriteArr);
	    
	    // adding sprite 
	    for(int i = 0; i < numSprites; i++) {
	    	double spriteX = (double)spriteArr[i].spriteXPos - camera.xPos;
	        double spriteY = (double)spriteArr[i].spriteYPos - camera.yPos;
	        
	        double invDet = 1.0 / (camera.xPlane * camera.yDir - camera.xDir * camera.yPlane); //required for correct matrix multiplication
	        
	        double transformX = invDet * (camera.yDir * spriteX - camera.xDir * spriteY);
	        double transformY = invDet * (-camera.yPlane * spriteX + camera.xPlane * spriteY); //this is actually the depth inside the screen, that what Z is in 3D

	        int spriteScreenX = (int)((width / 2) * (1 + transformX / transformY));
	      //calculate height of the sprite on screen
	        int spriteHeight = Math.abs((int)(height / (transformY)));//using 'transformY' instead of the real distance prevents fisheye
	        //calculate lowest and highest pixel to fill in current stripe
	        int drawStartY = -spriteHeight / 2 + height / 2;
	        if(drawStartY < 0) drawStartY = 0;
	        int drawEndY = spriteHeight / 2 + height / 2;
	        if(drawEndY >= height) drawEndY = height - 1;
	        
	      //calculate width of the sprite
	        int spriteWidth = Math.abs( (int) (height / (transformY)));
	        int drawStartX = -spriteWidth / 2 + spriteScreenX;
	        if(drawStartX < 0) drawStartX = 0;
	        int drawEndX = spriteWidth / 2 + spriteScreenX;
	        if(drawEndX >= width) drawEndX = width - 1;
	        
	        if (spriteArr[i].spriteDist > sprite_render_dist) {
	        	drawStartX = 0; // sets a limmited view rendering distance
	        	drawEndX = 0;
	        }
	        
	      //loop through every vertical stripe of the sprite on screen
	        
	        int texNum = map[(int)spriteArr[i].spriteXPos][(int)spriteArr[i].spriteYPos] - 1;
	        for(int stripe = drawStartX; stripe < drawEndX; stripe++)
	        {
	        	int texX = (int)(256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * spriteArr[i].getTexture(textures, texNum).SIZE / spriteWidth) / 256;
	          //the conditions in the if are:
	          //1) it's in front of camera plane so you don't see things behind you
	          //2) it's on the screen (left)
	          //3) it's on the screen (right)
	          //4) ZBuffer, with perpendicular distance
	          if(transformY > 0 && stripe > 0 && stripe < width && transformY < perp_wall_dist_buffer[stripe])
	          for(int y = drawStartY; y < drawEndY; y++) //for every pixel of the current stripe
	          {
	              int d = (y) * 256 - height * 128 + spriteHeight * 128; //256 and 128 factors to avoid floats
	              int texY = ((d * spriteArr[i].getTexture(textures, texNum).SIZE) / spriteHeight) / 256;
	              int color = spriteArr[i].getTexture(textures, texNum).pixels[Math.abs(texX + (texY * spriteArr[i].getTexture(textures, texNum).SIZE))];
	              
	              //get current color from the texture
		            if(color != 0x000000) {
		            	double percd = spriteArr[i].spriteDist/sprite_render_dist;
			            int rc = (int)((percd)*((fog_color & 0xFF0000) >> 16) + (1-percd)*((color & 0xFF0000) >> 16));
			            int gc = (int)((percd)*((fog_color & 0xFF00) >> 8) + (1-percd)*((color & 0xFF00) >> 8));
			            int bc = (int)((percd)*((fog_color & 0xFF)) + (1-percd)*((color & 0xFF)));
			            color = ((rc&0x0ff)<<16)|((gc&0x0ff)<<8)|(bc&0x0ff);
		            	for (int l=0; l<pixel_effect; l++) pixels[(stripe+l) + y*(width)] = color;
		            } //paint pixel if it isn't black, black is the invisible color
	              
	          }
	        }
	        
	    }
        
        // here you can display any extra stuff you would want to draw
	    // EDIT HERE
	    
		}
	    return pixels;
	    
	}
	
}
