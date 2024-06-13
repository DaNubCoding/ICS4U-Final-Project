import greenfoot.*;

/**
 * A particle used by the {@link JesterSword}.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class JesterParticle extends Particle {
    private static final Color[] colors = {
        new Color(238, 0, 0),
        new Color(255, 255, 51),
        new Color(0, 0, 255),
    };

    public JesterParticle() {
        super(getRandomImage(), (int) (Math.random() * 40 + 20));
        physics.setAffectedByFrictionalForces(false);
        physics.applyForce(new Vector3(0, Math.random() * 0.4 + 0.3, 0));
    }

    private static GreenfootImage getRandomImage() {
        final int size = (int) (Math.random() * 2 + 1);
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(colors[(int) (Math.random() * colors.length)]);
        image.fill();
        return image;
    }
}
