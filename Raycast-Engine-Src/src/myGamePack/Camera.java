package myGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Camera Class
// Description: The class that allows for movement of the player when inputting keys. Main keys are detected here.
public class Camera implements KeyListener {
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back, enter, space, shift;
	public double MOVE_SPEED;
	public double ROTATION_SPEED;
	public double player_degree = 0;
	public double BUFFER_DISTANCE = 0.4;

	public Camera(double x, double y, double xd, double yd, double xp, double yp, double walkingSpeed,
			double turningSpeed) {
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		player_degree = (Math.toDegrees(Math.atan2(yDir, xDir))+270)%360;
		MOVE_SPEED = walkingSpeed;
		ROTATION_SPEED = turningSpeed;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		return;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			forward = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			back = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enter = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			space = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			forward = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			back = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enter = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			space = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		}
	}

	public void update(String[][] layer0, String[][] layer1) {
		if (forward) {
			if (layer1[(int) (xPos + xDir * (MOVE_SPEED + BUFFER_DISTANCE))][(int) yPos].contains("sprite"))
				xPos += xDir * MOVE_SPEED;
			if (layer1[(int) xPos][(int) (yPos + yDir * (MOVE_SPEED + BUFFER_DISTANCE))].contains("sprite"))
				yPos += yDir * MOVE_SPEED;
		}
		if (back) {
			if (layer1[(int) (xPos - xDir * (MOVE_SPEED + BUFFER_DISTANCE))][(int) yPos].contains("sprite"))
				xPos -= xDir * MOVE_SPEED;
			if (layer1[(int) xPos][(int) (yPos - yDir * (MOVE_SPEED + BUFFER_DISTANCE))].contains("sprite"))
				yPos -= yDir * MOVE_SPEED;
		}
		if (right) {
			double oldxDir = xDir;
			xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir * Math.sin(-ROTATION_SPEED);
			yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane * Math.sin(-ROTATION_SPEED);
			yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane * Math.cos(-ROTATION_SPEED);
			player_degree = (Math.toDegrees(Math.atan2(yDir, xDir)) + 270)%360;
		}
		if (left) {
			double oldxDir = xDir;
			xDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
			yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
			yPlane = oldxPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);
			player_degree = (Math.toDegrees(Math.atan2(yDir, xDir)) + 270)%360;
		}
	}
}
