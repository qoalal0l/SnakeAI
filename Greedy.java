package com.amcbridge.snake.pathfinder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amcbridge.snake.GameMap;
import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;

public final class Greedy extends PathFinder {

	// Imaginary snake that is used to predict paths.
	private Snake virtualSnake = null;
	GameMap gameMap = null;

	Path path = null;

	public Greedy() {
		super();
	}

	// Calculates path from start to finish on map using my own greedy
	// algorithm.
	@Override
	public Path calculatePath(final Point start, final Point finish, final GameMap gameMap) {

		super.calculatePath(start, finish, gameMap);

		this.gameMap = gameMap;
		virtualSnake = gameMap.getSnake();
		path = new Path(virtualSnake.getHead());

		while (true) {
			Point min = findAStar(virtualSnake.getHead());
			if (min == null) {
				Point bad = path.getLast().getPoint();
				path.removeLast();
				if (path.isEmpty()) {
					return path;
				}
				path.getLast().addBadPoint(bad);
				virtualSnake.goBack();
				continue;

			}
			path.add(min);

			if (min.equals(gameMap.getFood())) {

				if (pathChecker.isPathSafe(path, virtualSnake, gameMap)) {
					virtualSnake.moveAndEat(min);
					break;
				} else {
					path.removeLast();
					path.getLast().addBadPoint(min);
					continue;
				}
			} else {
				virtualSnake.moveTo(min);
			}

		}
		return path;
	}

	// Returns neighbour with smallest Manhattan distance to the food.
	private Point findAStar(final Point point) {

		if (point == null) {
			throw new NullPointerException("Point must not be null.");
		}

		List<Point> cross = gameMap.getNeighbours(point);
		Map<Point, Integer> values = new LinkedHashMap<>();

		for (Point p : cross) {
			boolean badCont = false;
			if (!path.isEmpty()) {
				badCont = path.getLast().getBadPoints().contains(p);
			}

			if (!badCont) {
				if (!virtualSnake.contains(p)) {
					if (!gameMap.getWalls().contains(p)) {
						values.put(p, GameMap.distance(p, gameMap.getFood()));
					}
				}
			}
		}

		if (values.isEmpty()) {
			return null;
		}

		Iterator<Entry<Point, Integer>> it = values.entrySet().iterator();
		Point minPoint = it.next().getKey();
		int minVal = values.get(minPoint);
		List<Point> minValues = new LinkedList<>();
		List<Point> bigValues = new LinkedList<>();

		for (Entry<Point, Integer> entry : values.entrySet()) {
			if (entry.getValue() < minVal) {
				minVal = entry.getValue();
			}
		}

		for (Entry<Point, Integer> entry : values.entrySet()) {
			if (entry.getValue() == minVal) {
				minValues.add(entry.getKey());
			} else {
				bigValues.add(entry.getKey());
			}
		}

		if (minValues.isEmpty())
			return null;

		if ((Math.random() < 0.1) && !bigValues.isEmpty()) {
			return pathChecker.getBestRandom(point, bigValues);
		}
		return pathChecker.getBestRandom(point, minValues);
	}

}
