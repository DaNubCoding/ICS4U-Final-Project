import greenfoot.*;

/**
 * This class fully replaces the Greenfoot Actor class. It is a sprite that can
 * be rendered to a PixelWorld at its screen position.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class Sprite {
    private Layer layer;
    private Vector2 screenPos;
    private GreenfootImage image;
    private PixelWorld world;

    /**
     * Create a new Sprite on the given {@link Layer}.
     *
     * @param layer the Layer to render this sprite on
     * @see Layer
     */
    public Sprite(Layer layer) {
        screenPos = new Vector2();
        image = null;
        world = null;
        this.layer = layer;
    }

    /**
     * Get the Layer that this sprite is rendered on.
     *
     * @return the Layer of this sprite
     */
    public Layer getLayer() {
        return layer;
    }

    /**
     * Set the image of this sprite.
     *
     * @param image the image to set
     */
    public void setImage(GreenfootImage image) {
        this.image = new GreenfootImage(image);
    }

    /**
     * Get the image of this sprite.
     *
     * @return the image of this sprite
     */
    public GreenfootImage getImage() {
        return image;
    }

    /**
     * Set the screen position of this sprite.
     * <p>
     * The sprite will be rendered centered at this position.
     *
     * @param x the x position
     * @param y the y position
     */
    public void setScreenPos(double x, double y) {
        screenPos = new Vector2(x, y);
    }

    /**
     * Set the screen position of this sprite.
     * <p>
     * The sprite will be rendered centered at this position.
     *
     * @param position the position as a {@link Vector2}
     */
    public void setScreenPos(Vector2 position) {
        this.screenPos = position;
    }

    /**
     * Get the screen position of this sprite.
     *
     * @return the screen position as a {@link Vector2}
     */
    public Vector2 getScreenPos() {
        return screenPos;
    }

    /**
     * Get the x position of this sprite on the screen.
     *
     * @return the x position
     */
    public double getScreenX() {
        return screenPos.x;
    }

    /**
     * Get the y position of this sprite on the screen.
     *
     * @return the y position
     */
    public double getScreenY() {
        return screenPos.y;
    }

    /**
     * Render this sprite to the given canvas.
     * <p>
     * The canvas is almost always the canvas of the {@link PixelWorld} that
     * this sprite is in.
     *
     * @param canvas the canvas to render to
     */
    public void render(GreenfootImage canvas) {
        int x = (int) screenPos.x - image.getWidth() / 2;
        int y = (int) screenPos.y - image.getHeight() / 2;
        canvas.drawImage(image, x, y);
    }

    /**
     * Perform any logic for this sprite.
     * <p>
     * Override this method to add logic to your sprite.
     * <p>
     * By default, this method does nothing.
     */
    public void update() {}

    /**
     * Set the world that this sprite lives in.
     *
     * @param world the world to set
     */
    public void setWorld(PixelWorld world) {
        this.world = world;
    }

    /**
     * Get the world that this sprite lives in.
     *
     * @return the world
     */
    public PixelWorld getWorld() {
        return world;
    }

    /**
     * Called when this sprite is added to a world.
     * <p>
     * Override this method to add logic that should run when this sprite is
     * added to a world.
     *
     * @param world the world that this sprite was added to
     */
    public void addedToWorld(PixelWorld world) {}

    /**
     * Called when this sprite is removed from a world.
     * <p>
     * Override this method to add logic that should run when this sprite is
     * removed from a world.
     *
     * @param world the world that this sprite was removed from
     */
    public void removedFromWorld(PixelWorld world) {}

    /**
     * Get the distance from this sprite to the given screen position.
     *
     * @param x the x position
     * @param y the y position
     * @return the distance
     */
    public double distanceTo(double x, double y) {
        return screenPos.distanceTo(x, y);
    }
}
