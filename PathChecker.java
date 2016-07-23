package com.amcbridge.snake.pathfinder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.amcbridge.snake.GameMap;
import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;

public final class PathChecker {

	private final static int MAX_OPERATIONS = 500;
	
	private GameMap gameMap = null;
	private Snake ghostSnake = null;
	private Path freePath = null;

	// If there is enough space for snake to move on her full length after snake
	// traveled along the path, then path is save.
	public boolean isPathSafe(final Path path, final Snake virtualSnake, final GameMap gameMap) {

		if ((path == null) || (path.isEmpty())) {
			throw new NullPointerException("path_null");
		}

		if (virtualSnake == null) {
			throw new NullPointerException("snake_null");
		}

		if ((gameMap == null) || (gameMap.getWalls() == null) || (gameMap.getFood() == null)) {
			throw new NullPointerException("map_null_init");
		}
		
		int operationsCount = 0;

		this.gameMap = gameMap;
		Point min = path.getLast().getPoint();
		ghostSnake = new Snake(virtualSnake.getBody());
		ghostSnake.moveAndEat(min);
		freePath = new Path(min);
		boolean safe = true;
		int initialMegaSnakeSize = ghostSnake.getBody().size();

		while (freePath.getLenght() < initialMegaSnakeSize) {
			
			operationsCount++;

			Point neighbour = findRandomNeighbour(ghostSnake.getHead());
			if (neighbour == null) {
				Point badPoint = freePath.getLast().getPoint();
				if (freePath.getPoints().size() <= 1) {
					safe = false;
					break;
				} else {
					freePath.removeLast();
					freePath.getLast().addBadPoint(badPoint);
					ghostSnake.goBack();
					
					if (operationsCount>MAX_OPERATIONS){
						return true;
					}
					continue;
				}

			}
			freePath.add(neighbour);
			ghostSnake.moveTo(neighbour);
		}
		if (safe) {
			return true;
		} else {
			return false;
		}

	}

	private Point findRandomNeighbour(final Point point) {

		if (point == null) {
			throw new NullPointerException("Point should not be null.");
		}

		List<Point> neighbours = gameMap.getNeighbours(point);
		List<Point> validNeighbours = new LinkedList<>();
		for (Point neighbour : neighbours) {
			boolean isNeighbourBad = false;
			if (!freePath.isEmpty()) {
				isNeighbourBad = freePath.getLast().getBadPoints().contains(neighbour);
			}

			if (!isNeighbourBad) {
				if (!ghostSnake.contains(neighbour)) {
					if (!gameMap.getWalls().contains(neighbour)) {
						validNeighbours.add(neighbour);
					}
				}
			}

		}
		if (validNeighbours.isEmpty()) {
			return null;
		}

		return getBestRandom(point, validNeighbours);
	}

	// Randomness to reduce chance of looping.
	public Point getBestRandom(final Point center, final List<Point> neighbours) {
		Collections.shuffle(neighbours);
		Point random = neighbours.get(0);
		if (random.equals(center.getLastRandomNeighbour()) && neighbours.size() >= 2) {
			center.setLastRandomNeighbour(neighbours.get(1));
			return neighbours.get(1);
		} else {
			center.setLastRandomNeighbour(neighbours.get(0));
			return neighbours.get(0);
		}
	}
}
