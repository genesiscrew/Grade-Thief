package gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import f_server.ChatClient;
import f_server.ClientChatTest;
import f_server.Server;
import f_server.ServerChat;
import f_server.ServerChatTest;
import saving.LoadGame;
import saving.SaveGame;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Room room;
	private Room room1 = new Room("level", 0, 0);
	private Room room2 = new Room("co238", 0, 0);

	// The polygon that the mouse is currently over
	static Polygon polygonOver = null;


	// Player (First Player)
	public static int startX = 50;
	public static int startY = 50;
	public static int startZ = 10;

	// Used for keeping mouse in center
	Robot r;
	private boolean jumping = false;

	/**
	 * This is the co-ordinates of where the player is (x, y, z)
	 */
	double[] viewFrom = new double[] { 15, 5, 10 }, ViewTo = new double[] { 0, 0, 0 },
			LightDir = new double[] { 1, 1, 1 };

	// The smaller the zoom the more zoomed out you are and visa versa, although
	// altering too far from 1000 will make it look pretty weird
	static double zoom = 1000;
	static double minZoom = 500;
	static double maxZoom = 2500;
	static double mouseX = 0;
	static double mouseY = 0;
	static double movementSpeed = 5;

	// FPS is a bit primitive, you can set the maxFPS as high as u want
	double drawFPS = 0, maxFPS = 1000, LastRefresh = 0, lastFPSCheck = 0, fpsCheck = 0;

	double VertLook = -0.9; // Goes from 0.999 to -0.999, minus being looking
							// down and + looking up
	double HorLook = 0; // takes any number and goes round in radians
	double aimSight = 4; // Changes the size of the center-cross.

	// The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate
	// in those directions
	double HorRotSpeed = 900;
	double VertRotSpeed = 2200;
	double SunPos = 0;

	/**
	 * Will hold the order that the polygons in the ArrayList DPolygon should be
	 * drawn meaning DPolygon.get(polygonDrawOrder[0]) gets drawn first
	 */
	int[] polygonDrawOrder;

	static boolean drawOutlines = true;
	boolean[] Keys = new boolean[4];

	private GameController controller;
	private boolean guard;
	private items.Player otherPlayer;

	/**
	 * Create a new screen
	 */
	public Screen(GameController controller, boolean guard) {
		this.controller = controller;
		this.guard = guard;
		if (guard)
			otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.green);
		else
			otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.blue);
		this.addKeyListener(this);
		setFocusable(true);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		invisibleMouse();

		// Load the section of the map
		room = new Room("level", startX, startY);

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

	@Override
	public void paintComponent(Graphics g) {
		// Clear screen and draw background color
		g.setColor(new Color(140, 180, 180));
		g.fillRect(0, 0, (int) GameController.ScreenSize.getWidth(), (int) GameController.ScreenSize.getHeight());

		cameraMovement();

		// Calculated all that is general for this camera position
		Calculator.setPredeterminedInfo(this);
		controlSunAndLight();

		// All polygons that need to be drawn
		List<Polygon> allPolygons = new ArrayList<>();

		// Add all polygons to the list
		allPolygons.addAll(room.getFloorPolygons()); // floor tiles
		room.getWalls().forEach(o -> allPolygons.addAll(o.getPolygons())); // walls
		room.getRoomObjects().forEach(o -> allPolygons.addAll(o.getPolygons())); // room
																					// objects
		room.getDoors().forEach(d -> { // doors
			if (d.isDraw())
				allPolygons.addAll(d.getPolygons());
		});
		allPolygons.addAll(updateOtherPlayersPosition()); // other player

		// Updates each polygon for this camera position
		for (int i = 0; i < allPolygons.size(); i++)
			allPolygons.get(i).updatePolygon(this);

		// Set drawing order so closest polygons gets drawn last
		setOrder(allPolygons);

		// Set the polygon that the mouse is currently over
		// setPolygonOver();

		// Draw polygons in the Order that is set by the 'setOrder' function
		for (int i = 0; i < polygonDrawOrder.length; i++)
			allPolygons.get(polygonDrawOrder[i]).drawPolygon(g);

		// Draw the cross in the center of the screen
		drawMouseAim(g);

		// FPS display
		g.drawString("FPS: " + (int) drawFPS + " (Benchmark)", 40, 40);
		sleepAndRefresh();
	}

	/**
	 * @return
	 */
	private List<Polygon> updateOtherPlayersPosition() {
		// Lets start by getting there position from the controller and see how
		// much they have moved
		double[] otherPos = controller.getOtherPlayersPosition(guard);
		double dx = otherPos[0] - otherPlayer.getX();
		double dy = otherPos[1] - otherPlayer.getY();
		double dz = otherPos[0] - otherPlayer.getZ();
		dz = 0;

		otherPlayer.updatePosition(dx, dy, dz);
		return otherPlayer.getPolygons();
	}

	/**
	 * This sets the order that the polygons are drawn in
	 */
	private void setOrder(List<Polygon> polys) {
		double[] k = new double[polys.size()];
		polygonDrawOrder = new int[polys.size()];

		for (int i = 0; i < polys.size(); i++) {
			k[i] = polys.get(i).averageDistance;
			polygonDrawOrder[i] = i;
		}

		double temp;
		int tempr;
		for (int a = 0; a < k.length - 1; a++)
			for (int b = 0; b < k.length - 1; b++)
				if (k[b] < k[b + 1]) {
					temp = k[b];
					tempr = polygonDrawOrder[b];
					polygonDrawOrder[b] = polygonDrawOrder[b + 1];
					k[b] = k[b + 1];

					polygonDrawOrder[b + 1] = tempr;
					k[b + 1] = temp;
				}
	}

	/**
	 * This hides the mouse cursor so we can use the cross hairs instead
	 */
	private void invisibleMouse() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
		Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");
		setCursor(invisibleCursor);
	}

	/**
	 * This aims the mouse on the graphics
	 *
	 * @param g
	 */
	private void drawMouseAim(Graphics g) {
		g.setColor(Color.black);
		g.drawLine((int) (Main.ScreenSize.getWidth() / 2 - aimSight), (int) (Main.ScreenSize.getHeight() / 2),
				(int) (Main.ScreenSize.getWidth() / 2 + aimSight), (int) (Main.ScreenSize.getHeight() / 2));
		g.drawLine((int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 - aimSight),
				(int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 + aimSight));
	}

	/**
	 * This refreshes the display when required. In th meantime it simply
	 * sleeps.
	 */
	private void sleepAndRefresh() {
		long timeSLU = (long) (System.currentTimeMillis() - LastRefresh);

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

		LastRefresh = System.currentTimeMillis();
		repaint();
	}

	/**
	 * Sets the the values for the light direction
	 */
	void controlSunAndLight() {
		// SunPos += 0.005;
		double mapSize = room.getFloor().getMapWidth() * room.getFloor().getMapWidth();
		LightDir[0] = mapSize / 2 - (mapSize / 2 + Math.cos(SunPos) * mapSize * 10);
		LightDir[1] = mapSize / 2 - (mapSize / 2 + Math.sin(SunPos) * mapSize * 10);
		LightDir[2] = -200;
	}

	/**
	 * Called on every refresh, this updates the direction the camera is facing
	 */
	void cameraMovement() {
		Vector ViewVector = new Vector(ViewTo[0] - viewFrom[0], ViewTo[1] - viewFrom[1], ViewTo[2] - viewFrom[2]);
		double xMove = 0, yMove = 0, zMove = 0;
		Vector VerticalVector = new Vector(0, 0, 1);
		Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);

		if (Keys[0]) {
			xMove += ViewVector.x;
			yMove += ViewVector.y;
		}

		if (Keys[2]) {
			xMove -= ViewVector.x;
			yMove -= ViewVector.y;
		}

		if (Keys[1]) {
			xMove += SideViewVector.x;
			yMove += SideViewVector.y;
		}

		if (Keys[3]) {
			xMove -= SideViewVector.x;
			yMove -= SideViewVector.y;
		}

		Vector MoveVector = new Vector(xMove, yMove, zMove);
		moveTo(viewFrom[0] + MoveVector.x * movementSpeed, viewFrom[1] + MoveVector.y * movementSpeed,
				viewFrom[2] + MoveVector.z * movementSpeed);
	}

	/**
	 * Move the player to x, y, z
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	void moveTo(double x, double y, double z) {
		// Check that the player isn't out of the maps floorPolygons
		if (room.positionOutOfBounds(x, y, z, startX, startY))
			return;

		// Check that the player isn't moving into any roomObjects
		if (room.movingIntoAnObject(x, y, z))
			return;

		viewFrom[0] = x;
		viewFrom[1] = y;
		viewFrom[2] = z;
		// System.out.printf("x: %f y: %f z: %f \n", x, y, z);
		controller.updatePosition(guard, viewFrom);
		updateView();
	}

	/**
	 * Highlights the polygon that the cursor is on
	 */
	void setPolygonOver() {
		polygonOver = null;
		for (int i = polygonDrawOrder.length - 1; i >= 0; i--)
			if (room.getPolygons().get(polygonDrawOrder[i]).mouseOver()
					&& room.getPolygons().get(polygonDrawOrder[i]).draw
					&& room.getPolygons().get(polygonDrawOrder[i]).visible) {
				polygonOver = room.getPolygons().get(polygonDrawOrder[i]);
				break;
			}
	}

	/**
	 * Called when the mouse is moved, calculates the amount it was moved and
	 * sets the vert and horizontal looking angles It also updates the view
	 *
	 * @param NewMouseX
	 * @param NewMouseY
	 */
	void mouseMovement(double NewMouseX, double NewMouseY) {
		double difX = (NewMouseX - Main.ScreenSize.getWidth() / 2);
		double difY = (NewMouseY - Main.ScreenSize.getHeight() / 2);
		difY *= 6 - Math.abs(VertLook) * 5;
		VertLook -= difY / VertRotSpeed;
		HorLook += difX / HorRotSpeed;

		if (VertLook > 0.999)
			VertLook = 0.999;

		if (VertLook < -0.999)
			VertLook = -0.999;

		updateView();
	}

	/**
	 * Sets the x, y, z that the player is looking at
	 */
	void updateView() {
		double r = Math.sqrt(1 - (VertLook * VertLook));
		ViewTo[0] = viewFrom[0] + r * Math.cos(HorLook);
		ViewTo[1] = viewFrom[1] + r * Math.sin(HorLook);
		ViewTo[2] = viewFrom[2] + VertLook;
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
			Keys[0] = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = true;
		if (e.getKeyCode() == KeyEvent.VK_O)
			drawOutlines = !drawOutlines;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			jump();
		if (e.getKeyCode() == KeyEvent.VK_R)
			loadMap();
		if (e.getKeyCode() == KeyEvent.VK_U)
			changeAllDoorState();
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			gameOptionPane();

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
			loadGame(0);
		} else if (n == 4) {
			Help();
		} else if (n == 5) {
			AboutUs();
		} else if (n == 6) {
			System.exit(0);
		}

	}

	private void loadGame(int i) {
		// TODO: Needs To load the Game
		LoadGame loadGame = new LoadGame(SelecFile());
		loadGame.load();
	}

	private void saveGame() {
		// TODO: Needs to Save the game
		SaveGame saveGame = new SaveGame(SelecFile());
		saveGame.save();
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
		ServerChat sc = new ServerChat();
		sc.startRunning();
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

	public void loadMap() {
		if (room == room1) {
			try {
				System.out.println("room1");
				Thread.sleep(200);
			} catch (Exception e) {
			}
			room = room2;
		} else {
			System.out.println("room2");
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}
			room = room1;
		}
	}

	private void changeAllDoorState() {
		room.getDoors().forEach(d -> d.changeState());
	}

	/**
	 * This makes the player jump in the game. It uses a seperate thread to
	 * simulate the jumping so we can continue updating the display throughout
	 * the jump.
	 */
	public void jump() {
		if (jumping)
			return;
		jumping = true;
		Thread jumpingThread = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					viewFrom[2] += 2;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < 5; i++) {
					viewFrom[2] -= 2;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				jumping = false;
			}
		};
		jumpingThread.start();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMovement(arg0.getX(), arg0.getY());
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		centerMouse();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseMovement(arg0.getX(), arg0.getY());
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		centerMouse();
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


	/*========================================================================*
	 *                                                                        *
	 * 						SETERS AND GETTERS								  *
	 *                                                                        *
	 *========================================================================*/

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

	public static Polygon getPolygonOver() {
		return polygonOver;
	}

	public static void setPolygonOver(Polygon polygonOver) {
		Screen.polygonOver = polygonOver;
	}

	public Robot getR() {
		return r;
	}

	public void setR(Robot r) {
		this.r = r;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public double[] getViewFrom() {
		return viewFrom;
	}

	public void setViewFrom(double[] viewFrom) {
		this.viewFrom = viewFrom;
	}

	public double[] getViewTo() {
		return ViewTo;
	}

	public void setViewTo(double[] viewTo) {
		ViewTo = viewTo;
	}

	public double[] getLightDir() {
		return LightDir;
	}

	public void setLightDir(double[] lightDir) {
		LightDir = lightDir;
	}

	public static double getZoom() {
		return zoom;
	}

	public static void setZoom(double zoom) {
		Screen.zoom = zoom;
	}

	public static double getMinZoom() {
		return minZoom;
	}

	public static void setMinZoom(double minZoom) {
		Screen.minZoom = minZoom;
	}

	public static double getMaxZoom() {
		return maxZoom;
	}

	public static void setMaxZoom(double maxZoom) {
		Screen.maxZoom = maxZoom;
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static void setMouseX(double mouseX) {
		Screen.mouseX = mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}

	public static void setMouseY(double mouseY) {
		Screen.mouseY = mouseY;
	}

	public static double getMovementSpeed() {
		return movementSpeed;
	}

	public static void setMovementSpeed(double movementSpeed) {
		Screen.movementSpeed = movementSpeed;
	}

	public double getDrawFPS() {
		return drawFPS;
	}

	public void setDrawFPS(double drawFPS) {
		this.drawFPS = drawFPS;
	}

	public double getMaxFPS() {
		return maxFPS;
	}

	public void setMaxFPS(double maxFPS) {
		this.maxFPS = maxFPS;
	}

	public double getLastRefresh() {
		return LastRefresh;
	}

	public void setLastRefresh(double lastRefresh) {
		LastRefresh = lastRefresh;
	}

	public double getLastFPSCheck() {
		return lastFPSCheck;
	}

	public void setLastFPSCheck(double lastFPSCheck) {
		this.lastFPSCheck = lastFPSCheck;
	}

	public double getFpsCheck() {
		return fpsCheck;
	}

	public void setFpsCheck(double fpsCheck) {
		this.fpsCheck = fpsCheck;
	}

	public double getVertLook() {
		return VertLook;
	}

	public void setVertLook(double vertLook) {
		VertLook = vertLook;
	}

	public double getHorLook() {
		return HorLook;
	}

	public void setHorLook(double horLook) {
		HorLook = horLook;
	}

	public double getAimSight() {
		return aimSight;
	}

	public void setAimSight(double aimSight) {
		this.aimSight = aimSight;
	}

	public double getHorRotSpeed() {
		return HorRotSpeed;
	}

	public void setHorRotSpeed(double horRotSpeed) {
		HorRotSpeed = horRotSpeed;
	}

	public double getVertRotSpeed() {
		return VertRotSpeed;
	}

	public void setVertRotSpeed(double vertRotSpeed) {
		VertRotSpeed = vertRotSpeed;
	}

	public double getSunPos() {
		return SunPos;
	}

	public void setSunPos(double sunPos) {
		SunPos = sunPos;
	}

	public int[] getPolygonDrawOrder() {
		return polygonDrawOrder;
	}

	public void setPolygonDrawOrder(int[] polygonDrawOrder) {
		this.polygonDrawOrder = polygonDrawOrder;
	}

	public static boolean isDrawOutlines() {
		return drawOutlines;
	}

	public static void setDrawOutlines(boolean drawOutlines) {
		Screen.drawOutlines = drawOutlines;
	}

	public boolean[] getKeys() {
		return Keys;
	}

	public void setKeys(boolean[] keys) {
		Keys = keys;
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}

	public boolean isGuard() {
		return guard;
	}

	public void setGuard(boolean guard) {
		this.guard = guard;
	}

	public items.Player getOtherPlayer() {
		return otherPlayer;
	}

	public void setOtherPlayer(items.Player otherPlayer) {
		this.otherPlayer = otherPlayer;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getStartZ() {
		return startZ;
	}

	public static void setStartX(double x) {
		Screen.startX = (int) x;
	}

	public static void setStartY(double y) {
		Screen.startY = (int) y;
	}

	public static void setStartZ(int startZ) {
		Screen.startZ = startZ;
	}


}