import java.util.Random;

/**
 * A projectile, shot by a ranged weapon.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public abstract class Projectile extends WorldSprite {
    private Vector3 moveDirection;
    private Sprack owner;
    private int lifespan;
    private Random rand = new Random();

    /**
     * Create a new projectile using direction, starting position, and inaccuracy
     *
     * @param owner the sprack that created this projectile
     * @param direction the movement direction of the projectile, speed included
     * @param startPos the initial position of the projectile
     * @param inaccuracy the inaccuracy of the projectile, measured in degrees
     * @param lifespan the number of frames this projectile can last
     */
    public Projectile(Sprack owner, Vector3 direction, Vector3 startPos,
                      int inaccuracy, int lifespan) {
        super();
        this.owner = owner;
        double horizAngle = new Vector2(direction.x, direction.z).angle();
        double dAngle = inaccuracy == 0 ? 0 : (rand.nextDouble() * inaccuracy - inaccuracy/2.0);
        double adjustedAngle = horizAngle + dAngle;
        Vector2 adjVector = new Vector2(adjustedAngle);
        moveDirection = direction.add(new Vector3(adjVector.x, 0, adjVector.y));
        setWorldPos(startPos);
        this.lifespan = lifespan;
    }

    @Override
    public void update() {
        setWorldPos(getWorldPos().add(moveDirection));
        lifespan--;
        movingUpdate();
        // TODO: collision logic here
        if (lifespan<=0) disappear();
    }

    /**
     * An update that is performed while moving.
     * <p>
     * This method currently only rotates, so override it to add some features!
     */
    public void movingUpdate() {
        setScreenRotation(getScreenRotation()+5);
    }

    /**
     * An update that is performed when hitting a target.
     * <p>
     * This method currently only makes the projectile disappear, so override it
     * to add some features!
     */
    public void hit() {
        disappear();
    }

    /**
     * Updates to be performed when disappearing.
     * <p>
     * This method currently only makes this disappear, so override it to add
     * some interesting features!
     */
    public void disappear() {
        getWorld().removeSprite(this);
    }
}
