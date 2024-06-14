import java.util.List;
import greenfoot.GreenfootImage;

/**
 * Write a description of class RepeaterProjectile here.
 *
 * @author Matthew Li
 * @author Martin Baldwin
 * @version June 2024
 */
public class RepeaterProjectile extends Projectile {
    public RepeaterProjectile(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 100);
        setOriginalImage(new GreenfootImage("repeater_projectile.png"));
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 15);
        for (Sprack s : l) {
            if (s == getOwner() || !Projectile.isSprackSolid(s)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public void movingUpdate() {
        setWorldRotation(physics.getVelocity().xz.angle());
    }

    @Override
    public void hit() {
        // create damage
        Damage dmg = new Damage(getOwner(), this, 35, getWorldPos(), 15);
        getWorld().getDamages().add(dmg);
        disappear();
    }
}
