package com.amcbridge.snake.components;

public final class Point {

	private final int x;
	private final int y;

	private Point parent = null;

	// A* algorithm variables.
	private int F = 0;
	private int G = 0;
	private int H = 0;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getF() {
		return F;
	}

	public void setF(int f) {
		F = f;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		Point p = (Point) obj;
		return (this.getX() == p.getX()) && (this.getY() == p.getY());
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + x;
		hashCode = 31 * hashCode + y;
		return hashCode;
	}

	public void setParent(Point p) {
		parent = p;
	}

	public Point getParent() {
		return parent;
	}

	private Point lastRandomNeighbour = null;

	public Point getLastRandomNeighbour() {
		return lastRandomNeighbour;
	}

	public void setLastRandomNeighbour(Point lastRandomNeighbour) {
		this.lastRandomNeighbour = lastRandomNeighbour;
	}

}
