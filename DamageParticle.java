import greenfoot.*;

/**
 * A particle that is spawned when an entity takes damage.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class DamageParticle extends Particle {
    public DamageParticle() {
        super(getBloodImage(), (int) (Math.random() * 40 + 20));
        physics.applyForce(new Vector2(Math.random() * 360).multiply(Math.random() * 0.7 + 0.5));
        physics.setAffectedByGravity(true);
    }

    private static GreenfootImage getBloodImage() {
        final int size = (int) (Math.random() * 3 + 2);
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(new Color(138, 3, 3));
        image.fill();
        return image;
    }
}
