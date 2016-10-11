package view;

import java.util.List;

import controller.GameController;
import model.characters.GuardBot;
import model.characters.Player;
import model.rendering.Polygon;
import model.rendering.Vector;

/**
 * @Author Adam Wareing
 */
public class PlayerMovement {

    public static double movementSpeed = 5;

    /**
     * Called on every refresh, this updates the direction the camera is facing
     */
    public static void cameraMovement(double[] viewTo, double[] viewFrom, boolean[] keys, Room currentRoom) {
        Vector viewVector = new Vector(viewTo[0] - viewFrom[0], viewTo[1] - viewFrom[1], viewTo[2] - viewFrom[2]);
        double xMove = 0, yMove = 0, zMove = 0;
        Vector verticalVector = new Vector(0, 0, 1);
        Vector sideViewVector = viewVector.crossProduct(verticalVector);

        if (keys[0]) {
            xMove += viewVector.x;
            yMove += viewVector.y;
        }

        if (keys[2]) {
            xMove -= viewVector.x;
            yMove -= viewVector.y;
        }

        if (keys[1]) {
            xMove += sideViewVector.x;
            yMove += sideViewVector.y;
        }

        if (keys[3]) {
            xMove -= sideViewVector.x;
            yMove -= sideViewVector.y;
        }

        Vector MoveVector = new Vector(xMove, yMove, zMove);
        moveTo(viewFrom[0] + MoveVector.x * movementSpeed, viewFrom[1] + MoveVector.y * movementSpeed,
                viewFrom[2] + MoveVector.z * movementSpeed, currentRoom, viewFrom);
    }

    /**
     * Move the player to x, y, z and set the new viewFrom
     *
     * @param x
     * @param y
     * @param z
     */
   static void moveTo(double x, double y, double z, Room currentRoom, double[] viewFrom) {
        // Check that the player isn't out of the maps floorPolygons
        if (currentRoom.positionOutOfBounds(x, y, z))
            return;

        // Check that the player isn't moving into any roomObjects
        if (currentRoom.movingIntoAnObject(x, y, z))
            return;

        viewFrom[0] = x;
        viewFrom[1] = y;
        viewFrom[2] = z;
    }

    /**
     * Updates the other players position
     * @param otherPos [x, y, z]
     * @param otherPlayer
     * @return
     */
    public static List<Polygon> updateOtherPlayersPosition(double[] otherPos, Player otherPlayer) {
        // Lets start by getting there position from the controller and see how much they have moved
        double dx = otherPos[0] - otherPlayer.getX();
        double dy = otherPos[1] - otherPlayer.getY();
        double dz = otherPos[2] - otherPlayer.getZ();

        otherPlayer.updatePosition(dx, dy, dz);
        return otherPlayer.getPolygons();
    }

    /**
     * this method updates the guard bot position
     *
     * @return
     */
    public static List<Polygon> updateGuardBotPosition(String guardName, GameController controller) {
        // Lets start by getting there position from the controller and see how much they have moved
        GuardBot g = controller.getGuardBot(guardName);
        g.move(); // move the guard bot and update position based on heading
        return g.getPolygons();
    }

}
