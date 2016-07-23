package com.amcbridge.snake;

import com.amcbridge.snake.components.Path;
import com.amcbridge.snake.components.Point;
import com.amcbridge.snake.components.Snake;
import com.amcbridge.snake.pathfinder.AStar;
import com.amcbridge.snake.pathfinder.Greedy;
import com.amcbridge.snake.pathfinder.PathFinder;

public class Game {
	
	public final static void main(String[] args) {
		
		try {
			
			Connector con = new Connector();
			GameMap gameMap = null;
			Path path = null;
			
			PathFinder aStar = new AStar();
			PathFinder greedy = new Greedy();

			con.readParams();
			gameMap = con.readMap();		

			while (true) {
				
				Point food = gameMap.getFood();
				Snake virtualSnake = gameMap.getSnake();				

				path = aStar.calculatePath(virtualSnake.getHead(), food, gameMap);
				if (path == null) {
					path = greedy.calculatePath(virtualSnake.getHead(), food, gameMap);
				}
				if ((path == null)||(path.isEmpty())) {
					return;
				}
				con.moveByPath(path);
				gameMap = con.getMap();
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
