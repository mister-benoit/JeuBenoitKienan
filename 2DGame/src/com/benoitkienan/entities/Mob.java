package com.benoitkienan.entities;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.benoitkienan.tiles.Tile;

import CDIO.pathFinder.AStar;
import CDIO.pathFinder.AreaMap;
import CDIO.pathFinder.Node;
import CDIO.pathFinder.heuristics.AStarHeuristic;
import CDIO.pathFinder.heuristics.DiagonalHeuristic;

public class Mob extends Entity {
    AreaMap map;
    AStarHeuristic heuristic = new DiagonalHeuristic();
    AStar aStar;
    public ArrayList<Point> shortestPath;
    ArrayList<Point> path;
    Node node;
    Player nearestPlayer;
    int xMin, xMax, yMin, yMax;
    int distanceX, distanceY;

    public Mob(String name) {
	super(name);
	speed = 10;
    }
    
    public Mob(String name, String imgName) {
 	super(name);
 	speed = 10;
	try {
	    image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Pictures/"+imgName));
	} catch (IOException e) {
	    e.printStackTrace();
	}
     }
    

    public void refreshPath(int bx, int by, Tile[][] array) {
	map = new AreaMap(array.length, array[0].length, array);
	aStar = new AStar(map, heuristic);
	shortestPath = aStar.calcShortestPath((int) (posX / panGame.cellSizeX), (int) (posY / panGame.cellSizeY), bx, by);

    }

    public void followPath() {
	if (shortestPath != null) {
	    if (shortestPath.size() != 0) {
		if (shortestPath.get(0).getX() > (int) (posX / panGame.cellSizeX)) {
		    addForceX(speed);
		} else if (shortestPath.get(0).getX() < (int) (posX / panGame.cellSizeX)) {
		    addForceX(-speed);
		}

		if (shortestPath.get(0).getY() < (int) (posY / panGame.cellSizeY)) {
		    addForceY(-speed);
		} else if (shortestPath.get(0).getY() > (int) (posY / panGame.cellSizeY)) {
		    addForceY(speed);
		}
	    }
	}

    }

    public Player getNearestPlayer(ArrayList<Player> plist, Tile[][] array) {
	if ((int) ((posX / panGame.cellSizeX) - 100) < 0) {
	    xMin = 0;
	} else {
	    xMin = (int) ((posX / panGame.cellSizeX) - 100);
	}

	if ((int) ((posX / panGame.cellSizeX) + 100) > array.length) {
	    xMax = array.length;
	} else {
	    xMax = (int) ((posX / panGame.cellSizeX) + 100);
	}

	if ((int) ((posY / panGame.cellSizeY) - 100) < 0) {
	    yMin = 0;
	} else {
	    yMin = (int) ((posY / panGame.cellSizeY) - 100);
	}

	if ((int) ((posY / panGame.cellSizeY) + 100) > array[0].length) {
	    yMax = array[0].length;
	} else {
	    yMax = (int) ((posY / panGame.cellSizeY) + 100);
	}

	Tile[][] aggroMob = new Tile[xMax][yMax];

	for (int x = xMin; y < xMax; y++) {
	    for (int y = yMin; y < yMax; y++) {
		aggroMob[x][y] = array[x][y];
	    }
	}

	for (Player pl : plist) {
	    distanceX = (int) Math.abs((pl.getPosX() / panGame.cellSizeX) - (posX / panGame.cellSizeX));
	    distanceY = (int) Math.abs((pl.getPosY() / panGame.cellSizeY) - (posY / panGame.cellSizeY));
	    if (distanceX < 100 && distanceY < 100) {
		map = new AreaMap(aggroMob.length, aggroMob[0].length, aggroMob);
		aStar = new AStar(map, heuristic);
		path = aStar.calcShortestPath((int) (posX / panGame.cellSizeX), (int) (posY / panGame.cellSizeY), (int) (pl.getPosX() / panGame.cellSizeX), (int) (pl.getPosY() / panGame.cellSizeY));

		if (path != null) {
		    if (shortestPath == null) {
			shortestPath = path;
		    } else if (shortestPath.size() > path.size()) {
			nearestPlayer = pl;
		    }

		}
	    }
	}
	return nearestPlayer;
    }

    public void goToNearestPlayer(ArrayList<Player> plist, Tile[][] array) {

	this.getNearestPlayer(plist, array);
	if (distanceX < 100 && distanceY < 100) {
	    if (nearestPlayer != null)
		this.refreshPath((int) (this.nearestPlayer.getPosX() / panGame.cellSizeX), (int) (this.nearestPlayer.getPosY() / panGame.cellSizeY), array);
	}
    }

    public ArrayList<Point> getPath() {
	return shortestPath;
    }

    public void setPath(ArrayList<Point> path) {
	shortestPath = path;
    }

}