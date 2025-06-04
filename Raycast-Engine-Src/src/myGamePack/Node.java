package myGamePack;

// Node Class
// Description: This class is mainly used for pathfinding of entities
public class Node {
	public final double x;
	public final double y;
	public final String id;
	public final double parentX;
	public final double parentY;
	public final String parentId;
	public final double h;

	public Node(double x, double y, double parentX, double parentY, double h) {
		this.x = x;
	    this.y = y;
	    this.id = x + "," + y;
	    this.parentX = parentX;
	    this.parentY = parentY;
	    this.parentId = parentX + "," + parentY;
	    this.h = h;
	}
}
