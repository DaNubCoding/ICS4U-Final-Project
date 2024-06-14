import java.util.List;
import greenfoot.GreenfootImage;
/**
 * Write a description of class RPGProjectile here.
 *
 * @author Matthew Li
 * @author Martin Baldwin
 * @version June 2024
 */
public class RPGProjectile extends Projectile
{
    public RPGProjectile(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 75);
        setOriginalImage(new GreenfootImage("rpg_projectile.png"));
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 13);
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
        disappear();
    }

    @Override
    public void disappear() {
        Damage dmg = new Damage(getOwner(), this, 50, getWorldPos(), 25);
        getWorld().getDamages().add(dmg);
        getWorld().addWorldObject(new RPGExplosion(), getWorldPos());
        super.disappear();
    }
}
