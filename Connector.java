package com.amcbridge.snake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;

public final class Connector {

	public static final char MOVEMENT_UP = '^';
	public static final char MOVEMENT_DOWN = 'v';
	public static final char MOVEMENT_LEFT = '<';
	public static final char MOVEMENT_RIGHT = '>';

	public static final char[] MOVEMENT_DIRECTIONS = { MOVEMENT_UP, MOVEMENT_DOWN, MOVEMENT_LEFT, MOVEMENT_RIGHT };

	public static final char READY = '\n';

	public static final char BODY_PART_UP = '^';
	public static final char BODY_PART_DOWN = 'v';
	public static final char BODY_PART_LEFT = '<';
	public static final char BODY_PART_RIGHT = '>';

	public static final char[] BODY_PARTS = { BODY_PART_UP, BODY_PART_DOWN, BODY_PART_LEFT, BODY_PART_RIGHT };

	public static final char BLOCK_GROUND = '.';
	public static final char BLOCK_WALL = '#';

	public static final char BLOCK_FOOD = '$';
	public static final char BLOCK_HEAD = '*';

	public static final int GAME_PARAM_ROWS = 0;
	public static final int GAME_PARAM_COLS = 1;
	public static final int GAME_PARAM_TIME = 2;
	public static final int GAME_PARAM_MOVES = 3;

	public static final int[] GAME_PARAMS = { GAME_PARAM_ROWS, GAME_PARAM_COLS, GAME_PARAM_TIME, GAME_PARAM_MOVES };

	public static final Map<Integer, Character> NEIGHBOUR_BODY_PART = new HashMap<>();
	static {
		NEIGHBOUR_BODY_PART.put(GameMap.NEIGHBOUR_FROM_TOP, BODY_PART_DOWN);
		NEIGHBOUR_BODY_PART.put(GameMap.NEIGHBOUR_FROM_BOTTOM, BODY_PART_UP);
		NEIGHBOUR_BODY_PART.put(GameMap.NEIGHBOUR_FROM_RIGHT, BODY_PART_LEFT);
		NEIGHBOUR_BODY_PART.put(GameMap.NEIGHBOUR_FROM_LEFT, BODY_PART_RIGHT);
	}

	private final static String PROPERTIES_FILE = "string.properties";
	private final static Properties properties = new Properties();

	private static String notNumberRegex = "[^0-9]";

	private int colCount = 0;
	private int rowCount = 0;
	private int moveTime = 0;
	private int moveCount = 0;

	private GameMap gameMap = null;
	private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

	public final static Properties getProperties() {
		return properties;
	}

	public Connector() {
		try {
			File file = new File(PROPERTIES_FILE);
			FileInputStream fileInput = new FileInputStream(file);
			properties.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getMoveTime() {
		return moveTime;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public GameMap readMap() throws IOException {

		gameMap = new GameMap();
		Point[][] map = new Point[colCount][rowCount];
		char notRotatedcharMap[][] = new char[rowCount][colCount];
		Point food = null;
		Point head = null;
		Set<Point> walls = new HashSet<>();
		int snakeBodyPartsCount = 0;

		for (int i = 0; i < rowCount; i++) {
			String line = inputReader.readLine();
			char t[] = line.toCharArray();
			notRotatedcharMap[i] = t;
		}

		// Switching to X/Y coordinates for easier usage.
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {

				map[j][i] = new Point(j, i);

				switch (notRotatedcharMap[i][j]) {

				case BLOCK_WALL:
					walls.add(map[j][i]);
					break;

				case BLOCK_FOOD:
					food = map[j][i];
					break;

				case BLOCK_HEAD:
					head = map[j][i];
					break;

				default:
					break;
				}
			}
		}

		gameMap.setMap(map);
		gameMap.setWalls(walls);
		gameMap.setFood(food);
		gameMap.setHead(head);

		// Adding walls around the map.
		for (int i = -1; i <= colCount; i++) {
			walls.add(new Point(i, -1));
			walls.add(new Point(i, rowCount));
		}

		for (int i = -1; i <= rowCount; i++) {
			walls.add(new Point(-1, i));
			walls.add(new Point(colCount, i));
		}

		// Counting snake's body parts.
		for (int x = 0; x < colCount; x++) {
			for (int y = 0; y < rowCount; y++) {
				for (char bodyPart : BODY_PARTS) {
					if (notRotatedcharMap[y][x] == bodyPart) {
						snakeBodyPartsCount++;
						break;
					}
				}
			}
		}

		LinkedList<Point> snakeBodyCoords = new LinkedList<>();

		// Parsing body from head to tail.
		snakeBodyCoords.addFirst(head);
		for (int i = 0; i < snakeBodyPartsCount; i++) {
			List<Point> neighbours = gameMap.getNeighbours(snakeBodyCoords.getFirst());

			for (int neighbourN : GameMap.NEIGHBOURS) {
				if (!walls.contains(neighbours.get(neighbourN))
						&& notRotatedcharMap[neighbours.get(neighbourN).getY()][neighbours.get(neighbourN)
								.getX()] == NEIGHBOUR_BODY_PART.get(neighbourN)) {
					snakeBodyCoords.addFirst(neighbours.get(neighbourN));
				}
			}
		}

		gameMap.setSnake(new Snake(snakeBodyCoords));
		return gameMap;
	}

	public void readParams() throws IOException {
		for (int gameParam : GAME_PARAMS) {
			String line = inputReader.readLine();
			String value = line.replaceAll(notNumberRegex, "");
			switch (gameParam) {
			case GAME_PARAM_ROWS:
				rowCount = Integer.parseInt(value);
				break;

			case GAME_PARAM_COLS:
				colCount = Integer.parseInt(value);
				break;

			case GAME_PARAM_TIME:
				moveTime = Integer.parseInt(value);
				break;

			case GAME_PARAM_MOVES:
				moveCount = Integer.parseInt(value);
				break;

			default:
				break;
			}
		}
		System.out.print(READY);
	}

	// Used when there is no need to read map, just make a next move.
	public void readMapBare() throws IOException {
		for (int i = 0; i < rowCount; i++) {
			inputReader.readLine();
		}
	}

	public void moveByPath(final Path path) throws IOException {

		if ((path == null)) {
			throw new NullPointerException(getProperties().getProperty("path_null"));
		}

		List<Point> points = path.getPoints();

		// First (0) point it's current head.
		for (int i = 1; i < points.size(); i++) {
			Point point = points.get(i);
			Point head = gameMap.getHead();
			if (point.getX() != head.getX()) {
				if (head.getX() > point.getX()) {
					move(MOVEMENT_LEFT);
				} else {
					move(MOVEMENT_RIGHT);
				}
			} else if (point.getY() != head.getY()) {
				if (head.getY() > point.getY()) {
					move(MOVEMENT_UP);
				} else {
					move(MOVEMENT_DOWN);
				}
			}

			gameMap.setHead(point);

			// When path is over read map.
			if (i == (points.size() - 1)) {
				gameMap = readMap();
			} else {
				readMapBare();
			}

		}
	}

	public void move(final char direction) {

		boolean isCharValid = false;
		for (char c : MOVEMENT_DIRECTIONS) {
			if (direction == c) {
				isCharValid = true;
			}
		}

		if (!isCharValid) {
			throw new RuntimeException(getProperties().getProperty("bad_move"));
		}

		System.out.print(direction);
	}

	public GameMap getMap() {
		return gameMap;
	}
}
