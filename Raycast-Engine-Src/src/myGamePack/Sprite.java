package myGamePack;

import java.util.HashMap;

// Sprite Class
// Description: This class is for sprites mainly. This will be good for sorting them and then drawing them on the screen.
public class Sprite implements Comparable<Sprite> {
	public double spriteXPos;
	public double spriteYPos;
	public double spriteDist;
	public String spritename;

	public Sprite(String spritename, double sx, double sy) {
		spriteXPos = sx;
		spriteYPos = sy;
		this.spritename = spritename;
	}

	public Texture getTexture(HashMap<String, Texture> tex) {
		return tex.get(spritename);
	}

	@Override
	public int compareTo(Sprite otherSprite) {
		if (otherSprite.spriteDist > this.spriteDist) {
			return 1;
		}
		if (otherSprite.spriteDist < this.spriteDist) {
			return -1;
		}
		;
		return (int) (otherSprite.spriteDist - this.spriteDist);
	}
}
