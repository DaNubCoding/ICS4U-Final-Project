import greenfoot.*;

/**
 * A particle that is removed after a certain amount of time.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class Particle extends WorldSprite {
    private Timer lifeTimer;
    public final PhysicsController physics = new PhysicsController(this);

    public Particle(GreenfootImage image, int lifetime) {
        lifeTimer = new Timer(lifetime);
        setOriginalImage(image);
        physics.setAffectedByGravity(false);
    }

    public Particle(String imagePath, int lifetime) {
        this(new GreenfootImage(imagePath), lifetime);
    }

    public void updateMovement() {}

    @Override
    public void update() {
        updateMovement();

        if (lifeTimer.ended()) {
            getWorld().removeSprite(this);
        }
    }
}
