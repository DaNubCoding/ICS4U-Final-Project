import greenfoot.GreenfootImage;
import java.util.List;

/**
 * A projectile that teleports the owner on hit.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class SaintBlast extends Projectile {
    private Entity owner;
    private Entity target;
    private int homing = 60;

    public SaintBlast(Entity owner, Vector3 initialVel, Vector3 startpos, Entity target) {
        super(owner, initialVel.rotateY(Math.random()*20-10), startpos, 500);
        setOriginalImage(new GreenfootImage("holy_blast.png"));
        physics.setAffectedByGravity(false);
        setWorldRotation(initialVel.xz.angle());
        this.owner = owner;
        this.target = target;
    }

    @Override
    public void movingUpdate() {
        //Polish this for me lol
        //if (Math.random() < 0.5) {
        //    getWorld().addWorldObject(new StatueParticle(), getWorldPos());
        //}
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
        for (Sprack s:l) {
            if (s instanceof Player || s instanceof Feature) {
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
