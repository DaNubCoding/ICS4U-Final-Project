import java.awt.Point;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Robot;

import greenfoot.Greenfoot;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;

/**
 * A utility class to manage the mouse cursor.
 * <p>
 * This was built specifically for the cursor locking functionality.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class MouseManager {
    private static Vector2 anchor;
    private static boolean locked = false;

    // Prevent instantiation
    private MouseManager() {}

    /**
     * Get the position of the cursor relative to the top-left corner of the
     * main screen.
     *
     * @return the global position of the cursor
     */
    public static Vector2 getMousePos() {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        Point mousePos = pointerInfo.getLocation();
        return new Vector2(mousePos.getX(), mousePos.getY());
    }

    /**
     * Get the position of the cursor relative to the world.
     * @return the world position of the cursor
     */
    public static Vector2 getMouseWorldPos() {
        greenfoot.MouseInfo mouseInfo = Greenfoot.getMouseInfo();
        if (mouseInfo == null) return null;
        Vector2 screenCenter =
            new Vector2(SpriteStackingWorld.WORLD_WIDTH * PixelWorld.PIXEL_SCALE / 2,
                        SpriteStackingWorld.WORLD_HEIGHT * PixelWorld.PIXEL_SCALE / 2);
        int x = Greenfoot.getMouseInfo().getX();
        int y = Greenfoot.getMouseInfo().getY();
        Vector2 mousePos = new Vector2(x, y).subtract(screenCenter);
        double screenRot = Camera.getRotation();
        double scale = Camera.getZoom() * PixelWorld.PIXEL_SCALE;
        mousePos = mousePos.divide(scale);
        mousePos = mousePos.rotate(screenRot);
        return new Vector2(Camera.getX(), Camera.getZ()).add(mousePos);
    }

    /**
     * Get the position of the cursor relative to the anchor to which the cursor
     * was locked to.
     *
     * @return the relative position of the cursor
     */
    public static Vector2 getMouseRel() {
        if (anchor == null) return new Vector2(0, 0);
        Vector2 mousePos = getMousePos();
        return mousePos.subtract(anchor);
    }

    /**
     * Initiate the mouse lock at the current cursor position.
     */
    public static void initMouseLock() {
        anchor = getMousePos();
        locked = true;
    }

    /**
     * Release the mouse lock.
     */
    public static void releaseMouseLock() {
        locked = false;
    }

    /**
     * Check if the mouse is locked.
     *
     * @return true if the mouse is locked, false otherwise
     */
    public static boolean isLocked() {
        return locked;
    }

    /**
     * Lock the mouse to the anchor position initially set by
     * {@link #initMouseLock()}.
     * <p>
     * Adapted from <a href="https://stackoverflow.com/a/10665280/14692430">this
     * StackOverflow answer</a>.
     * <p>
     * This should work for multi-monitor setups, but somehow it doesn't.
     *
     * @param x the x-coordinate of the screen to lock the mouse to
     * @param y the y-coordinate of the screen to lock the mouse to
     */
    public static void lockMouse() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();

        // Search the devices for the one that draws the specified point
        for (GraphicsDevice device : devices) {
            GraphicsConfiguration[] configurations = device.getConfigurations();
            for (GraphicsConfiguration config : configurations) {
                final Rectangle bounds = config.getBounds();
                if (bounds.contains(anchor.x, anchor.y)) {
                    // Find screen coordinates
                    final Point topleft = bounds.getLocation();
                    final int relX = (int) anchor.x - topleft.x;
                    final int relY = (int) anchor.y - topleft.y;

                    try {
                        Robot robot = new Robot(device);
                        robot.mouseMove(relX, relY);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        }
    }
}
