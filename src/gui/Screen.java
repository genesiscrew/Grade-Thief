package gui;

import items.Door;
import items.Item;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.*;

import characters.GuardBot;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private Room room;
    private Room room1 = new Room("level", 0, 0);
    private Room room2 = new Room("co238", 0, 0);

    // The polygon that the mouse is currently over
    static Polygon polygonOver = null;

    final int startX = 50;
    final int startY = 50;
    final int startZ = 10;

    // Used for keeping mouse in center
    Robot r;
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean jumping = false;

    /**
     * This is the co-ordinates of where the player is  (x, y, z)
     */
    double[] viewFrom = new double[]{15, 5, 10},
            ViewTo = new double[]{0, 0, 0},
            LightDir = new double[]{1, 1, 1};


    // The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
    static double zoom = 1000;
    static double minZoom = 500;
    static double maxZoom = 2500;
    static double mouseX = 0;
    static double mouseY = 0;
    static double movementSpeed = 5;

    //FPS is a bit primitive, you can set the maxFPS as high as u want
    double drawFPS = 0, maxFPS = 1000, LastRefresh = 0, lastFPSCheck = 0, fpsCheck = 0;


    double VertLook = -0.9;  // Goes from 0.999 to -0.999, minus being looking down and + looking up
    double HorLook = 0;     // takes any number and goes round in radians
    double aimSight = 4; // Changes the size of the center-cross.

    // The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate in those directions
    double HorRotSpeed = 900;
    double VertRotSpeed = 2200;
    double SunPos = 0;

    /**
     * Will hold the order that the polygons in the ArrayList DPolygon should be drawn meaning
     * DPolygon.get(polygonDrawOrder[0]) gets drawn first
     */
    int[] polygonDrawOrder;

    static boolean drawOutlines = true;
    boolean[] Keys = new boolean[4];

    private GameController controller;
    private boolean guard;
    private items.Player otherPlayer;
    private String messageToDisplay = "";

    /**
     * Create a new screen
     */
    public Screen(GameController controller, boolean guard) {
        this.controller = controller;
        this.guard = guard;
        if(guard)
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

        // re-closes doors previously opened by player as soon as he is not near it.
        for(Item i : room.getDoors()){
            if(!i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2]) &&   !((Door) i).isDraw()){
               ((Door) i).changeState();
            }
        }

        // Add all polygons to the list
        allPolygons.addAll(room.getFloorPolygons()); // floor tiles
        room.getWalls().forEach(o -> allPolygons.addAll(o.getPolygons())); // walls

        room.getRoomObjects().forEach(o -> allPolygons.addAll(o.getPolygons())); // room objects
        room.getDoors().forEach(d -> { // doors
            if(d.isDraw())
                allPolygons.addAll(d.getPolygons());
        });
        allPolygons.addAll(updateOtherPlayersPosition()); // other player
        allPolygons.addAll(this.updateGuardBotPosition()); // move guard bot


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
        g.drawString("FPS: " + (int) drawFPS + "(Benchmark)", 40, 40);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        messageToDisplay = "";
        isPlayerNearObject();
        if(!messageToDisplay.equals("")){
            g.drawString(messageToDisplay, (int) ScreenSize.getWidth()/2 -120, (int) ScreenSize.getHeight()/2 - 50);
        }

		//System.out.println(viewFrom[0] + " " + viewFrom[1] + " "  + viewFrom[2]);
        sleepAndRefresh();
    }


    private List<Polygon> updateGuardsPosition() {
		// TODO Auto-generated method stub
		List<Polygon> guards = new ArrayList<Polygon>();
		for (GuardBot g: this.room.guardList) {
			guards.addAll(g.getPolygons());
		}

		return guards;
	}

	/**
     * @return
     */
    private List<Polygon> updateOtherPlayersPosition() {
        // Lets start by getting there position from the controller and see how much they have moved
        double[] otherPos = controller.getOtherPlayersPosition(guard);
        double dx = otherPos[0] - otherPlayer.getX();
        double dy = otherPos[1] - otherPlayer.getY();
        double dz = otherPos[2] - otherPlayer.getZ();

        otherPlayer.updatePosition(dx, dy, dz);
        return otherPlayer.getPolygons();
    }

    /** this method updates the guard bot position
     * @return
     */
    private List<Polygon> updateGuardBotPosition() {
        // Lets start by getting there position from the controller and see how much they have moved
        double[] otherPos = controller.getOtherBotPosition("guard1");
        GuardBot g = controller.getGuardBot("guard1");
        g.move(); // move the guardbot and update position based on heading
        return g.getPolygons();
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
        g.drawLine((int) (Main.ScreenSize.getWidth() / 2 - aimSight), (int) (Main.ScreenSize.getHeight() / 2), (int) (Main.ScreenSize.getWidth() / 2 + aimSight), (int) (Main.ScreenSize.getHeight() / 2));
        g.drawLine((int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 - aimSight), (int) (Main.ScreenSize.getWidth() / 2), (int) (Main.ScreenSize.getHeight() / 2 + aimSight));
    }

    /**
     * This refreshes the display when required. In the meantime it simply sleeps.
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
        //SunPos += 0.005;
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
        moveTo(viewFrom[0] + MoveVector.x * movementSpeed, viewFrom[1] + MoveVector.y * movementSpeed, viewFrom[2] + MoveVector.z * movementSpeed);
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
        //System.out.printf("x: %f y: %f z: %f \n", x, y, z);
        controller.updatePosition(guard, viewFrom);
        updateView();
    }


    /**
     * Highlights the polygon that the cursor is on
     */
    void setPolygonOver() {
        polygonOver = null;
        for (int i = polygonDrawOrder.length - 1; i >= 0; i--)
            if (room.getPolygons().get(polygonDrawOrder[i]).mouseOver() && room.getPolygons().get(polygonDrawOrder[i]).draw
                    && room.getPolygons().get(polygonDrawOrder[i]).visible) {
                polygonOver = room.getPolygons().get(polygonDrawOrder[i]);
                break;
            }
    }

    /**
     * Called when the mouse is moved, calculates the amount it was moved and sets the vert and horizontal looking angles
     * It also updates the view
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            jump();
        if (e.getKeyCode() == KeyEvent.VK_R)
            loadMap();
        if (e.getKeyCode() == KeyEvent.VK_U)
            changeAllDoorState();
        if(e.getKeyCode() == KeyEvent.VK_E)
            playerWantingToInteractWithItem();

    }

    private void playerWantingToInteractWithItem() {
        for(Item i : room.getRoomObjects()){
            if(i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])){
                int n = showOptionPane(i.getInteractionsAvaliable());
                i.performAction(i.getInteractionsAvaliable().get(n));
            }

        }

        for(Item i : room.getDoors()){
            if(i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])){
                int n = showOptionPane(i.getInteractionsAvaliable());
                i.performAction(i.getInteractionsAvaliable().get(n));
            }
        }
    }

    public void loadMap() {
        if (room == room1) {
            room = room2;
        }
        else {
            room = room1;
        }
    }

    private void changeAllDoorState(){
        room.getDoors().forEach(d -> d.changeState());
    }


    private void isPlayerNearObject(){

        for(Item i : room.getRoomObjects()){
            if(i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])){
                messageToDisplay = "Press e To Interact With The Item";
            }
        }

        for(Item i : room.getDoors()){
            if(i.pointNearObject(viewFrom[0], viewFrom[1], viewFrom[2])){
                messageToDisplay = "Press e To Open The Door";
            }
        }
    }

    /**
     * This makes the player jump in the game. It uses a seperate thread to simulate the jumping so we can continue
     * updating the display throughout the jump.
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


    private int showOptionPane(List<Item.Interaction> optionsList){
        String[] options = new String[optionsList.size()];
        for(int i=0; i < optionsList.size(); i++){
            options[i] = optionsList.get(i).toString();
        }

        int n = JOptionPane.showOptionDialog(this, "What would you like to do?", "Select option", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return n;
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
  /**
   * starts all guard bots moving in different threads
   */
    /*
	public void startGuardsMovement() {

		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < this.room.guardList.size(); i++) {
		 threads.add(this.room.guardList.get(i).startMovement(this));

		}

		threads.forEach(d -> d.start());


	}
	*/

public double[] getPlayerView() {
	// TODO Auto-generated method stub
	return this.viewFrom;
}
}