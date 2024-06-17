import java.util.List;
import java.util.Random;
import greenfoot.GreenfootImage;

/**
 * A projectile shot {@link Pistol} that bounces around on hit.
 *
 * @author Lucas Fu
 * @author Martin Baldwin
 * @version June 2024
 */
public class PistolBullet extends Projectile {
    Random rand;

    public PistolBullet(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 100);
        setOriginalImage(new GreenfootImage("pistol_bullet.png"));
        rand = new Random();
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 10);
        for (Sprack s : l) {
            if (s == getOwner() || !Projectile.isSprackSolid(s)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public void hit() {
        // bounce
        // get impulse before losing momentum
        Vector3 impulse = physics.getVelocity().rotateY(rand.nextInt(360));
        physics.reduceMomentum(1.0); // lose all momentum
        physics.applyForce(impulse);

        // create damage
        Damage dmg = new Damage(getOwner(), this, 3, getWorldPos(), 10);
        getWorld().getDamages().add(dmg);
    }
}
