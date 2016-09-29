package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import characters.Player;
import game.floor.EmptyTile;
import game.floor.Floor;
import game.floor.Location;
import game.floor.Room;
import game.floor.RoomTile;
import game.floor.Tile;
import game.floor.TileMap;
import items.Direction;
import items.Door;
import model.Game;
import saving.Employee;
import saving.Employees;

public class PlayerInteractWithItem {

	public static void main(String[] args) throws IOException {

		Game game = new Game();
		// create room
		Room room = makeRoom();
		TileMap tileMap = room.getRoomTileMap();
		// add items to room
		tileMap.populateRoom(room, tileMap.getItems(), null);
		//create player
		Player p = new Player(0000, "H", game, 0);
		game.addPlayer(p);
		p.setCharacterLocation(5,2);
		Location pL = p.getCharacterLocation();
		// set user direction facing item
		p.setDirection(Direction.Dir.EAST);
		// add player to room
		EmptyTile tile =  (EmptyTile) tileMap.getTileMap()[pL.row()][pL.column()];
		tile.addObjectToTile(p);
		// draw board
        game.drawRoom(tileMap);
        Thread drawThread = game.drawRoomThread(700, tileMap);
        game.display.setVisible(true);
        drawThread.start();


		/*
		 * try { marshalingExample(p);
		 * System.out.println("************************************************"
		 * ); unMarshalingExample();
		 *
		 * } catch (JAXBException e) { e.printStackTrace(); }
		 */

	}



	private static void marshalingExample(Player pl) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(pl, System.out);
		jaxbMarshaller.marshal(pl, new File("//am//state-opera//home1//javahemans//workspace//grade-thief//src//saving//sample1.xml"));
	}


	private static void unMarshalingExample() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Employees emps = (Employees) jaxbUnmarshaller.unmarshal( new File("//am//state-opera//home1//javahemans//workspace//grade-thief//src//saving//sample1.xml") );

		for(Employee emp : emps.getEmployees())
		{
			System.out.println(emp.getId());
			System.out.println(emp.getFirstName());
		}
	}


	private static Room makeRoom() throws IOException {
		// TODO Auto-generated method stub
		Floor floor;
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001",0);
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";

		Room room_co237 = new Room(null, null);

		room_co237.setTileMap(co237);

		r.setTileMap(co237);
		return r;
	}




}
