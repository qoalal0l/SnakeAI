package com.amcbridge.snake;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;

public final class GameMap {

	// Indexes of corresponding elements in a list returned from getNeibourgs()
	public static final int NEIGHBOUR_FROM_TOP = 0;
	public static final int NEIGHBOUR_FROM_RIGHT = 1;
	public static final int NEIGHBOUR_FROM_BOTTOM = 2;
	public static final int NEIGHBOUR_FROM_LEFT = 3;

	public static final int[] NEIGHBOURS = { NEIGHBOUR_FROM_TOP, NEIGHBOUR_FROM_RIGHT, NEIGHBOUR_FROM_BOTTOM,
			NEIGHBOUR_FROM_LEFT };

	private Point food;
	private Point head;
	private Set<Point> walls;
	private Point[][] map;
	private Snake snake;

	// Calculates Manhattan distance between points.
	public final static int distance(final Point a, final Point b) {
		if ((a == null) || (b == null)) {
			throw new NullPointerException(Connector.getProperties().getProperty("points_null"));
		}
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	GameMap() {
	}

	public GameMap(final Point food, final Point head, final Set<Point> walls, final Point[][] map, final Snake snake) {
		super();

		if ((food == null) || (head == null) || (walls == null) || (map == null) || (snake == null)) {
			throw new NullPointerException(Connector.getProperties().getProperty("args_null"));
		}

		this.food = food;
		this.head = head;
		this.walls = walls;
		this.map = map;
		this.snake = snake;
	}

	public void setFood(final Point food) {

		if (food == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("food_null"));
		}
		this.food = food;
	}

	public void setWalls(final Set<Point> walls) {

		if (walls == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("walls_null"));
		}

		this.walls = walls;
	}

	public void setMap(final Point[][] map) {

		if (map == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("map_null"));
		}

		this.map = map;
	}

	public void setSnake(final Snake snake) {

		if (snake == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("snake_null"));
		}

		this.snake = snake;
	}

	public Point getFood() {
		return food;
	}

	public Point getHead() {
		return head;
	}

	public Set<Point> getWalls() {
		return walls;
	}

	public Point[][] getMap() {
		return map;
	}

	public Snake getSnake() {
		return snake;
	}

	public void setHead(final Point head) {

		if (head == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("head_null"));
		}

		this.head = head;
	}

	public int getWidth() {
		return map.length;
	}

	public int getHeight() {
		return map[0].length;
	}

	private boolean isInBorders(final int x, final int y) {
		return ((x >= 0) && (y >= 0) && (x < getWidth()) && (y < getHeight()));
	}

	// Returns four points attached to central one.
	public List<Point> getNeighbours(final Point center) {

		if (center == null) {
			return null;
		}

		int x = center.getX();
		int y = center.getY();

		if (!isInBorders(x, y)) {
			return null;
		}

		List<Point> neighbours = new LinkedList<>();

		int nX = x;
		int nY = y - 1;
		if (isInBorders(nX, nY)) {
			neighbours.add(map[nX][nY]);
		} else {
			neighbours.add(new Point(nX, nY));
		}

		nX = x + 1;
		nY = y;
		if (isInBorders(nX, nY)) {
			neighbours.add(map[nX][nY]);
		} else {
			neighbours.add(new Point(nX, nY));
		}

		nX = x;
		nY = y + 1;
		if (isInBorders(nX, nY)) {
			neighbours.add(map[nX][nY]);
		} else {
			neighbours.add(new Point(nX, nY));
		}

		nX = x - 1;
		nY = y;
		if (isInBorders(nX, nY)) {
			neighbours.add(map[nX][nY]);
		} else {
			neighbours.add(new Point(nX, nY));
		}

		return neighbours;
	}
}
