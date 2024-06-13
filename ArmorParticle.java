import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * A particle that is spawned when an entity takes armor damage.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class ArmorParticle extends Particle {
    public ArmorParticle() {
        super(getMetalImage(), (int) (Math.random() * 40 + 20));
        physics.applyForce(new Vector2(Math.random() * 360).multiply(Math.random() * 1.7 + 0.5));
        physics.setAffectedByGravity(true);
    }

    private static GreenfootImage getMetalImage() {
        final int size = (int) (Math.random() * 5 + 2);
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(new Color(238, 238, 238));
        image.fill();
        return image;
    }
}
