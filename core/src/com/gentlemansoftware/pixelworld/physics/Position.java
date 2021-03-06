package com.gentlemansoftware.pixelworld.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gentlemansoftware.pixelworld.world.MapTile;

public class Position {

	/**
	 * 
	 */
	public int x;
	public int y;
	public int z;
	public int xFraction;
	public int yFraction;
	public int zFraction;

	public static final int fractionMax_x = MapTile.tileWidth;
	public static final int fractionMax_y = MapTile.tileHeight;

	private static int speed = 1;
	public static final Position STOP = new Position(0, 0, 0, 0, 0, 0);
	public static final Position NORTH = new Position(0, 0, 0, speed, 0, 0);
	public static final Position EAST = new Position(0, 2 * speed, 0, 0, 0, 0);
	public static final Position SOUTH = new Position(0, 0, 0, -speed, 0, 0);
	public static final Position WEST = new Position(0, -2 * speed, 0, 0, 0, 0);

	public Position(int x, int xFraction, int y, int yFraction, int z, int zFraction) {
		set(x, xFraction, y, yFraction, z, zFraction);
	}

	public Position(int x, int xFraction, int y, int yFraction) {
		this(x, xFraction, y, yFraction, 0, 0);
	}

	public Position(int x, int y) {
		this(x, 0, y, 0, 0, 0);
	}

	public Position() {
		this(0, 0, 0, 0, 0, 0);
	}

	public static Position getDirection(Position from, Position to) {
		Position distance = from.distance(to);

		if (distance.length() < EAST.length()) {
			return distance;
		}

		Position dxn = distance.cpy().addAndSet(NORTH);
		Position dxe = distance.cpy().addAndSet(EAST);
		Position dxs = distance.cpy().addAndSet(SOUTH);
		Position dxw = distance.cpy().addAndSet(WEST);

		float dxlN = dxn.length();
		float dxlE = dxe.length();
		float dxlS = dxs.length();
		float dxlW = dxw.length();

		float minNE = Float.min(dxlN, dxlE);
		float minSW = Float.min(dxlS, dxlW);
		float minNESW = Float.min(minNE, minSW);

		if (dxlN == minNESW)
			return NORTH.cpy();
		if (dxlE == minNESW)
			return EAST.cpy();
		if (dxlS == minNESW)
			return SOUTH.cpy();
		if (dxlW == minNESW)
			return WEST.cpy();

		return STOP.cpy();
	}

	public static Position clambIfDistanceToLong(Position from, Position to, Position velocity) {
		Position distance = to.distance(from);
		Position back = velocity.cpy();
		if (velocity.lengthAbsoluteX() > distance.lengthAbsoluteX()) {
			back.set(distance.x, distance.xFraction, velocity.y, velocity.yFraction, velocity.z, velocity.zFraction);
		}
		if (velocity.lengthAbsoluteY() > distance.lengthAbsoluteY()) {
			back.set(velocity.x, velocity.xFraction, distance.y, distance.yFraction, velocity.z, velocity.zFraction);
		}

		return back;
	}

	public static Position getPositionDirectionFromVector(Vector2 v) {
		Direction dir = Direction.getDirectionFromVector(v);
		switch (dir) {
		case NORTH:
			return NORTH;
		case EAST:
			return EAST;
		case SOUTH:
			return SOUTH;
		case WEST:
			return WEST;
		default:
			return EAST;
		}
	}

	public Position set(int x, int xFraction, int y, int yFraction, int z, int zFraction) {
		this.x = x;
		this.xFraction = xFraction;
		this.y = y;
		this.yFraction = yFraction;
		this.z = z;
		this.zFraction = zFraction;
		return calcOverflow();
	}

	public Position calcOverflow() {
		if (xFraction < 0) {
			xFraction += fractionMax_x;
			this.x--;
		}
		if (yFraction < 0) {
			yFraction += fractionMax_y;
			this.y--;
		}

		this.x = this.x + xFraction / fractionMax_x;
		this.y = this.y + yFraction / fractionMax_y;
		this.xFraction = this.xFraction % fractionMax_x;
		this.yFraction = this.yFraction % fractionMax_y;
		return this;
	}

	public Position getPosition() {
		return cpy();
	}

	public Vector3 getVector3() {
		return new Vector3(x + (xFraction * 1f / fractionMax_x * 1f), 0, y + (yFraction * 1f / fractionMax_y * 1f));
	}

	public Position cpy() {
		return new Position(this.x, this.xFraction, this.y, this.yFraction, this.z, this.zFraction);
	}

