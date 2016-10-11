package view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.GameController;
import controller.Main;
import controller.ScreenUtil;
import model.characters.Player;
import model.floor.EmptyTile;
import model.floor.Location;
import model.items.Container;
import model.items.Door;
import model.items.Item;
import model.items.Item.Interaction;
import model.items.KeyDraw;
import model.items.Table;
import model.items.Laptop;
import model.rendering.Polygon;
import model.saving.FastLoad;
import model.saving.FastSaving;

/**
 * The screen itself is a JPanel which runs the game. It has all the input
 * listeners for mouse, wheel and keys.
 */
public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private Room room;
    public static final int PLAYING = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWON = 4;
    public Location otherPlayerLocation;
    public int timer;
    private boolean displayImage = false;

    // The polygon that the mouse is currently over
    public static Polygon polygonOver = null;

    public static int startX = 120;
    public static int startY = 90;
    public static int startZ = 10;

    Robot r; // Used for keeping mouse in center
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * This is the co-ordinates of where the player is (x, y, z)
     */
    double[] viewFrom = new double[]{15, 5, 10}, viewTo = new double[]{0, 0, 0},
            lightDir = new double[]{1, 1, 1};

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
    private String messageToDisplay2 = "";
    private int countSinceMessageUpdate = 0;
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
        if (guard) {
            otherPlayer = new model.characters.Player(20, 20, 0, 5, 3, 12, Color.green);
            otherPlayer.setLocation(new Location(startX,startY));
            currentPlayer.setLocation(new Location(startX,startY));

        }
        else{
            otherPlayer = new model.characters.Player(20, 20, 0, 5, 3, 12, Color.blue);
            otherPlayer.setLocation(new Location(100,100));
            currentPlayer.setLocation(new Location(100,100));

        }

        otherPlayer.setRoom(roomName);

        this.addKeyListener(this);
        setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        Cursor cursor = screenUtil.invisibleMouse();
        setCursor(cursor);

        // Load the section of the map
        room = new Room(roomName);
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
    	Location playerLocation = new Location((int) (viewFrom[0] / 10), (int) (viewFrom[1] / 10));
    	currentPlayer.setLocation(playerLocation);
    	return playerLocation;
       // return new Location((int) (viewFrom[0] / 10), (int) (viewFrom[1] / 10));
    }

    @Override
    public void paintComponent(Graphics g) {
        if(displayImage)
            return;

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

        // Move the suns position
        lightDir = Calculator.controlSunAndLight(lightDir, room.getWidth(), sunPos);
        sunPos += 0.01;

        if (timer > 0) {
            timer--;
        }

        polyDrawer.drawPolygons(g, guard, otherPlayer, currentPlayer, timer, currentPlayer.getLevelName(), viewFrom[0],
                viewFrom[1]);

        // Draw the cross in the center of the screen
        screenUtil.drawMouseAim(g);

        // FPS display
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + (int) drawFPS , 40, 40);

        Location l = getPlayerLocation();
        Location o = otherPlayer.getLocation();
        if (l != null && o != null)
            g.drawString("Loc" + l.row() + " , " + l.column(), 40, 60);


        // Message display
        g.setFont(new Font("Arial", Font.BOLD, 20));
        messageToDisplay = "";
        isPlayerNearObject();
        if (!messageToDisplay.equals("")) {
            g.drawString(messageToDisplay, (int) screenSize.getWidth() / 2 - 120,
                    (int) screenSize.getHeight() / 2 - 50);
        }

        if (countSinceMessageUpdate == 30) {
            countSinceMessageUpdate = 0;
            messageToDisplay2 = "";
        }
        if (!messageToDisplay2.equals("")) {
            g.drawString(messageToDisplay2, (int) screenSize.getWidth() / 2 - 120,
                    (int) screenSize.getHeight() / 2 - 120);
            countSinceMessageUpdate++;
        }

         this.checkIfPlayerCaught();
        // Has the player won the game?
        if (this.GAMESTATUS == this.GAMEWON){
            showWinImage(g);
        }
        if (this.GAMESTATUS == this.GAMEOVER){
        	showLoseImage(g);
        }
        this.currentPlayer.setLocation(new Location ((int)viewFrom[0], (int)viewFrom[1]));


        // Redraw
        sleepAndRefresh();
    }

    private void showLoseImage(Graphics g) {
    	  g.setColor(Color.RED);
          try {
              BufferedImage img = ImageIO.read(new File("/home/wareinadam/SWEN222/Cleaned-Grade-Thief/grade-thief/dp.jpg"));
              g.drawImage(img, 0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight(),  null);
          } catch (IOException e) {
              e.printStackTrace();
          }
          g.drawString("You were Caught! Good luck trying to pass SWEN222 now!", (int) (screenSize.getWidth() / 2) - 100, (int) (screenSize.getHeight() / 2));
          displayImage = true;

	}

	private void showWinImage(Graphics g){
        g.setColor(Color.RED);
        try {
            BufferedImage img = ImageIO.read(new File("/home/wareinadam/SWEN222/Cleaned-Grade-Thief/grade-thief/dp.jpg"));
            g.drawImage(img, 0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight(),  null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawString("You Won!", (int) (screenSize.getWidth() / 2) - 100, (int) (screenSize.getHeight() / 2));
        displayImage = true;
    }

    /**
     * This method updates the status of the game, more specifically, whether the guard coordinates
     * match those of other playing, indicating he has been caught, or whether player got a special item
     * in his inventory indicating he has succesfully got access to david's computer
     */
    private void checkIfPlayerCaught() {
        if (!this.guard) {
        	System.out.println("we are checking if users touched");
            // check whether player coordinate correspond with other player,
            // hence he is caught
//            if (viewFrom[0] == this.otherPlayer.getX() && viewFrom[1] == this.otherPlayer.getY()) {
        	System.out.println(this.currentPlayer.getLocation().toString() + " .current. ");
        	System.out.println(this.otherPlayer.getLocation().toString() + " .other. ");
        	otherPlayer.setLocation(new Location((int) Math.floor(this.otherPlayer.getLocation().row()/10),
        			(int) Math.floor(this.otherPlayer.getLocation().column()/10)));


        	if (this.currentPlayer.getLocation().toString().equals(otherPlayer.getLocation().toString())) {
                this.GAMESTATUS = this.GAMEOVER;
                System.out.println("game is over");

            }

        } else {
            if (viewFrom[0] == this.otherPlayer.getX() && viewFrom[1] == this.otherPlayer.getY()) {
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
    public void keyPressed(KeyEvent e)  {
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
        if (e.getKeyCode() == KeyEvent.VK_U)
            changeAllDoorState();
        if (e.getKeyCode() == KeyEvent.VK_E)
        	try {
            playerWantingToInteractWithItem();
        	}
        catch (Exception u) {

        }
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
        int itemLocation = 0;
        for (int i = 0; i < inventoryItems.length; i++) {
            if (inventoryItems[i] == stringItem) {
                itemLocation = i;
                break;
            }
        }
        Item selectedItem = currentPlayer.getInventory().get(itemLocation);
        performActionOnItem(selectedItem, true);
    }

    /**
     * When the player is near an item and wants to interact with it.
     */
    private void playerWantingToInteractWithItem() {
        for (Item item : room.getRoomObjects()) {
            if (item.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {

                if (item instanceof Table) // Cant interact with a table
                    continue;
                performActionOnItem(item, false);
            }
        }
        room.getDoors().stream().filter(i -> i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])).forEach(i -> {
            performActionOnItem(i, false);

            int n = 0;
            if (n == 0) // doorOpened
            {
                Location l = getPlayerLocation();


                if (l.row() == 80 && l.column() == 5) // hard coded to switch floor
                    loadMap("level2", 1);
                else if (l.row() == 80 && l.column() == 29)
                    loadMap("level2", 2);
                else if (l.row() == 1 && l.column() == 5)
                    loadMap("level", 1);
                else if (l.row() == 1 && l.column() == 21)
                    loadMap("level", 2);
                else if (l.row() == 51 && l.column() == 25) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    loadMap("level2", 2);
                }
            }
            return;
        });
    }

    public void loadMap(String map, int id) {

        maxFPS = drawFPS;
        room = new Room(map);

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

    /**
     * This calls showOptionPanel() which gets the interaction chosen and this
     * executes it.
     *
     * @param item
     */
    public void performActionOnItem(Item item, boolean inInventory) {
		List<Interaction> interactions = item.getInteractionsAvailable();

		if (item.isCanPickup()) {
			if (inInventory) {
				interactions.add(Interaction.DROP);
				interactions.remove(Interaction.TAKE);
			} else {
				interactions.add(Interaction.TAKE);
				interactions.remove(Interaction.DROP);
			}
		}

		int n = showOptionPane(interactions);
		Interaction interaction = interactions.get(n);
		// TAKE
		if (interaction.equals(Interaction.TAKE)) {
			currentPlayer.addToInventory(item);
			room.removeRoomObject(item);
			item.canDraw();

			// DROP
		} else if (interaction.equals(Interaction.DROP)) {
			item.moveItemBy(viewFrom[0] - item.getX(), viewFrom[1] - item.getY(), 0);
			room.addItemToRoom(item);
			item.canDraw();
			currentPlayer.removeFromInventory(item);

			// UNLOCK
		} else if (interaction.equals(Interaction.UNLOCK)) {
			String name = null;

			if (item instanceof Container || item instanceof Laptop) {

				if (item instanceof Container) {
					name = "Container";
					Container c = (Container) item;
					if (currentPlayer.containsKeyInInventory(c.getKeyID())) {
						c.unlock();
						// add items to player inventory
						for (Item t : c.getItems()) {
							currentPlayer.addToInventory(t);
						}
						messageToDisplay2 = " container has been unlocked. " + c.getItems().size() + " items have been added to your inventory";
					} else {
						messageToDisplay2 = "You need the key " + c.getKeyID() + ", to unlock the " + name;

					}

				} else if (item instanceof Laptop) {
					name = "Laptop";
					Laptop l = (Laptop) item;
					if (currentPlayer.containsKeyInInventory(l.getKeyID())) {
						// player has unlocked david's laptop, so he won the game
						l.unlock();
						this.GAMESTATUS = this.GAMEWON;
					} else {
						messageToDisplay2 = "You need the key " + l.getKeyID() + ", to unlock the " + name;

					}

				}
			} else {
				if (currentPlayer.containsKeyInInventory(item.getItemID())) {
					name = "Door";
					Door d = (Door) item;
					d.unlock();
				} else {
					messageToDisplay2 = "You need the key " + item.getItemID() + ", to unlock the " + name;
				}

			}

			// OPEN
		} else if (interaction.equals(Interaction.OPEN)) {
			Door d = (Door) item;
			if (d.isLocked()) {
				messageToDisplay2 = "You need to unlock the door first";
			} else {
				item.performAction(interaction);
			}

			// HACK
		} else if (interaction.equals(Interaction.HACK)) {
			this.GAMESTATUS = GAMEWON;

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

    private void changeAllDoorState() {
        room.getDoors().forEach(d -> d.changeState());
    }

    private void isPlayerNearObject() {
        List<Item> allObjects = new ArrayList<>();
        allObjects.addAll(room.getDoors());
        allObjects.addAll(room.getRoomObjects());

        for (Item i : allObjects) {

            // Nothing to display if item not rendered or the player isn't near it
            if (i.isDraw() && i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {

                if (i instanceof Door) {
                    messageToDisplay = "Press e to interact with Door";
                    messageToDisplay += ". Combination: " + i.getItemID();

                } else if (i instanceof KeyDraw) {
                    messageToDisplay = "Press e to interact with Key";
                    messageToDisplay += ". Combination: " + i.getItemID();

                } else {
                    messageToDisplay = "Press e to interact with " + i.getName();
                }
            }
        }
    }


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

        Object[] options = {"Resume", "Save", "Load", "Help", "About Us", "Exit"};
        int n = JOptionPane.showOptionDialog(this, "Please select the prefer option?", "Grade Thief",

                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if (n == 0) {
            // Nothing for skipping the Modal and continue game.
        } else if (n == 1) {
            saveGame();
        } else if (n == 2) {
            loadGame();
        } else if (n == 3) {
            help();
        } else if (n == 4) {
            aboutUs();
        } else if (n == 5) {
            System.exit(0);
        }
    }

    /**
     * Loading game based on the selected file that user select This method use
     * SelectFile() method in order to get the name of the selected file in
     * order to have low coupled code.
     */
    private void loadGame() {
        String fileName = selectFile();

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
            String fileName = selectFile();
            if (fileName.equals("Cancelled"))
                return;
            saveGame = new FastSaving(fileName, this);
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
    private String selectFile() {
    	  // Creating JFileChooser
        JFileChooser fileChooser = new JFileChooser(".");
        int status = fileChooser.showOpenDialog(null);

        if (status == JFileChooser.APPROVE_OPTION) {
            // getting the name of the file.
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getParent() + "/" + selectedFile.getName();
        } else if (status == JFileChooser.CANCEL_OPTION) {
            // If the didn't select any file
            // default title and icon
            JOptionPane.showMessageDialog(this, "Choosing file operation cancelled");
        }
        return "Cancelled";
    }

    /**
     * Setting an help and instruction for the ease of use for users and players
     * on ESCAPE button.
     */
    private void help() {
    	  String rules = "Rules for Game is as follow : Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, Here is the Rule, ";
          JTextArea textArea = new JTextArea(rules, 6, 20);
          textArea.setFont(new Font("Serif", Font.ITALIC, 16));
          textArea.setLineWrap(true);
          textArea.setWrapStyleWord(true);
          textArea.setOpaque(false);
          textArea.setEditable(false);
          textArea.setSize(new Dimension(400, 500));
          OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules, textArea);
          dlg.setVisible(true);
    }

    /**
     * About Us Button for getting more information about the Creators and the
     * students whom created this game
     */
    private void aboutUs() {
    	  String rules = "Grade Thief is a Software Engineering Group Project which leads by Victoria University of Wellington. Team members are: Adam Wareing, Hamid Osman, Stefan Vrecic, Mostafa Shenavaei, Mansour Javaher";
          JTextArea textArea = new JTextArea(rules, 6, 20);
          textArea.setFont(new Font("Serif", Font.ITALIC, 16));
          textArea.setLineWrap(true);
          textArea.setWrapStyleWord(true);
          textArea.setOpaque(false);
          textArea.setEditable(false);
          textArea.setSize(new Dimension(400, 500));
          OptionPane dlg = new OptionPane(new JFrame(), "GradeThief", rules, textArea);
          dlg.setVisible(true);
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

    public Room getRoom() {
        return room;
    }

    public model.characters.Player getOtherPlayer() {
        return this.otherPlayer;
    }
}