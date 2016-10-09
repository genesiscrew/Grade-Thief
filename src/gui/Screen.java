
package gui;

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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import game.floor.EmptyTile;
import game.floor.Location;
import game.floor.Tile;
import game.floor.TileMap;
import items.Item;
import items.Item.Interaction;
import items.Player;
import saving.FastLoad;
import saving.FastSaving;

/**
 * The screen itself is a JPanel which runs the game. It has all the input
 * listeners for mouse, wheel and keys.
 */
public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Room room;
	private Room room1 = new Room("level", 0, 0);
	private Room room2 = new Room("co238", 0, 0);
	public int timer;

	// The polygon that the mouse is currently over
	static Polygon polygonOver = null;

	public static int startX = 120;
	public static int startY = 150;
	public static int startZ = 10;

	Robot r; // Used for keeping mouse in center
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * This is the co-ordinates of where the player is (x, y, z)
	 */
	double[] viewFrom = new double[] { 15, 5, 10 }, viewTo = new double[] { 0, 0, 0 },
			lightDir = new double[] { 1, 1, 1 };

	// The smaller the zoom the more zoomed out you are and visa versa, although
	// altering too far from 1000 will make it look pretty weird
	static double zoom = 1000;
	static double minZoom = 500;
	static double maxZoom = 2500;
	static double mouseX = 0;
	static double mouseY = 0;

	// FPS
	double drawFPS = 0;
	double maxFPS = 30;
	double lastRefresh = 0;
	double lastFPSCheck = 0;
	double fpsCheck = 0;

	private ScreenUtil screenUtil = new ScreenUtil();
	private PolygonDrawer polyDrawer;
	double sunPos = 0;

	static boolean drawOutlines = true;
	boolean[] keys = new boolean[4];

	private GameController controller;
	private boolean guard;
	private items.Player currentPlayer;
	private items.Player otherPlayer;
	private String messageToDisplay = "";
	private String messageToDisplay2;







    /**
     * Create a new screen
     */
    public Screen(GameController controller, boolean guard, String roomName) {
        this.controller = controller;
        this.guard = guard;
        this.currentPlayer = new Player(0, 0, 0, 0, 0, 0, null);
        this.currentPlayer.setRoom(roomName);
        if (guard)
            otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.green);
        else
            otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.blue);

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
        return new Location((int) (viewFrom[0]/10), (int) (viewFrom[1]/10));

    }

    @Override
    public void paintComponent(Graphics g) {
        // Clear screen and draw background color
        g.setColor(new Color(140, 180, 180));
        g.fillRect(0, 0, (int) GameController.ScreenSize.getWidth(), (int) GameController.ScreenSize.getHeight());
        // resets tile the user is currently located at
        try {
        ((EmptyTile) this.room.getTileMap().getTileMap()[(int) (viewFrom[0]/10)][(int) (viewFrom[1]/10)])
        .resetEmptyTile();
        }
        catch (Exception e) {


        }
        PlayerMovement.cameraMovement(viewTo, viewFrom, keys, room);
        controller.updatePosition(guard, viewFrom);
        updateView();
        // adds the player to his new coordinate on tilemap
        try {
        ((EmptyTile) this.room.getTileMap().getTileMap()[(int) (viewFrom[0]/10)][(int) (viewFrom[1]/10)])
        .addObjectToTile(this.currentPlayer);
        }
        catch (Exception e) {

        }

        // Calculated all that is general for this camera position
        Calculator.setPredeterminedInfo(this);
        Calculator.controlSunAndLight(lightDir, room.getWidth(), sunPos);
        if (timer > 0) {
        	timer--;
        }
        //System.out.println(timer);

        polyDrawer.drawPolygons(g, guard, otherPlayer, timer, currentPlayer.getRoomName(), viewFrom[0], viewFrom[1]);



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

        // Redraw
        sleepAndRefresh();
    }

    private void isPlayerDetected() {

		if (timer == 199) {
			// player has been detected
			if (guard) {
				messageToDisplay2 = "Intruder has been detected at " + otherPlayer.getRoomName();

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

	private void showInventory() {
		String[] inventoryItems = new String[currentPlayer.getInventory().size()];
		int c = 0;
		for (Item i : currentPlayer.getInventory()) {
			inventoryItems[c] = i.toString();
			c++;
		}

		int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, inventoryItems,
				inventoryItems[0]);
		Item selectedItem = currentPlayer.getInventory().get(n);
		// at this point; shou;d get the position directly infront of player
		// should make sure that it will not obstruct another item
		selectedItem.moveItemBy(viewFrom[0] - selectedItem.getX(), viewFrom[1] - selectedItem.getY(), 0);

		room.addItemToRoom(selectedItem);
		selectedItem.canDraw();

		currentPlayer.removeFromInventory(selectedItem);
	}

	private void playerWantingToInteractWithItem() {

		room.getRoomObjects().stream().filter(i -> i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2]))
				.forEach(i -> {

					int n = showOptionPane(i.getInteractionsAvaliable());
					i.performAction(i.getInteractionsAvaliable().get(n));
					System.out.println(i.getInteractionsAvaliable().get(n).toString());
					if (i.getInteractionsAvaliable().get(n).equals(Interaction.TAKE)) {
						currentPlayer.addToInventory(i);
						System.out.println("add to inventory here");
						currentPlayer.getInventory().forEach(System.out::println);
					}
				});

		room.getDoors().stream().filter(i -> i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])).forEach(i -> {
			int n = showOptionPane(i.getInteractionsAvaliable());
			i.performAction(i.getInteractionsAvaliable().get(n));

			if (n == 0) // doorOpened
			{
				Location l = getPlayerLocation();

				if (l.row() == 80 && l.column() == 5) // hard coded to switch
														// floor
					loadMap("level2", 1);
				else if (l.row() == 80 && l.column() == 29)
					loadMap("level2", 2);
				else if (l.row() == 1 && l.column() == 5)
					loadMap("level", 1);
				else if (l.row() == 1 && l.column() == 21)
					loadMap("level", 2);

			}
		});
	}

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
		this.controller.setupGuardbots(map, this);
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

	private int showOptionPane(List<Item.Interaction> optionsList) {
		String[] options = new String[optionsList.size()];
		for (int i = 0; i < optionsList.size(); i++) {
			options[i] = optionsList.get(i).toString();
		}

		int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		return n;
	}

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

	private void loadGame() {
		// TODO: Needs To load the Game
		FastLoad loadGame = new FastLoad(SelecFile(), this);
		loadGame.load();
	}

	private void saveGame() {
		// TODO: Needs to Save the game
		FastSaving saveGame;
		try {
			saveGame = new FastSaving(SelecFile());
			saveGame.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private String SelecFile() {
		JFileChooser fileChooser = new JFileChooser(".");
		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// System.out.println(selectedFile);
			return selectedFile.getParent() + "/" + selectedFile.getName();
		} else if (status == JFileChooser.CANCEL_OPTION) {
			System.out.println("canceled");
		}
		return "Cancelled";
	}

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
		OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules, textArea);
	}

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
		OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules, textArea);
	}

	private void chat() {
		// Help: Instruction for playing game and rules
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

	public items.Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(items.Player currentPlayer) {
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

	public items.Player getOtherPlayer() {
		// TODO Auto-generated method stub
		return this.otherPlayer;
	}

}