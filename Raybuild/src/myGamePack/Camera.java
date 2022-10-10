package myGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
	public boolean isMenu = true;
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back, enter;
	public final double MOVE_SPEED = .04;
	public final double ROTATION_SPEED = .02;
	public double player_degree = 0;
	
	public Camera(double x, double y, double xd, double yd, double xp, double yp) {
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		player_degree = Math.toDegrees(Math.atan2(yDir, xDir))+180;
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
	}
	
	public void update(int[][] map) {
	    if (enter && isMenu) {
	    	isMenu = false;
	    	Sound.stopSound();
	    	Sound.playSound("C:\\Users\\rafas\\eclipse-workspace\\MyGame3d\\res\\dire.wav");
	    }
		if (forward) {
			if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos += xDir * MOVE_SPEED;
			}
			if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0) {
				yPos += yDir * MOVE_SPEED;
			}
		}
		if (back) {
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos -= xDir * MOVE_SPEED;
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)] == 0)
				yPos -= yDir * MOVE_SPEED;
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
