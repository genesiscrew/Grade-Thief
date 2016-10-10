
package view;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.GameController;
import controller.Main;
import controller.ScreenUtil;
import model.characters.Player;
import model.floor.EmptyTile;
import model.floor.Location;
import model.floor.Tile;
import model.items.Item;
import model.items.Item.Interaction;
import model.rendering.Polygon;
import model.saving.FastLoad;
import model.saving.FastSaving;

/**
 * The screen itself is a JPanel which runs the game. It has all the input
 * listeners for mouse, wheel and keys.
 */
public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Room room;
	private Room room1 = new Room("level", 0, 0);
	private Room room2 = new Room("level2", 0, 0);
	public static final int PLAYING = 2;
	public static final int GAMEOVER = 3;
	public static final int GAMEWON = 4;
	public int timer;

	// The polygon that the mouse is currently over
	public static Polygon polygonOver = null;

	public static int startX = 120;
	public static int startY = 150;
	public static int startZ = 10;

	Robot r; // Used for keeping mouse in center
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * This is the co-ordinates of where the player is (x, y, z)
	 */
	double[] viewFrom = new double[] { 15, 5, 10 }, viewTo = new double[] { 0, 0, 0 },
			lightDir = new double[] { 1, 1, 1 };

	// The smaller the zoom the more zoomed out you are and visa versa, although
	// altering too far from 1000 will make it look pretty weird
	public static double zoom = 1000;
	public static double minZoom = 500;
	public static double maxZoom = 2500;
	public static double mouseX = 0;
	public static double mouseY = 0;

	// FPS
	double drawFPS = 0;
	double maxFPS = 30;
	double lastRefresh = 0;
	double lastFPSCheck = 0;
	double fpsCheck = 0;

	private ScreenUtil screenUtil = new ScreenUtil();
	private PolygonDrawer polyDrawer;
	double sunPos = 0;

	public static boolean drawOutlines = true;
	boolean[] keys = new boolean[4];

	private GameController controller;
	private boolean guard;
	private model.characters.Player currentPlayer;
	private model.characters.Player otherPlayer;
	private String messageToDisplay = "";
	private String messageToDisplay2;
	private int GAMESTATUS;

	/**
	 * Create a new screen
	 */
	public Screen(GameController controller, boolean guard, String roomName) {
		this.controller = controller;
		this.guard = guard;
		this.currentPlayer = new Player(0, 0, 0, 0, 0, 0, null);
		this.currentPlayer.setRoom(roomName);
		this.GAMESTATUS = this.PLAYING;
		if (guard)
			otherPlayer = new model.characters.Player(20, 20, 0, 5, 3, 12, Color.green);
		else
			otherPlayer = new model.characters.Player(20, 20, 0, 5, 3, 12, Color.blue);

		otherPlayer.setRoom(roomName);

		this.addKeyListener(this);
		setFocusable(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		Cursor cursor = screenUtil.invisibleMouse();
		setCursor(cursor);

		// Load the section of the map
		room = new Room(roomName, startX, startY);
		this.polyDrawer = new PolygonDrawer(room, lightDir, viewFrom, controller);

		if (guard) {
			viewFrom[0] = 100;
			viewFrom[1] = 100;
			viewFrom[2] = 10;
		} else {
			viewFrom[0] = startX;
			viewFrom[1] = startY;
			viewFrom[2] = startZ;
		}
	}

	public Location getPlayerLocation() {
		return new Location((int) (viewFrom[0] / 10), (int) (viewFrom[1] / 10));

	}

	@Override
	public void paintComponent(Graphics g) {
		// Clear screen and draw background color
		g.setColor(new Color(140, 180, 180));
		g.fillRect(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		// resets tile the user is currently located at
		try {
			((EmptyTile) this.room.getTileMap().getTileMap()[(int) (viewFrom[0] / 10)][(int) (viewFrom[1] / 10)])
					.resetEmptyTile();
		} catch (Exception e) {

		}
		PlayerMovement.cameraMovement(viewTo, viewFrom, keys, room);
		controller.updatePosition(guard, viewFrom);
		updateView();
		// adds the player to his new coordinate on tilemap
		try {
			((EmptyTile) this.room.getTileMap().getTileMap()[(int) (viewFrom[0] / 10)][(int) (viewFrom[1] / 10)])
					.addObjectToTile(this.currentPlayer);
		} catch (Exception e) {

		}

		// Calculated all that is general for this camera position
		Calculator.setPredeterminedInfo(this);
		Calculator.controlSunAndLight(lightDir, room.getWidth(), sunPos);
		if (timer > 0) {
			timer--;
		}
		// System.out.println(timer);

		polyDrawer.drawPolygons(g, guard, otherPlayer, currentPlayer, timer, currentPlayer.getLevelName(), viewFrom[0],
				viewFrom[1]);

		// Draw the cross in the center of the screen
		screenUtil.drawMouseAim(g);

		// FPS display
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + (int) drawFPS + "(Benchmark)", 40, 40);

		Location l = getPlayerLocation();
		if (l != null)
			g.drawString("Loc" + l.row() + " , " + l.column(), 40, 60);

		// Message display
		g.setFont(new Font("Arial", Font.BOLD, 20));
		messageToDisplay = "";
		isPlayerNearObject();
		if (!messageToDisplay.equals("")) {
			g.drawString(messageToDisplay, (int) screenSize.getWidth() / 2 - 120,
					(int) screenSize.getHeight() / 2 - 50);
		}
		messageToDisplay2 = "";
		isPlayerDetected();
		if (!messageToDisplay2.equals("")) {
			g.drawString(messageToDisplay, (int) screenSize.getWidth() / 2 - 120,
					(int) screenSize.getHeight() / 2 - 50);
		}
		//check if game is still playing or is over
		this.updateGameStatus();
		if (this.GAMESTATUS == this.GAMEOVER) {
			// game is over so display certain message that game is lost
		}

		// Redraw
		sleepAndRefresh();
	}

	private void updateGameStatus() {
		if (!this.guard) {
			// check whether player coordinate correspond with other player,
			// hence he is caught
			if (viewFrom[0] == this.otherPlayer.getX() && viewFrom[1] == this.otherPlayer.getY()) {
				this.GAMESTATUS = this.GAMEOVER;

			}

			if (this.currentPlayer.getInventory().contains(null)) {
				// player has unlocked davids computer using a key and has a unique item added to his inventory, his modified grade sheet
				// if this item exists in inventory you win the game
				this.GAMESTATUS = this.GAMEWON;

			}

		} else {
			if (viewFrom[0] == this.otherPlayer.getX() && viewFrom[1] == this.otherPlayer.getY()) {
				this.GAMESTATUS = this.GAMEWON;
			}
			if (this.otherPlayer.getInventory().contains(null)) {
				// player has unlocked davids computer using a key and has a unique item added to his inventory, his modified grade sheet
				// if this item exists in inventory you win the game
				this.GAMESTATUS = this.GAMEWON;

			}

		}

	}

	private void isPlayerDetected() {

		if (timer == 200) {
			// player has been detected
			if (guard) {
				messageToDisplay2 = "Intruder has been detected at " + otherPlayer.getLevelName();

			}

		}

	}

	public boolean isGuard() {
		return this.guard;

	}

	/**
	 * This refreshes the display when required. In the meantime it simply
	 * sleeps.
	 */
	private void sleepAndRefresh() {
		long timeSLU = (long) (System.currentTimeMillis() - lastRefresh);

		fpsCheck++;
		if (fpsCheck >= 15) {
			drawFPS = fpsCheck / ((System.currentTimeMillis() - lastFPSCheck) / 1000.0);
			lastFPSCheck = System.currentTimeMillis();
			fpsCheck = 0;
		}

		if (timeSLU < 1000.0 / maxFPS) {
			try {
				Thread.sleep((long) (1000.0 / maxFPS - timeSLU));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		lastRefresh = System.currentTimeMillis();
		repaint();
	}

	/**
	 * Sets the x, y, z that the player is looking at
	 */
	void updateView() {

		double verticalLook = screenUtil.getVerticalLook();
		double horizontalLook = screenUtil.getHorizontalLook();
		double r = Math.sqrt(1 - (verticalLook * verticalLook));

		viewTo[0] = viewFrom[0] + r * Math.cos(horizontalLook);
		viewTo[1] = viewFrom[1] + r * Math.sin(horizontalLook);
		viewTo[2] = viewFrom[2] + verticalLook;

	}

	/**
	 * Centre the mouse in the centre of the screen
	 */
	void centerMouse() {
		try {
			r = new Robot();
			r.mouseMove((int) Main.ScreenSize.getWidth() / 2, (int) Main.ScreenSize.getHeight() / 2);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			keys[0] = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			keys[1] = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			keys[2] = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			keys[3] = true;
		if (e.getKeyCode() == KeyEvent.VK_O)
			drawOutlines = !drawOutlines;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			currentPlayer.jump(viewFrom);
		if (e.getKeyCode() == KeyEvent.VK_R)
			// loadMap();
			if (e.getKeyCode() == KeyEvent.VK_U)
			changeAllDoorState();
		if (e.getKeyCode() == KeyEvent.VK_E)
			playerWantingToInteractWithItem();
		if (e.getKeyCode() == KeyEvent.VK_I)
			showInventory();
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			gameOptionPane();
	}

	/**
	 * This brings up the inventory window which contains all of the items the
	 * player currently has.
	 */
	private void showInventory() {
		String[] inventoryItems = new String[currentPlayer.getInventory().size()];
		int c = 0;
		for (Item i : currentPlayer.getInventory()) {
			inventoryItems[c++] = i.getName();
		}
		if (inventoryItems.length == 0)
			return; // No items to show
		String stringItem = Inventory.showDialog(this, null, "Select an item to interact with", "Inventory",
				inventoryItems, inventoryItems[0], inventoryItems[0]);
		System.out.println(stringItem);
		int itemLocation = 0;
		for (int i = 0; i < inventoryItems.length; i++) {
			if (inventoryItems[i] == stringItem) {
				itemLocation = i;
				break;
			}
		}
		Item selectedItem = currentPlayer.getInventory().get(itemLocation);
		performActionOnItem(selectedItem);
	}

	/**
	 * When the player is near an item and wants to interact with it.
	 */
	private void playerWantingToInteractWithItem() {
		for (Item item : room.getRoomObjects()) {
			if (item.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {
				performActionOnItem(item);
			}
		}
		room.getDoors().stream().filter(i -> i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])).forEach(i -> {
			performActionOnItem(i);
			return;
		});
	}

	/**
	 * This calls showOptionPanel() which gets the interaction chosen and this
	 * executes it.
	 *
	 * @param item
	 */
	public void performActionOnItem(Item item) {
		int n = showOptionPane(item.getInteractionsAvailable());
		Interaction interaction = item.getInteractionsAvailable().get(n);
		// Take
		if (interaction.equals(Interaction.TAKE)) {
			currentPlayer.addToInventory(item);
			room.removeRoomObject(item);
			item.canDraw();
			// Drop
		} else if (interaction.equals(Interaction.DROP)) {
			item.moveItemBy(viewFrom[0] - item.getX(), viewFrom[1] - item.getY(), 0);
			room.addItemToRoom(item);
			item.canDraw();
			currentPlayer.removeFromInventory(item);
		} else {
			item.performAction(interaction);
		}
		return; // We only want to interact with one item
	}

	/**
	 * Shows a panel with a list of interactions a player can perform on an item
	 *
	 * @param optionsList
	 * @return
	 */
	private int showOptionPane(List<Item.Interaction> optionsList) {
		String[] options = new String[optionsList.size()];
		for (int i = 0; i < optionsList.size(); i++) {
			options[i] = optionsList.get(i).toString();
		}
		int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return n;
	}

	/*
	 * private void showInventory() { String[] inventoryItems = new
	 * String[currentPlayer.getInventory().size()]; int c = 0; for (Item i :
	 * currentPlayer.getInventory()) { inventoryItems[c] = i.toString(); c++; }
	 *
	 * int n = JOptionPane.showOptionDialog(this, "What would you like to do?",
	 * "Select option", JOptionPane.YES_NO_CANCEL_OPTION,
	 * JOptionPane.QUESTION_MESSAGE, null, inventoryItems, inventoryItems[0]);
	 * Item selectedItem = currentPlayer.getInventory().get(n); // at this
	 * point; shou;d get the position directly infront of player // should make
	 * sure that it will not obstruct another item
	 * selectedItem.moveItemBy(viewFrom[0] - selectedItem.getX(), viewFrom[1] -
	 * selectedItem.getY(), 0);
	 *
	 * room.addItemToRoom(selectedItem); selectedItem.canDraw();
	 *
	 * currentPlayer.removeFromInventory(selectedItem); }
	 *
	 *
	 *
	 * private void playerWantingToInteractWithItem() {
	 *
	 * room.getRoomObjects().stream().filter(i -> i.pointNearObject(viewFrom[0],
	 * viewFrom[1], viewFrom[2])) .forEach(i -> {
	 *
	 * int n = showOptionPane(i.getInteractionsAvailable());
	 * i.performAction(i.getInteractionsAvailable().get(n));
	 * System.out.println(i.getInteractionsAvailable().get(n).toString()); if
	 * (i.getInteractionsAvailable().get(n).equals(Interaction.TAKE)) {
	 * currentPlayer.addToInventory(i);
	 * System.out.println("add to inventory here");
	 * currentPlayer.getInventory().forEach(System.out::println); } });
	 *
	 * room.getDoors().stream().filter(i -> i.pointNearObject(viewFrom[0],
	 * viewFrom[1], viewFrom[2])).forEach(i -> { int n =
	 * showOptionPane(i.getInteractionsAvailable());
	 * i.performAction(i.getInteractionsAvailable().get(n));
	 *
	 *
	 * if (n == 0) // doorOpened { // player has entered a room so we set a
	 * boolean value inRoom to true
	 *
	 *
	 * i.setLocations(viewFrom[0], viewFrom[1]); Location l =
	 * getPlayerLocation();
	 *
	 * if (l.row() == 80 && l.column() == 5) // hard coded to switch // floor
	 * loadMap("level2", 1); else if (l.row() == 80 && l.column() == 29)
	 * loadMap("level2", 2); else if (l.row() == 1 && l.column() == 5)
	 * loadMap("level", 1); else if (l.row() == 1 && l.column() == 21)
	 * loadMap("level", 2);
	 *
	 * } }); }
	 */

	public void loadMap(String map, int id) {

		maxFPS = drawFPS;
		room = new Room(map, 20, 20);

		if (map.equals("level2") && id == 1) {
			this.currentPlayer.setRoom("level2");
			room.setPlayerStart(2 * 10, 6 * 10);
		} else if (map.equals("level2") && id == 2) {
			this.currentPlayer.setRoom("level2");
			room.setPlayerStart(2 * 10, 22 * 10);
		} else if (map.equals("level") && id == 1) {
			this.currentPlayer.setRoom("level");
			room.setPlayerStart(80 * 10, 5 * 10);
		} else if (map.equals("level") && id == 2) {
			this.currentPlayer.setRoom("level");
			room.setPlayerStart(80 * 10, 29 * 10);
		}
		viewFrom[0] = room.getPlayerStart().row();
		viewFrom[1] = room.getPlayerStart().column();
		this.controller.setupGuardbots(this);
		polyDrawer.updateRoom(room);
	}

	private void changeAllDoorState() {
		room.getDoors().forEach(d -> d.changeState());
	}

	private void isPlayerNearObject() {
		List<Item> removeItems = new ArrayList<Item>();
		for (Item i : room.getRoomObjects()) {
			if (!i.isDraw()) {// nothing to display if item not rendered
				removeItems.add(i);
				continue;
			}

			if (i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {
				// messageToDisplay = "Press e To Interact With The " +
				// i.getClass().getSimpleName();

				messageToDisplay = "Door" + i.getItemID();
			}
		}

		for (Item i2 : removeItems) {

			room.removeRoomObject(i2);
			((EmptyTile) room.getTileMap().getTileMap()[(int) i2.getX() / 10][(int) i2.getY() / 10]).resetEmptyTile();

		}

		for (Item i : room.getDoors()) {
			if (i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {
				// messageToDisplay = "Press e To Open The Door";
				// Location l = getPlayerLocation();
				// if (this.room.getTileMap().getTileMap()[l.row()][l.column()];
				messageToDisplay = "Press e " + i.itemID;
			}
		}
	}

	/*
	 * private int showOptionPane(List<Item.Interaction> optionsList) { String[]
	 * options = new String[optionsList.size()]; for (int i = 0; i <
	 * optionsList.size(); i++) { options[i] = optionsList.get(i).toString(); }
	 *
	 * int n = JOptionPane.showOptionDialog(this, "What would you like to do?",
	 * "Select option", JOptionPane.YES_NO_CANCEL_OPTION,
	 * JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	 *
	 * return n; }
	 */

	/**
	 * Moving Ability for the camera(Player) this function will set the keys
	 * array which use to update the camera and game vision for the player.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			keys[0] = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			keys[1] = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			keys[2] = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			keys[3] = false;
	}

	/**
	 * Creating an Modal and User Interface buttons for user to have options to
	 * select when they want to SAVE, LOAD, EXIT and READ more about instruction
	 * and game,
	 */
	public void gameOptionPane() {
		// Custom button text
		Object[] options = { "Resume", "Chat", "Save", "Load", "Help", "About Us", "Exit" };
		int n = JOptionPane.showOptionDialog(this, "Please select the prefer optin?", "Grade Thief",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		if (n == 0) {
			// Nothing for skipping the Modal and continue game.
		} else if (n == 1) {
			chat();
		} else if (n == 2) {
			saveGame();
		} else if (n == 3) {
			loadGame();
		} else if (n == 4) {
			Help();
		} else if (n == 5) {
			AboutUs();
		} else if (n == 6) {
			System.exit(0);
		}
	}

	/**
	 * Loading game based on the selected file that user select This method use
	 * SelectFile() method in order to get the name of the selected file in
	 * order to have low coupled code.
	 */
	private void loadGame() {
		String fileName = SelecFile();

		if (fileName.equals("Cancelled"))
			return;

		FastLoad loadGame = new FastLoad(fileName, this);
		loadGame.load();
	}

	/**
	 * Saving game based into the selected file that user select This method use
	 * SelectFile() method in order to get the name of the selected file that
	 * user wants to save the game in.
	 */
	private void saveGame() {
		FastSaving saveGame;
		try {
			String fileName = SelecFile();
			if (fileName.equals("Cancelled"))
				return;
			saveGame = new FastSaving(fileName);
			saveGame.save();
		} catch (FileNotFoundException e) {

			// custom title, error icon for uncompatable chema of lodaed file.
			JOptionPane.showMessageDialog(this,
					"Please select saved version of the Grade Thief Game, This file is not belong to Grade-Thief",
					"Grade-Thief Loading", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			// custom title, error icon for uncompatable chema of lodaed file.
			JOptionPane.showMessageDialog(this,
					"Please select saved version of the Grade Thief Game, This file is not belong to Grade-Thief",
					"Grade-Thief Loading", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * This method use JFileChooser for better User Experience and User
	 * interface in order to select the proper file for loading and saving the
	 * file
	 *
	 * @return String: FileName
	 */
	private String SelecFile() {
		// Creating JFileChooser
		JFileChooser fileChooser = new JFileChooser(".");
		int status = fileChooser.showOpenDialog(null);

		if (status == JFileChooser.APPROVE_OPTION) {
			// getting the name of the file.
			File selectedFile = fileChooser.getSelectedFile();
			// System.out.println(selectedFile);
			return selectedFile.getParent() + "/" + selectedFile.getName();
		} else if (status == JFileChooser.CANCEL_OPTION) {
			// If the didn't select any file
			// default title and icon
			JOptionPane.showMessageDialog(this, "Choosing file operation cancelled");
		}
		return "Cancelled";
	}

	/**
	 * Setting an Help and instruction for the ease of use for users and players
	 * on ESCAPE button.
	 */
	private void Help() {
		// Help: Instruction for playing game and rules
		String rules = "Rules for Game is as follow : Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, ";
		JTextArea textArea = new JTextArea(rules, 6, 20);
		textArea.setFont(new Font("Serif", Font.ITALIC, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setSize(new Dimension(400, 500));
		// OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules,
		// textArea);
	}

	/**
	 * About Us Button for getting more information about the Creators and the
	 * students whom created this game
	 */
	private void AboutUs() {
		// Help: Instruction for playing game and rules
		String rules = "Grade Thief is a Software Engineering Group Project which leads by Victoria University of Wellington. Team members are: Adam Wareing, Hamid Osman, Stefan Vrecic, Mostafa Shenavaei, Mansour Javaher";
		JTextArea textArea = new JTextArea(rules, 6, 20);
		textArea.setFont(new Font("Serif", Font.BOLD, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setSize(new Dimension(400, 500));
		// OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules,
		// textArea);
	}

	/**
	 * For adding chat ability to Game.
	 */
	private void chat() {
		// TODO: Adding chat ability to the game
		System.err.println("CHAT CODES HERE");
	}

	public void restart() {
		StringBuilder cmd = new StringBuilder();
		cmd.append(System.getProperty("Main.home") + File.separator + "bin" + File.separator + "java ");
		for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			cmd.append(jvmArg + " ");
		}
		cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
		cmd.append(Window.class.getName()).append(" ");
		try {
			Runtime.getRuntime().exec(cmd.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		screenUtil.mouseMovement(arg0.getX(), arg0.getY());
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		centerMouse();
		updateView();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		screenUtil.mouseMovement(arg0.getX(), arg0.getY());
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		centerMouse();
		updateView();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1)
			if (polygonOver != null)
				polygonOver.seeThrough = false;

		if (arg0.getButton() == MouseEvent.BUTTON3)
			if (polygonOver != null)
				polygonOver.seeThrough = false;
	}

	public Tile[][] getRoomMap() {
		return this.room.getTileMap().getTileMap();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getUnitsToScroll() > 0) {
			if (zoom > minZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		} else {
			if (zoom < maxZoom)
				zoom -= 25 * arg0.getUnitsToScroll();
		}
	}

	public static int getStartX() {
		return startX;
	}

	public static void setStartX(int startX) {
		Screen.startX = startX;
	}

	public static int getStartY() {
		return startY;
	}

	public static void setStartY(int startY) {
		Screen.startY = startY;
	}

	public static int getStartZ() {
		return startZ;
	}

	public static void setStartZ(int startZ) {
		Screen.startZ = startZ;
	}

	public void setStartX(double startX) {
		this.startX = (int) startX;
	}

	public void setStartY(double startY) {
		this.startY = (int) startY;
	}

	public void setViewFrom(double[] viewFrom) {
		this.viewFrom = viewFrom;
	}

	public double[] getPlayerView() {
		return this.viewFrom;
	}

	public double[] getViewFrom() {
		return viewFrom;
	}

	public void setX(double x) {
		viewFrom[0] = x;
	}

	public void setY(double x) {
		viewFrom[1] = x;
	}

	public model.characters.Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(model.characters.Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Room getRoom1() {
		return room1;
	}

	public void setRoom1(Room room1) {
		this.room1 = room1;
	}

	public Room getRoom2() {
		return room2;
	}

	public void setRoom2(Room room2) {
		this.room2 = room2;
	}

	public model.characters.Player getOtherPlayer() {
		// TODO Auto-generated method stub
		return this.otherPlayer;
	}

}