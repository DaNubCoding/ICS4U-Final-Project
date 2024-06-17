import greenfoot.*;

/**
 * Explosion created by {@link RPGProjectile}.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class RPGExplosion extends Particle {
    public RPGExplosion() {
        super(new GreenfootImage("rpg_explosion.png"), 20);
        Camera.shake(9, 9);
    }
}
