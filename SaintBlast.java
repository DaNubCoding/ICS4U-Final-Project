import greenfoot.GreenfootImage;
import java.util.List;

/**
 * A projectile homes in on a target.
 *
 * @author Stanley Wang
 * @author Martin Baldwin
 * @version June 2024
 */
public class SaintBlast extends Projectile {
    private Entity target;
    private int homing = 60;

    public SaintBlast(Entity owner, Vector3 initialVel, Vector3 startpos, Entity target) {
        super(owner, initialVel.rotateY(Math.random()*20-10), startpos, 500);
        setOriginalImage(new GreenfootImage("holy_blast.png"));
        physics.setAffectedByGravity(false);
        setWorldRotation(initialVel.xz.angle());
        this.target = target;
    }

    @Override
    public void movingUpdate() {
        setWorldRotation(physics.getVelocity().xz.angle());
        if (homing > 0) {
            homing -= 1;
            double angle = target.getWorldPos().subtract(getWorldPos()).xz.angle();
            Vector3 homeVel = physics.getVelocity();
            if ((angle-physics.getVelocity().xz.angle()) < 0) {
                homeVel = physics.getVelocity().rotateY(-1.5);
            } else {
                homeVel = physics.getVelocity().rotateY(1.5);
            }
            physics.reduceMomentum(1);
            physics.applyForce(homeVel);
        }
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 12);
        for (Sprack s : l) {
            if (s instanceof Player || (s instanceof Feature && Projectile.isSprackSolid(s))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void hit() {
        Entity owner = getOwner();

        Damage dmg = new Damage(owner, owner, 15, getWorldPos(), 12);
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 12);
        for (Sprack s:l) {
            try {
                if (s instanceof SaintShield || s instanceof Saint || s instanceof Feature) { continue; }
                else { ((Entity)s).damage(dmg); }
            } catch (ClassCastException e) {
                // do nothing if the sprack is not an entity
            }
        }

        disappear();
    }
}
