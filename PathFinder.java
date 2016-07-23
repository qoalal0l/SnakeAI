package com.amcbridge.snake.pathfinder;

import com.amcbridge.snake.GameMap;
import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;

public abstract class PathFinder {

	protected PathChecker pathChecker = null;

	public PathFinder() {
		pathChecker = new PathChecker();
	}

	public Path calculatePath(Point start, Point finish, GameMap gameMap) {

		if ((start == null) || (finish == null)) {
			throw new NullPointerException("Points should not be null.");
		}

		if ((gameMap == null) || (gameMap.getSnake() == null)) {
			throw new NullPointerException("Map should be initialized.");
		}

		return null;
	}
}
