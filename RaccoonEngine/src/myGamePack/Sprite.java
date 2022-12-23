package myGamePack;

import java.util.ArrayList;

// a small class that stores info about sprite
public class Sprite implements Comparable<Sprite> {
	public double spriteXPos;
	public double spriteYPos;
	public double spriteDist; // the distance to the player. Set by screen
	
	public Sprite(double sx, double sy) {
		spriteXPos = sx;
		spriteYPos = sy;
	}
	
	// returns the texture class for a specific sprite from the Texture Array given the position to get them from
	public Texture getTexture(ArrayList<Texture> tex, int num) {
		return tex.get(num);
	}

	@Override
	public int compareTo(Sprite otherSprite) {
		if (otherSprite.spriteDist > this.spriteDist) {
		    return 1;
		}
		if (otherSprite.spriteDist < this.spriteDist) {
		    return -1;
		};
		return (int)(otherSprite.spriteDist - this.spriteDist);
	}
	
}
