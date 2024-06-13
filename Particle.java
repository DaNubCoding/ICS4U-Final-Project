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

    public Particle(GreenfootImage image, int lifetime, Layer layer) {
        super(layer);
        lifeTimer = new Timer(lifetime);
        setOriginalImage(image);
        physics.setAffectedByGravity(false);
    }

    public Particle(GreenfootImage image, int lifetime) {
        this(image, lifetime, Layer.SPRACK_DEFAULT);
    }

    public Particle(String imagePath, int lifetime) {
        this(new GreenfootImage(imagePath), lifetime);
    }

    public Particle(String imagePath, int lifetime, Layer layer) {
        this(new GreenfootImage(imagePath), lifetime, layer);
    }

    public void updateMovement() {}

    @Override
    public void update() {
        updateMovement();
        physics.update();

        if (lifeTimer.ended()) {
            getWorld().removeSprite(this);
        }
    }

    /**
     * Get the {@link Timer} that controls the lifetime of this particle.
     *
     * @return the life timer
     */
    public Timer getLifeTimer() {
        return lifeTimer;
    }
}
