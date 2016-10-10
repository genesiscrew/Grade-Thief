package view;

import model.characters.GuardBot;
import model.characters.Player;
import model.floor.Location;
import model.rendering.Polygon;

import java.awt.*;
import java.util.*;

import controller.GameController;

/**
 * @Author Adam Wareing
 *
 */
public class PolygonDrawer {

	/**
	 * Will hold the order that the polygons in the ArrayList DPolygon should be
	 * drawn meaning DPolygon.get(polygonDrawOrder[0]) should be drawn first,
	 * and hence be on the bottom most layer
	 */
	private int[] polygonDrawOrder;
	private Room room;
	private double[] lightDir;
	private double[] viewFrom;
	private GameController controller;
	private ArrayList<Location> polygonxy;

	public PolygonDrawer(Room room, double[] lightDir, double[] viewFrom, GameController controller) {
		this.room = room;
		this.lightDir = lightDir;
		this.viewFrom = viewFrom;
		this.controller = controller;
	}

	public void drawPolygons(Graphics g, boolean guard, Player otherPlayer, Player currentPlayer, int timer,
			String RoomName, double X, double Y) {
		java.util.List<Polygon> allPolygons = getAllPolygonsThatNeedToBeDrawn(guard, otherPlayer, currentPlayer,
				RoomName, X, Y);

		// Updates each polygon for this camera position
		for (int i = 0; i < allPolygons.size(); i++)
			allPolygons.get(i).updatePolygon(lightDir, viewFrom);

		// Set drawing order so closest polygons gets drawn last
		setOrder(allPolygons);

		// Set the polygon that the mouse is currently over
		// setPolygonOver();

		// Draw polygons in the Order that is set by the 'setOrder' function
		for (int i = 0; i < polygonDrawOrder.length; i++)
			allPolygons.get(polygonDrawOrder[i]).drawPolygon(g);
		// draws a 2D map of game based on polygons drawn
		for (int i = 0; i < polygonDrawOrder.length; i++)
			allPolygons.get(polygonDrawOrder[i]).drawMap(g, guard, timer, otherPlayer.getX(), otherPlayer.getY(), X, Y);

	}

	private java.util.List<Polygon> getAllPolygonsThatNeedToBeDrawn(boolean guard, Player otherPlayer,
			Player currentPlayer, String RoomName, double X, double Y) {
		// All polygons that need to be drawn
		java.util.List<Polygon> allPolygons = new ArrayList<>();

		// re-closes doors previously opened by player as soon as he is not near
		// it.
		room.getDoors().stream().filter(i -> !i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2]) && !i.isDraw())
				.forEach(i -> {
					i.changeState();
					// checks whether player is in room, if not , then it resets
					// his status to not in a room
					i.setLocations(viewFrom[0], viewFrom[1]);

					if (i.passedThrough()) {

						if (currentPlayer.isInRoom()) {
							currentPlayer.inRoom(false);

						} else {
							currentPlayer.inRoom(true);

						}

					}

					;

				});

		// Add all polygons to the list
		allPolygons.addAll(room.getFloorPolygons()); // floor tiles
		room.getWalls().forEach(o -> allPolygons.addAll(o.getPolygons())); // walls

		room.getRoomObjects().forEach(o -> { // room objects
			if (o.isDraw())
				allPolygons.addAll(o.getPolygons());
		});

		room.getDoors().forEach(d -> { // doors
			if (d.isDraw())
				allPolygons.addAll(d.getPolygons());
		});

		double[] otherPos = controller.getOtherPlayersPosition(guard);
		// gets the other player's polygons only if he is in same room as
		// current player
		if (otherPlayer.getLevelName().equals(RoomName)) {
			allPolygons.addAll(PlayerMovement.updateOtherPlayersPosition(otherPos, otherPlayer)); // other
																									// player
		}

		for (GuardBot r : this.controller.getGuardList())
		if (r.getLevel().equals(currentPlayer.getLevelName())) {
			allPolygons.addAll(PlayerMovement.updateGuardBotPosition(r.getName(), controller));
		}
		return allPolygons;
	}

	/**
	 * This sets the order that the polygons are drawn in
	 */
	private void setOrder(java.util.List<Polygon> polys) {
		double[] k = new double[polys.size()];
		polygonDrawOrder = new int[polys.size()];
		// polygonxy = new ArrayList<Location>();

		for (int i = 0; i < polys.size(); i++) {
			k[i] = polys.get(i).averageDistance;
			polygonDrawOrder[i] = i;
		}

		double temp;
		int temp2;
		for (int a = 0; a < k.length - 1; a++)
			for (int b = 0; b < k.length - 1; b++)
				if (k[b] < k[b + 1]) {
					temp = k[b];
					temp2 = polygonDrawOrder[b];
					polygonDrawOrder[b] = polygonDrawOrder[b + 1];
					k[b] = k[b + 1];

					polygonDrawOrder[b + 1] = temp2;
					k[b + 1] = temp;
				}
	}

	public void updateRoom(Room room2) {
		this.room = room2;
	}
}
