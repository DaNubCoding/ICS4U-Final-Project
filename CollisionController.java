/**
 * A controller for handling collision between objects. This is used to handle
 * collision between objects that should have collision.
 * <p>
 * To add collision to a client object, create a new {@link CollisionController}
 * with the client object, and add it to the world in the
 * {@link Sprite#addedToWorld(PixelWorld)} method, using the
 * {@link SprackWorld#addCollisionController(CollisionController)} method.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class CollisionController {
    private final WorldObject client;
    private final int radius;
    private final double pushoutFactor;
    private final double resistance;
    private boolean removed;

    /**
     * Create a new CollisionController for the given client object.
     * <p>
     * The radius defines the radius of the sphere in which the object will
     * collide with other objects. The pushoutFactor defines how much to push
     * out the object by, this is useful for soft collision. The resistance
     * defines how much this object resists being pushed out by other objects,
     * this is useful for objects that cannot be moved or should be perceived
     * as heavy.
     * <p>
     * The pushoutFactor should be between 0 and 1, where 0 means no pushout
     * and 1 means full pushout, will never overlap. The resistance should be
     * between 0 and 1, where 0 means no resistance and 1 means full resistance
     * (cannot be moved).
     *
     * @param client the client object
     * @param radius the radius of the collision
     * @param pushoutFactor the factor to push out the object by
     * @param resistance the amount this object resists being pushed out by
     *                   other objects
     */
    public CollisionController(WorldObject client, int radius, double pushoutFactor, double resistance) {
        this.client = client;
        this.radius = radius;
        this.pushoutFactor = pushoutFactor;
        this.resistance = resistance;
    }

    /**
     * Get the world that this controller's client is in.
     *
     * @return the world
     */
    public SprackWorld getWorld() {
        return client.getWorld();
    }

    /**
     * Get the world position of this controller's client.
     *
     * @return the world position
     */
    public Vector3 getWorldPos() {
        return client.getWorldPos();
    }

    /**
     * Set the world position of this controller's client.
     *
     * @param pos the new world position
     */
    public void setWorldPos(Vector3 pos) {
        client.setWorldPos(pos);
    }

    /**
     * Update the controller, checking for collisions and pushing out the client
     * object if necessary.
     */
    public void update() {
        if (getWorld() == null) {
            removed = true;
            return;
        }

        for (CollisionController other : getWorld().getCollisionControllers()) {
            if (other == this) continue;
            final int radSum = radius + other.radius;
            if (getWorldPos().distanceTo(other.getWorldPos()) > radSum) continue;
            Vector3 delta = getWorldPos().subtract(other.getWorldPos());
            try {
                delta = delta.scaleToMagnitude(radSum - delta.magnitude());
            } catch (ArithmeticException e) {
                delta = delta.setX(1).scaleToMagnitude(radSum - delta.magnitude());
            }
            final double mult = other.pushoutFactor * (1 - resistance);
            setWorldPos(getWorldPos().add(delta.multiply(mult)));
        }
    }

    /**
     * Check if this controller has been removed from the world.
     * <p>
     * This is used to tell the world that this controller should be removed
     * at the end of the update cycle.
     *
     * @return true if this controller has been removed, false otherwise
     */
    public boolean isRemoved() {
        return removed;
    }
}
