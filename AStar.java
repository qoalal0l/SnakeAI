package com.amcbridge.snake.pathfinder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.amcbridge.snake.GameMap;
import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;

public final class AStar extends PathFinder {

	// Imaginary snake that is used to predict paths.
	private Snake virtualSnake = null;
	private GameMap gameMap = null;

	// Part of A* algorithm.
	private List<Point> closedList = null;

	Path finalPath = null;
	boolean reset = true;

	public AStar() {
		super();
	}

	// Calculates path from start to finish on map using A* algorithm.
	@Override
	public Path calculatePath(Point start, final Point finish, final GameMap gameMap) {

		super.calculatePath(start, finish, gameMap);

		this.gameMap = gameMap;
		virtualSnake = gameMap.getSnake();

		outer: while (true) {

			if (reset) {
				finalPath = new Path(start);
				reset = false;
			}

			Point minPoint = null;
			Path intermidiatePath = new Path(start);
			List<Point> openList = new LinkedList<>();
			closedList = new LinkedList<>();
			List<Point> neighbours = findValidNeighbours(start);

			start.setParent(null);
			openList.add(start);
			openList.addAll(neighbours);

			for (Point p : neighbours) {
				p.setParent(start);
			}

			openList.remove(start);
			closedList.add(start);

			for (Point p : openList) {
				p.setH(GameMap.distance(p, finish));
				p.setG(1);
				p.setF(p.getH() + p.getG());
			}

			while (true) {
				if (openList.isEmpty()) {

					if (finalPath.getLenght() == 0) {
						return null;
					}

					finalPath.removeLast();
					finalPath.getLast().addBadPoint(start);
					start = finalPath.getLast().getPoint();
					virtualSnake.goBack();

					continue outer;
				}

				minPoint = openList.get(0);

				for (Point p : openList) {
					if (p.getF() < minPoint.getF()) {
						minPoint = p;
					}
				}

				openList.remove(minPoint);
				closedList.add(minPoint);

				if (minPoint.equals(finish)) {
					break;
				}

				neighbours = findValidNeighbours(minPoint);
				List<Point> newcomers = new LinkedList<>();

				for (Point neighbour : neighbours) {
					if (!openList.contains(neighbour)) {
						newcomers.add(neighbour);
						neighbour.setH(GameMap.distance(neighbour, finish));
						neighbour.setG(minPoint.getG() + 1);
						neighbour.setF(neighbour.getH() + neighbour.getG());

					} else {
						if (neighbour.getG() > (minPoint.getG() + 1)) {
							neighbour.setG(minPoint.getG() + 1);
							neighbour.setF(neighbour.getH() + neighbour.getG());
							neighbour.setParent(minPoint);
						}
					}
				}
				openList.addAll(newcomers);
				for (Point p : newcomers) {
					p.setParent(minPoint);
				}
			}

			List<Point> reversedPath = new LinkedList<>();
			while (minPoint != null) {
				reversedPath.add(minPoint);
				minPoint = minPoint.getParent();
			}

			intermidiatePath = new Path(start);

			// A* star gives path from finish to start, so we need to reverse
			// it.
			for (int i = reversedPath.size() - 2; i >= 0; i--) {
				intermidiatePath.add(reversedPath.get(i));
			}

			start = intermidiatePath.getPoints().get(1);
			finalPath.add(start);
			if (!start.equals(finish)) {
				virtualSnake.moveTo(start);

			} else {
				if (pathChecker.isPathSafe(intermidiatePath, virtualSnake, gameMap)) {
					virtualSnake.moveAndEat(start);
					reset = true;
					return finalPath;

				} else {
					finalPath.removeLast();
					finalPath.getLast().addBadPoint(start);
					start = finalPath.getLast().getPoint();
				}
			}
		}
	}

	private List<Point> findValidNeighbours(final Point point) {

		if (point == null) {
			throw new NullPointerException("Point must not be null.");
		}

		List<Point> newighbours = gameMap.getNeighbours(point);
		List<Point> validNeighbours = new LinkedList<>();

		for (Point p : newighbours) {
			if (!closedList.contains(p) && !virtualSnake.contains(p) && !gameMap.getWalls().contains(p)) {
				if (reset) {
					validNeighbours.add(p);
				} else {
					if (!finalPath.getLast().getBadPoints().contains(p)) {
						validNeighbours.add(p);
					}
				}
			}
		}

		Collections.shuffle(validNeighbours);
		return validNeighbours;
	}

}
