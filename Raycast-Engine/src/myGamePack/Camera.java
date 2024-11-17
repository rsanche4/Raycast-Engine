package myGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
	public boolean isMenu = true;
	public static double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back, enter, one, two, three, four, five, six, seven, eight, nine, zero;
	public static int speed_setting = 3;
	public static int rotate_setting = 1;
	public static double[] speed_options = {.01,.02,.03,.04, .05, .06, .07, .08,.09,.1, .11, .12, .13, .14, .15};
	public static double MOVE_SPEED = speed_options[speed_setting];
	public static double ROTATION_SPEED = speed_options[rotate_setting];
	public static int retro_feel_i = 0;
	public static int[] retro_options = {2,4,5,8,10,20,40,50};
	public static int floor_i = 0;
	public static int[] floor_texts_nums = {1001,1003,1005,1007,1009,1011,1013,1015,1017,1019,1021,1023,1025,1027,1029,1031,1033,1035,1037,1039,1041,1043,1045,1047,1049,1051,1053,1055,1057,1059,1061,1063,1065,1067,1069,1071,1073,1075,1077,1079,1081,1083,1085,1087,1089,1091,1093,1095};
	public static int top_i = 0;
	public static int[] ceiling_textures = {1100, 1101, 1102, 1103, 1104, 1105, 1106,1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115};
	public static Texture[] skybox_textures = {Texture._66, Texture._67, Texture._68, Texture._69, Texture._70, Texture._71, Texture._72, Texture._73, Texture._74, Texture._75, Texture._76, Texture._77, Texture._78, Texture._79, Texture._80, Texture._81};
	public static boolean gate12 = false;
	public static boolean gate22 = false;
	public static boolean gate13 = false;
	public static boolean gate23 = false;
	public static boolean gate15 = false;
	public static boolean gate25 = false;
	public static boolean gate16 = false;
	public static boolean gate26 = false;
	public static boolean gate17 = false;
	public static boolean gate27 = false;
	public static boolean gate18 = false;
	public static boolean gate28 = false;
	public double player_degree = 0;
	public String bgm;
	
	public Camera(double x, double y, double xd, double yd, double xp, double yp, String background) {
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		player_degree = Math.toDegrees(Math.atan2(yDir, xDir))+180;
		bgm = background;
	}
	
	public void changeSpeed() {
		speed_setting = (speed_setting + 1) % speed_options.length;
		MOVE_SPEED = speed_options[speed_setting];
		System.out.println(MOVE_SPEED);
	}

	public void changeRotate() {
		rotate_setting = (rotate_setting + 1) % speed_options.length;
		ROTATION_SPEED = speed_options[rotate_setting];
		System.out.println(ROTATION_SPEED);
	}
	
	public void changeRenderDistance() {
		Screen.render_dist = (Screen.render_dist + 1) % 100;
		Screen.sprite_render_dist = 12*Screen.render_dist;
	}
	
	public void changeRetroFeel() {
		retro_feel_i = (retro_feel_i + 1) % retro_options.length;
		Screen.pixel_effect = retro_options[retro_feel_i];
		System.out.println(Screen.pixel_effect);
	}
	
	public void changeFloor() {
		floor_i = (floor_i + 1) % floor_texts_nums.length;
		Screen.default_tile_number = floor_texts_nums[floor_i]-1;
	}
	
	public void changeSkyMove() {
		if (Screen.sky_wave) {
			Screen.sky_wave = false;
		} else {
			Screen.sky_wave = true;
		}
	}
	
	public void changeFogColorPlus() {
		Screen.fog_color = (Screen.fog_color + 10000) % 16777215;
		System.out.println(Screen.fog_color);
	}
	
	public void changeFogColorMinus() {
		Screen.fog_color = Math.abs(Screen.fog_color - 10000) % 16777215;
		System.out.println(Math.abs(Screen.fog_color));
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		return;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT)) {
			left = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_RIGHT)) {
			right = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_UP)) {
			forward = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_DOWN)) {
			back = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
			enter = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		/*
		 * if ((e.getKeyCode() == KeyEvent.VK_1)) { one = true; } if ((e.getKeyCode() ==
		 * KeyEvent.VK_2)) { two = true; } if ((e.getKeyCode() == KeyEvent.VK_3)) {
		 * three = true; } if ((e.getKeyCode() == KeyEvent.VK_4)) { four = true; } if
		 * ((e.getKeyCode() == KeyEvent.VK_5)) { five = true; } if ((e.getKeyCode() ==
		 * KeyEvent.VK_6)) { six = true; } if ((e.getKeyCode() == KeyEvent.VK_7)) {
		 * seven = true; } if ((e.getKeyCode() == KeyEvent.VK_8)) { eight = true; } if
		 * ((e.getKeyCode() == KeyEvent.VK_9)) { nine = true; } if ((e.getKeyCode() ==
		 * KeyEvent.VK_0)) { zero = true; }
		 */
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT)) {
			left = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_RIGHT)) {
			right = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_UP)) {
			forward = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_DOWN)) {
			back = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
			enter = false;
		}
		/*
		 * if ((e.getKeyCode() == KeyEvent.VK_1)) { one = false; } if ((e.getKeyCode()
		 * == KeyEvent.VK_2)) { two = false; } if ((e.getKeyCode() == KeyEvent.VK_3)) {
		 * three = false; } if ((e.getKeyCode() == KeyEvent.VK_4)) { four = false; } if
		 * ((e.getKeyCode() == KeyEvent.VK_5)) { five = false; } if ((e.getKeyCode() ==
		 * KeyEvent.VK_6)) { six = false; } if ((e.getKeyCode() == KeyEvent.VK_7)) {
		 * seven = false; } if ((e.getKeyCode() == KeyEvent.VK_8)) { eight = false; } if
		 * ((e.getKeyCode() == KeyEvent.VK_9)) { nine = false; } if ((e.getKeyCode() ==
		 * KeyEvent.VK_0)) { zero = false; }
		 */
	}
	
	public void update(int[][] map) {
	    if (enter && isMenu) {
	    	isMenu = false;
	    	Sound.stopSound();
	    	//System.out.println("> World generation finished. " +Game.username + " joined the world!");
	    	Sound.playSound(bgm);
	    }
		/*
		 * if (one) { Game.generate_new_world = true; }
		 * 
		 * if (two) { if (gate12) { gate22 = true; } gate12 = true; if (!gate22) {
		 * changeSpeed(); } } else { gate12 = false; gate22 = false; }
		 * 
		 * if (three) { if (gate13) { gate23 = true; } gate13 = true; if (!gate23) {
		 * changeRotate(); } } else { gate13 = false; gate23 = false; }
		 * 
		 * if (four) { changeRenderDistance(); }
		 * 
		 * if (five) { if (gate15) { gate25 = true; } gate15 = true; if (!gate25) {
		 * changeRetroFeel(); } } else { gate15 = false; gate25 = false; }
		 * 
		 * if (six) { if (gate16) { gate26 = true; } gate16 = true; if (!gate26) {
		 * changeFloor(); } } else { gate16 = false; gate26 = false; }
		 * 
		 * if (seven) { if (gate17) { gate27 = true; } gate17 = true; if (!gate27) {
		 * changeCeil(); } } else { gate17 = false; gate27 = false; }
		 * 
		 * if (eight) { if (gate18) { gate28 = true; } gate18 = true; if (!gate28) {
		 * changeSkyMove(); } } else { gate18 = false; gate28 = false; }
		 * 
		 * if (nine) { changeFogColorPlus(); }
		 * 
		 * if (zero) { changeFogColorMinus(); }
		 */
	    
		if (forward) {
			if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] < 1 || map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] > 699) {
				if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] != 999) {
					xPos += xDir * MOVE_SPEED;
				}
				
			}
			if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] < 1 || map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] > 699) {
				if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] != 999) {
					yPos += yDir * MOVE_SPEED;
				}
				
			}
		}
		if (back) {
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] < 1 || map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] > 699 )
				if (map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] != 999) {
					xPos -= xDir * MOVE_SPEED;
				}
				
				
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] < 1 || map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] > 699)
				if (map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] != 999) {
					yPos -= yDir * MOVE_SPEED;
				}
				
				
		}
		if(right) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
			yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
			player_degree = Math.toDegrees(Math.atan2(yDir, xDir))+180; // gives me current degree player is facing
		}
		if(left) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
			player_degree = Math.toDegrees(Math.atan2(yDir, xDir))+180; // gives me current degree player is facing
		}
	}
	
	
	
}
