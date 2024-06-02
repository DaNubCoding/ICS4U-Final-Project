import java.util.Random;

/**
 * A projectile, shot by a ranged weapon.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public abstract class Projectile extends WorldSprite {
    private Vector3 moveDirection;
    private Random rand = new Random();

    /**
     * Create a new projectile using direction, starting position, and inaccuracy
     * 
     * @param direction the movement direction of the projectile, speed included
     * @param startPos the initial position of the projectile
     * @param inaccuracy the inaccuracy of the projectile, measured in degrees
     */
    public Projectile(Vector3 direction, Vector3 startPos, int inaccuracy) {
        super();
        double horizAngle = new Vector2(direction.x, direction.z).angle();
        double adjustedAngle = horizAngle + rand.nextDouble(-inaccuracy/2.0, (inaccuracy+0.01)/2.0);
        Vector2 adjVector = new Vector2(adjustedAngle);
        moveDirection = direction.add(new Vector3(adjVector.x, 0, adjVector.y));
        setWorldPos(startPos);
    }

    @Override
    public void update() {
        setWorldPos(getWorldPos().add(moveDirection));
        movingUpdate();
    }

    /**
     * An update that is performed while moving.
     * <p>
     * This method currently only rotates, so override it to add some features,
     * such as gravity!
     */
    public void movingUpdate() {
        setScreenRotation(getScreenRotation()+5);
    }

    /**
     * An update that is performed when hitting a target.
     */
    public abstract void hit();
}