	public Position addAndSet(int x, int xFraction, int y, int yFraction, int z, int zFraction) {
		return set(this.x + x, this.xFraction + xFraction, this.y + y, this.yFraction + yFraction, this.z + z,
				this.zFraction + zFraction);
	}

	public Position addAndSet(int x, int xFraction, int y, int yFraction) {
		return addAndSet(x, xFraction, y, yFraction, 0, 0);
	}

	public Position addAndSet(Position p) {
		return addAndSet(p.x, p.xFraction, p.y, p.yFraction, p.z, p.zFraction);
	}

	public Position subAndSet(Position p) {
		return addAndSet(p.scaleAndSet(-1));
	}

	public Position scaleAndSet(int scale_x, int scale_y, int scale_z) {
		return set((this.x * scale_x), (this.xFraction * scale_x), (this.y * scale_y), (this.yFraction * scale_y),
				(this.z * scale_z), (this.zFraction * scale_z));
	}

	public Position scaleAndSet(int scale_x, int scale_y) {
		return scaleAndSet(scale_x, scale_y, 1);
	}

	public Position scaleAndSet(int scale) {
		return scaleAndSet(scale, scale, scale);
	}

	public Position scaleAndSet(Position p) {
		return scaleAndSet(p.x, p.y, p.z);
	}

	public Position scaleAndSet(float scale_x, float scale_y, float scale_z) {
		return set((int) (this.x * scale_x), (int) (this.xFraction * scale_x), (int) (this.y * scale_y),
				(int) (this.yFraction * scale_y), (int) (this.z * scale_z), (int) (this.zFraction * scale_z));
	}

	public Position scaleAndSet(float scale) {
		return scaleAndSet(scale);
	}

	public Position distance(Position other) {
		Position me = this.cpy();
		Position ot = other.cpy();
		return me.addAndSet(ot.scaleAndSet(-1));
	}

	public float lengthAbsoluteX() {
		return Math.abs(lengthX());
	}

	public float lengthX() {
		return this.x + fractionLengthX();
	}

	public float lengthAbsoluteY() {
		return Math.abs(lengthY());
	}

	public float lengthY() {
		return this.y + fractionLengthY();
	}

	public float length() {
		return lengthAbsoluteX() + lengthAbsoluteY();
	}

	public Position rotate(float degree) {
		int divisor = getFractionDivisor();
		int x_total = this.x * divisor + getFractionXComparable();
		int y_total = this.y * divisor + getFractionYComparable();

		/*
		 * Normal Vector rotation https://en.wikipedia.org/wiki/Rotation_matrix
		 */

		int x_total_new = (int) (Math.cos(Math.toRadians(degree)) * x_total
				- Math.sin(Math.toRadians(degree)) * y_total);
		int y_total_new = (int) (Math.sin(Math.toRadians(degree)) * x_total
				+ Math.cos(Math.toRadians(degree)) * y_total);

		int x_new = x_total_new / divisor;
		int y_new = y_total_new / divisor;

		int xFraction_new = (x_total_new - x_new * divisor) / fractionMax_y;
		int yFraction_new = (y_total_new - y_new * divisor) / fractionMax_x;

		set(x_new, xFraction_new, y_new, yFraction_new, this.z, this.zFraction);
		return this;
	}

	private int getFractionDivisor() {
		int divisor = fractionMax_x * fractionMax_y;
		return (divisor == 0) ? 1 : divisor;
	}

	private int getFractionXComparable() {
		return this.xFraction * fractionMax_y;
	}

	private int getFractionYComparable() {
		return this.yFraction * fractionMax_x;
	}

	float fractionLengthX() {
		int xComp = getFractionXComparable();
		int comp = getFractionDivisor();
		return (1.0f * xComp) / comp;
	}

	float fractionLengthY() {
		int yComp = getFractionYComparable();
		int comp = getFractionDivisor();
		return (1.0f * yComp) / comp;
	}

	public boolean equals(Position p) {
		return this.x == p.x && this.xFraction == p.xFraction && this.y == p.y && this.yFraction == p.yFraction;
	}

	public String toString() {
		return this.x + "|" + this.xFraction + "|" + this.y + "|" + this.yFraction + "|" + this.z + "|"
				+ this.zFraction;
	}

	public static Position positionFromString(String s) {
		String[] splits = s.split("|");
		int x = Integer.parseInt(splits[0]);
		int xf = Integer.parseInt(splits[1]);
		int y = Integer.parseInt(splits[2]);
		int yf = Integer.parseInt(splits[3]);
		int z = Integer.parseInt(splits[4]);
		int zf = Integer.parseInt(splits[5]);
		return new Position(x, xf, y, yf, z, zf);
	}

}
