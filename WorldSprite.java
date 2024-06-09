import greenfoot.*;

/**
 * A 2D sprite that exists in world space.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class WorldSprite extends TransformableSprite implements WorldObject {
    private Vector3 worldPos;
    private double worldRotation;

    public WorldSprite() {
        super(Layer.SPRACK_DEFAULT);
        worldPos = new Vector3();
    }

    /**
     * Set the world position of the sprite.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    @Override
    public void setWorldPos(double x, double y, double z) {
        worldPos = new Vector3(x, y, z);
    }

    /**
     * Set the world position of the sprite.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @param position the position
     */
    @Override
    public void setWorldPos(Vector3 position) {
        worldPos = position;
    }

    /**
     * Get the current world position of the sprite.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @return the current world position of the sprite.
     */
    @Override
    public Vector3 getWorldPos() {
        return worldPos;
    }

    /**
     * Get the x position of the sprite in the world.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @return the x position of the sprite in the world
     */
    @Override
    public double getWorldX() {
        return worldPos.x;
    }

    /**
     * Get the y position of the sprite in the world.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @return the y position of the sprite in the world
     */
    @Override
    public double getWorldY() {
        return worldPos.y;
    }

    /**
     * Get the z position of the sprite in the world.
     * <p>
     * The world position is the position of the sprite in the game world, not
     * the screen position.
     *
     * @return the z position of the sprite in the world
     */
    @Override
    public double getWorldZ() {
        return worldPos.z;
    }

    /**
     * Get the world rotation of the sprite.
     *
     * @return the rotation of the sprite
     */
    @Override
    public double getWorldRotation() {
        return worldRotation;
    }

    /**
     * Set the world rotation of the sprite.
     *
     * @param rotation the rotation of the sprite
     */
    @Override
    public void setWorldRotation(double rotation) {
        worldRotation = rotation;
    }

    public void render(GreenfootImage canvas) {
        double scale = Camera.getZoom();
        setScale(scale);
        double offsetX = (worldPos.x - Camera.getX()) * scale;
        double offsetY = (worldPos.y - Camera.getY()) * scale;
        double offsetZ = (worldPos.z - Camera.getZ()) * scale;
        double screenRad = Math.toRadians(-Camera.getRotation());
        double screenX = canvas.getWidth() / 2 + offsetX * Math.cos(screenRad) - offsetZ * Math.sin(screenRad);
        double screenY = canvas.getHeight() / 2 + offsetX * Math.sin(screenRad) + offsetZ * Math.cos(screenRad);
        screenY -= offsetY;
        setScreenPos(screenX, screenY);
        setScreenRotation(worldRotation - Camera.getRotation());
        super.render(canvas);
    }

    @Override
    public SprackWorld getWorld() {
        return (SprackWorld) super.getWorld();
    }
}
