import greenfoot.*;

/**
 * Explosion created by {@link RPGProjectile}.
 *
 * @author Andrew Wang
 * @author Martin Baldwin
 * @version June 2024
 */
public class RPGExplosion extends Particle {
    private static final SoundEffect explosionSound = new SoundEffect("explosion.wav");

    public RPGExplosion() {
        super(new GreenfootImage("rpg_explosion.png"), 20);
        Camera.shake(9, 9);
        explosionSound.play();
    }
}
