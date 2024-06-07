/**
 * Interface for objects that exist in the world, with a 3D position and a
 * rotation.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public interface WorldObject {
    /**
     * Get the world position of the object.
     *
     * @return the world position
     */
    public Vector3 getWorldPos();

    /**
     * Get the x position of the object in the world.
     *
     * @return the x position
     */
    public double getWorldX();

    /**
     * Get the y position of the object in the world.
     *
     * @return the y position
     */
    public double getWorldY();

    /**
     * Get the z position of the object in the world.
     *
     * @return the z position
     */
    public double getWorldZ();

    /**
     * Set the world position of the object.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    public void setWorldPos(double x, double y, double z);

    /**
     * Set the world position of the object.
     *
     * @param position the position
     */
    public void setWorldPos(Vector3 position);

    /**
     * Get the rotation of the object in the world.
     *
     * @return the rotation
     */
    public double getWorldRotation();

    /**
     * Set the rotation of the object in the world.
     *
     * @param rotation the rotation
     */
    public void setWorldRotation(double rotation);

    /**
     * Get the world this object is in, this is always an instance of a
     * {@link SpriteStackingWorld}.
     *
     * @return the world
     */
    public SpriteStackingWorld getWorld();
}
