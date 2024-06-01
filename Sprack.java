import greenfoot.*;

/**
 * A sprite stack (Sprack) is a sprite made up of multiple layers of 2D images,
 * each rotated to the same angle, but with a varying vertical offset. This
 * creates the illusion of a 3D object.
 * <p>
 * Note that the Spracks are not actually dynamically rendered layer by layer,
 * but are instead pre-rendered and stored in a {@link SprackView} object.
 *
 * @author Martin Baldwin
 * @version May 2024
 *
 * @see SprackView
 */
public class Sprack extends Sprite {
    private final String sheetName;

    private Vector3 worldPos;
    private double rotation;

    /**
     * Create a new Sprack with the given sheet name.
     * <p>
     * The sheet name is used to look up the {@link SprackView} object that
     * contains the pre-rendered images for this Sprack.
     *
     * @param sheetName the name of the Sprack sheet
     */
    public Sprack(String sheetName) {
        super(Layer.SPRACK_DEFAULT);
        this.sheetName = sheetName;
        worldPos = new Vector3();
    }

    /**
     * Create a new Sprack with the given sheet name and layer.
     * <p>
     * The sheet name is used to look up the {@link SprackView} object that
     * contains the pre-rendered images for this Sprack.
     *
     * @param sheetName the name of the Sprack sheet
     * @param layer the layer to render the Sprack on
     */
    public Sprack(String sheetName, Layer layer) {
        super(layer);
        this.sheetName = sheetName;
        worldPos = new Vector3();
    }

    /**
     * Set the in-world rotation of the Sprack.
     *
     * @param rotation the rotation of the Sprack, in degrees
     */
    public void setWorldRotation(double rotation) {
        this.rotation = Vector2.normalizeAngle(rotation);
    }

    /**
     * Get the in-world rotation of the Sprack.
     * <p>
     * The rotation is in degrees, and is normalized to the range [0, 360).
     *
     * @return the rotation of the Sprack, in degrees
     */
    public double getWorldRotation() {
        return rotation;
    }

    /**
     * Set the world position of the Sprack.
     * <p>
     * The world position is the position of the Sprack in the game world, not
     * the screen position.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    public void setWorldPos(double x, double y, double z) {
        worldPos = new Vector3(x, y, z);
    }

    /**
     * Set the world position of the Sprack.
     * <p>
     * The world position is the position of the Sprack in the game world, not
     * the screen position.
     *
     * @param position the position
     */
    public void setWorldPos(Vector3 position) {
        worldPos = position;
    }

    @Override
    public void render(GreenfootImage canvas) {
        SprackView view = SprackView.getView(sheetName);
        if (view == null) {
            return;
        }

        // Update screen position, rotated around zoomed camera position
        double scale = Camera.getZoom();
        double offsetX = (worldPos.x - Camera.getX()) * scale;
        double offsetY = (worldPos.y - Camera.getY()) * scale;
        double offsetZ = (worldPos.z - Camera.getZ()) * scale;
        double screenRad = Math.toRadians(-Camera.getRotation());
        double screenX = getWorld().getWidth() / 2 + offsetX * Math.cos(screenRad) - offsetZ * Math.sin(screenRad);
        double screenY = getWorld().getHeight() / 2 + offsetX * Math.sin(screenRad) + offsetZ * Math.cos(screenRad);
        screenY -= offsetY;
        setScreenPos(screenX, screenY);

        // Don't render if offscreen
        double imageRotation = rotation - Camera.getRotation();
        int centerX = view.getCenterX(imageRotation, scale);
        int centerY = view.getCenterY(imageRotation, scale);
        if (screenX + centerX < 0
            || screenX - centerX >= getWorld().getWidth()
            || screenY + (view.getTransformedHeight(imageRotation, scale) - centerY) < 0
            || screenY - centerY >= getWorld().getHeight()) {
            return;
        }

        // Draw image, screen position at center of bottom layer
        GreenfootImage image = view.getTransformedImage(imageRotation, Camera.getZoom());
        if (image == null) {
            return;
        }
        canvas.drawImage(image, (int) screenX - centerX, (int) screenY - centerY);
    }

    /**
     * Get the x position of the Sprack in the world.
     *
     * @return the x position of the Sprack
     */
    public double getWorldX() {
        return worldPos.x;
    }

    /**
     * Get the y position of the Sprack in the world.
     *
     * @return the y position of the Sprack
     */
    public double getWorldY() {
        return worldPos.y;
    }

    /**
     * Get the z position of the Sprack in the world.
     *
     * @return the z position of the Sprack
     */
    public double getWorldZ() {
        return worldPos.z;
    }

    /**
     * Get the world position of the Sprack.
     *
     * @return the world position of the Sprack
     */
    public Vector3 getWorldPos() {
        return worldPos;
    }

    /**
     * Get the rotation of the Sprack as it appears on the screen, relative to
     * the camera rotation.
     *
     * @return the visual rotation of the Sprack
     */
    public double getVisualRotation() {
        return Vector2.normalizeAngle(rotation - Camera.getRotation());
    }
}
