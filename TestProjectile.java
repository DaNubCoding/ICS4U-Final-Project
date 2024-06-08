import java.util.List;
import java.util.Random;
import greenfoot.GreenfootImage;

/**
 * A projectile shot by the testing pistol, also for testing pyrposes :)
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class TestProjectile extends Projectile {
    Random rand;

    public TestProjectile(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 100);
        setOriginalImage(new GreenfootImage("test_pistol.png"));
        rand = new Random();
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 10);
        if(l.size() > 0 && l.contains(getOwner())) return false;
        return l.size() > 0;
    }

    @Override
    public void hit() {
        // bounce
        // get impulse before losing momentum
        Vector3 impulse = physics.getVelocity().rotateY(rand.nextInt(360));
        physics.reduceMomentum(1.0); // lose all momentum
        physics.applyImpulse(impulse);

        // create damage
        Damage dmg = new Damage(getOwner(), this, 10, getWorldPos(), 10);
        getWorld().getDamages().add(dmg);
    }
}
