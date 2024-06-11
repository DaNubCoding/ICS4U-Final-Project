import java.util.List;
import greenfoot.GreenfootImage;
/**
 * Write a description of class RPGProjectile here.
 *
 * @author Matthew Li
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
        if(l.size() > 0 && l.contains(getOwner())) return false;
        return l.size() > 0;
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
