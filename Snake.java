package com.amcbridge.snake.components;

import java.util.LinkedList;
import java.util.List;

import com.amcbridge.snake.Connector;

public final class Snake {

	private List<Point> bodyPoints;
	private int tailPosition;

	public Snake(final List<Point> bodyPoints) {

		if (bodyPoints == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("body_null"));
		}

		this.bodyPoints = bodyPoints;
		tailPosition = 0;
	}

	public void moveTo(final Point point) {

		if (point == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
		}

		bodyPoints.add(point);
		tailPosition++;
	}

	public void moveAndEat(final Point point) {

		if (point == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
		}

		bodyPoints.add(point);
	}

	public void goBack() {
		bodyPoints.remove(bodyPoints.size() - 1);
		tailPosition--;

		if (tailPosition == -1) {
			throw new RuntimeException(Connector.getProperties().getProperty("backward_out"));
		}
	}

	public boolean contains(final Point point) {
		return bodyPoints.subList(tailPosition + 1, bodyPoints.size()).contains(point);
	}

	public List<Point> getBody() {
		return new LinkedList<Point>(bodyPoints.subList(tailPosition, bodyPoints.size()));
	}

	public Point getHead() {
		return bodyPoints.get(bodyPoints.size() - 1);
	}

}
