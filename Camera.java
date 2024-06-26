/**
 * The class containing a position, rotation, and zoom factor used to control
 * where spracks are rendered on the visible viewport.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public class Camera {
    // Don't let anyone instantiate this class
    private Camera() {}

    private static Vector3 centralPosition = new Vector3(0, 0, 0);
    private static Vector3 position = new Vector3(0, 0, 0);
    private static double rotation;
    private static double zoom;
    private static double closeness;
    private static Timer shakeTimer;
    private static int shakeStrength;

    /**
     * Set the factor by which the {@link #targetPosition} and
     * {@link #targetRotation} methods try to match their given values. The
     * specified closeness factor must be in the range [0.0, 1.0], where 0.0
     * corresponds to no movement and 1.0 corresponds to snapping directly to
     * targets.
     *
     * @param closeness the interpolation factor of position and rotation, in
     *                  the range [0.0, 1.0]
     */
    public static void setCloseness(double closeness) {
        if (closeness < 0.0 || closeness > 1.0) {
            throw new IllegalArgumentException("Camera closeness factor must be between 0.0 and 1.0");
        }
        Camera.closeness = closeness;
    }

    /**
     * Move towards the specified position by a factor specified by
     * {@link setCloseness closeness}.
     *
     * @param x the x position to interpolate towards
     * @param y the y position to interpolate towards
     * @param z the z position to interpolate towards
     */
    public static void targetPosition(double x, double y, double z) {
        targetPosition(new Vector3(x, y, z));
    }

    /**
     * Move towards the specified position by a factor specified by
     * {@link setCloseness closeness}.
     *
     * @param position the position to interpolate towards
     */
    public static void targetPosition(Vector3 position) {
        Vector3 delta = position.subtract(Camera.centralPosition);
        Camera.centralPosition = Camera.centralPosition.add(delta.multiply(closeness));

        if (shakeTimer != null && !shakeTimer.ended()) {
            Camera.position = Camera.centralPosition.add(new Vector3(
                (Math.random() - 0.5) * shakeStrength,
                (Math.random() - 0.5) * shakeStrength,
                (Math.random() - 0.5) * shakeStrength
            ));
        } else {
            Camera.position = Camera.centralPosition;
            shakeTimer = null;
        }
    }

    /**
     * Rotate towards the specified rotation by a factor specified by
     * {@link setCloseness closeness}.
     *
     * @param rotation the angle to interpolate towards, in degrees
     */
    public static void targetRotation(double rotation) {
        rotation = Vector2.normalizeAngle(rotation);
        double diff = rotation - Camera.rotation;
        if (diff > 180.0) {
            diff -= 360.0;
        } else if (diff < -180.0) {
            diff += 360.0;
        }
        Camera.rotation = Vector2.normalizeAngle(Camera.rotation + diff * closeness);
    }

    /**
     * Set the zoom factor of the camera.
     *
     * @return the zoom factor of the camera
     * @throws IllegalArgumentException if the given zoom factor is negative
     */
    public static void setZoom(double zoom) {
        if (zoom < 0) {
            throw new IllegalArgumentException("Zoom factor must not be negative");
        }
        Camera.zoom = zoom;
    }

    /**
     * Set the position and rotation directly to the given values.
     *
     * @param x the x position to set the camera to
     * @param x the y position to set the camera to
     * @param z the z position to set the camera to
     * @param rotation the rotation angle to set the camera to
     * @param zoom the zoom factor to set the camera to
     */
    public static void resetTo(double x, double y, double z, double rotation, double zoom) {
        Camera.centralPosition = new Vector3(x, y, z);
        Camera.rotation = Vector2.normalizeAngle(rotation);
        setZoom(zoom);
    }

    /**
     * Shake the camera for a specified duration with a specified strength.
     * <p>
     * The camera will shake randomly within a square of side length
     * {@code strength} centered around the camera's current position.
     *
     * @param strength the maximum distance the camera will shake
     * @param duration the duration of the shake
     */
    public static void shake(int strength, int duration) {
        shakeStrength = strength;
        if (shakeTimer == null) {
            shakeTimer = new Timer(duration);
        } else {
            double remaining = shakeTimer.getTotalFrames() * (1 - shakeTimer.progress());
            shakeTimer.restart((int) Math.max(remaining, duration));
        }
    }

    /**
     * Return the x position of the camera.
     *
     * @return the x position of the camera, in world coordinates
     */
    public static double getX() {
        return position.x;
    }

    /**
     * Return the y position of the camera.
     *
     * @return the y position of the camera, in world coordinates
     */
    public static double getY() {
        return position.y;
    }

    /**
     * Return the z position of the camera.
     *
     * @return the z position of the camera, in world coordinates
     */
    public static double getZ() {
        return position.z;
    }

    /**
     * Return the position of the camera as a {@link Vector2}.
     *
     * @return the position of the camera, in world coordinates
     */
    public static Vector3 getPosition() {
        return position;
    }

    /**
     * Return the rotation of the camera.
     *
     * @return the rotation angle of the camera, in degrees
     */
    public static double getRotation() {
        return rotation;
    }

    /**
     * Return the zoom factor of the camera.
     *
     * @return the zoom factor of the camera
     */
    public static double getZoom() {
        return zoom;
    }
}
