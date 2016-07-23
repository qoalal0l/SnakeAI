package com.amcbridge.snake.components;

import java.util.LinkedList;
import java.util.List;

import com.amcbridge.snake.Connector;

public final class Path {

	public final static class Entry {
		private final Point p;
		private final List<Point> badPoints;

		Entry(final Point p) {

			if (p == null) {
				throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
			}

			this.p = p;
			badPoints = new LinkedList<>();
		}

		public Point getPoint() {
			return p;
		}

		public List<Point> getBadPoints() {
			return badPoints;
		}

		public void addBadPoint(final Point badPoint) {

			if (p == null) {
				throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
			}

			badPoints.add(badPoint);
		}
	}

	private List<Entry> path = null;

	public Path(final Point head) {

		if (head == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("head_null"));
		}

		path = new LinkedList<>();
		add(head);
	}

	public void add(final Point p) {

		if (p == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
		}

		path.add(new Entry(p));
	}

	public List<Point> getPoints() {
		List<Point> points = new LinkedList<>();
		for (Entry e : path) {
			points.add(e.getPoint());
		}
		return points;
	}

	public List<Point> getBadPoints(final Point p) {

		if (p == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
		}

		return path.get(path.indexOf(p)).getBadPoints();
	}

	public void addBadPoint(final Point p) {

		if (p == null) {
			throw new NullPointerException(Connector.getProperties().getProperty("point_null"));
		}

		path.get(path.indexOf(p)).addBadPoint(p);
	}

	public void removeLast() {
		path.remove(path.size() - 1);

	}

	public Entry getLast() {
		return path.get(path.size() - 1);
	}

	public boolean isEmpty() {
		return path.isEmpty();
	}

	public int getLenght() {
		return path.size() - 1;
	}
}
