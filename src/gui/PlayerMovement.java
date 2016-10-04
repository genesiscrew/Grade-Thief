package gui;

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
     * Move the player to x, y, z
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


//    /**
//     * Highlights the polygon that the cursor is on
//     */
//    void setPolygonOver() {
//        polygonOver = null;
//        for (int i = polygonDrawOrder.length - 1; i >= 0; i--)
//            if (room.getPolygons().get(polygonDrawOrder[i]).mouseOver() && room.getPolygons().get(polygonDrawOrder[i]).draw
//                    && room.getPolygons().get(polygonDrawOrder[i]).visible) {
//                polygonOver = room.getPolygons().get(polygonDrawOrder[i]);
//                break;
//            }
//    }


}
