import greenfoot.GreenfootImage;
import java.util.List;

/**
 * A projectile that teleports the owner on hit.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class StatuePearl extends Projectile {
    public StatuePearl(Entity owner, Vector3 initialVel, Vector3 startpos) {
        super(owner, initialVel, startpos, 200);
        setOriginalImage(new GreenfootImage("statue_projectile.png"));
        physics.setAffectedByGravity(false);
        setWorldRotation(initialVel.xz.angle());
    }

    @Override
    public void movingUpdate() {
        if (Math.random() < 0.5) {
            getWorld().addWorldObject(new StatueParticle(), getWorldPos());
        }
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 15);
        for (Sprack s:l) {
            if(s instanceof Statue) return false;
        }
        return l.size() > 0;
    }

    @Override
    public void hit() {
        Entity owner = getOwner();

        Vector3 impulse = physics.getVelocity();
        owner.setWorldRotation(impulse.xz.angle());
        physics.reduceMomentum(1.0); // lose all momentum
        physics.applyForce(impulse);

        // create damage and knockback
        Damage dmg = new Damage(owner, owner, 20, getWorldPos(), 15);
        getWorld().getDamages().add(dmg);
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 15);
        for (Sprack s:l) {
            if(s instanceof Statue) continue;
            if(s instanceof Feature) ((Feature)s).removeFromWorld();
            if(s instanceof Entity) {
                impulse = impulse.setY(3);
                ((Entity)s).physics.applyForce(impulse);
                ((Entity)s).damage(dmg);
            }
        }

        // teleport statue to location and trigger the attack animation
        Vector3 pos = getWorldPos();
        double y = Math.max(pos.y, 0);
        owner.setWorldPos(pos.setY(y));
        try {
            ((Statue)owner).triggerAttack();
        } catch (ClassCastException e) {} // do nothing if not shot by a statue
        catch (NullPointerException e) {} // do nothing if the statue is dead

        disappear();
    }
}
