import greenfoot.Color;
import greenfoot.GreenfootImage;

public class RickParticle extends Particle {
    public RickParticle() {
        super(getRickImage(), (int) (Math.random() * 40 + 20));
        physics.applyForce(new Vector2(Math.random() * 360).multiply(Math.random() * 1.7 + 0.5));
        physics.setAffectedByGravity(true);
    }

    private static GreenfootImage getRickImage() {
        final int size = (int) (Math.random() * 3 + 2);
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(new Color(226, 138, 114));
        image.fill();
        return image;
    }
}
