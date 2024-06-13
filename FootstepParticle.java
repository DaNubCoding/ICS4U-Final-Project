import greenfoot.*;

/**
 * The particle that appears under the player's feet when they walk,
 * representing footsteps.
 *
 * @author Andrew Wang
 * @version June 2021
 */
public class FootstepParticle extends Particle {
    public static final GreenfootImage image;

    static {
        image = new GreenfootImage(4, 4);
        image.setColor(Color.BLACK);
        image.fill();
    }

    public FootstepParticle() {
        super(image, 160, Layer.GROUND);
    }

    @Override
    public void update() {
        super.update();

        setTransparency((int) (40 - getLifeTimer().progress() * 40));
    }
}
