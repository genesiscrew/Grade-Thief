package gui;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    // List of all the 3D polygons - each 3D polygon has a 2D 'PolygonObject' inside called 'DrawablePolygon'
    static List<ThreeDPolygon> polygonFloor = new ArrayList<>();

    static ArrayList<Cube> cubes = new ArrayList<Cube>();
    static ArrayList<Prism> prisms = new ArrayList<Prism>();
    static ArrayList<Pyramid> pyramids = new ArrayList<Pyramid>();

    // The polygon that the mouse is currently over
    static PolygonObject polygonOver = null;

    final int startX = 15;
    final int startY = 5;
    final int startZ = 10;


    // Used for keeping mouse in center
    Robot r;

    // The floors map
    Terrain terrain;

    /**
     * This is the co-ordinates of where the player is  (x, y, z)
     */
    static double[] ViewFrom = new double[]{15, 5, 10},
            ViewTo = new double[]{0, 0, 0},
            LightDir = new double[]{1, 1, 1};


    // The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
    static double zoom = 1000;
    static double minZoom = 500;
    static double maxZoom = 2500;
    static double mouseX = 0;
    static double mouseY = 0;
    static double movementSpeed = 2;

    //FPS is a bit primitive, you can set the MaxFPS as high as u want
    double drawFPS = 0, MaxFPS = 1000, SleepTime = 1000.0 / MaxFPS, LastRefresh = 0, StartTime = System.currentTimeMillis(), LastFPSCheck = 0, Checks = 0;


    double VertLook = -0.9;  // Goes from 0.999 to -0.999, minus being looking down and + looking up
    double HorLook = 0;     // takes any number and goes round in radians
    double aimSight = 4; // Changes the size of the center-cross.

    // The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate in those directions
    double HorRotSpeed = 900;
    double VertRotSpeed = 2200;
    double SunPos = 0;

    /**
     * Will hold the order that the polygons in the ArrayList DPolygon should be drawn meaning
     * DPolygon.get(NewOrder[0]) gets drawn first
     */
    int[] NewOrder;

    static boolean drawOutlines = false;
    boolean[] Keys = new boolean[4];

    /**
     * Create a new screen
     */
    public Screen() {
        this.addKeyListener(this);
        setFocusable(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        invisibleMouse();

        // Load the terrain
        terrain = new Terrain();
        polygonFloor = terrain.generateMap();

        int wallLength = (int) (terrain.getMapHeight() * terrain.getTileSize()) - 5;
        int wallHeight = 50;

        cubes.add(new Cube(0, 0, 0, 5, wallLength, wallHeight, Color.blue));
        cubes.add(new Cube(5, 0, 0, wallLength - 10, 5, wallHeight, Color.blue));
        cubes.add(new Cube(wallLength - 5, 0, 0, 5, wallLength, wallHeight, Color.green));
        cubes.add(new Cube(0, wallLength, 0, wallLength - 5, 5, wallHeight, Color.blue));

        ViewFrom[0] = startX;
        ViewFrom[1] = startY;
        ViewFrom[2] = startZ;
    }

    public void paintComponent(Graphics g) {
        //Clear screen and draw background color
        g.setColor(new Color(140, 180, 180));
        g.fillRect(0, 0, (int) Main.ScreenSize.getWidth(), (int) Main.ScreenSize.getHeight());

        cameraMovement();

        //Calculated all that is general for this camera position
        Calculator.setPredeterminedInfo();

        controlSunAndLight();

        //Updates each polygon for this camera position
        for (int i = 0; i < polygonFloor.size(); i++)
            polygonFloor.get(i).updatePolygon();


        //Set drawing order so closest polygons gets drawn last
        setOrder();

        //Set the polygon that the mouse is currently over
        // setPolygonOver();

        //draw polygons in the Order that is set by the 'setOrder' function
        for (int i = 0; i < NewOrder.length; i++)
            polygonFloor.get(NewOrder[i]).DrawablePolygon.drawPolygon(g);

        //draw the cross in the center of the screen
        drawMouseAim(g);

        //FPS display
        g.drawString("FPS: " + (int) drawFPS + " (Benchmark)", 40, 40);

        sleepAndRefresh();
    }

    /**
     * This sets the order that the polygons are drawn in
     */
    private void setOrder() {
        double[] k = new double[polygonFloor.size()];
        NewOrder = new int[polygonFloor.size()];

        for (int i = 0; i < polygonFloor.size(); i++) {
            k[i] = polygonFloor.get(i).AvgDist;
            NewOrder[i] = i;
        }

        double temp;
        int tempr;
        for (int a = 0; a < k.length - 1; a++)
            for (int b = 0; b < k.length - 1; b++)
                if (k[b] < k[b + 1]) {
                    temp = k[b];
                    tempr = NewOrder[b];
                    NewOrder[b] = NewOrder[b + 1];
                    k[b] = k[b + 1];

                    NewOrder[b + 1] = tempr;
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
     * This calculates the frame rate
     */
    private void sleepAndRefresh() {
        long timeSLU = (long) (System.currentTimeMillis() - LastRefresh);

        Checks++;
        if (Checks >= 15) {
            drawFPS = Checks / ((System.currentTimeMillis() - LastFPSCheck) / 1000.0);
            LastFPSCheck = System.currentTimeMillis();
            Checks = 0;
        }

        if (timeSLU < 1000.0 / MaxFPS) {
            try {
                Thread.sleep((long) (1000.0 / MaxFPS - timeSLU));
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
        SunPos += 0.005;
        double mapSize = terrain.getMapWidth() * terrain.getMapWidth();
        LightDir[0] = mapSize / 2 - (mapSize / 2 + Math.cos(SunPos) * mapSize * 10);
        LightDir[1] = mapSize / 2 - (mapSize / 2 + Math.sin(SunPos) * mapSize * 10);
        LightDir[2] = -200;
    }

    /**
     * Called when the camera is moved
     */
    void cameraMovement() {
        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
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
        moveTo(ViewFrom[0] + MoveVector.x * movementSpeed, ViewFrom[1] + MoveVector.y * movementSpeed, ViewFrom[2] + MoveVector.z * movementSpeed);
    }

    /**
     * Move the player to x, y, z
     * @param x
     * @param y
     * @param z
     */
    void moveTo(double x, double y, double z) {
        System.out.println(x + " " + y + " " + z);

        if (!positionOutOfBounds(x, y, z)) {
            ViewFrom[0] = x;
            ViewFrom[1] = y;
            ViewFrom[2] = z;
            updateView();
        }
    }

    /**
     * Is position x, y, z outside of the floor?
     * @param x
     * @param y
     * @param z
     * @return
     */
    private boolean positionOutOfBounds(double x, double y, double z) {
        double mapSize = terrain.getMapWidth() * terrain.getTileSize();
        if (x < 0 || y < 0 || z < 0)
            return true;
        if (x > startX + mapSize || y > startY + mapSize || z > startZ + mapSize)
            return true;
        return false;
    }

    /**
     * Highlights the polygon that the cursor is on
     */
    void setPolygonOver() {
        polygonOver = null;
        for (int i = NewOrder.length - 1; i >= 0; i--)
            if (polygonFloor.get(NewOrder[i]).DrawablePolygon.mouseOver() && polygonFloor.get(NewOrder[i]).draw
                    && polygonFloor.get(NewOrder[i]).DrawablePolygon.visible) {
                polygonOver = polygonFloor.get(NewOrder[i]).DrawablePolygon;
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
        ViewTo[0] = ViewFrom[0] + r * Math.cos(HorLook);
        ViewTo[1] = ViewFrom[1] + r * Math.sin(HorLook);
        ViewTo[2] = ViewFrom[2] + VertLook;
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
}