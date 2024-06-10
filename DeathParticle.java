import greenfoot.*;

/**
 * A particle that is spawned when an entity dies.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class DeathParticle extends Particle {
    private Vector3 bouyancyForce;

    public DeathParticle() {
        super(getSmokeImage(), (int) (Math.random() * 40 + 20));
        bouyancyForce = new Vector3(0, Math.random() * 0.03 + 0.015, 0);
        physics.applyForce(new Vector2(Math.random() * 360).multiply(Math.random() * 0.5 + 0.4));
    }

    @Override
    public void updateMovement() {
        physics.applyForce(bouyancyForce);
    }

    private static GreenfootImage getSmokeImage() {
        final int size = (int) (Math.random() * 3 + 2);
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(Color.GRAY);
        image.fill();
        return image;
    }
}
