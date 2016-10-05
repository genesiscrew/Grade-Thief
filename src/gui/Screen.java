package gui;

import items.Item;
import items.Item.Interaction;
import items.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


/**
 * The screen itself is a JPanel which runs the game. It has all the input listeners for mouse, wheel and keys.
 */
public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private Room room;
    private Room room1 = new Room("level", 0, 0);
    private Room room2 = new Room("co238", 0, 0);

    // The polygon that the mouse is currently over
    static Polygon polygonOver = null;

    public static int startX = 120;
    public static int startY = 150;
    public static int startZ = 10;

    Robot r; // Used for keeping mouse in center
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * This is the co-ordinates of where the player is  (x, y, z)
     */
    double[] viewFrom = new double[]{15, 5, 10},
            viewTo = new double[]{0, 0, 0},
            lightDir = new double[]{1, 1, 1};


    // The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
    static double zoom = 1000;
    static double minZoom = 500;
    static double maxZoom = 2500;
    static double mouseX = 0;
    static double mouseY = 0;

    // FPS
    double drawFPS = 0;
    double maxFPS = 1000;
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

    /**
     * Create a new screen
     */
    public Screen(GameController controller, boolean guard) {
        this.controller = controller;
        this.guard = guard;
        this.currentPlayer = new Player(0, 0, 0, 0, 0, 0, null);
        if (guard)
            otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.green);
        else
            otherPlayer = new items.Player(20, 20, 0, 5, 3, 12, Color.blue);

        this.addKeyListener(this);
        setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        Cursor cursor = screenUtil.invisibleMouse();
        setCursor(cursor);

        // Load the section of the map
        room = new Room("level", startX, startY);
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

    @Override
    public void paintComponent(Graphics g) {
        // Clear screen and draw background color
        g.setColor(new Color(140, 180, 180));
        g.fillRect(0, 0, (int) GameController.ScreenSize.getWidth(), (int) GameController.ScreenSize.getHeight());

        PlayerMovement.cameraMovement(viewTo, viewFrom, keys, room);
        controller.updatePosition(guard, viewFrom);
        updateView();

        // Calculated all that is general for this camera position
        Calculator.setPredeterminedInfo(this);
        Calculator.controlSunAndLight(lightDir, room.getWidth(), sunPos);

        polyDrawer.drawPolygons(g, guard, otherPlayer);

        // Draw the cross in the center of the screen
        screenUtil.drawMouseAim(g);

        // FPS display
        g.drawString("FPS: " + (int) drawFPS + "(Benchmark)", 40, 40);

        // Message display
        g.setFont(new Font("Arial", Font.BOLD, 20));
        messageToDisplay = "";
        isPlayerNearObject();
        if (!messageToDisplay.equals("")) {
            g.drawString(messageToDisplay, (int) screenSize.getWidth() / 2 - 120, (int) screenSize.getHeight() / 2 - 50);
        }

        // Redraw
        sleepAndRefresh();
    }


    /**
     * This refreshes the display when required. In the meantime it simply sleeps.
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            currentPlayer.jump(viewFrom);
        if (e.getKeyCode() == KeyEvent.VK_R)
            loadMap();
        if (e.getKeyCode() == KeyEvent.VK_U)
            changeAllDoorState();
        if (e.getKeyCode() == KeyEvent.VK_E)
            playerWantingToInteractWithItem();
        if (e.getKeyCode() == KeyEvent.VK_I)
            showInventory();
    }

    private void showInventory() {
        String[] inventoryItems = new String[currentPlayer.getInventory().size()];
        int c = 0;
        for (Item i : currentPlayer.getInventory()) {
            inventoryItems[c] = i.toString();
            c++;
        }

        int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, inventoryItems, inventoryItems[0]);
        Item selectedItem = currentPlayer.getInventory().get(n);
        // at this point; shou;d get the position directly infront of player
        // should make sure that it will not obstruct another item
        selectedItem.moveItemBy(viewFrom[0] - selectedItem.getX(),
                viewFrom[1] - selectedItem.getY(), 0);

        room.addItemToRoom(selectedItem);
        selectedItem.canDraw();

        currentPlayer.removeFromInventory(selectedItem);
    }

    private void playerWantingToInteractWithItem() {

        room.getRoomObjects().stream().filter(i -> i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])).forEach(i -> {

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
        });
    }

    public void loadMap() {
        if (room == room1) {
            room = room2;
        } else {
            room = room1;
        }
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
                messageToDisplay = "Press e To Interact With The " + i.getClass().getSimpleName();
            }
        }


        for (Item i2 : removeItems) {
            room.removeRoomObject(i2);
        }

        for (Item i : room.getDoors()) {
            if (i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])) {
                messageToDisplay = "Press e To Open The Door";
            }
        }
    }




    private int showOptionPane(List<Item.Interaction> optionsList) {
        String[] options = new String[optionsList.size()];
        for (int i = 0; i < optionsList.size(); i++) {
            options[i] = optionsList.get(i).toString();
        }

        int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

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
}